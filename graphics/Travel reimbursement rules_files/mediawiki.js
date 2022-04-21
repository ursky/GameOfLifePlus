/**
* Scripts to support Mediawiki syntaxes
*/

require(['jquery'], function($){
    // See https://code.amazon.com/packages/IntranetMediaWikiJS/blobs/mainline/--/configuration/mediawiki/extensions/JS/scripts/ToggleSectionJQuery.js
    $(document).ready(function() {
        // Handle the show/hide links in section headers
        $(".toggleclass").click(function (e) {
            e.preventDefault();
            toggleText(this);
            $(this).parent().parent().next(".collapsible").toggle();
        });

        // Handle <toggledisplay> sections
        $(".toggledisplay").click(function (e) {
            e.preventDefault();
            toggleText(this);
            $(this).next().toggle();
        });

        function toggleText(element)
        {
            if ($(element).text() == "show") {
                $(element).text("hide");
            }
            else {
                $(element).text("show");
            }
        }
    });
});
