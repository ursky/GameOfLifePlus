require(['jquery', 'bootstrap'], function($) {
    var editSourceFunction = debounce(function () {
        var title = fetchTitle('xwikidoctitleinput');
        var pageReference = document.childNodes[1].getAttribute("data-xwiki-document");
        var contentElement = document.getElementById('content');
        if (contentElement != null) {
            var doc = contentElement.value;
            var words = doc.match(/(\s|^|\(|\)|\=|\*|\\\|\||\"|\'|\#|\^|\-\-|\_\_|\/\/|\,\,|gr[ea]y\/)((([Bb])lack([ ])?([Dd])ay(s)?)|(white([ ])?([Dd])ay(s?))|(master)|(slave[s]?)|((white([ ])?([Ll])ist)(ed|ing|s)?)|((([Bb])lack([ ])?([Ll])ist)(ed|ing|s)?))(\s|$|\.|\!|\?|\,|\(|\)|\=|\*|\||\"|\'|\#|\^|\-\-|\_\_|\/\/|\,\,)(?!(@)|([^{]*{{\/(html|box|code|SIMForm|iframe|formula|plantUML|gallery))|(([^[]*)]])|([^{]*}})|([\w\s\.\"\/\-\=\:\?\,]*[\>])|(([^<>]*)<\/pre>)|(([\w\s\.\"\/\-\=\:]*)<\/a>))/gmi) || [];
            var exclude_title = (title.match(/(run|cook)[-_ ]?book|SOP|dash[-_ ]?board/gi) || []).length;
            exclude_title += (pageReference.match(/(run|cook)[-_ ]?book|SOP/gi) || []).length;

            var graph_count = (doc.match(/{{:?([^|]*?)(graph)/gi) || []).length;
            var has_graph = false;
            if (graph_count > 2) {
                has_graph = true;
            }

            var lower_case_matched_words = words.map(function(word) {
                return word.toLowerCase();
            });

            if (lower_case_matched_words.includes('master') && lower_case_matched_words.includes('slave')) {
                var word_count = (doc.match(/(\s|^|\(|\)|\=|\*|\\\|\||\"|\'|\#|\^|\-\-|\_\_|\/\/|\,\,|gr[ea]y\/)((([Bb])lack([ ])?([Dd])ay(s)?)|(white([ ])?([Dd])ay(s?))|(master)|(slave[s]?)|((white([ ])?([Ll])ist)(ed|ing|s)?)|((([Bb])lack([ ])?([Ll])ist)(ed|ing|s)?))(\s|$|\.|\!|\?|\,|\(|\)|\=|\*|\||\"|\'|\#|\^|\-\-|\_\_|\/\/|\,\,)(?!(@)|([^{]*{{\/(html|box|code|SIMForm|iframe|formula|plantUML|gallery))|(([^[]*)]])|([^{]*}})|([\w\s\.\"\/\-\=\:\?\,]*[\>])|(([^<>]*)<\/pre>)|(([\w\s\.\"\/\-\=\:]*)<\/a>))/gmi) || []).length;
                editWordCheck(word_count, has_graph, exclude_title);
            } else {
                var word_count = (doc.match(/(\s|^|\(|\)|\=|\*|\\\|\||\"|\'|\#|\^|\-\-|\_\_|\/\/|\,\,|gr[ea]y\/)((([Bb])lack([ ])?([Dd])ay(s)?)|(white([ ])?([Dd])ay(s?))|(slave[s]?)|((white([ ])?([Ll])ist)(ed|ing|s)?)|((([Bb])lack([ ])?([Ll])ist)(ed|ing|s)?))(\s|$|\.|\!|\?|\,|\(|\)|\=|\*|\||\"|\'|\#|\^|\-\-|\_\_|\/\/|\,\,)(?!(@)|([^{]*{{\/(html|box|code|SIMForm|iframe|formula|plantUML|gallery))|(([^[]*)]])|([^{]*}})|([\w\s\.\"\/\-\=\:\?\,]*[\>])|(([^<>]*)<\/pre>)|(([\w\s\.\"\/\-\=\:]*)<\/a>))/gmi) || []).length;
                editWordCheck(word_count, has_graph, exclude_title);
            }
        }
    },500)

    var whatYouSeeEditFunction = debounce(function () {
        var title = fetchTitle('xwikidoctitleinput');
        var pageReference = document.childNodes[1].getAttribute("data-xwiki-document");

        if (!document.getElementsByClassName('cke_wysiwyg_frame').length || !document.getElementsByClassName('cke_wysiwyg_frame')[0].contentWindow.document.getElementsByTagName("html").length) {
            return;
        }

        var doc = document.getElementsByClassName('cke_wysiwyg_frame')[0].contentWindow.document.getElementsByTagName("html")[0].innerHTML;
        var words = doc.match(/\b(black[-_ ]?day(s)?\b|\bwhite[-_ ]?day(s)?\b|\bslave\b|\bmaster\b|\bblack[-_ ]?list(ing|ed|s)?\b|\bwhite[-_ ]?list(ing|ed|s)?\b)(?!([\w\s\.\"\/\-\=\:\;\&\#\?\%]*[\>])|(([\w\s\.\"\/\-\=\:\;\&\#\?\%]*)<\/a>)|(([\w\s\.\"\/\-\=\:\_\(\)\,\&]*)<\/pre>)|(([\w\-\.]*)@))/gi) || [];

        var lower_case_matched_words = words.map(function(word) {
            return word.toLowerCase();
        });

        var exclude_title = (title.match(/(run|cook)[-_ ]?book|SOP|dash[-_ ]?board/gi) || []).length;
        exclude_title += (pageReference.match(/(run|cook)[-_ ]?book|SOP/gi) || []).length;

        var has_graph = false;
        if ($('script[data-requiremodule="portalwiki"]').length > 0) {
            has_graph = true;
        }

        if (lower_case_matched_words.includes('master') && lower_case_matched_words.includes('slave')) {
            var word_count = (doc.match(/\b(black[-_ ]?day(s)?\b|\bwhite[-_ ]?day(s)?\b|\bslave\b|\bmaster\b|\bblack[-_ ]?list(ing|ed|s)?\b|\bwhite[-_ ]?list(ing|ed|s)?\b)(?!([\w\s\.\"\/\-\=\:\;\&\#\?\%]*[\>])|(([\w\s\.\"\/\-\=\:\;\&\#\?\%]*)<\/a>)|(([\w\s\.\"\/\-\=\:\_\(\)\,\&]*)<\/pre>)|(([\w\-\.]*)@))/gi) || []).length;
            editWordCheck(word_count, has_graph, exclude_title);
        } else {
            var word_count = (doc.match(/\b(black[-_ ]?day(s)?\b|\bwhite[-_ ]?day(s)?\b|\bslave\b|\bblack[-_ ]?list(ing|ed|s)?\b|\bwhite[-_ ]?list(ing|ed|s)?\b)(?!([\w\s\.\"\/\-\=\:\;\&\#\?\%]*[\>])|(([\w\s\.\"\/\-\=\:\;\&\#\?\%]*)<\/a>)|(([\w\s\.\"\/\-\=\:\_\(\)\,\&]*)<\/pre>)|(([\w\-\.]*)@))/gi) || []).length;
            editWordCheck(word_count, has_graph, exclude_title);
        }
    },500)


    // For preview, if the user wants to remove the exception, the form should include the 'removeException' input ( removeException != null)
    // If it is, we should highlight the words for the user
    var pageHasExclusion = function() {
        return ($('#removeExceptionCheckboxStore')[0] == null
                || $('#removeExceptionCheckboxStore')[0].innerText != 'true')
                && ($('#content-scanner-box')[0] != null);
    }

    $(document).ready(function() {
        if (window.location.pathname.startsWith('/bin/view') && exclusionTagExists() === false) {
            inclusiveLanguageCheck();
        } else if (window.location.pathname.startsWith('/bin/preview') && !pageHasExclusion()) {
            inclusiveLanguageCheck();
        } else if (window.location.pathname.startsWith('/bin/edit')) {
            let urlParams = new URLSearchParams(window.location.search);
            let editor = urlParams.get('editor');
            if (editor === 'wiki') {
                // Run scan first on-load to detect terms
                editSourceFunction();
                document.addEventListener('keyup', editSourceFunction);
            } else {
                document.observe('amazon:xwiki:ckeditor:instance:created', function () {
                    if (CKEDITOR !== undefined) {
                        CKEDITOR.on('instanceReady', function (event) {
                            // Run scan first on-load to detect terms
                            whatYouSeeEditFunction();

                            event.editor.document.on('keyup', whatYouSeeEditFunction);
                        });
                    }
                });
            }
        }
    });
});

let nonInclusiveBannerText = 'As part of Amazon\'s company-wide ongoing inclusive language <a href="https://w.amazon.com/bin/view/InclusiveTech" target="_blank">goal</a>, we are replacing non-inclusive terms. To learn what needs to change or to review recommended inclusive alternatives, visit this <a href="https://w.amazon.com/bin/view/EE/Programs/Inclusive_Tech/Guidelines/#HWhatneedstochange3F" target="_blank">wiki page</a>. Attend an <a href="https://w.amazon.com/bin/view/EE/Programs/Inclusive_Tech/Get_Involved#HAttendaWorkshop" target="_blank">inclusive language workshop</a>, or become an <a href="https://w.amazon.com/bin/view/EE/Programs/Inclusive_Tech/Get_Involved" target="_blank">Inclusive Tech Ambassador</a> to understand the importance and impact non-inclusive language perpetuates. If this usage cannot be safely replaced yet, you can find information about Exceptions on this <a href="https://w.amazon.com/bin/view/InclusiveTech/FAQ/#HFAQsforExceptions" target="_blank">wiki page</a>.';

function fetchTitle(titleElementId) {
    // Always set title to the document title so we have a fallback title
    var title = document.title;
    var titleElement = document.getElementById(titleElementId);

    // If the title input element is present and not empty, then use it
    if (titleElement != null) {
        // For edit modes, use element value
        // For view mode, use innerHTML value
        if (titleElement.value != null && titleElement.value !== "") {
            title = titleElement.value;
        } else if (titleElement.innerHTML != null && titleElement.innerHTML !== "") {
            title = titleElement.innerHTML;
        }
    } else if (title != null) {
        // If the title isn't null and we didn't get into the above condition
        // Then we are probably in a section edit, so split on the section editing structure
        // This will return the full title untouched (no-op) if " (§" is not present in the string
        title = title.split(" (§")[0];
    } else {
        // Just to be extra safe, we default to an empty string if even the document.title is null (which should never happen)
        title = "";
    }

    return title;
}

function exclusionTagExists() {
    var tagsElement = document.getElementById('xdocTags');
    if (tagsElement != null) {
        var tags = tagsElement.innerHTML;

        if (tags.indexOf('inclusive_tech_exception') !== -1) {
            return true;
        } else {
            return false;
        }
    }
}

function inclusiveLanguageCheck() {
    var docElement = document.getElementById('xwikicontent');
    if (docElement != null) {
        var title = fetchTitle('document-title');

        var pageReference;

        // Try to fetch child node 0 (for preview)
        if (document.childNodes[0].attributes != null ) {
          pageReference = document.childNodes[0].getAttribute("data-xwiki-document");
        } else {
          pageReference = document.childNodes[1].getAttribute("data-xwiki-document");
        }

        var doc = docElement.innerHTML;
        var matched_words = doc.match(/\b(black[-_ ]?day(s)?\b|\bwhite[-_ ]?day(s)?\b|\bslave\b|\bmaster\b|\bblack[-_ ]?list(ing|ed|s)?\b|\bwhite[-_ ]?list(ing|ed|s)?\b)(?!([\w\s\.\"\/\-\=\:\;\&\#\?\%]*[\>])|(([\w\s\.\"\/\-\=\:\;\&\#\?\%]*)<\/a>)|(([\w\s\.\"\/\-\=\:\_\(\)\,\&]*)<\/pre>)|(([\w\-\.]*)@))/gi) || [];

        var lower_case_matched_words = matched_words.map(function(word) {
            return word.toLowerCase();
        });

        var has_graph = false;

        // If the portalwiki JS is required (brought in by IGraph template/macros)
        // OR (because of race conditions, the portalwiki JS might not be loaded by the time this is called), if there are > 2 instances of the monitorportal link being used as a src
        if (document.querySelectorAll('script[data-requiremodule="portalwiki"]').length > 0 || (doc.match(/src="https:\/\/monitorportal.amazon.com/g) || []).length > 1) {
            has_graph = true;
        }

        var exclude_title = (title.match(/(run|cook)[-_ ]?book|SOP|dash[-_ ]?board/gi) || []).length;
        exclude_title += (pageReference.match(/(run|cook)[-_ ]?book|SOP/gi) || []).length;

        if (lower_case_matched_words.includes('master') && lower_case_matched_words.includes('slave')) {
            var word_count = (matched_words || []).length;
            viewWordCheckWithMaster(word_count, has_graph, exclude_title);
        } else {
            var word_count = (doc.match(/\b(black[-_ ]?day(s)?\b|\bwhite[-_ ]?day(s)?\b|\bslave\b|\bblack[-_ ]?list(ing|ed|s)?\b|\bwhite[-_ ]?list(ing|ed|s)?\b)(?!([\w\s\.\"\/\-\=\:\;\&\#\?\%]*[\>])|(([\w\s\.\"\/\-\=\:\;\&\#\?\%]*)<\/a>)|(([\w\s\.\"\/\-\=\:\_\(\)\,\&]*)<\/pre>)|(([\w\-\.]*)@))/gi) || []).length;
            viewWordCheckNoMaster(word_count, has_graph, exclude_title);
        }
    }
}

// Creates invisible p tags to denote highlighted text start/stop for screen readers
function injectAccessibilityPreAndPostHighlightElements() {
    document.querySelectorAll('mark').forEach((highlightedElement) => {
        let preHighlightElement = document.createElement('span');
            preHighlightElement.setAttribute('style', 'display: none');
            preHighlightElement.innerHTML = 'begin highlight';

            let postHighlightElement = document.createElement('span');
            postHighlightElement.setAttribute('style', 'display: none');
            postHighlightElement.innerHTML = 'end highlight';

            highlightedElement.before(preHighlightElement);
            highlightedElement.after(postHighlightElement);
    });
}

function insertViewBanner() {
    removeWikiBanner();

    var banner = document.createElement('div');
    banner.setAttribute('id', 'inclusive-banner-view');

    banner.innerHTML = 'The wiki you are viewing contains non-inclusive words. ' + nonInclusiveBannerText;
    var sitewideAnnouncementBanner = document.getElementById('sitewide-announcement-banner');
    if (sitewideAnnouncementBanner != null) {
        sitewideAnnouncementBanner.appendChild(banner);
    }
}

function viewWordCheckNoMaster(word_count, has_graph, exclude_title) {
   if (word_count > 0 && !has_graph && exclude_title === 0) {
        require(['jquery', 'mark'], function($) {
            $("#xwikicontent").markRegExp(/((\s|^|\>|\(|\)|\=|\*|\\|\||\"|\'|\#|\^|\-\-|\_\_|\/\/|\,\,)((black[-_ ]?day(s)?)|(white[-_ ]?day(s)?)|(slave[s]?)|(white[-_ ]?list(ing|ed|s)?)|(black[-_ ]?list(ing|ed|s)?))(\b|\<|\s|$|\.\s|\!\s|\?\s|\,|\(|\)|\=|\*|\||\"|\'|\#|\^|\-\-|\_\_|\/\/|\,\,|\#|\^|\-\-|\_\_|\/\/|\,\,))(?!(([\w\-\.]*)@))/gmi, {
                "exclude": [
                    ".wikigeneratedlinkcontent",
                    ".wikilink",
                    ".box",
                    ".wikiinternallink",
                    "pre",
                    "a"
                ],
                "done": function(counter){
                    if(counter > 0){
                        insertViewBanner();
                        injectAccessibilityPreAndPostHighlightElements();
                    }
                },
            });

            $('mark').css({'background-color':'#F9D5E2', 'padding': '0em'});
        });
    }
}

function viewWordCheckWithMaster(word_count, has_graph, exclude_title) {
    if (word_count > 0 && !has_graph && exclude_title === 0) {
        require(['jquery', 'mark'], function($) {
            $("#xwikicontent").markRegExp(/((\s|^|\>|\(|\)|\=|\*|\\|\||\"|\'|\#|\^|\-\-|\_\_|\/\/|\,\,)((black[-_ ]?day(s)?)|(white[-_ ]?day(s)?)|(slave[s]?)|(master)|(white[-_ ]?list(ing|ed|s)?)|(black[-_ ]?list(ing|ed|s)?))(\b|\<|\s|$|\.\s|\!\s|\?\s|\,|\(|\)|\=|\*|\||\"|\'|\#|\^|\-\-|\_\_|\/\/|\,\,|\#|\^|\-\-|\_\_|\/\/|\,\,))(?!(([\w\-\.]*)@))/gmi, {
                "exclude": [
                    ".wikigeneratedlinkcontent",
                    ".wikilink",
                    ".box",
                    ".wikiinternallink",
                    "pre",
                    "a"
                ],
                "done": function(counter){
                    if(counter > 0){
                        insertViewBanner();
                        injectAccessibilityPreAndPostHighlightElements();
                    }
                },
            });

            $('mark').css({'background-color':'#F9D5E2', 'padding': '0em'});
        });
    }
}

// Add a hidden element to the page to trigger the screen reader alert
function addAlertElement(id, message) {
    let alertElement = document.createElement("p");
    alertElement.setAttribute('id', id);
    alertElement.setAttribute('style', 'font-size: 0px');
    alertElement.setAttribute('role', 'alert');
    let alertElementText = document.createTextNode(message);
    alertElement.appendChild(alertElementText);
    document.body.appendChild(alertElement);
}

function insertEditBanner() {
    var banner = document.createElement('div');

    banner.setAttribute('id', 'inclusive-banner-top');
    let editBannerText = 'The wiki you are editing contains non-inclusive words. ' + nonInclusiveBannerText;
    banner.innerHTML = editBannerText;
    // Used to announce to screen readers

    addAlertElement('inclusive-banner-show-alert', 'The wiki you are editing contains non-inclusive words. We recommend taking proactive action and replacing any non-inclusive language yourself.');
    let hideAlertMessage = document.getElementById('inclusive-banner-hide-alert');
    if (hideAlertMessage != null) {
        hideAlertMessage.remove();
    }

    var sitewideAnnouncementBanner = document.getElementById('sitewide-announcement-banner');
    if (sitewideAnnouncementBanner != null) {
        sitewideAnnouncementBanner.appendChild(banner);
    }

    var banner = document.getElementById('data-warning-box');
    if (banner != null) {
        banner.insertAdjacentHTML('afterend', "<div id='inclusive-banner-bottom'>" + editBannerText + "</div>");
    }
}

function removeElement(elementId) {
    let elementToRemove = document.getElementById(elementId);
    if (elementToRemove != null) {
        elementToRemove.remove();
    }
}

function editWordCheck(word_count, has_graph, exclude_title) {
    if (word_count == 0) {
        removeElement('inclusive-banner-top');
        removeElement('inclusive-banner-bottom');

        if (document.getElementById('inclusive-banner-show-alert') != null) {
          removeElement('inclusive-banner-show-alert');
          // Used to announce to screen readers
          addAlertElement('inclusive-banner-hide-alert', 'All non-inclusive terms were removed from the document.');
        }

        var sitewidebannertext = document.getElementById('sitewide-announcement-banner-text');
        if (sitewidebannertext != null) {
            sitewidebannertext.style.display = 'block';
        }
    }

    if (word_count > 0 && document.getElementById('inclusive-banner-bottom') == null && !has_graph && document.getElementById('inclusive-banner-top') == null && exclude_title === 0) {
        removeWikiBanner();
        insertEditBanner();
    }
}

function removeWikiBanner() {
    var sitewidebannertext = document.getElementById('sitewide-announcement-banner-text');
    if (sitewidebannertext != null) {
        sitewidebannertext.style.display = 'none';
    }
}

function debounce(func, wait, immediate) {
    var timeout;
    return function() {
        var context = this, args = arguments;
        var later = function() {
            timeout = null;
            if (!immediate) func.apply(context, args);
        };
        var callNow = immediate && !timeout;
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
        if (callNow) func.apply(context, args);
    };
}