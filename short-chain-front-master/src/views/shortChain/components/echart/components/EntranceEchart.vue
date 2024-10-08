<template>
  <div ref="chart" class="echarts"></div>
</template>
<script setup>
import { ref, onMounted, defineProps } from 'vue'
import * as echarts from 'echarts'
const { item } = defineProps(['item'])
const chart = ref(null)
const init = () => {
  const myChart = echarts.init(chart.value)
  const option = {
    // 图例
    legend: {},
    series: [
      {
        data: item.equipmentList,
        // 设置饼图半径，第一项内半径，第二项外半径
        radius: ['40%', '70%'],
        // 设置环形图的文本标签
        label: {
          show: true,
          formatter: '{d}%',
          // outside外侧，inside内测
          position: 'inside'
        },
        type: 'pie'
      }
    ],
    itemStyle: {
      borderRadius: 5,
      borderColor: '#fff',
      borderWidth: 2
    },
    graphic: [
      {
        type: 'text',
        left: 'center',
        top: '48%',
        left: '45%',
        style: {
          text: item.equipment,
          textAlign: 'center',
          fill: '#000100',
          fontSize: 14
        }
      },
      {
        type: 'text',
        left: '10%',
        top: '85%',
        style: {
          text: item.equipmentList.length ? `${item.equipmentList[0]?.name}: ${item.equipmentList[0]?.value}次` : '',
          fill: '#000100',
          fontSize: 14
        }
      },
      {
        type: 'text',
        left: '55%',
        top: '85%',
        style: {
          text: item.equipmentList.length > 1 ? `${item.equipmentList[1]?.name}: ${item.equipmentList[1]?.value}次` : '',
          fill: '#000100',
          fontSize: 14
        }
      },
      {
        type: 'text',
        left: '10%',
        top: '90%',
        style: {
          text: item.equipmentList.length > 2 ? `${item.equipmentList[2]?.name}: ${item.equipmentList[2]?.value}次` : '',
          fill: '#000100',
          fontSize: 14
        }
      },
      {
        type: 'text',
        left: '55%',
        top: '90%',
        style: {
          text: item.equipmentList.length > 3 ? `${item.equipmentList[3]?.name}: ${item.equipmentList[3]?.value}次` : '',
          fill: '#000100',
          fontSize: 14
        }
      },
      {
        type: 'text',
        left: '10%',
        top: '95%',
        style: {
          text: item.equipmentList.length > 4 ? `${item.equipmentList[4]?.name}: ${item.equipmentList[4]?.value}次` : '',
          fill: '#000100',
          fontSize: 14
        }
      },
      {
        type: 'text',
        left: '55%',
        top: '95%',
        style: {
          text: item.equipmentList.length > 5 ? `${item.equipmentList[5]?.name}: ${item.equipmentList[5]?.value}次` : '',
          fill: '#000100',
          fontSize: 14
        }
      },
    ]
  }
  option && myChart.setOption(option)
}
onMounted(() => {
  init()
})
</script>

<style lang="less" scoped>
.echarts {
  width: 50%;
  height: 300px;
}
</style>
