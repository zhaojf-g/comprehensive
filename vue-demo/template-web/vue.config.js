const {defineConfig} = require('@vue/cli-service')
const path = require('path')
function resolve(dir) {
    return path.join(__dirname, dir)
}

module.exports = defineConfig({
    transpileDependencies: true,
    lintOnSave: false,
    configureWebpack: {
        resolve: {
            fallback: {path: require.resolve('path-browserify')}
        }
    },
    publicPath: './',
    outputDir: 'dist',
    assetsDir: 'view-static',
    devServer: {
        port: 9527,
        proxy: {
            '/tpms': {
                target: 'http://127.0.0.1:22510',
                secure: false,
                changeOrigin: true, // 是否改变域名：在本地会创建一个虚拟服务端，然后发送请求的数据，并同时接收请求的数据，这样服务端和服务端进行数据的交互就不会有跨域问题
                ws: true, // 是否启用websockets
                pathRewrite: {
                    // 路径重写
                    // '^/rfidpcs': '' // 用'/api'代替target里面的地址,比如我要调用'http://40.00.100.100:3002/user/add'，直接写'http://127.0.0.1:8080/user/add'
                }
            },
            '/asset': {
                target: 'http://127.0.0.1:22510',
                secure: false,
                changeOrigin: true, // 是否改变域名：在本地会创建一个虚拟服务端，然后发送请求的数据，并同时接收请求的数据，这样服务端和服务端进行数据的交互就不会有跨域问题
                ws: true, // 是否启用websockets
                pathRewrite: {
                    // 路径重写
                    '^/asset': '/rfidpcs/asset' // 用'/api'代替target里面的地址,比如我要调用'http://40.00.100.100:3002/user/add'，直接写'http://127.0.0.1:8080/user/add'
                }
            }
        }
        // open: true,
        // overlay: {
        //   warnings: false,
        //   errors: true
        // },
        // before: require('./mock/mock-server.js')

    },

    chainWebpack(config) {
        // set svg-sprite-loader
        config.module
            .rule('svg')
            .exclude.add(resolve('src/icons'))
            .end()
        config.module
            .rule('icons')
            .test(/\.svg$/)
            .include.add(resolve('src/icons'))
            .end()
            .use('svg-sprite-loader')
            .loader('svg-sprite-loader')
            .options({
                symbolId: 'icon-[name]'
            })
            .end()
    }
})
