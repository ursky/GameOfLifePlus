// Custom popup box for use with section permalinks. Based on the XWiki confirmationBox. This alternate template
// is necessary for the additional flexibility in which buttons to display and how to invoke bound functions.

if(typeof(XWiki) == "undefined" || typeof(XWiki.widgets) == "undefined" || typeof(XWiki.widgets.ModalPopup) == "undefined") {
  if(typeof console != "undefined" && typeof console.warn == "function") {
    console.warn("[PermalinkBox widget] Required class missing: XWiki.widgets.ModalPopup");
  }
} else {
  XWiki.widgets.PermalinkBox = Class.create(XWiki.widgets.ModalPopup, {

    defaultInteractionParameters : {
      confirmationText: "Are you sure?",
      yesButtonText:    "Yes",
      noButtonText:     "No",
      cancelButtonText: "Cancel",
      showYesButton:    true,
      showNoButton:     false,
      showCancelButton: false,
      deferCloseDialog: false
    },

    initialize : function($super, behavior, interactionParameters) {
      this.interactionParameters = Object.extend(Object.clone(this.defaultInteractionParameters), interactionParameters || {});
      var buttons = {
        "show"  : {method:this.showDialog, keys:[]},
        "yes"   : {method:this.onYes, keys:["Enter", "Space", "y"]},
        "no"    : {method:this.onNo, keys:["n"]},
        "close" : {method:this.closeDialog, keys:["c"]}
      };

      if (this.interactionParameters.showCancelButton) {
        buttons.close.keys.push("Esc");
      } else if (this.interactionParameters.showNoButton) {
        buttons.no.keys.push("Esc");
      }

      $super(this.createContent(this.interactionParameters), buttons, {displayCloseButton:false, removeOnClose:true});
      this.showDialog();
      this.setClass("permalink");
      this.behavior = behavior || {};
    },

    createContent : function(data) {
      var question = new Element("div", {"class": "question"}).update(data.confirmationText);
      var buttons = new Element("div", {"class": "buttons"});
      if(data.showYesButton){
        var yesButton = this.createButton("button", data.yesButtonText, "", "");
        Event.observe(yesButton, "click", this.onYes.bindAsEventListener(this));
        buttons.insert(yesButton);
      }
      if (data.showNoButton) {
        var noButton = this.createButton("button", data.noButtonText, data.showCancelButton ? "(n)" : "(Esc)", "", "secondary");
        Event.observe(noButton, "click", this.onNo.bindAsEventListener(this));
        buttons.insert(noButton);
      }
      if (data.showCancelButton) {
        var cancelButton = this.createButton("button", data.cancelButtonText, "(Esc)", "", "cancel secondary");
        Event.observe(cancelButton, "click", this.onCancel.bindAsEventListener(this));
        buttons.insert(cancelButton);
      };
      var content = new Element("div");
      content.insert(question).insert(buttons);
      return content;
    },

    // Note: it might be necessary to close the dialog after invoking the bound function to allow for access to
    // content in the popup. (For example, in copying content to the clipboard.)
    onYes : function() {
      if (!this.interactionParameters.deferCloseDialog) {
        this.closeDialog();
      }
      if (typeof(this.behavior.onYes) == "function") {
        this.behavior.onYes();
      }
      if (this.interactionParameters.deferCloseDialog) {
        this.closeDialog();
      }
    },

    onNo : function() {
      this.closeDialog();
      if (typeof(this.behavior.onNo) == "function") {
        this.behavior.onNo();
      }
    },

    onCancel : function() {
      this.closeDialog();
      if (typeof(this.behavior.onCancel) == "function") {
        this.behavior.onCancel();
      }
    }
  })
};