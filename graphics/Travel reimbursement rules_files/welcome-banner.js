require(['jquery'], function($) {
  $('#welcomeBanner .dismiss a').click(function() {
    $('#welcomeBanner').slideUp();
    $.ajax({
      type: 'GET',
      contentType: 'application/xml',
      url: '/bin/view/XWiki/Banner?show=false',
      success: function(data) {
        new XWiki.widgets.Notification('As a reminder, you can take the tour at any time via the Help menu at the top of the screen', 'info');
      },
      error: function(jqXHR, textStatus, errorThrown) {
        new XWiki.widgets.Notification('An error occurred while setting banner preferences to "hidden"', 'error');
      }
    });
  });
});
