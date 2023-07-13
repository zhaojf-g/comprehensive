<template>
  <div id="app">
<!--    <main-test></main-test>-->
    <router-view></router-view>
  </div>
</template>

<script>

import mainTest from "@/views/index";
export default {
  name: 'App',
  components: {mainTest},
}
</script>

<style>

body {
  display: block;
  margin: 0;
}
</style>
