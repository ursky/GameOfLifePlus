require(['jquery', 'bootstrap'], function($) {

  const SAVE_IN_PROGRESS = "Updating replication status...";
  const SAVE_SUCCESS = "Replication status updated successfully";
  const SAVE_ERROR = "An error occurred while updating replication. Please try again. If this problem continues, <a href=\"https:\/\/w.amazon.com\/bin\/view\/XWiki\/SIMForm\">cut us a ticket<\/a>";
  const REPLICATION_ENABLE = $('#tmReplicationEnable');
  const REPLICATION_DISABLE = $('#tmReplicationDisable');

  function init() {
    REPLICATION_ENABLE.on('click', updateReplicationStatus);
    REPLICATION_DISABLE.on('click', updateReplicationStatus);
  }

  function updateReplicationStatus() {
      const savingBox = new XWiki.widgets.Notification(SAVE_IN_PROGRESS, "inprogress", {inactive: true});
      const savedBox = new XWiki.widgets.Notification(SAVE_SUCCESS, "done", {inactive: true});
      const errorBox = new XWiki.widgets.Notification(SAVE_ERROR, "error", {inactive: true});

      savingBox.show();

      new $.ajax({
          url: $(this).attr('href'),
          type: 'POST',
          //data: { enabled : !REPLICATION_TOGGLE.attr('data-enabled')},
          error: function() {
              savingBox.replace(errorBox);
          },
          success: function() {
              toggleReplicationButtons()
              savingBox.replace(savedBox);
          }
      });
      return false;
  }

  function toggleReplicationButtons() {
    REPLICATION_ENABLE.toggleClass("hidden");
    REPLICATION_DISABLE.toggleClass("hidden");
  }

  init();

});