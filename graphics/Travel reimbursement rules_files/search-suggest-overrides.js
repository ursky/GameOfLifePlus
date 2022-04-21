//
// search-suggest-overrides.js
//
// Custom overrides for search suggestions.
//
// This code is copied from
// webapp/resources/uicomponents/search/searchSuggest.js
// and relevant parts have been customized as required.
// (See the revision log for details)
//

//
// See the NOTICE file distributed with this work for additional
// information regarding copyright ownership.
//
// This is free software; you can redistribute it and/or modify it
// under the terms of the GNU Lesser General Public License as
// published by the Free Software Foundation; either version 2.1 of
// the License, or (at your option) any later version.
//
// This software is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this software; if not, write to the Free
// Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
// 02110-1301 USA, or see the FSF site: http://www.fsf.org.
//

var XWiki = (function (XWiki) {

  XWiki.SearchSuggest = Class.create(XWiki.SearchSuggest, {

    //
    // Overwrite function createSuggest() from searchSuggest.js
    //
    // CHANGES:
    //
    // - Remove the explicit width setting on the suggestions panel.
    //   This allows the suggestion panel to grow and shrink dynamically.
    //   See https://github.com/xwiki/xwiki-platform/blob/stable-7.4.x/xwiki-platform-core/xwiki-platform-web/src/main/webapp/resources/uicomponents/suggest/suggest.js#L526
    //
    createSuggest: function() {

      // Create dummy suggestion node to hold the "Go to search page..." option.
      var valueNode = new Element('div')
            .insert(new Element('span', {'class':'suggestId'}))
            .insert(new Element('span', {'class':'suggestValue'}))
            .insert(new Element('span', {'class':'suggestInfo'}));
      this.noResultsMessage = new Element('div', {'class': 'hidden'})
        .update("No results!".escapeHTML());
      var gotoSearchPageMessage = new Element('div')
        .update("Go to search page\u2026"
        .escapeHTML());
      var content = new Element('div').insert(this.noResultsMessage).insert(gotoSearchPageMessage)
        .insert(new Element('div', {'class': 'clearfloats'}));
      var allResultsNode = new XWiki.widgets.XList([
        new XWiki.widgets.XListItem( content, {
          'containerClasses': 'suggestItem',
          'classes': 'showAllResults',
          'eventCallbackScope' : this,
          'noHighlight' : true,
          'value' : valueNode
        } ),
      ],
      {
        'classes' : 'suggestList',
        'eventListeners' : {
          'click': function(event){
            this.searchInput.up('form').submit();
          },
          'mouseover':function(event){
            this.suggest.clearHighlight();
            this.suggest.iHighlighted = event.element();
            event.element().addClassName('xhighlight');
          }
        }
      });
      var allResults = allResultsNode.getElement();
      this.suggest = new XWiki.widgets.Suggest( this.searchInput, {
        parentContainer: $('searchSuggest'),
        className: 'searchSuggest horizontalLayout',
        fadeOnClear: false,
        align: "auto",
        minchars: 3,
        sources : this.sources,
        insertBeforeSuggestions : new Element("div", {'class' : 'results'}).update( allResults ),
        displayValue: true,
        displayValueText: "in ",
        resultInfoHTML: true,
        timeout: 0,
        unifiedLoader: true,
        loaderNode: allResults.down("li"),
        shownoresults: false,
        propagateEventKeyCodes : [ Event.KEY_RETURN ]
      });
    }
  });

  return XWiki;
})(XWiki);
