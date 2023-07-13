<template>
  <el-container style="height: calc(100vh - 15px);">
    <el-aside width="200px" style="background-color: rgb(238, 241, 246)">
      <el-menu
          :default-active="active"
          class="el-menu-vertical-demo"
          background-color="#545c64"
          text-color="#fff"
          active-text-color="#ffd04b"
          :router="true">
        <el-menu-item index="websocket" :route="{name: 'websocket'}">
          <i class="el-icon-setting"></i>
          <span slot="title">发送</span>
        </el-menu-item>

        <el-menu-item index="websocket2" :route="{name: 'websocket2'}">
          <i class="el-icon-setting"></i>
          <span slot="title">接收</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header style="text-align: right; font-size: 12px">
      </el-header>

      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import websocket from './websocket/index'

export default {
  components: {
    websocket
  },
  name: "index",
  data(){
    return{
      active: 'websocket'
    }
  },
  methods: {
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    }
  }
}
</script>

<style scoped>

</style>
