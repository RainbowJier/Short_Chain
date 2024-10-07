<template>
  <div class="personal_content">
    <div class="title">
      <i class="icon"></i>
      我的信息
    </div>
    <div class="userInfo" v-show="userInfo?.headImg">
      <img :src="userInfo?.headImg">
      <div class="text">
        <div class="name">昵称：{{ userInfo?.username }}</div>
        <div class="num">
          <span>手机号：{{ userInfo?.phone }}</span>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { defineProps } from 'vue';
const { userInfo } = defineProps(['userInfo'])
</script>
<style lang='less' scoped>
@import '~@/style/commonColor.less';

.personal_content {
  background-color: #fff;
  flex: 1;
  padding: 20px;

  .title {
    font-size: 24px;
    margin-bottom: 10px;
    display: flex;
    align-items: center;

    .icon {
      display: inline-block;
      width: 3px;
      height: 38px;
      background-color: #FF9600;
      margin-right: 10px;
    }
  }

  .userInfo {
    display: flex;
    align-items: center;
    padding: 30px;
    box-shadow: 4px 16px 16px 4px rgb(0 0 0 / 10%);

    img {
      width: 100px;
      height: 100px;
      border-radius: 50%;
      margin-right: 30px;
    }

    .text {
      .name {
        font-size: 24px;
        margin-bottom: 20px;
      }

      .num {
        span {
          i {
            color: @minorColor;
          }

          &:not(:last-child) {
            padding-right: 10px;
            margin-right: 10px;
            border-right: 1px solid @minorColor;
          }
        }
      }
    }
  }
}
</style>