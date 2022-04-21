const LOCAL_STORAGE_TARGET = 'wikiSourceContent';
const LOCAL_STORAGE_TARGET_LOCATION = 'wikiSourceContentLocation';
const LOCAL_STORAGE_TARGET_CACHING_TIME = 'wikiSourceContentCachingTime';
const LOCAL_STORAGE_TARGET_CURRENT_VERSION = 'wikiSourceContentCurrentVersion';
const LOCAL_STORAGE_EXPIRATION_HOURS = 3;

/*
 * This object interacts with the browser's localStorage and stores
 * the source from the CKEditor or text input every 30 seconds in the case that a 
 * save fails the content is stored in the browser and can be restored
 * if the user comes back and has content in localStorage they are presented
 * with the option to reload that content.
 *
 * example of usage:
 * var wikiLocalStorage = new wikiLocalStorage('wikiSourceContent', 'xwikieditcontent');
 * then you can call the methods on the new object
 * wikiLocalStorage.start();
 *
 * @param (string) divSibling = The ID of the div that we will insert of div before
*/
function wikiLocalStorage(divSibling) {

    var currentUrl = window.location.href;
    // retrieving html tag (which is only one in the entire document) to get a xwiki page version.
    var currentVersion = document.getElementsByTagName('html')[0].dataset.xwikiVersion;
    // this the button we want to add an event to which will clear localStorage
    // on click. Add more button names here if you our interest in other buttons grows
    var buttonsWeCareAbout = ['action_cancel'];

    self = this;

    this.start = function() {
        var sourceContent = localStorage.getItem(LOCAL_STORAGE_TARGET);
        var sourceUrl = localStorage.getItem(LOCAL_STORAGE_TARGET_LOCATION);
        var sourceVersion = localStorage.getItem(LOCAL_STORAGE_TARGET_CURRENT_VERSION);
        // Check to see if there’s any data in local storage.
        // If there is and the current version of editing page equals the one in the local storage then show Reload section.
        if (sourceContent && sourceContent != '' && sourceUrl == currentUrl && currentVersion == sourceVersion) {
            this.displayReload();
        } else {
            // start local storage loop
            this.startLoop();
        }
        // append a listener to clear when save is clicked
        this.addButtonListeners();
    },

    this.startLoop = function() {
        setInterval(function() {
            self.saveToLocalStorage(currentUrl, self.getSource(), new Date().toISOString(), currentVersion);
        }, 30000);
    },

    this.addButtonListeners = function() {
        var submits = document.getElementsByTagName('input');
        submits = Array.prototype.slice.call(submits);
        submits.forEach(function(element) {
            // we only care about 1 button type 
            if (buttonsWeCareAbout.includes(element.name)) {
                element.addEventListener(
                    'click',
                    function() {
                        self.clearLocalStorage();
                    }
                );
            }
        });

    },

    this.getSource = function() {
        if (typeof CKEDITOR !== 'undefined') {
            var content = CKEDITOR.instances.content.getData();
        } else if (document.getElementById('content')) {
            var content = document.getElementById('content').value;
        }
        return content;
    },

    this.loadSource = function(sourceContent) {
        if (sourceContent != '' && typeof CKEDITOR !== 'undefined') {
            CKEDITOR.instances.content.setData(sourceContent);
            CKEDITOR.instances.content.execCommand('xwiki-refresh');
        } else if (sourceContent != '' && document.getElementById('content')) {
            document.getElementById('content').value = sourceContent;
        }
    },

    this.saveToLocalStorage = function(currentUrl, content, currentTime, currentVersion) {
        localStorage.setItem(LOCAL_STORAGE_TARGET_LOCATION, currentUrl);
        localStorage.setItem(LOCAL_STORAGE_TARGET, content);
        localStorage.setItem(LOCAL_STORAGE_TARGET_CACHING_TIME, currentTime);
        localStorage.setItem(LOCAL_STORAGE_TARGET_CURRENT_VERSION, currentVersion);
    },

    this.retrieveContentFromLocalStorage = function() {
        return localStorage.getItem(LOCAL_STORAGE_TARGET);
     },

    this.displayReload = function() {
        var reloadDiv = document.createElement('div');
        var target = document.getElementById(divSibling);
        if (target) {
            var parentTarget = target.parentNode;
            parentTarget.insertBefore(reloadDiv, target);
            reloadDiv.id = 'reloadDiv';
            reloadDiv.innerHTML = '<a href="/bin/view/AmazonWiki/LocalStorage/">You have content stored from your last session. </a>';
            reloadDiv.innerHTML += '<input type="button" id="sourceReloadButton" name="sourceReloadButton" value="Reload Content" class="btn btn-default" />';
            reloadDiv.innerHTML += ' <input type="button" id="sourceCancelButton" name="sourceCancelButton" value="Cancel Reload" class="btn btn-default" />';
            // set the button listeners 
            var reloadButton = document.getElementById('sourceReloadButton');
            var cancelButton = document.getElementById('sourceCancelButton');
            reloadButton.addEventListener('click', function() {
                var sourceContent = self.retrieveContentFromLocalStorage();
                self.loadSource(sourceContent);
                self.removeReloadDiv();
            });
            cancelButton.addEventListener('click', function() {
                self.clearLocalStorage();
                self.removeReloadDiv();
                self.startLoop();
            });
        }
    },

    this.clearLocalStorage = function() {
        localStorage.removeItem(LOCAL_STORAGE_TARGET);
        localStorage.removeItem(LOCAL_STORAGE_TARGET_LOCATION);
        localStorage.removeItem(LOCAL_STORAGE_TARGET_CACHING_TIME);
        localStorage.removeItem(LOCAL_STORAGE_TARGET_CURRENT_VERSION);
    },

    this.removeReloadDiv = function() {
        var reloadDiv = document.getElementById('reloadDiv');
        if (reloadDiv) {
            reloadDiv.parentNode.removeChild(reloadDiv);
        }
    }
}

// works for everything except MS IE 8 and before. Acceptable for our internal customers.
document.addEventListener('DOMContentLoaded', function(event) {
    var sourceStorage = new wikiLocalStorage('flex-layout');
    // both editors have the xwikidoctitleinput input field so we can safely assume we're
    // in the editor and start the localStorage loop
    if (document.getElementById('xwikidoctitleinput') &&
        document.getElementById('xwikimaincontainer').dataset.private == 'false') {
        sourceStorage.start();
    } else {
        var localStorageLastSaveTime = localStorage.getItem(LOCAL_STORAGE_TARGET_CACHING_TIME);
        var currentTime = new Date();
        if (localStorageLastSaveTime && Date.parse(localStorageLastSaveTime) <=
            currentTime.setHours(currentTime.getHours() - LOCAL_STORAGE_EXPIRATION_HOURS)) {
            sourceStorage.clearLocalStorage();
        }
    }
});
