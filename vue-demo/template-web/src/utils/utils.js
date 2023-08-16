export default {
  install(Vue, options) {
    Vue.prototype.$copy = function(obj) {
      return Object.assign({}, obj)
    }
    Vue.prototype.$copy2 = function(obj1, obj2) {
      return Object.assign(obj1, obj2)
    }
  }
}
