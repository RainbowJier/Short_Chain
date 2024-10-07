<template>
  <div class="scheme">
    <div class="scheme_item" v-for="item, index in data.productList">
      <div class="title">
        {{ item.title }}
      </div>
      <div class="price">
        <div>
          <span>{{ item.level == 'FIRST' ? '免费' : `￥${item.amount}元/月` }}</span>
          <span class="oldPrice" v-show="item.level !== 'FIRST'">￥{{ item.oldAmount }}元/月</span>
        </div>
        <a-button type="primary" class="button" @click="toPay(index)">
          {{ item.level == 'FIRST' ? '立即使用' : '立即购买' }}
        </a-button>
      </div>
      <div class="describe">
        <div class="describe_item" v-for="subItem in item.detail">
          <span>{{ subItem }}</span>
          <check-outlined :style="{ color: '#FF9600' }" />
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { CheckOutlined } from '@ant-design/icons-vue';
import { onMounted, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { getProductList } from '../../api/shopServer'
import emitter from '../../utils/eventBus';

const data = reactive({
  productList: []
})

/**
 * 获取商品列表数据
 */
const detailChange = (val) => {
  const text = val.text.split('||').map((i) => {
    if (i.includes('{{dayTimes}}')) {
      return i.replace('{{dayTimes}}', val.dayTimes)
    }
    return i
  })
  return text
}
const getProductListData = async () => {
  const res = await getProductList()
  if (res.code === 0) {
    const list = res.data.map((i, index) => {
      return {
        id: i.id,
        title: i.title,
        amount: i.amount,
        oldAmount: i.oldAmount,
        detail: detailChange({ text: i.detail, dayTimes: i.dayTimes }),
        level: i.level
      }
    })
    data.productList = list
  }
}
onMounted(() => {
  getProductListData()
})

/**
 * 立即购买
 */
const router = useRouter()
const toPay = (val) => {
  if (val == 'FIRST') {
    router.push('/shortChain')
    emitter.emit('shortChain', 2)
    return
  }
  router.push({
    path: '/payPage',
    query: {
      ...data.productList[val]
    }
  })
}



</script>
<style lang='less' scoped>
@import '~@/style/commonColor.less';

.button {
  background-color: @minorColor;
  border-color: @minorColor;
  width: 90px;
  margin-top: 10px;
}

.scheme {
  width: 1200px;
  display: flex;
  flex: 1;
  margin: auto;
  overflow-y: auto;
  align-items: center;
  justify-content: space-around;

  .scheme_item {
    width: 300px;
    height: 70vh;
    background-color: #fff;


    .title {
      font-size: 24px;
      height: 100px;
      display: flex;
      justify-content: center;
      align-items: center;
      background: linear-gradient(0deg, #ff9601 0%, #ff6600 100%);
      color: #fff;
      font-weight: bold;
    }

    .price {
      height: 100px;
      font-size: 20px;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      border-bottom: 1px solid #b0b0b0;

      .oldPrice {
        color: #b0b0b0;
        margin-left: 10px;
        font-size: 12px;
        text-decoration: line-through;
      }
    }

    .describe {
      padding: 20px 30px;

      .describe_item {
        font-size: 20px;
        text-align: center;
        display: flex;
        justify-content: space-between;
        align-items: center;
        border-bottom: 1px solid #eee;
        margin-bottom: 20px;
      }
    }
  }
}
</style>