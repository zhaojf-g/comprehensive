<template>

  <div>

    <el-input v-model="url" placeholder="请输入websocket地址" style="width: 200px"></el-input>
    <el-button type="primary" @click="conn">连接</el-button>
    <br>
    <el-input v-model="topic" placeholder="请输入topic" style="width: 200px"></el-input>

    <el-button type="primary" @click="jt">监听</el-button>
    <el-button type="primary" @click="clear">清空</el-button>
    {{ count }}
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
      textarea: '',
      count: 0,
      msg: ''
    }
  },
  created() {
  },
  methods: {
    conn(){
      websocket.init(this.url + '/rfid/sockjs')
    },
    jt() {
      let vm = this
      websocket.subscribe('/s2c/' + this.topic, function (msg) {

        if (vm.msg !== msg.body) {
          vm.msg = msg.body
          vm.textarea += '接收消息' + msg.body + '\r\n'
        }
        vm.count++
      })
      this.$message.success('监听' + this.topic)
    },
    clear() {
      this.msg = ''
      this.textarea = ''
      this.count = 0
      this.textarea = ''
    }
  }
}
</script>

<style scoped>

</style>
