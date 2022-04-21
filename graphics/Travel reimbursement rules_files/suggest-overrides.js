//
// suggest-overrides.js
//
// Custom overrides for suggestions.
//
// This code is copied from
// webapp/resources/uicomponents/suggest/suggest.js
// and relevant parts have been customized as required.
// (See the revision log for details)
//

/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

//
// OPTIONS
// See https://github.com/xwiki/xwiki-platform/blob/stable-7.4.x/xwiki-platform-core/xwiki-platform-web/src/main/webapp/resources/uicomponents/suggest/suggest.js
//

//
// Issue AJAX requests for suggestions at 250ms default rather than 500ms.
// Current found uses of this (there could be more):
//     - Page Search Autosuggestion (uicomponents/search/search-suggest-overrides.js)
//     - Tag Search Autosuggestion (uicomponents/viewers/tags.js)
//
XWiki.widgets.Suggest.prototype.options.delay = 250;

//
// Do not use the unified loader, as we have only one suggest source.
// This lets us use a single central spinner in the UI, and also
// lets us clean up and simplify some of the overridden code.
//
XWiki.widgets.Suggest.prototype.options.unifiedLoader = false;

// Overwrite function prepareContainer() from suggest.js.
XWiki.widgets.Suggest.prototype.prepareContainer = function() {

  if (!$(this.options.parentContainer).down('.suggestItems')) {
    // If the suggestion top container is not in the DOM already, we create it and inject it

    var div = new Element("div", { 'class': "suggestItems "+ this.options.className });

    // Get position of target textfield
    var pos = $(this.options.parentContainer).tagName.toLowerCase() == 'body' ? this.fld.cumulativeOffset() : this.fld.positionedOffset();

    // Container width is passed as an option, or field width if no width provided.
    // The 1px subtracted corresponds to one pixel of border on each side of the field,
    // this allows to have the suggestion box borders well aligned with the field borders.
    // FIXME this should be computed instead, since border might not always be 1px.
    var fieldWidth = this.fld.offsetWidth - 1;
    var containerWidth = this.options.width || fieldWidth;
    var inputPositionLeft = this.fld.viewportOffset().left;
    var browserWidth = $('body').getWidth();

    // if the option is 'auto', we make sure that we have enough place to display it on the left. If not, it will go on the right.
    if (this.options.align == 'left' || (this.options.align == 'auto' && inputPositionLeft + this.options.width < browserWidth)) {
      // Align the box on the left
      div.style.left = pos.left + "px";
    } else if (this.options.align == "center") {
      // Align the box to the center
      div.style.left = pos.left + (fieldWidth - containerWidth) / 2 + "px";
    } else {
      // Align the box on the right.
      // This has a visible effect only when the container width is not the same as the input width
      div.style.left = (pos.left + fieldWidth - containerWidth) + "px";
    }

    div.style.top = (pos.top + this.fld.offsetHeight + this.options.offsety) + "px";
    // Don't enforce the width if it wasn't specified to let the container adjust its width to fit the suggest items.
    div.style[this.options.width ? 'width' : 'minWidth'] = containerWidth + "px";

    // set mouseover functions for div
    // when mouse pointer leaves div, set a timeout to remove the list after an interval
    // when mouse enters div, kill the timeout so the list won't be removed
    var pointer = this;
    div.onmouseover = function(){ pointer.killTimeout() }
    div.onmouseout = function(){ pointer.resetTimeout() }

    this.resultContainer = new Element("div", {'class':'resultContainer'});
    div.appendChild(this.resultContainer);

    // add DIV to document
    $(this.options.parentContainer).insert(div);

    this.container = div;

    if (this.options.insertBeforeSuggestions) {
      this.resultContainer.insert(this.options.insertBeforeSuggestions);
    }

    document.fire("xwiki:suggest:containerCreated", {
      'container' : this.container,
      'suggest' : this
    });
  }

  if (this.isInMultiSourceMode) {
    // If we are in multi-source mode, we need to prepare a sub-container for each of the suggestion source
    for (var i=0;i<this.sources.length;i++) {

      var source = this.sources[i];
      source.id = source.id || i;

      if(this.resultContainer.down('.results' + source.id)) {
        // If the sub-container for this source is already present, we just re-initialize it :
        // - remove its content
        // - set it as loading
        if (this.resultContainer.down('.results' + source.id).down('ul')) {
          this.resultContainer.down('.results' + source.id).down('ul').remove();
        }

        this.resultContainer.down('.results' + source.id).down('.sourceContent').addClassName('loading');
      }
      else {
        // The sub-container for this source has not been created yet
        // Really create the subcontainer for this source and inject it in the global container
        var sourceContainer = new Element('div', {'class' : 'results results' + source.id});

        var classes = "sourceContent loading";
        sourceContainer.insert( new Element('div', {'class':classes}));

        if (typeof source.before !== 'undefined') {
          this.resultContainer.insert(source.before);
        }
        this.resultContainer.insert(sourceContainer);
        if (typeof source.after !== 'undefined') {
          this.resultContainer.insert(source.after);
        }
      }
    }
  } else {
    // In mono-source mode, reset the list if present
    if (this.resultContainer.down("ul")) {
      this.resultContainer.down("ul").remove();
    }
  }

  var ev = this.container.fire("xwiki:suggest:containerPrepared", {
    'container' : this.container,
    'suggest' : this
  });

  return this.container;
}


// Overwrite function doAjaxRequests() from suggest.js.
XWiki.widgets.Suggest.prototype.doAjaxRequests = function(requestId, ajaxRequestParameters) {
  if (this.fld.value.length < this.options.minchars) {
    return;
  }

  for (var i=0;i<this.sources.length;i++) {
    var source = this.sources[i];
    if (typeof source.script == 'function') {
      source.script(this.fld.value.strip(), function(suggestions) {
        if (requestId == this.latestRequest) {
          this.aSuggestions[source.id] = suggestions || [];
          suggestions && this.createList(this.aSuggestions[source.id], source);
        }
      }.bind(this));
    } else {
      this.doAjaxRequest(source, requestId, ajaxRequestParameters);
    }
  }
}

// Overwrite function doAjaxRequest() from suggest.js.
XWiki.widgets.Suggest.prototype.doAjaxRequest = function(source, requestId, ajaxRequestParameters) {
  var url = source.script + (source.script.indexOf('?') < 0 ? '?' : '&') + source.varname + "=" + encodeURIComponent(this.fld.value.strip());
  var method = source.method || "get";
  var headers = {};
  if (source.json) {
    headers.Accept = "application/json";
  } else {
    headers.Accept = "application/xml";
  }

  // Allow the default request parameters to be overwritten.
  var defaultAjaxRequestParameters = {
    method: method,
    requestHeaders: headers,
    onSuccess: this.setSuggestions.bindAsEventListener(this, source, requestId),
    onFailure: function (response) {
      new XWiki.widgets.Notification("Failed to retrieve suggestions: " + response.statusText, "error", {timeout: 5});
    }
  }
  // Inject a reference to the (cloned) default AJAX request parameters to be able
  // to access the defaults even when they are overwritten by the provided values.
  defaultAjaxRequestParameters.defaultValues = Object.clone(defaultAjaxRequestParameters);
  new Ajax.Request(url, Object.extend(defaultAjaxRequestParameters, ajaxRequestParameters || {}));
}
