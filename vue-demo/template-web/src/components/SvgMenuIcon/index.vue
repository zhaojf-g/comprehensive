<template>
  <span :id="iconId" :class="svgClass" style="font-size: 20px"></span>
</template>

<script>

import { getToken } from '@/utils/auth'

export default {
  name: 'SvgMenuIcon',
  props: {
    iconClass: {
      type: String,
      required: true
    },
    className: {
      type: String,
      default: ''
    },
    size: {
      type: Number,
      default: 14
    }
  },
  data() {
    return {
      svgDom: '',
      iconId: ''
    }
  },
  computed: {

    svgClass() {
      if (this.className) {
        return 'svg-menu-icon' + this.className
      } else {
        return 'svg-menu-icon'
      }
    }
  },
  created() {
    this.iconId = this.guid()
  },
  mounted() {
    this.getSvg()
  },
  methods: {
    // 初始化svg
    getSvg: function() {
      try {
        /* 创建xhr对象 */
        const xhr = new XMLHttpRequest()
        this.svgUrl = '/rfidpcs/static/icons/' + this.iconClass + '.svg'
        xhr.open('GET', this.svgUrl, true)
        xhr.setRequestHeader('X-Token', getToken())
        xhr.send()

        /* 监听xhr对象 */
        xhr.addEventListener('load', () => {
          const resXML = xhr.responseXML
          if (resXML) {
            this.svgDom = resXML.documentElement.cloneNode(true)
            // this.svgDom.
            this.svgDom.setAttribute('width', '')
            this.svgDom.setAttribute('height', '')
            this.svgDom.setAttribute('viewBox', '0,0,1024,1024')
            this.svgDom.setAttribute('class', 'svg')
            this.svgDom.setAttribute('style', 'width: 1em;height: 1em;vertical-align: middle;fill: currentColor;overflow: hidden;')
            const paths = this.svgDom.children
            for (let i = 0; i < paths.length; i++) {
              paths[i].removeAttribute('fill')
            }
            const span = document.getElementById(this.iconId)
            span.appendChild(this.svgDom)
          }
        })
      } catch (e) {
        console.log(e)
      }
    },

    parseDom(arg) {
      const objE = document.createElement('div')
      objE.innerHTML = arg
      return objE.childNodes[0]
    },

    guid() {
      return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        const r = Math.random() * 16 | 0
        const v = c === 'x' ? r : (r & 0x3 | 0x8)
        return v.toString(16)
      })
    }
  }
}
</script>

<style>
.svg-menu-icon {
  margin-right: 5px;
  margin-bottom: 15px;
}
</style>
