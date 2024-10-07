<template>
  <a-form :model="formState" name="basic" :rules="rules" :label-col="{ span: 8 }" :wrapper-col="{ span: 16 }"
    autocomplete="off" @finish="onFinish" @finishFailed="onFinishFailed" class="registerForm">
    <div class="title">注册账号</div>
    <!-- 用户名 -->
    <a-form-item label="用户名" name="username">
      <a-input v-model:value="formState.username" />
    </a-form-item>

    <!-- 手机号 -->
    <a-form-item label="手机号" name="phone">
      <a-input v-model:value="formState.phone" />
    </a-form-item>

    <!-- 用户协议 -->
    <a-form-item name="agree" :wrapper-col="{ offset: 8, span: 16 }">
      <a-checkbox v-model:checked="formState.agree">同意 用户注册协议</a-checkbox>
      <div class="xieyi" v-if="!formState.agree">请先同意用户协议!</div>
    </a-form-item>

    <!-- 下一步 -->
    <a-form-item :wrapper-col="{ offset: 8, span: 16 }">
      <a-button type="primary" html-type="submit">下一步</a-button>
    </a-form-item>

    <!-- 去登录 -->
    <a-form-item :wrapper-col="{ offset: 8, span: 16 }">
      <a-button type="primary" @click="toChangePop('login')">已有账号去登录</a-button>
    </a-form-item>
  </a-form>
</template>
<script setup>
import { reactive } from 'vue';
const { toChangePop, setUserInfo } = defineProps(['toChangePop', 'setUserInfo'])

const formState = reactive({
  username: '',
  phone: '',
  agree: true,
});
/**
 * 规则校验
 */
const rules = {
  username: [
    { required: true, message: '请输入用户名!', trigger: 'blur' },
    { pattern: /^.{2,10}$/, message: '请输入6~16位用户名', trigger: 'blur' }],
  phone: [
    { required: true, message: '请输入手机号!', trigger: 'blur' },
    { pattern: /^1[3|4|5|7|8][0-9]\d{8}$/, message: '请输入正确的手机号', trigger: 'blur' }],
};

const onFinish = values => {
  if (formState.agree) {
    toChangePop('pwdCheck')
    setUserInfo(values)
  }
};

const onFinishFailed = errorInfo => {
  console.log('Failed:', errorInfo);
};

</script>
<style lang='less' scoped>
</style>