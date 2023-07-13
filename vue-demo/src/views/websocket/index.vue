<template>

  <div>
    <el-input v-model="url" placeholder="请输入websocket地址" style="width: 200px"></el-input>
    <el-button type="primary" @click="conn">连接</el-button>
    <br>

    <el-input v-model="topic" placeholder="请输入topic" style="width: 200px"></el-input>
    <el-input v-model="msg" placeholder="请输入msg" style="width: 200px"></el-input>
    <el-button v-if="!start" type="primary" @click="send" >发送</el-button>
    <el-button v-if="start" type="primary" @click="stop">停止</el-button>
    <el-button type="primary" @click="clear">清空</el-button>
    {{count}}
    <el-input
        type="textarea"
        :autosize="{ minRows: 20, maxRows: 40}"
        placeholder="请输入内容"
        v-model="textarea">
    </el-input>

  </div>
</template>

<script>
import websocket from "@/utils/websocket";

export default {
  name: "websocket",
  data() {
    return {
      url: 'http://192.168.8.120:51071',
      topic: '',
      msg: '',
      textarea: '',
      interval: '',
      count: 0,
      start: false,
      oldMsg: '',
    }
  },
  created() {

  },
  methods: {
    conn(){
      websocket.init(this.url + '/rfid/sockjs')
    },
    send() {



      this.start = true
      let vm = this
      this.interval = setInterval(function () {
        let data = {
          topic: vm.topic,
          message: vm.msg
        }
        websocket.send('/c2s', JSON.stringify(data))
        if(vm.oldMsg !== vm.msg){
          vm.oldMsg = vm.msg
          vm.textarea += ('发送成功：' + vm.msg + '\r\n')
        }

        vm.count++
      }, 1)

    },
    stop() {
      this.start = false
      clearInterval(this.interval)
    },
    clear() {
      this.oldMsg = ''
      this.count = 0
      this.textarea = ''
    }
  }
}
</script>

<style scoped>

</style>
