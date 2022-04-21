require(['jquery'], function($) {
    $(function(){
        const links = Array.from(document.querySelectorAll('a'));
        const linksData = links.map((linkElement) => {
            return {
                linkElement: linkElement,
            }
        });

        require(['IA'], function (IA) {
            IA.autoTrack(IA.CLICKED_LINK, {
                linksData: linksData
            });
        });
    });
})();
