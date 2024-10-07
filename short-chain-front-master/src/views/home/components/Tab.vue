<template>
  <div class="main_two_out">
    <div class="main_two">
      <div class="title">应用场景</div>
      <div class="tab">
        <div :class="{ bottom: index === tabNum }" v-for='(i, index) in data' @click="tabChange(index)">
          <img :src="i.icon">
          <span>{{ i.title }}</span>
        </div>
      </div>
      <div class="content_one" v-for='(i, index) in data' v-show="index === tabNum">
        <div class="text_left">
          <div class="text_title">{{ i.title }}</div>
          <div class="text_content" v-for="ii in i.list">
            <div>{{ ii.num }}</div>
            <div>{{ ii.text }}</div>
          </div>
        </div>
        <img class="text_right" :src="i.image" />
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref } from 'vue'
import dianshang from '../../../assets/dianshang.png'
import shequn from '../../../assets/shequn.png'
import tongji from '../../../assets/tongji.png'
import qudao from '../../../assets/qudao.png'
import spaceOne from '../../../assets/space1.png'
import spaceTwo from '../../../assets/space2.png'
import spaceThree from '../../../assets/space3.png'
import spaceFour from '../../../assets/space4.png'

const data = [
  {
    title: '电商平台',
    icon: dianshang,
    image: spaceOne,
    list: [
      {
        num: '01',
        text: '电商及社交等各类app根据不同业务场景进行分享时，使用短网址时可缩短链接口，便于分享传播，提高转化率。'
      },
      {
        num: '02',
        text: '外卖平台短信，通过短网址实时监控距离，提升用户体验！'
      },
      {
        num: '03',
        text: '短链接结合营销短信，在有限的字数里更多的表达，降本增效！'
      },
    ]
  },
  {
    title: '社群运营',
    icon: shequn,
    image: spaceTwo,
    list: [
      {
        num: '01',
        text: '生成渠道短链接，简洁文案链接，更精准触达用户！'
      },
      {
        num: '02',
        text: '根据短链点击数据，挖掘潜在用户，精准锁定目标人群！'
      },
    ]
  },
  {
    title: '数据统计',
    icon: tongji,
    image: spaceThree,
    list: [
      {
        num: '01',
        text: '无须技术开发，无需数据埋点，低成本接入。'
      },
      {
        num: '02',
        text: '多维度统计短网址点击数据，为精细化运营决策提供数据支持，帮助企业提高获客成本的投入产出比。'
      }
    ]
  },
  {
    title: '渠道分析',
    icon: qudao,
    image: spaceFour,
    list: [
      {
        num: '01',
        text: '无须技术开发，无需数据埋点，低成本接入为收集用户信息提供有力支撑。'
      },
      {
        num: '02',
        text: '以商用短链平台为依托，对各大渠道进行营销渠道分析/AB测试分析，提供数据、优化成本及效率，推动项目决策效果最大化落地！'
      }
    ]
  },
]

// tab切换点击
const tabNum = ref(0)
const tabChange = (val) => {
  tabNum.value = val
}
</script>
<style lang='less' scoped>
@import '~@/style/commonColor.less';

.main_two_out {
  background-color: #fff;

  .main_two {
    width: 1200px;
    padding: 50px 0;
    margin: auto;

    .title {
      font-size: 24px;
      margin-bottom: 50px;
    }

    .content_one {
      height: 378px;
      margin-top: 20px;
      display: flex;
      justify-content: space-around;
      box-shadow: 4px 0px 16px 4px rgb(0 0 0 / 10%);

      .text_right {
        width: 400px;
        height: 300px;
      }

      .text_left {
        width: 600px;

        .text_title {
          font-size: 20px;
          margin: 30px 0;
        }

        .text_content {
          margin-bottom: 20px;
          font-size: 16px;
        }

      }
    }

    .tab {
      font-size: 20px;
      display: flex;
      justify-content: space-around;

      .bottom {
        color: @minorColor;
      }

      .bottom::after {
        content: '';
        display: block;
        width: 100px;
        margin-top: 20px;
        margin-left: 44px;
        border-radius: 5px;
        border-bottom: 3px solid @minorColor;
      }

      span {
        margin-left: 20px;
      }
    }
  }
}
</style>