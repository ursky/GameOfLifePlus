// Loads portalwiki.min.js and applies some polyfills and DOM transformations required to make iGraph functions work.

// if the requirejs configuration exists, we must have already loaded the js from the older iGraph macro. Therefore
// bail out of this script so that we don't get duplicate logic.
if (!requirejs.s.contexts._.config.paths.portalwiki) {

    window.portalData = window.portalData || {
        portalURL: "https://monitorportal.amazon.com"
    };

    // monitorportal.amazon.com
    requirejs.config({
        "paths": {
            "portalwiki":  window.portalData["portalURL"] + "/javascripts/portalwiki.min"
        },
        "shim": {
            "portalwiki": ["jquery"]
        },
        // Allow more time for portalwiki.min.js to load. Default is 7. Unit in seconds.
        "waitSeconds": 60
    });

    require(['jquery'], function($) {
        $(document).ready(function() {
            // Add jQuery.browser
            polyfillJQueryBrowser($);

            // Transform IGraph ribbon-customization-related <span>s to <div>s. We output these as <span>s in this macro's Velocity code in order to avoid XWiki potentially complaining
            // about "transcluding block HTML in inline context". However PortalWiki JS needs these to be <div>s to do its work properly, so we perform this transformation after
            // XWiki page rendering, but before loading PortalWiki JS.
            $('#xwikicontent').find('span.MPWdefaultWidgetsHere, span.IGraphNoDefaultWidgets, span.IGraphWidget').each(function(i, e) {
                var $e = $(e);
                var text = $e.text();

                if ($e.is('.IGraphWidget')) {
                    // Ensure there are enough argument slots to make PortalWiki JS happy
                    text += '||||||';
                }

                var $replacement = $('<div>', { 'class': $e.attr('class'), 'style': $e.attr('style') })
                    .append(text);
                $e.replaceWith($replacement);
            });

            // A small timeout appears to be necessary to prevent the iGraph header bar from occasionally failing to appear
            setTimeout(function() {
                var xwikiIGraphImages = $('#xwikicontent').find('img[data-igraph-src]')
                var containsMediawikiIGraph = ($('.IGraphWidget').size() > 0) || (/\bautoadd=/.test(window.location.href));
                if (!containsMediawikiIGraph) {
                    for (var i = 0; i < document.images.length; i++) {
                        var image = document.images[i];
                        if (/mws\?.*?&?Action=GetGraph/.test(image.src)) {
                            containsMediawikiIGraph = true;
                            break;
                        }
                    }
                }

                if (xwikiIGraphImages.length > 0 || containsMediawikiIGraph) {
                    // DOM and jQuery have been prepared for portalwiki JS, so we can load it now.
                    require(['portalwiki'], function() {
                        // Transfer all data-igraph-src attributes to be src attributes on <img>s on the page. This mechanism is used to defer loading of actual iGraph images until after
                        // portalwiki JS has a chance to run its initialization; otherwise, on XWiki, it won't do any initialization until after *every* iGraph image on a page is loaded.
                        xwikiIGraphImages.each(function(i, e) {
                            var $e = $(e);
                            // Transition data-igraph-src to src attribute, adding on the &iGHrefresh parameter to guarantee a non-cached graph image is always obtained
                            $e.attr({
                                'src': $e.attr('data-igraph-src'), 'data-igraph-src': null
                            });
                        });
                    });
                }
            }, 100);
        });
    });


    // Polyfill for jQuery.browser.
    // Extracted from jQuery Migrate 1.x: http://code.jquery.com/jquery-migrate-1.4.1.js
    // jQuery.browser is relied upon by portalwiki in several instances.
    function polyfillJQueryBrowser(jQuery) {
        jQuery.uaMatch = function( ua ) {
            ua = ua.toLowerCase();

            var match = /(chrome)[ \/]([\w.]+)/.exec( ua ) ||
                /(webkit)[ \/]([\w.]+)/.exec( ua ) ||
                /(opera)(?:.*version|)[ \/]([\w.]+)/.exec( ua ) ||
                /(msie) ([\w.]+)/.exec( ua ) ||
                ua.indexOf("compatible") < 0 && /(mozilla)(?:.*? rv:([\w.]+)|)/.exec( ua ) ||
                [];

            return {
                browser: match[ 1 ] || "",
                version: match[ 2 ] || "0"
            };
        };

        // Don't clobber any existing jQuery.browser in case it's different
        if ( !jQuery.browser ) {
            matched = jQuery.uaMatch( navigator.userAgent );
            browser = {};

            if ( matched.browser ) {
                browser[ matched.browser ] = true;
                browser.version = matched.version;
            }

            // Chrome is Webkit, but Webkit is also Safari.
            if ( browser.chrome ) {
                browser.webkit = true;
            } else if ( browser.webkit ) {
                browser.safari = true;
            }

            jQuery.browser = browser;
        }
    }
}

