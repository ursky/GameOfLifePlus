// Show an "Warning: This is an external link to <hostname>" message to the user when they are trying to visit a host other than *.amazon.com.
require(['jquery','bootstrap'], function ($) {

    $('.wikiexternallink > a').each(function() {
        const url = $(this).attr('href');
        const isValidUrl = /^(?:http(s)?:\/\/)?[\w.-]+(?:\.[\w\.-]+)+[\w\-\._~:/?#[\]@!\$&'\(\)\*\+,;=.]+$/g.test(url);

        if (isValidUrl) {
          // Make sure relative urls have the correct hosts
          const host = $(this)[0].hostname;
          $(this).attr({'data-toggle': 'tooltip', 'data-placement': 'top', 'title': `Warning: This is an external link to ${host}`});
          // Don't want to leak sensitive data to third parties. See https://issues.amazon.com/issues/KTC-9643
          $(this).attr({'rel': 'nofollow noreferrer noopener'});
        }
    });
    $('[data-toggle="tooltip"]').tooltip();

});

