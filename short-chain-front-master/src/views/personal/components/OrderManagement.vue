<template>
  <div class="orderManagement">
    <div class="title">
      <i class="icon"></i>
      订单管理
    </div>
    <a-table :columns="data.columns" :data-source="orderList" :pagination="false">
      <template #bodyCell="{ column, record }">
      </template>
    </a-table>
    <a-pagination v-model:current="data.current" :total='orderTotal' show-less-items @change='pageChange'
      :show-size-changer="false" class="pagination" />
  </div>
</template>
<script setup>
import { reactive } from 'vue'
import { defineProps } from 'vue';
const { orderList, orderPagination, orderTotal } = defineProps(['orderList', 'orderPagination', 'orderTotal'])
/**
 * 订单数据
 */
const data = reactive({
  columns: [
    { title: '订单号', dataIndex: 'id' },
    { title: '商品单价', dataIndex: 'productAmount' },
    { title: '付款数量', dataIndex: 'buyNum' },
    { title: '下单时间', dataIndex: 'createTime' },
    { title: '实付金额', dataIndex: 'payAmount' },
    { title: '订单状态', dataIndex: 'state' },
  ],
  current: 1
})

// 分页切换
const pageChange = (val) => {
  orderPagination(val)
}

</script>
<style lang='less' scoped>
.orderManagement {
  background-color: #fff;
  flex: 1;
  padding: 20px;

  .pagination {
    margin-top: 20px;
    text-align: right;
  }

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
}
</style>