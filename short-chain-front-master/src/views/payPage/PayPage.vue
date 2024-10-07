<template>
  <div class="payPage">
    <div class="title">
      <i class="icon"></i>
      请选择你要购买的流量包
    </div>
    <div class="productMess">
      <div class="product_title">{{ data.title }}</div>
      <div class="mess">
        <div class="mess_left">
          <div v-for="item in data.detail">{{ item }}</div>
        </div>
        <div class="mess_right">
          <div class="price">单价：￥<span>{{ data.amount }}</span>/个</div>
          <span class="number">请选择购买个数：</span>
          <a-input-number id="inputNumber" v-model:value="numValue" :min="1" />
          <div class="priceAll">实付：￥<span>{{ data.amount * numValue }}</span></div>
        </div>
      </div>
    </div>
    <!-- <div class="agreement">
      <a-checkbox v-model:checked="checked" :style="{ fontSize: '20px' }" />
      <span class="tongyi">我已阅读并同意<a class="text">《用户服务协议》</a></span>
    </div> -->
    <div class="pay">
      <div class="pay_title">
        <i class="icon"></i>
        请选择您的支付方式
      </div>
      <div class="pay_mode">
        <div class="wechat"></div>
      </div>
      <a-button class="button" @click="wechatPay">立即支付</a-button>
    </div>
  </div>
  <!-- 微信支付二维码 -->
  <div class="wechatCode" v-if="codeShow">
    <div class="code">
      <close-outlined @click='closeCode' class="close" />
      <div class="title">微信扫描支付</div>
      <div id="wechatQrcode" v-if="codeShow"></div>
      <div class="price">￥{{ data.amount * numValue }}</div>
    </div>
  </div>
</template>
<script setup>
import { onMounted, onUnmounted, reactive, ref } from 'vue'
import { useRoute } from 'vue-router'
import { getOrder, getOrderToken, getOrderState } from '../../api/shopServer'
import QRcode from 'qrcodejs2'
import { CloseOutlined } from '@ant-design/icons-vue';
import { message } from 'ant-design-vue';
import router from '@/router';
// const checked = ref(true)

/**
 * 路由获取参数
 */
const route = useRoute()
const data = route.query

/**
 * 支付按钮
 */
const wechatPay = () => {
  getOrderData()
}

/**
 * 生成二维码
 */
const codeShow = ref(false)
const qrcode = (url) => {
  new QRcode('wechatQrcode', {
    width: 200,
    height: 200,
    text: url
  })

}

/**
 * 关闭二维码
 */
const closeCode = () => {
  codeShow.value = false
  clearInterval(timer.value)
}

/**
 * 下单接口
 */
const numValue = ref(1)
const getOrderData = async () => {
  const res = await getOrder({
    productId: Number(data.id),
    buyNum: Number(numValue.value),
    clientType: 'PC',
    payType: 'WECHAT_PAY',
    totalAmount: Number(data.amount) * Number(numValue.value),
    payAmount: Number(data.amount) * Number(numValue.value),
    billType: 'NO_BILL',
    billHeader: '',
    billContent: '',
    billReceiverPhone: '',
    billReceiverEmail: ''
  })
  if (res.code === 0) {
    codeShow.value = true
    setTimeout(() => {
      qrcode(res.data.code_url)
      autoQuery(res.data.out_trade_no)
    })
  }
}

/**
 * 轮询查询订单状态
 */
const timer = ref(null)
const autoQuery = (val) => {
  timer.value = setInterval(() => {
    getOrderStateData(val);
  }, 3000);
}
onUnmounted(() => {
  clearInterval(timer.value)
})


/**
 * 查询订单状态
 */
const getOrderStateData = async (val) => {
  const res = await getOrderState({ out_trade_no: val })
  if (res.code === 0) {
    if (res.data === 'PAY') {
      message.success('支付成功！')
      localStorage.setItem('userTab', 2)
      router.push('/personal')
    }
  }
}




/**
 * 获取下单令牌
 */
const getOrderTokenData = async () => {
  const res = await getOrderToken()
  if (res.code === 0) {
    localStorage.setItem('request-token', res.data)
  }
}
onMounted(() => {
  getOrderTokenData()
})



</script>
<style lang='less' scoped>
@import '~@/style/commonColor.less';

.wechatCode {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(55, 55, 55, 0.6);

  .code {
    width: 300px;
    height: 300px;
    transform: translate(-50%, -50%);
    background-color: #fff;
    position: absolute;
    top: 50%;
    left: 50%;
    display: flex;
    position: relative;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    position: relative;

    .close {
      position: absolute;
      right: 10px;
      top: 10px;
      font-size: 20px;
    }

    .title {
      font-size: 16px;
      font-weight: bold;
      margin-bottom: 10px;
    }

    .price {
      margin-top: 10px;
      color: red;
    }
  }
}

.payPage {
  position: relative;
  width: 1200px;
  display: flex;
  flex: 1;
  margin: auto;
  overflow-y: auto;
  background-color: #fff;
  padding: 50px;
  flex-direction: column;

  .pay {
    margin-top: 20px;

    .button {
      color: #fff;
      background-color: @minorColor;
      border-color: @minorColor;
      width: 150px;
      border-radius: 5px;
      text-align: center;
      font-size: 24px;
      height: 45px;
      margin-top: 20px;
    }

    .pay_mode {
      padding: 20px;
      display: flex;
      font-size: 20px;

      .wechat {
        margin-right: 20px;
        background-image: url('../../assets/pay_select.png');
        background-position: bottom;
        background-repeat: no-repeat;
        background-size: 100%;
        width: 200px;
        height: 100px;
      }
    }

    .pay_title {
      display: flex;
      align-items: center;
      font-size: 24px;
      margin-bottom: 20px;

      .icon {
        display: inline-block;
        width: 3px;
        height: 38px;
        background-color: @minorColor;
        margin-right: 10px;
      }
    }
  }

  .agreement {
    text-align: right;
    margin-top: 20px;

    :deep(.ant-checkbox-inner) {
      width: 20px;
      height: 20px;
    }

    .tongyi {
      margin-left: 10px;
    }

    .text {
      color: red;
    }

    span {
      font-size: 16px;
    }
  }

  .productMess {

    .product_title {
      color: #fff;
      background-color: @minorColor;
      border-radius: 5px;
      text-align: center;
      font-size: 24px;
      display: inline-block;
      padding: 10px;
    }

    .mess {
      padding: 20px;
      font-size: 16px;
      box-shadow: 4px 8px 10px 4px rgb(0 0 0 / 10%);

      .mess_left {
        padding-bottom: 10px;

        div {
          width: 250px;
          display: flex;
          justify-content: space-between;
          border: 1px solid @minorColor;
          padding: 10px;

          &:not(:last-child) {
            margin-bottom: 10px;
          }
        }
      }

      .mess_right {
        text-align: right;
        border-top: 1px solid #f0f0f0;
        padding-top: 10px;

        .number {
          color: @minorColor;
        }

        .priceAll {
          margin-top: 10px;

          span {
            font-weight: bold;
            font-size: 20px;
            color: @minorColor;
            display: inline-block;
          }
        }

        .price {
          margin-bottom: 10px;
        }
      }
    }
  }

  .title {
    font-size: 24px;
    margin-bottom: 30px;
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
}
</style>