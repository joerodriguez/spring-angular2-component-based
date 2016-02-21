/**
 *  Turn on full stack traces in errors to help debugging
 * */
Error.stackTraceLimit = Infinity;

jasmine.DEFAULT_TIMEOUT_INTERVAL = 1000;

// Cancel Karma's synchronous start
__karma__.loaded = function () { };

var pathsMapping = function (path) {
    var pathFilter = new RegExp(path.replace('/', '\\/') + '.*\/.*\.js$');
    var pathReplace = new RegExp('^' + path.replace('/', '\\/'));
    // console.log(pathFilter.source, pathReplace.source);


    return Object.keys(window.__karma__.files)
        .filter(function (path) { return pathFilter.test(path); })
        // .filter(function (path) { console.log(path); return true; })
        .reduce(function (systemjsPaths, karmaPath) {
            /**
             * Creates local module name mapping to global path with karma's fingerprint in path, e.g.:
             * './hero.service': '/base/dist/scripts/hero.service.js?f4523daf879cfb7310ef6242682ccf10b2041b3e'
             *
             * and, when using GoogleAppEngine:
             * './hero.service': '/base/dist/static/scripts/hero.service.js?f4523daf879cfb7310ef6242682ccf10b2041b3e'
             */
            var systemjsPath = karmaPath.replace(pathReplace, './').replace(/\.js$/, '');
            systemjsPaths[systemjsPath] = karmaPath + '?' + window.__karma__.files[karmaPath];
            return systemjsPaths;
        }, {});
};

/**
 * Setup SystemJS to correctly
 * locate user modules served by Karma.
 */
System.config({
    packages: {
        '/base/build/app/': {
            defaultExtension: "js",
            format: 'register',
            map: pathsMapping('/base/build/app/')
        },
        '/base/build/lib/': {
            defaultExtension: "js",
            format: 'register',
            map: pathsMapping('/base/build/lib/')
        }
    }
});

var specFiles =
    Object.keys(window.__karma__.files)
        .filter(function (path) { return /\.test\.js$/.test(path); })
        // .filter(function (path) { console.log(path); return true; })
        .map(function (moduleName) { return System.import(moduleName); });

System
    .import('angular2/platform/browser').then(function (b) { b.BrowserDomAdapter.makeCurrent(); })
    .then(function () { return Promise.all(specFiles); })
    .then(function () { __karma__.start(); }, function (error) { __karma__.error(error.stack || error); });
