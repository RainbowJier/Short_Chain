<template>
  <div class="header_out">
    <div class="header">
      <div class="header_right">
        <span v-for="(i, index) in data.tabList" @click="toGo(i.path, index)"
          :class="{ fontColor: fontColor == index }">
          {{ i.title }}
        </span>
      </div>
      <!-- 未登录展示登录注册 -->
      <div class="header_left" @click="toGo('/loginRegister')" v-show="!showLoginRegister">
        <a-space>
          <user-outlined :style="{ fontSize: '20px', marginRight: '5px' }" />
        </a-space>
        <span>登录</span>/
        <span>注册</span>
      </div>
      <!-- 登录展示头像 -->
      <div class="header_img" v-show="showLoginRegister">
        <img :src="data.userInfo.headImg" @click="toPersonal">
        <a-card size="small" :title="data.userInfo.username" style="width: 150px" class="userMess">
          <div @click="outLogin" class="outLogin">退出登录</div>
        </a-card>
      </div>
    </div>
  </div>

</template>
<script setup>
import { UserOutlined } from '@ant-design/icons-vue';
import { message } from 'ant-design-vue';
import { onMounted, reactive, ref } from 'vue'
import router from '../router/index';
import emitter from '../utils/eventBus';
import { getUserInfo } from '../api/accountServer'
import noHeader from '../assets/noHeader.png'
/**
 * 初始化数据
 */
const data = reactive({
  userInfo: {
    username: '',
    headImg: '',
  },
  tabList: [
    { title: '小滴短链平台', path: '/home' },
    { title: '个人空间', path: '/personal' },
    { title: '我的短链', path: '/shortChain' },
    { title: '定价方案', path: '/scheme' },
    { title: '报表demo', path: '/demo' },
  ]
})
const showLoginRegister = ref(localStorage.getItem('token') || '');
const fontColor = ref(localStorage.getItem('tabIndex') || 0)

/**
 * 去注册登录
 */
const toGo = (val, index) => {
  tabState(index)
  router.push(val)
}
//tab选中
const tabState = (index) => {
  if (index !== 4) {
    localStorage.setItem('tabIndex', index)
    fontColor.value = index
  }
}
onMounted(() => {
  emitter.on('shortChain', (val) => {
    tabState(val)
  })
})

/**
 * 获取用户信息
 */
const getUserInfoData = async () => {
  const res = await getUserInfo()
  if (res.code === 0) {
    data.userInfo.headImg = res.data.headImg || noHeader
    data.userInfo.username = res.data.username || '测试角色'
  }
}
onMounted(() => {
  getUserInfoData()
})

/**
 * 去个人中心页面
 */
const toPersonal = () => {
  router.push('/personal')
  localStorage.setItem('tabIndex', 1)
}

/**
 * 退出登录
 */
const outLogin = () => {
  router.push('/loginRegister')
  localStorage.removeItem('token')
  emitter.emit('shortChain', 5)
  message.success('退出成功！')
  showLoginRegister.value = false
}


/**
 * 监听登录
 */
onMounted(() => {
  emitter.on('loginHeadImg', () => {
    showLoginRegister.value = true
    getUserInfoData()
  })
})

</script>
<style lang='less' scoped>
@import '~@/style/commonColor.less';

.edu {
  padding-bottom: 12px;
  border-bottom: 1px solid #f0f0f0;
}

.outLogin {
  cursor: pointer;

  &:hover {
    color: @minorColor;
  }
}

.header_out {
  background-color: @mainColor;

  .header {
    width: 1200px;
    height: 70px;
    margin: auto;
    color: #fff;
    font-size: 16px;
    display: flex;
    justify-content: space-between;
    align-items: center;

    .header_left:hover {
      color: @minorColor;
    }


    .header_left {
      cursor: pointer
    }

    .header_img {
      position: relative;
      height: 100%;
      display: flex;
      align-items: center;

      &:hover {
        .userMess {
          display: block;
        }
      }

      img {
        width: 50px;
        height: 50px;
        border-radius: 50%;
      }

      .userMess {
        display: none;
        box-shadow: 4px 0px 16px 4px rgb(0 0 0 / 10%);
        position: absolute;
        top: 70px;
        right: 0;
        color: @fontColor;
        z-index: 20;
        border-radius: 5px;

        p:hover {
          color: @minorColor;
        }
      }
    }

    .header_right {
      .fontColor {
        color: @minorColor
      }

      span {
        margin-right: 50px;
        cursor: pointer;

        &:nth-child(1) {
          margin-right: 80px;
          font-size: 24px;
          font-weight: bold;
        }

        &:hover {
          color: @minorColor;
        }
      }

    }
  }
}
</style>