/* 
/ Use this file to override any logic stemming from xwiki.js, the javascript is included on the page by the javascript.vm file.
/ 
/ 
/ The following code ensures that clicking on a create link
/ on the wiki does not cause a modal to pop up, preventing the scenario
/ where the request for the create page would go through WPF, and end up
/ rendered within the modal.  

/ Upon executing this function, wait for the document to load. Once loaded
/ find all wikicreatelink anchors, and remove the onclick events associated with them
/ Those events are initially added in xwiki.js
/ Github link:
/ https://github.com/xwiki/xwiki-platform/blob/stable-7.4.x/xwiki-platform-core/xwiki-platform-web/src/main/webapp/resources/js/xwiki/xwiki.js
/ @line 368
*/

(function() {
    document.observe('dom:loaded', function(){
      var links = $$('span.wikicreatelink a');

      links.forEach(function(entry){
          entry.stopObserving('click');
      });
    });
})();

var XWiki = (function(XWiki) {
  XWiki.widgets = XWiki.widgets || {};

  Object.extend(XWiki, {

    createAjaxUpdater: function (dhtmlSwitch, extraID, extraTemplate, scrollToAnchor, masterWiki) {
      new Ajax.Updater(
        extraID + "pane",
        window.docgeturl + '?' +
        ['xpage=xpart' , 'vm=' + extraTemplate, masterWiki, simulatedRequestParameters].filter(Boolean).join("&"),
        {
          method: 'post',
          evalScripts: true,
          onComplete: function(transport){
            $("docextrapanes").className = "";

            // Let other know new content has been loaded
            document.fire("xwiki:docextra:loaded", {
              "id" : extraID,
              "element": $(extraID + "pane")
            });

            // switch tab
            dhtmlSwitch(extraID);

            if (scrollToAnchor) {
              // Yes, this is a POJW (Plain Old JavaScript Ha^Wworkaround) which
              // prevents the anchor 'jump' after a click event but enable it
              // when the user is arriving from a direct /Space/Page#Section URL
              $(extraID + 'anchor').id = extraID;
              location.href = '#' + extraID;
              $(extraID).id = extraID + 'anchor';
            }
          }
        }
      );

      return;
    },

    // We are overriding the existing displayDocExtra functionality in order to
    // pass through the 'master' flag (if present) to the history pane
    // See original functionality here: https://github.com/xwiki/xwiki-platform/blob/831157480cb19f1e77505cf8bf5e6146ec0f464b/xwiki-platform-core/xwiki-platform-web/src/main/webapp/resources/js/xwiki/xwiki.js#L166

    displayDocExtra: function (extraID, extraTemplate, scrollToAnchor) {

      // Hides the previously displayed extra pane (window.activeDocExtraPane)
      // and display the one that is passed as an argument (extraID).
      // Fires an event to notify that the pane has changed.

      var dhtmlSwitch = function (extraID) {
        var tab = document.getElementById(extraID + "tab");
        var pane = document.getElementById(extraID + "pane");
        if (window.activeDocExtraTab != null) {
          window.activeDocExtraTab.className = "";
          window.activeDocExtraPane.className = "hidden";
        }
        window.activeDocExtraTab = tab;
        window.activeDocExtraPane = pane;
        window.activeDocExtraTab.className = "active";
        window.activeDocExtraPane.className = "";
        tab.blur();

        document.fire("xwiki:docextra:activated", {"id": extraID});
      };
      // Use Ajax.Updater to display the requested pane (extraID) : comments, attachments, etc.
      // On complete :
      //   1. Call dhtmlSwitch()
      //   2. If the function call has been triggered by an event : reset location.href to #extraID
      //      (because when the link has been first clicked the anchor was not loaded)
      if ($(extraID + "pane").className.indexOf("empty") != -1) {
        if (window.activeDocExtraPane != null) {
            window.activeDocExtraPane.className = "invisible";
        }
        $("docextrapanes").className = "loading";

        // [Overriding change]
        // If the master has been set to xwiki in the query parameters (index >= 0)
        // pass it onto historyinline.vm POST call
        // otherwise, don't pass it in

        var masterWiki = (location.search.indexOf("master=xwiki") >= 0) ? 'master=xwiki' : '';

        this.createAjaxUpdater(dhtmlSwitch, extraID, extraTemplate, scrollToAnchor, masterWiki);
      } else {
        dhtmlSwitch(extraID);
        if (scrollToAnchor) {
          $(extraID + 'anchor').id = extraID;
          location.href = '#' + extraID;
          $(extraID).id = extraID + 'anchor';
        }
      }
    },

    //When editing using Source, if user tries to unload, we are checking whether the contents have been changed.
    //(Contents mean contents for title and/or the contents in the main textArea)
    //If the contents changed, a window will pop out to ensure user wants to unload without saving the change.
    //However, when user tries to unload by clicking the bottom buttons like Save, Preview or Cancel button,
    //the alert window won't pop out.

    setupSourcePageEventListeners: function () {
      require(['jquery'], function ($) {
        const originalContents = $("#content").val();
        const originalTitle = $('#xwikidoctitleinput').val();
        let bottomButtonsClicked = false;
        $('[name="action_save"], [name="action_preview"], [name="action_cancel"]').click(function () {
          bottomButtonsClicked = true;
        });

        window.onbeforeunload = function () {
          if (!bottomButtonsClicked) {
            const currentContents = $('#content').val();
            const currentTitle = $('#xwikidoctitleinput').val();
            //Check whether the contents are changed.
            if (currentContents !== originalContents || currentTitle !== originalTitle) {
              return true;
            }
          }
          return;
        }
      });
    },

    //Pop out an alert window for the preview page no matter whether the user makes changes or not.
    //Reason: we need to call getContent() in previewactions.vm if we try to get whether the contents are changed or not.
    //Next step: Implement MD5 in backend and use it to get the hash value for the contents and compare the hash value.

    setupPreviewPageEventListeners: function () {
      require(['jquery'], function ($) {
        let bottomButtonsClicked = false;
        $('[name="action_save"], [name="action_edit"], [name="action_cancel"]').click(function () {
          bottomButtonsClicked = true;
        });
        window.onbeforeunload = function () {
          if (!bottomButtonsClicked) {
            return true;
          }
          return;
        }
      });
    },

    //We are overriding the existing insertSectionEditLinks function in order to better prepare the page for
    //additional section tools (e.g., permalink). Some code, and comments, taken from the overridden function.
    //See original functionality at: https://github.com/xwiki/xwiki-platform/blob/master/xwiki-platform-core/xwiki-platform-web/src/main/webapp/resources/js/xwiki/xwiki.js#L309

    insertSectionEditLinks: function(container) {
      // Insert links only if enabled, in view mode and for documents not in xwiki/1.0 syntax
      if ( XWiki.docsyntax != "xwiki/1.0" && XWiki.contextaction == "view") {

        // Check whether user has edit rights
        var hasEditRights = true && XWiki.hasEdit;

        // Section count starts at one, not zero.
        var sectioncount = 1;

        container = $(container || 'body');
        container = container.id == 'xwikicontent' ? container : container.down('#xwikicontent');
        if (!container) {
          return;
        }

        // We can't use element.select() since it does not keep the order of the elements in the flow.
        var nodes = container.childNodes;

        // Only allow section tools up to the specified depth level
        var headerPattern = new RegExp("H[1-" + 6 + "]");

        // Add a SPAN element to preserve compatibility with overridden function, and a DIV (button group) element as
        // the container for the section tools. Add an A element for editing the section if and only if the section
        // is not from a transclude.
        for (var i = 0; i < nodes.length; i++) {
          var node = $(nodes[i]);
          if (headerPattern.test(node.nodeName)) {
            // This span is redundant, but is preserved for compatibility with overridden function.
            var editspan = document.createElement("SPAN");
            editspan.className = "edit_section";

            // This div holds the section tool buttons.
            var tooldiv = document.createElement("DIV");
            tooldiv.className = "btn-group";

            // Hide the section tools if the section heading is hidden.
            (!node.visible() || node.hasClassName('hidden')) && editspan.hide();

            // Add the section edit button and permalink button if the section is not transcluded.
            if (node.className.include("wikigeneratedheader") == false) {
              // Edit section content in WYSIWYG Editor
              var editLink = this.createEditButton(
                  "glyphicon glyphicon-pencil",
                  window.docediturl + "?section=" + sectioncount,
                  "Edit Section"
              );
              // Edit section source in XWiki syntax
              var editSourceLink = this.createEditButton(
                  "fa fa-code",
                  window.docediturl + "?section=" + sectioncount + "&editor=wiki",
                  "Edit Section Source"
              );

              // If the document syntax is not the default (XWiki) syntax, then the WYSIWYG section edit
              // button will be displayed inactive since we don't support WYSIWYG editing for non-XWiki syntaxes.
              // Also disable if user does not have edit rights.
              if (!XWiki.docInDefaultSyntax || !hasEditRights) {
                var disabledTitle = "This page\'s syntax doesn\'t support section editing!";
                this.makeButtonDisabled(editLink, disabledTitle);

                // Because Mediawiki syntax is currently rendered as a single RawBlock,
                // we cannot properly fetch sections for editing. Also disable if user does not have edit rights.
                if (XWiki.docsyntax.startsWith('mediawiki') || !hasEditRights) {
                  this.makeButtonDisabled(editSourceLink, disabledTitle);
                }
              }

              // Add permalink button
              var permaLinkButton = this.createPermalinkButton();
              var pagePath = this.currentDocument.space + "." + this.currentDocument.page;
              this.addHeaderPermalinkListener(permaLinkButton, pagePath, node);

              tooldiv.appendChild(editLink);
              tooldiv.appendChild(editSourceLink);
              tooldiv.appendChild(permaLinkButton);
              sectioncount++;
            }

            editspan.appendChild(tooldiv);
            node.insert( { 'after': editspan } );
          }
        }
      }
    },

    // Create a specific Edit Section button (either for WYSIWYG or XWiki editor).
    createEditButton: function(iconClassName, href, title) {
      var editButton = document.createElement("A");
      editButton.className = "edit btn btn-default btn-xs";
      editButton.rel = "nofollow";
      editButton.href = href;
      editButton.title = title;
      editButton.setAttribute('aria-label', title);

      var editIcon = document.createElement("SPAN");
      editIcon.className = iconClassName;
      editButton.append(editIcon);

      return editButton;
    },

    createPermalinkButton: function() {
      var permalinkButton = document.createElement("A");
      var title = "Permalink";
      permalinkButton.className = "permalink btn btn-default btn-xs";
      permalinkButton.rel = "nofollow";
      permalinkButton.title = title;
      permalinkButton.setAttribute('aria-label', title);

      var permalinkIcon = document.createElement("SPAN");
      permalinkIcon.className = "glyphicon glyphicon-link";
      permalinkButton.append(permalinkIcon);

      return permalinkButton;
    },

    // Make a button disabled.
    makeButtonDisabled: function(button, disabledTitle) {
      button.className = button.className + " disabled";
      button.title = disabledTitle;
      button.href = null;
      button.onclick = function(){
        return false;
      }
    },
    addHeaderPermalinkListener(element, pagePath, node) {

        var headerName = node.id || node.firstChild.id;
        var newWikiLink = "[[LABEL>>" + pagePath + "||anchor=" + headerName + "]]";
        var fullURL = node.baseURI.split('#')[0] + "#" + headerName;

        element.addEventListener('click', function(event) {
            element.blur();
            event.stop();

            var permalinkBox = new XWiki.widgets.PermalinkBox(
            // event handler overrides
            {},
            // interaction parameters
            {
              confirmationText: "<strong>Full URL</strong><br/>Link to this section:" +
                                "<div class='input-group'><input type='text' class='form-control' id='external-text' " +
                                "value='" +
                                fullURL +
                                "'/><span class='input-group-btn'><button type='button' class='btn btn-primary' " +
                                "title='Copy to clipboard' id='external-button'><span class='glyphicon glyphicon-copy'>" +
                                "</span></button></span></div>" +
                                "<br/>" +
                                "<strong>Internal Wiki Links</strong><br/>" +
                                "Use this snippet in the Source editor to link to this section from a New Wiki page:" +
                                "<div class='input-group'><input type='text' class='form-control' id='internal-text'" +
                                "value='" +
                                newWikiLink +
                                "'/><span class='input-group-btn'><button type='button' class='btn btn-primary' " +
                                "title='Copy to clipboard' id='internal-button'><span class='glyphicon glyphicon-copy'>" +
                                "</span></button></span></div>" +
                                "<br/>" +
                                "More information about constructing links on New Wiki can be found <a href=https://w.amazon.com/bin/view/XWiki/XWikiSyntax#HLinks target=_blank>here</a>. More information about constructing links on Legacy Wiki can be found <a href=https://www.mediawiki.org/wiki/Help:Links target=_blank>here</a>.",
              yesButtonText: "Done"
            });

            permalinkBox.dialog.addClassName('permalinkBox');

            permalinkBox.dialog.down('button[id="external-button"]').addEventListener("click", function(){
                permalinkBox.dialog.down('input[id="external-text"]').select();
                document.execCommand('copy');s
            });
            permalinkBox.dialog.down('button[id="internal-button"]').addEventListener("click", function(){
                permalinkBox.dialog.down('input[id="internal-text"]').select();
                document.execCommand('copy');
            });
        });
    }
  });

  return XWiki;
})(XWiki);