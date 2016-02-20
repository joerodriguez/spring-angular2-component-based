var fs = require('fs-extra');

var files = {
    "es6": "node_modules/es6-shim/es6-shim.min.js",
    "systemjs-polyfills": "node_modules/systemjs/dist/system-polyfills.js",
    "angular2-polyfills": "node_modules/angular2/bundles/angular2-polyfills.js",
    "systemjs": "node_modules/systemjs/dist/system.src.js",
    "rxjs": "node_modules/rxjs/bundles/Rx.js",
    "angular2": "node_modules/angular2/bundles/angular2.dev.js",
    "angular2-http": "node_modules/angular2/bundles/http.min.js"
};

try {
    Object.keys(files).forEach(function(key) {
        var val = files[key];
        fs.copySync(val, 'build/lib/' + key + '.js')
    });

    console.log("success!")
} catch (err) {
    console.error(err)
}
