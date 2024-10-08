<template>
  <div class="flowPacket">
    <div class="title">
      <i class="icon"></i>
      我的流量包
    </div>
    <a-table :columns="data.columns" :data-source="trafficList" :pagination="false">
      <template #bodyCell="{ column, record }">
      </template>
    </a-table>
    <a-pagination v-model:current="data.current" :total='trafficTotal' show-less-items @change='pageChange'
      :show-size-changer="false" class="pagination" />
  </div>
</template>
<script setup>
import { reactive, defineProps } from 'vue'
const { trafficList, trafficListPagination, trafficTotal } = defineProps(['trafficList', 'trafficListPagination', 'trafficTotal'])
/**
 * 流量包数据
 */
const data = reactive({
  columns: [
    { title: '流量包id', dataIndex: 'id' },
    { title: '流量包过期时间', dataIndex: 'expiredDate' },
    { title: '日短链总数', dataIndex: 'dayLimit' },
    { title: '日已用短链数', dataIndex: 'dayUsed' },
    { title: '日剩余短链数', dataIndex: 'surplus' },
  ],
  current: 1
})


// 分页切换
const pageChange = (val) => {
  trafficListPagination(val)
}



</script>
<style lang='less' scoped>
.flowPacket {
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