<template>
  <div class="loginRegister">
    <div class="content">
      <!-- 登录弹窗 -->
      <Login v-if="stateShow === 'login'" :toChangePop='toChangePop' />
      <!-- 注册弹窗 -->
      <Register v-if="stateShow === 'register'" :toChangePop='toChangePop' :setUserInfo='setUserInfo' />
      <!-- 设置密码 -->
      <PwdCheck v-if="stateShow === 'pwdCheck'" :toChangePop='toChangePop' :userInfo='userInfo' />
    </div>
  </div>
</template>
<script setup>
import Login from './components/Login.vue'
import Register from './components/Register.vue'
import PwdCheck from './components/PwdCheck.vue'
import { reactive, ref } from 'vue';
/**
 * 切换登录、注册、设置密码弹窗展示
 */
const stateShow = ref('login')
const toChangePop = (val) => {
  stateShow.value = val
}

/**
 * 用户数据初始化
 */
const userInfo = reactive({
  phone: '',
  username: ''
})

/**
 * 注册弹窗中的手机号和昵称传给设置密码弹窗
 */
const setUserInfo = (val) => {
  userInfo.phone = val.phone
  userInfo.username = val.username
}

</script>
<style lang='less'>
.loginRegister {
  display: flex;
  flex-direction: column;
  height: 100%;

  .content {
    background: url('../../assets/login.jpg') no-repeat;
    background-position: center;
    background-size: 100%;
    flex: 1;
    display: flex;
    justify-content: center;
    align-items: center;

    .loginForm,
    .pwdCheck,
    .registerForm {
      padding: 30px 0;
      width: 500px;
      box-shadow: 4px 0px 16px 4px rgb(0 0 0 / 10%);

      .xieyi {
        color: red;
        position: absolute;
      }

      .pwdcss {
        cursor: pointer;
        margin-left: 100px;
      }

      .ant-row {
        margin-right: 70px;

        .ant-btn-primary {
          width: 246px;
        }
      }

      .title {
        font-size: 35px;
        text-align: center;
        margin-bottom: 30px;
      }
    }
  }
}
</style>