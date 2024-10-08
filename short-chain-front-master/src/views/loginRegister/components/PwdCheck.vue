<template>
  <a-form :model="formState" name="basic" :label-col="{ span: 8 }" :wrapper-col="{ span: 16 }" autocomplete="off"
    @finish="onFinish" @finishFailed="onFinishFailed" class="pwdCheck" :rules="rules">
    <div class="title">设置密码</div>

    <!-- 图形验证码 -->
    <a-form-item label="图形验证码" name="pictureCode">
      <a-input v-model:value="formState.pictureCode" />
      <img src="http://api.link.open1024.com/account-server/api/notify/v1/captcha" class="pictureCode">
    </a-form-item>

    <!-- 手机验证码 -->
    <a-form-item label="手机验证码" name="phoneCode">
      <a-input v-model:value="formState.phoneCode" />
      <a-button type="primary" class="codeButton" @click="getPhoneCode" id="codeDom">获取验证码</a-button>
    </a-form-item>

    <!-- 密码 -->
    <a-form-item label="密码" name="password">
      <a-input-password v-model:value="formState.password" />
    </a-form-item>

    <!-- 确认密码 -->
    <a-form-item label="确认密码" name="confirm">
      <a-input-password v-model:value="formState.confirm" />
    </a-form-item>

    <!-- 上传用户头像 -->
    <UploadUserAvatar :changeAvatar='changeAvatar' />

    <!-- 提交 -->
    <a-form-item :wrapper-col="{ offset: 8, span: 16 }">
      <a-button type="primary" html-type="submit">提交</a-button>
    </a-form-item>

    <!-- 返回上一步 -->
    <a-form-item :wrapper-col="{ offset: 8, span: 16 }">
      <a-button type="primary" @click="prev">上一步</a-button>
    </a-form-item>
  </a-form>
</template>
<script setup>
import UploadUserAvatar from './UploadUserAvatar.vue'
import { message } from 'ant-design-vue';
import { reactive } from 'vue';
import { getCaptcha, getRegister } from '../../../api/accountServer'
const { toChangePop, userInfo } = defineProps(['toChangePop', 'userInfo'])

const formState = reactive({
  pictureCode: '',
  phoneCode: '',
  password: '',
  confirm: '',
  url: ''
});

const changeAvatar = (url) => {
  formState.url = url
}
const onFinish = values => {
  console.log('sucess:', values);
  if (!formState.url) {
    message.error('请上传头像！')
    return
  }
  getRegisterData()
};

const onFinishFailed = errorInfo => {
  console.log('Failed:', errorInfo);
};

/**
 * 确认密码校验
 */
let validatePass = async (_rule, value) => {
  if (value === '') {
    return Promise.reject('请输入确认密码！');
  } else if (value !== formState.password) {
    return Promise.reject("两次密码不一致！");
  } else {
    return Promise.resolve();
  }
};

/**
 * 校验规则
 */
const rules = {
  pictureCode: [{ required: true, trigger: 'blur', message: '请输入图形验证码!' }],
  phoneCode: [{ required: true, trigger: 'blur', message: '请输入手机验证码!' }],
  password: [{ required: true, trigger: 'blur', message: '请输入密码!' }, { pattern: /^.{6,16}$/, message: '请输入6~16位密码', trigger: 'blur' }],
  confirm: [{ required: true, validator: validatePass, trigger: 'blur' }]
};

/**
 * 上一步
 */
const prev = () => {
  toChangePop('register')
}

/**
 * 获取手机验证码
 */
//获取倒计时
const getPhoneCode = () => {
  const codeDom = document.getElementsByClassName('codeButton')[0]
  codeDom.setAttribute('disabled', true)
  let num = 30;
  let timer = null
  if (timer) {
    clearInterval(timer);
  }
  timer = setInterval(() => {
    num--;
    codeDom.innerText = `${num}秒后重新获取`;
    if (num === 0) {
      codeDom.disabled = false;
      codeDom.innerText = "重新获取验证码";
      clearInterval(timer);
    }
  }, 1000);
  getCaptchaData()
}
//获取手机验证码接口调用
const getCaptchaData = async () => {
  await getCaptcha({ captcha: formState.pictureCode, to: userInfo.phone })
}

/**
 * 注册接口
 */
const getRegisterData = async () => {
  const res = await getRegister({
    phone: userInfo.phone,
    pwd: formState.password,
    username: userInfo.username,
    code: formState.phoneCode,
    headImg: formState.url
  })
  if (res?.code === 0) {
    message.success('注册成功！')
    toChangePop('login')
  } else if (res?.code === 240003) {
    message.error('短信验证码错误！')
  } else if (res?.code === -1) {
    message.error('该手机号已注册！')
  }
}



</script>
<style lang='less' scoped>
:deep(#basic_pictureCode) {
  width: 130px;
}

:deep(#basic_phoneCode) {
  width: 130px;
}

.codeButton {
  width: 120px !important;
  margin-left: 36px;
}


.pictureCode {
  width: 120px;
  height: 32px;
  margin-left: 36px;
}
</style>