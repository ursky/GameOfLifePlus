/**
 * This file overrides (read MonkeyPatches) functionality of the XWiki Comments class by creating a
 * subclass and pointing XWiki.viewers.Comments to this subclass.  When the
 * DOM is finished loading the original JavaScript file will call our constructor instead.
 * Original file can be found: /xwiki-platform-core/xwiki-platform-web/src/main/webapp/resources/uicomponents/viewers/comments.js
 */
var XWiki = (function (XWiki) {
// Start XWiki augmentation.
  var viewers = XWiki.viewers = XWiki.viewers || {};

  viewers.Comments = Class.create( viewers.Comments, {

    /**
     * Add a preview button that generates the rendered comment,
     *
     * This changes the positioning of the preview and save button so that they're consistent throughout the wiki.
     */
    addPreview : function(form) {
      if (!form || !XWiki.hasEdit) {
        return;
      }
      var previewURL = "/bin/preview/__space__/__page__".replace("__space__", encodeURIComponent(XWiki.currentSpace)).replace("__page__", encodeURIComponent(XWiki.currentPage));

      // Preview links for pages with periods and under Webhome are broken, but the current fix in WPF is breaking periods elsewhere
      // See: https://cr.amazon.com/r/4877586/
      // We first replace all periods with slashes before going back to determining which periods were actually escaped
      // This is due to the lack of a lookbehind in Javascript regex
      if (XWiki.currentPage === 'WebHome') {
        previewURL = previewURL.replace(/\./g, "/").replace(/%5C\//g, ".");
      }

      form.commentElt = form.down('textarea');
      var buttons = form.down('input[type=submit]');
      form.previewButton = new Element('span', {'class' : 'buttonwrapper'}).update(new Element('input', {'type' : 'button', 'class' : 'button', 'value' : "Preview"}));
      form.previewButton._x_modePreview = false;
      form.previewContent = new Element('div', {'class' : 'commentcontent commentPreview'});
      form.commentElt.insert({'before' : form.previewContent});
      form.previewContent.hide();
      buttons.insert({'after' : form.previewButton});
      form.previewButton.observe('click', function() {
        if (!form.previewButton._x_modePreview && !form.previewButton.disabled) {
          form.previewButton.disabled = true;
          var notification = new XWiki.widgets.Notification("Generating preview...", "inprogress");
          new Ajax.Request(previewURL, {
            method : 'post',
            parameters : {'xpage' : 'plain', 'sheet' : '', 'content' : form.commentElt.value},
            onSuccess : function (response) {
              this.doPreview(response.responseText, form);
              notification.hide();
            }.bind(this),
            /* If the content is empty or does not generate anything, we have the "This template does not exist" response,
             with a 400 status code. */
            on400 : function(response) {
              this.doPreview('&nbsp;', form);
              notification.hide();
            }.bind(this),
            onFailure : function (response) {
              var failureReason = response.statusText;
              if (response.statusText == '' /* No response */ || response.status == 12031 /* In IE */) {
                failureReason = 'Server not responding';
              }
              notification.replace(new XWiki.widgets.Notification("Failed to generate preview: " + failureReason, "error"));
            },
            on0 : function (response) {
              response.request.options.onFailure(response);
            },
            onComplete : function (response) {
              form.previewButton.disabled = false;
            }.bind(this)
          });
        } else {
          this.cancelPreview(form);
        }
      }.bindAsEventListener(this));
    },
  });

// End XWiki augmentation.
  return XWiki;
}(XWiki || {}));
