/**
 * This file overrides (read MonkeyPatches) functionality of the XWiki Attachments class by creating a 
 * subclass and pointing XWiki.viewers.Attachments to this subclass.  When the
 * DOM is finished loading the original JavaScript file will call our constructor instead.
 * Original file can be found: resources/js/xwiki/viewers/attachments.js
 */
var XWiki = (function (XWiki) {
// Start XWiki augmentation.
var viewers = XWiki.viewers = XWiki.viewers || {};

viewers.Attachments = Class.create( viewers.Attachments, {
  
  /**
   * attachHTML5Uploader function of super class does not hide the progress 
   * bar when upload is complete.  We are overriding this method so
   * and configuring the FileUploader to hide the progress bar when
   * file upload is finished.
   */
  attachHTML5Uploader : function($super, input) {
        var response = $super(input)
        
        // Super class returns false if browser does not support HTML5 file uploading.
        if(response) {
            // Tell the FileUploader class to auto hide on complete.
            response.options.progressAutohide = true;
        }
        
        return response;
    }
});

// End XWiki augmentation.
return XWiki;
}(XWiki || {}));
