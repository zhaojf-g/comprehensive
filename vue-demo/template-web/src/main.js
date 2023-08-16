import Vue from 'vue'
import App from './App.vue'
import router from './router'
import Element from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
// import 'normalize.css/normalize.css' // a modern alternative to CSS resets
import './styles/element-variables.scss'
import '@/styles/index.scss' // global css
import copy from './utils/utils'

import store from './store'
import './permission' // permission control

import i18n from './lang' // internationalization

import './icons' // icon

Vue.use(Element, {
    size: 'medium', // set element-ui default size
    i18n: (key, value) => i18n.t(key, value)
})
Vue.use(copy)
Vue.config.productionTip = false
new Vue({
    render: h => h(App),
    router,
    store,
    i18n
}).$mount('#app')
