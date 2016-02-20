var browserSync = require('browser-sync');
var proxyMiddleware = require('http-proxy-middleware');

var proxy = proxyMiddleware('/api', {
    target: 'http://localhost:8080',
    changeOrigin: true
});

browserSync.init("build/**/*", {
    server: {
        baseDir: 'build',
        middleware: [proxy]
    }
});