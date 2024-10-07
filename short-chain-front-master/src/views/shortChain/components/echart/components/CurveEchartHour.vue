<template>
  <div class="visit_call">
    <div class="vist_header">
      <span>访问曲线-小时级别</span>
    </div>
    <div class="chart">
      <div class="left">
        <div class="numbs">总访问次数：{{ curData?.pvSum }}</div>
        <div class="peobs">总访问人数：{{ curData?.uvSum }}</div>
        <div class="ipbs">总访问IP数：{{ curData?.ipSum }}（小时去重）</div>
      </div>
      <div ref="charts" class="echarts"></div>
    </div>
  </div>
</template>
<script setup>
import { ref, onMounted, defineProps } from "vue";
import * as echarts from "echarts";
const { curData } = defineProps(['curData']);
const charts = ref(null);


onMounted(() => {
  init()
});


// 初始化
const init = () => {
  // 基于准备好的dom，初始化echarts实例
  const myChart = echarts.init(charts.value);

  // 指定图表的配置项和数据
  const option = {
    // 提示框组件
    tooltip: {
      trigger: "axis",
    },
    // 实例
    legend: {
      data: ["访问次数", "访问人数", "IP数"],
    },
    // 位置
    grid: {
      left: "3%",
      right: "4%",
      bottom: "3%",
      containLabel: true,
    },
    // x轴数据
    xAxis: {
      type: "category",
      boundaryGap: false,
      data: curData?.dateList,
    },
    // y为类目轴
    yAxis: {
      type: "value",
    },
    // 配置
    series: [
      {
        name: "访问次数",
        type: "line",
        stack: "Total",
        data: curData?.pvCount,
      },
      {
        name: "访问人数",
        type: "line",
        stack: "Total",
        data: curData?.uvCount,
      },
      {
        name: "IP数",
        type: "line",
        stack: "Total",
        data: curData?.ipCount,
      },
    ],
  };

  // 挂载
  option && myChart.setOption(option);
};


</script>
<style lang="less" scoped>
@import '~@/style/commonColor.less';

.echarts {
  width: 60%;
  height: 405px;
}

.visit_call {
  padding: 20px 0;

  .vist_header {
    margin-bottom: 30px;
    font-size: 16px;
    font-weight: bold;
    color: #000;
    border-bottom: 1px solid #f0f0f0;
  }

  .chart {
    padding: 10px;
  }

  .chart {
    margin-top: 8px;
    display: flex;
    justify-content: space-around;

    .left {
      width: 20%;
      display: flex;
      flex-direction: column;
      font-weight: normal;
      padding-top: 40px;

      div {
        margin-bottom: 10px;
      }

      .numbs {
        color: #5470c6;
      }

      .peobs {
        color: #91cc75;
      }

      .ipbs {
        color: @minorColor;
      }
    }
  }
}
</style>
