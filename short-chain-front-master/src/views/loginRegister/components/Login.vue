<template>
  <a-form :model="formState" :rules="rules" :label-col="{ span: 8 }" :wrapper-col="{ span: 16 }" autocomplete="off"
    @finish="onFinish" @finishFailed="onFinishFailed" class="loginForm">
    <div class="title">登录账号</div>
    <!-- 手机号 -->
    <a-form-item label="手机号" name="phone">
      <a-input v-model:value="formState.phone" />
    </a-form-item>

    <!-- 密码 -->
    <a-form-item label="密码" name="password">
      <a-input-password v-model:value="formState.password" />
    </a-form-item>

    <!-- 记住账号信息 -->
    <!-- <a-form-item name="remember" :wrapper-col="{ offset: 8, span: 16 }">
      <a-checkbox v-model:checked="formState.remember">下次自动登录</a-checkbox>
      <span @click="toChangePwd" class="pwdcss">忘记密码？</span>
    </a-form-item> -->

    <!-- 登录按钮 -->
    <a-form-item :wrapper-col="{ offset: 8, span: 16 }">
      <a-button type="primary" html-type="submit">登录</a-button>
    </a-form-item>

    <!-- 去注册 -->
    <a-form-item :wrapper-col="{ offset: 8, span: 16 }">
      <a-button type="primary" @click="toChangePop('register')">去注册</a-button>
    </a-form-item>
  </a-form>
</template>
<script setup>
import router from '@/router';
import emitter from '@/utils/eventBus';
import { message } from 'ant-design-vue';
import { reactive, defineProps, ref } from 'vue';
import { getLogin } from '../../../api/accountServer'

const { toChangePop } = defineProps(['toChangePop'])
const formState = reactive({
  phone: '',
  password: '',
});
/**
 * 规则校验
 */
const rules = {
  phone: [
    { required: true, message: '请输入手机号!', trigger: 'blur' },
    { pattern: /^1[3|4|5|7|8][0-9]\d{8}$/, message: '请输入正确的手机号', trigger: 'blur' }],
  password: [
    { required: true, message: '请输入密码!', trigger: 'blur' },
    { pattern: /^.{6,16}$/, message: '请输入6~16位密码', trigger: 'blur' }],
};

/**
 * 登录按钮
 */
const lockLogin = ref(true)   //防止重复多次点击登录
const onFinish = values => {
  if (lockLogin.value) {
    lockLogin.value = false
    getLoginData({ phone: values.phone, password: values.password })
  }
};

/**
 * 错误提醒
 */
const onFinishFailed = errorInfo => {
  console.log('Failed:', errorInfo);
};

/**
 * 登录接口
 */
const getLoginData = async (val) => {
  const res = await getLogin({ phone: val.phone, pwd: val.password })
  if (res.code === 0) {
    localStorage.setItem('token', res.data)
    router.push('/shortChain')
    message.success('登录成功！')
    emitter.emit('shortChain', 2)
    emitter.emit('loginHeadImg')
  } else {
    message.error(res.msg)
  }
  lockLogin.value = true
}
</script>
<style lang='less'>
</style>