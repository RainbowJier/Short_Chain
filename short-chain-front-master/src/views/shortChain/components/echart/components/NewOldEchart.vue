<template>
  <div class="frr_item newold">
    <div class="header">新老访客</div>
    <div>
      <div ref="chart" class="echarts"></div>
    </div>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'
const chart = ref(null)
const newOldTitle = ['product', '新访客', '旧访客']
const newOldList = [
  { product: 'Mon', 新访客: 43.3, 旧访客: 85.8 },
  { product: 'Tue', 新访客: 55, 旧访客: 32 },
  { product: 'Wed', 新访客: 25, 旧访客: 65 },
  { product: 'Thu', 新访客: 90, 旧访客: 76 },
  { product: 'Fri', 新访客: 88, 旧访客: 67 },
  { product: 'Sat', 新访客: 35, 旧访客: 59 },
  { product: 'Sun', 新访客: 53, 旧访客: 83 }
]
const init = () => {
  const myChart = echarts.init(chart.value)
  const option = {
    legend: {},
    tooltip: {},
    dataset: {
      dimensions: newOldTitle,
      source: newOldList
    },
    xAxis: { type: 'category' },
    yAxis: {},
    series: [{ type: 'bar', itemStyle: { color: '#FF9600' } }, { type: 'bar' }]
  }
  option && myChart.setOption(option)
}
onMounted(() => {
  init()
})
</script>
<style lang="less" scoped>
.echarts {
  width: 100%;
  height: 500px;
}

.header {
  margin-bottom: 30px;
  font-size: 16px;
  font-weight: bold;
  color: #000;
  display: inline-block;
  border-bottom: 1px solid #f0f0f0;
}
</style>
