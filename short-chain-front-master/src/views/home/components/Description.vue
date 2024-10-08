<template>
  <div class="description_out">
    <div class="description">
      <div class="description_title">
        功能特性
      </div>
      <div class="description_con">
        <div class="con_item" v-for="i in data">
          <img :src="i.image">
          <div class="sub_title">{{ i.title }}</div>
          <div class="con_text">{{ i.description }}</div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import lianjie from '../../../assets/lianjie.png'
import yongjiu from '../../../assets/yongjiu.png'
import fangwen from '../../../assets/fangwen.png'
import baobiao from '../../../assets/baobiao.png'
import xiugai from '../../../assets/xiugai.png'
import piliang from '../../../assets/piliang.png'
import suiji from '../../../assets/suiji.png'
import fenzu from '../../../assets/fenzu.png'
const data = [
  {
    image: lianjie,
    title: '链接缩短',
    description: '将原始网页链接快速生成短链接，美化链接，减少链接所占字数，便于短信营销、分渠道传播。'
  },
  {
    image: yongjiu,
    title: '永久有效',
    description: '无需担心过期失效，生成的短链接永久有效。'
  },
  {
    image: fangwen,
    title: '访问统计',
    description: '短链接自带访问量统计功能，并记录每次访问信息，生成包含地区分布、设备信息等在内的专业统计图表。'
  },
  {
    image: baobiao,
    title: '报表分享',
    description: '将数据访问报表通过链接、图片、小程序码的形式分享给其他人。'
  },
  {
    image: xiugai,
    title: '随时修改',
    description: '短链接跳转的原始链接随时可修改，投放工作不再受到约束。'
  },
  {
    image: piliang,
    title: '批量创建',
    description: '批量创建可以快速将大量的原始链接转换为短链接，操作便捷高效。'
  },
  {
    image: suiji,
    title: '随机跳转',
    description: '多个原始链接转为一条短链，访问者随机跳转不同的网址，随机、记忆两种跳转模式让AB测试、广告投放更容易。'
  },
  {
    image: fenzu,
    title: '分组管理',
    description: '不同类型、不同用途的链接使用分组进行管理，链接再多也能方便管理。'
  },
]
</script>
<style lang='less' scoped>
.description_out {
  background-color: #f2f1f1;

  .description {
    width: 1200px;
    margin: auto;
    padding: 50px 0;

    .description_title {
      font-size: 24px;
      margin-bottom: 50px;
    }

    .description_con {
      display: flex;
      justify-content: space-around;
      flex-wrap: wrap;

      .con_item {
        width: 25%;
        display: flex;
        flex-direction: column;
        align-items: center;
        padding: 30px 0;

        img {
          width: 60px;
          height: 60px;
          margin-bottom: 20px;
        }

        .sub_title {
          font-size: 20px;
          margin-bottom: 20px;
        }

        .con_text {
          font-size: 14px;
          width: 220px;
        }
      }
    }
  }
}
</style>