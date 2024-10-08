<template>
  <div class="frr_item">
    <div class="header">访问来源TOP10</div>
    <div>
      <div ref="chart" class="echarts"></div>
    </div>
  </div>
</template>
<script setup>
import { ref, onMounted, defineProps } from 'vue'
import * as echarts from 'echarts'
const chart = ref(null)
const { sourceData } = defineProps(['sourceData']);

onMounted(() => {
  init()
})


const init = () => {
  const myChart = echarts.init(chart.value)
  //   定义对象的内容
  const option = {
    xAxis: {
      show: false,
    },
    yAxis: {
      boundaryGap: true,
      type: 'category',
      data: sourceData.regionList,
      inverse: true,
      axisLine: {
        show: false
      },
      axisTick: {
        show: false
      },
      axisLabel: {
        inside: true,
        color: 'blue',
        margin: 200,
      }
    },
    series: [
      {
        backgroundStyle: {
          color: 'rgba(0, 0, 0, 0.05)',
        },
        showBackground: true,
        realtimeSort: true,
        type: 'bar',
        data: sourceData.pvCountList,
        label: {
          show: true,
          position: 'right',
          color: 'red',
          fontSize: 16,
        },
        itemStyle: { color: '#ff96003d' }
      }
    ],
    legend: {
      show: true
    },
  }
  option && myChart.setOption(option)
}

</script>

<style lang="less" scoped>
.echarts {
  width: 100%;
  height: 450px;
}

.header {
  font-size: 16px;
  font-weight: bold;
  color: #000;
  display: inline-block;
  border-bottom: 1px solid #f0f0f0;
}
</style>
