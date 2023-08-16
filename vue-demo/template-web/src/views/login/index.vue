<template>
  <div class="login-container">

    <el-card class="login_div">
      <div style="text-align: center">
        <h3>{{ this.$store.getters.systemTitle }}</h3>
      </div>
      <br/>
      <el-form ref="loginForm" :rules="rules" :model="loginForm" style="text-align: center">

        <el-form-item prop="username">
          <el-input v-model="loginForm.username" placeholder="用户名" style="width: 350px;height: 25px">
            <svg-icon style="font-size: 24px;margin-top: 8px;" slot="prefix" icon-class="user"></svg-icon>
          </el-input>
        </el-form-item>

        <el-form-item prop="password">
          <el-input v-model="loginForm.password" placeholder="密  码" style="width: 350px;height: 25px"
                    show-password>
            <svg-icon style="font-size: 24px;margin-top: 8px" slot="prefix" icon-class="lock"></svg-icon>
          </el-input>
        </el-form-item>
      </el-form>

      <div style="width: 100%; text-align: center;margin-top: 70px">
        <el-button type="primary" style="width: 350px" @click="login" :loading="loading" >登录</el-button>
      </div>

    </el-card>


  </div>
</template>

<script>

export default {
  name: 'LoginIndex',
  data() {
    return {
      loading: false,
      loginForm: {
        username: '',
        password: ''
      },
      rules: {
        username: [{required: true, message: '账号不能为空！', trigger: 'blur'},],
        password: [{required: true, message: '密码不能为空！', trigger: 'blur'},]
      }
    }
  },
  methods: {
    login() {
      this.$refs['loginForm'].validate((valid) => {
        if (valid) {
          this.loading = true
          this.$store.dispatch('user/login', this.loginForm).then(() => {
            this.$router.push({path: '/'})
          }).catch(data => {
            this.$message.error(data.message)
          }).finally(() => {
            this.loading = false
          })
        }
      })

    }
  }
}
</script>

<style scoped>


.login-container {
  width: 100%;
  height: 100vh;
  background: url('../../assets/images/login-background.jpg') no-repeat;
  background-size: 100% 100%;
}

.login_div {
  position: absolute;
  height: 350px;
  width: 400px;
  margin-top: calc((100vh - 400px) * 0.45);
  margin-left: calc((100vw - 400px) * 0.5);
}

</style>

<style>
.login_div .el-input--medium .el-input__inner {
  height: 40px;
  line-height: 40px;
}

.login_div .el-form-item__error {
  padding-top: 6px;
  padding-left: 2px;
}
</style>
