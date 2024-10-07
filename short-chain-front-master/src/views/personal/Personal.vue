<template>
  <div class="personal">
    <div class="personal_left">
      <div v-for="i, index in data.list" :class="{ tab: tab == index }" @click="tabClick(index)">{{ i }}</div>
    </div>
    <div class="personal_right">
      <!-- 用户信息 -->
      <UserInfo v-if='tab == 0' :userInfo='data.userInfo' />
      <!-- 流量包管理 -->
      <FlowPacket v-else-if='tab == 1' :trafficList='trafficList.list' :trafficTotal='trafficList.trafficTotal'
        :trafficListPagination='trafficListPagination' />
      <!-- 订单管理 -->
      <OrderManagement v-else='tab == 2' :orderList='orderList.list' :orderPagination='orderPagination'
        :orderTotal='orderList.orderTotal' />
    </div>
  </div>
</template>
<script setup>
import UserInfo from './components/UserInfo.vue'
import OrderManagement from './components/OrderManagement.vue'
import FlowPacket from './components/FlowPacket.vue'
import { getUserInfo, getTraffic, } from '../../api/accountServer'
import { getOrderList } from '../../api/shopServer'
import { reactive, ref, onMounted } from 'vue';
import dayjs from 'dayjs'
const data = reactive({
  list: ['账号信息', '流量包信息', '订单信息'],
  userInfo: {
    headImg: '',
    username: '',
    phone: '',
  },
})
const tab = ref(localStorage.getItem('userTab') || 0)


/**
 * 获取用户信息
 */
const getUserInfoData = async () => {
  const res = await getUserInfo()
  if (res.code === 0) {
    data.userInfo.headImg = res.data.headImg
    data.userInfo.username = res.data.username
    data.userInfo.phone = res.data.phone
  }
}
onMounted(() => {
  getUserInfoData()
})


/**
 * 获取流量包数据
 */
const trafficList = reactive({
  list: [],
  trafficTotal: null,
})
const timeChange = (time) => {
  return dayjs(time).format('YYYY-MM-DD HH:mm:ss')
}
const getTrafficData = async (page) => {
  const res = await getTraffic({
    page: page || 1,
    size: 10
  })
  if (res.code === 0) {
    const list = res.data.current_data.map((i, index) => {
      return {
        key: index + 1,
        id: JSON.parse(JSON.stringify(i.id)),
        expiredDate: timeChange(i.expiredDate),
        dayLimit: i.dayLimit,
        dayUsed: i.dayUsed,
        surplus: i.dayLimit - i.dayUsed
      }
    })
    trafficList.list = list
    trafficList.trafficTotal = res.data.total_record
  }
}
onMounted(() => {
  getTrafficData()
})
//流量包列表分页切换
const trafficListPagination = (page) => {
  getTrafficData(page)
}



/**
 * 获取订单列表
 */
const orderList = reactive({
  list: [],
  orderTotal: null
})
const stateData = {
  NEW: '新订单',
  CANCEL: '未支付',
  PAY: '已支付',
}
const getOrderListData = async (page) => {
  const res = await getOrderList({
    page: page || 1,
    size: 10
  })
  if (res.code === 0) {
    const list = res.data.current_data.map((i, index) => {
      return {
        key: index + 1,
        id: JSON.parse(JSON.stringify(i.id)),
        productAmount: i.productAmount,
        buyNum: i.buyNum,
        createTime: timeChange(i.createTime),
        payAmount: i.payAmount,
        state: stateData[i.state]
      }
    })
    orderList.list = list
    orderList.orderTotal = res.data.total_record
  }
}
onMounted(() => {
  getOrderListData()
})
//订单列表分页切换
const orderPagination = (page) => {
  getOrderListData(page)
}




/**
 * tab切换
 */
const tabClick = (val) => {
  tab.value = val
  localStorage.setItem('userTab', val)
}












</script>
<style lang='less' scoped>
@import '~@/style/commonColor.less';

.personal {
  width: 1200px;
  display: flex;
  flex: 1;
  margin: auto;
  overflow-y: auto;

  .personal_left {
    background-color: #fff;
    width: 200px;

    .tab {
      color: @minorColor;
      background-color: #ff960030 !important;
    }

    div {
      height: 40px;
      line-height: 40px;
      padding: 0 20px;

      &:hover {
        background-color: #f5f5f5;
      }
    }
  }

  .personal_right {
    padding: 20px;
    display: flex;
    flex: 1;
  }
}
</style>