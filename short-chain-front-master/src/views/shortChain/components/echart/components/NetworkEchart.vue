<template>
  <div class="frr_item visping">
    <div class="header">访问网络</div>
    <div>
      <div ref="chart" class="echarts"></div>
    </div>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'
const chart = ref()
const init = () => {
  const myChart = echarts.init(chart.value)

  const option = {
    legend: {},
    tooltip: {},
    dataset: {
      dimensions: ['product', '无线网络', '移动数据'],
      source: [
        { product: 'Mon', 无线网络: 43.3, 移动数据: 85.8 },
        { product: 'Tue', 无线网络: 55, 移动数据: 32 },
        { product: 'Wed', 无线网络: 25, 移动数据: 65 },
        { product: 'Thu', 无线网络: 90, 移动数据: 76 },
        { product: 'Fri', 无线网络: 88, 移动数据: 67 },
        { product: 'Sat', 无线网络: 35, 移动数据: 59 },
        { product: 'Sun', 无线网络: 53, 移动数据: 83 }
      ]
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
