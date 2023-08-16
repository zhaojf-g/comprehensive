<template>
  <div class="navbar">
    <hamburger id="hamburger-container" :is-active="sidebar.opened" class="hamburger-container"
               @toggleClick="toggleSideBar"/>

    <breadcrumb id="breadcrumb-container" class="breadcrumb-container"/>

    <div class="right-menu">
      <!--      <template v-if="device!=='mobile'">-->
      <!--        <search id="header-search" class="right-menu-item" />-->

      <!--        <error-log class="errLog-container right-menu-item hover-effect" />-->

      <!--        <screenfull id="screenfull" class="right-menu-item hover-effect" />-->

      <!--        <el-tooltip content="Global Size" effect="dark" placement="bottom">-->
      <!--          <size-select id="size-select" class="right-menu-item hover-effect" />-->
      <!--        </el-tooltip>-->

      <!--      </template>-->

      <el-dropdown class="avatar-container right-menu-item hover-effect" trigger="click">
        <div class="avatar-wrapper">
          <svg-icon icon-class="user" style="font-size: 34px" class="user-avatar"></svg-icon>
          <span style="font-size: 15px">&nbsp;{{ this.$store.getters.userName }}</span>
          <i class="el-icon-caret-bottom"/>
        </div>
        <el-dropdown-menu slot="dropdown">

          <el-dropdown-item divided @click.native="updatePassword">
            <span style="display:block;">修改密码</span>
          </el-dropdown-item>

          <el-dropdown-item divided @click.native="logout">
            <span style="display:block;">退出</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>


    <!--修改密码-->
    <el-dialog :title="$t('customer.updatePassword')" :visible.sync="udpFormShow" width="500px"
               :close-on-click-modal="false">
      <el-form ref="updatePasswordForm" :model="userPassword" label-width="80px" :rules="udpValidateRule">
        <el-form-item label="旧密码" prop="oldPassword">
          <el-input v-model="userPassword.oldPassword"></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="userPassword.newPassword"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="userPassword.confirmPassword"></el-input>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="udpFormShow = false">取消</el-button>
        <el-button type="primary" @click="passwordSubmit">确认</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {mapGetters} from 'vuex'
import hamburger from './Hamburger'
import breadcrumb from './Breadcrumb'

export default {
  name: 'NavbarIndex',
  components: {
    hamburger,
    breadcrumb
  },
  computed: {
    ...mapGetters([
      'sidebar'
    ])
  },
  data() {
    return {
      avatar: '',
      udpFormShow: false,
      userPassword: {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      },
      udpValidateRule: {
        oldPassword: [{
          required: true, trigger: 'blur'
        }],
        newPassword: [{
          required: true, trigger: 'blur'
        }],
        confirmPassword: [{
          required: true, trigger: 'blur'
        }]
      },
    }
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    async logout() {
      await this.$store.dispatch('user/logout')
      location.reload()
    },

    updatePassword() {
      this.userPassword = {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      }
      this.udpFormShow = true
    },
    passwordSubmit() {

    }
  }
}
</script>

<style lang="scss" scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, .08);

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background .3s;
    -webkit-tap-highlight-color: transparent;

    &:hover {
      background: rgba(0, 0, 0, .025)
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background .3s;

        &:hover {
          background: rgba(0, 0, 0, .025)
        }
      }
    }

    .avatar-container {
      margin-right: 30px;

      .avatar-wrapper {
        margin-top: 5px;
        position: relative;

        .user-avatar {
          cursor: pointer;
          width: 40px;
          height: 40px;
          border-radius: 10px;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>
