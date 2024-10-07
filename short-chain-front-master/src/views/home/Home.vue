<template>
  <div class="home_out">
    <div class="home">
      <!-- 首页内容1 -->
      <div class="main_one_out">
        <div class="main_one">
          <div class="title">小滴短链平台</div>
          <div class="describe">简单易用的渠道短链接统计工具</div>
          <a-button shape="round" @click="toGo" class="button">创建短连接</a-button>
        </div>
      </div>

      <!-- 首页内容2 -->
      <Tab />

      <!-- 首页内容3 -->
      <Description />
      <div class="blank" />

      <!-- 首页内容4 -->
      <div class="main_four_out">
        <div class="main_four">
          <div class="title">
            简单易用的渠道短链接统计工具
          </div>
          <a-button @click="toGo">开始使用</a-button>
        </div>
      </div>
      <div class="beian">Copyright © 2018 Company,lnc.粤ICP备15092968号 Trems</div>
    </div>
  </div>
</template>
<script setup>
import Tab from './components/Tab.vue'
import Description from './components/Description.vue'
import router from '../../router/index'
import emitter from '../../utils/eventBus';

const toGo = () => {
  router.push('/shortChain')
  emitter.emit('shortChain', 2)
}
</script>
<style lang='less' scoped>
@import '~@/style/commonColor.less';

:deep(.ant-btn:hover) {
  border-color: @minorColor;
}

.home_out {
  .home {
    padding-bottom: 50px;

    .blank {
      height: 100px;
      background-color: #fff;
    }

    .beian {
      margin-top: 50px;
      text-align: center;
    }

    .main_four_out {
      background-color: #2C3134;
      height: 300px;
      color: #fff;

      .main_four {
        height: 100%;
        width: 1200px;
        margin: auto;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;

        .title {
          font-size: 40px;
          margin-bottom: 40px;
        }
      }
    }

    .main_one_out {
      background: url('../../assets/bg.jpg') no-repeat;
      background-position: center;

      .main_one {
        width: 1200px;
        height: 500px;
        margin: auto;
        display: flex;
        flex-direction: column;
        justify-content: center;

        .ant-btn-round {
          background-color: @minorColor;
          width: 100px;
          color: #fff;
        }

        .title {
          font-size: 60px;
        }

        .describe {
          font-size: 30px;
          margin-bottom: 40px;
        }

        .button {
          height: 40px;
          background-color: @minorColor;
          border-color: @minorColor;
        }
      }
    }
  }
}
</style>