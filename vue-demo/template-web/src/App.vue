<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>
body {
  margin: 0;
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "\5FAE\8F6F\96C5\9ED1", Arial, sans-serif
}

.content_div {
  /*height: calc(100vh - 100px);*/
  padding: 20px
}
.el-table-header{
  background-color: #f8f8f9;
}
</style>
