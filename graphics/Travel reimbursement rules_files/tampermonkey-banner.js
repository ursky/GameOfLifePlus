require(['jquery'], function($) {
    function TampermonkeyWarningBanner(id) {
        this.tampermonkeyWarningBanner = $(id);
        this.tampermonkeyWarningDismissButton = $(id + ' a.close');

        this.show = function() {
            this.tampermonkeyWarningBanner.show();
        }
    }

    $(document).ready(function() {
        var banner = new TampermonkeyWarningBanner("#tampermonkey-warning");
        var dismissed = XWiki.cookies.read("tm_banner_dismiss");

        banner.tampermonkeyWarningDismissButton.on('click', function() {
            confirmDismiss();
        });

        if (dismissed !== "true") {
            banner.show();
        }
    });

    function confirmDismiss() {
        XWiki.cookies.create("tm_banner_dismiss", true);
    }
 });