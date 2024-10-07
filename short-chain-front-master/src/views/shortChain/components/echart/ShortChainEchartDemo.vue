<template>
  <div class="echart">
    <div class="echart_content">
      <div class="echart_header">
        <div class="img">
          <img src="../../../../assets/noHeader.png">
        </div>
        <div class="text">
          <div class="title">测试角色</div>
          <div>https://baidu.com</div>
        </div>
        <close-outlined class="close" @click='closeEchart' />
      </div>
      <!-- 数据表 -->
      <a-tabs v-model:activeKey="activeKey" @change="tabChange">
        <a-tab-pane key="1" tab="数据图表" size="large">
          <div>
            <!-- 地图数据图 -->
            <MapEchart :list="mapData.list" />

            <!-- 天曲线数据图 -->
            <CurveEchartDay :curData="curveData.curData" />

            <!-- 小时曲线数据图 -->
            <CurveEchartHour :curData="curveDatas.curData" />

            <div class="wrapEchart">
              <!-- 来源数据图 -->
              <SourceEchart :sourceData="sourceData" />

              <!-- 入口数据图 -->
              <div class="frr_item">
                <div class="header">访问入口</div>
                <div class="boxsec">
                  <EntranceEchart v-for='item, index in entranceData.list' :item='item' :key='index' />
                </div>
              </div>
            </div>
          </div>
        </a-tab-pane>

        <!-- 访问记录 -->
        <a-tab-pane key="2" tab="访问记录" force-render>
          <a-table :columns="recordData.columns" :data-source="recordData.list" :pagination="false">
            <template #bodyCell="{ column, record }">
              <template v-if="column.dataIndex === 'equipment'">
                <span v-for="i in record.equipment">{{ i }}</span>
              </template>
            </template>
          </a-table>
          <a-pagination v-model:current="recordData.recordPage" :total="recordData.recordTotal" show-less-items
            :show-size-changer="false" class="pagination" />
        </a-tab-pane>
      </a-tabs>
    </div>
  </div>
</template>
<script setup>
import { CloseOutlined } from '@ant-design/icons-vue';
import CurveEchartHour from './components/CurveEchartHour.vue'
import CurveEchartDay from './components/CurveEchartDay.vue'
import EntranceEchart from './components/EntranceEchart.vue'
import SourceEchart from './components/SourceEchart.vue'
import MapEchart from './components/MapEchart.vue'
import { ref, reactive } from 'vue';
import router from '@/router';
import dayjs from 'dayjs';

const activeKey = ref('1')
/**
 * 时间范围选择
 */
const dayHandle = (date) => {
  return dayjs(date).format('YYYYMMDD')
}
const data = reactive({
  dataList: [],
})

/**
 * 回到首页
 */
const closeEchart = () => {
  router.back()
}

/**
 * 时间范围切换
 */
const dateChange = (val) => {
  data.dataList = val.map(i => {
    return dayHandle(i)
  });
  //访问入口数据
  data.entranceShow = false
  getEntranceData()
}


/**
 * 地图数据接口
 */
const mapData = reactive({
  list: [
    { name: '广州', value: 50 },
    { name: '济南', value: 28 },
    { name: '北京', value: 26 },
    { name: '深圳', value: 21 },
    { name: '杭州', value: 13 },
    { name: '上海', value: 102 },
    { name: '青岛', value: 34 },
    { name: '长春', value: 45 },
    { name: '成都', value: 89 },
    { name: '苏州', value: 23 },
    { name: '重庆', value: 45 },
    { name: '南京', value: 34 },
    { name: '南宁', value: 43 },
    { name: '汕头', value: 34 },
    { name: '遵义', value: 23 },
    { name: '福州', value: 34 },
    { name: '武汉', value: 2 },
    { name: '厦门', value: 8 },
    { name: '郑州', value: 12 },
    { name: '台州', value: 54 },
    { name: '镇江', value: 1 },
    { name: '徐州', value: 34 },
    { name: '金华', value: 7 },
    { name: '梅州', value: 4 },
    { name: "台湾", value: 50 },
    { name: "香港", value: 11 },
    { name: "哈密", value: 11 },
    { name: "和田", value: 11 },
    { name: "巴音郭楞", value: 11 },
    { name: "吐鲁番", value: 11 },
    { name: "图木舒克", value: 11 },
    { name: "拉萨", value: 11 },
    { name: "日喀则", value: 11 },
    { name: "克拉玛依", value: 11 },
    { name: "乌鲁木齐", value: 11 }
  ]
})



/**
 * TOP10来源访问数据
 */
const sourceData = reactive({
  regionList: ['直接访问', 'https://baidu.com', 'https://zhihu.com', 'https://taobao.com', 'https://douyin.com', 'https://qq.com', 'baidu.com'],
  pvCountList: [206, 15, 12, 11, 10, 7, 1]
})



/**
 * 访问曲线数据-天级别
 */
const curveData = reactive({
  curData: {
    // 访问次数
    pvCount: [93, 93, 4, 34, 52, 34, 2, 32, 99, 65, 76, 35, 33, 54, 234, 56, 67, 45, 2],
    // 访问人数
    uvCount: [43, 93, 4, 76, 52, 1, 123, 32, 99, 2, 76, 23, 33, 33, 34, 111, 333, 1, 222],
    // IP数
    ipCount: [13, 2, 65, 34, 52, 34, 34, 1, 34, 34, 76, 35, 33, 54, 43, 54, 67, 45, 12],
    dateList: ['5-11', '5-12', '5-13', '5-14', '5-15', '5-16', '5-17', '5-18', '5-19', '5-20', '5-21', '5-22', '5-23', '5-24', '5-25', '5-26', '5-27', '5-28', '5-29'],
    pvSum: '262',
    uvSum: '200',
    ipSum: '93',
  },
});



/**
 * 曲线图数据接口-小时级别
 */
const count = (time) => {
  const list = []
  if (time) {
    for (let i = 0; i < 24; i++) {
      list.push(i)
    }
  } else {
    for (let i = 0; i < 24; i++) {
      list.push(2)
    }
    list[22] = 10
    list[23] = 9
    list[0] = 13
    list[1] = 4
    list[9] = 34
    list[10] = 12
    list[11] = 22
    list[12] = 15
    list[13] = 12
    list[14] = 12
    list[15] = 12
    list[16] = 12
    list[17] = 12
    list[18] = 12
    list[19] = 12
  }
  return list
}
const curveDatas = reactive({
  curData: {
    // 一周访问次数
    pvCount: count(),
    // 访问人数
    uvCount: count(),
    // IP数
    ipCount: count(),
    dateList: count(1),
    pvSum: 262,
    uvSum: 200,
    ipSum: 171,
  },
});




/**
 * 访问入口数据
 */
const entranceData = reactive({
  list: [{
    equipment: '设备',
    equipmentList: [
      {
        itemStyle: { color: 'red' },
        name: "MOBILE",
        value: 42
      },
      {
        itemStyle: { color: 'blue' },
        name: "COMPUTER",
        value: 220
      }
    ]
  },
  {
    equipment: '系统',
    equipmentList: [
      {
        itemStyle: { color: 'red' },
        name: "iOS",
        value: 13
      },
      {
        itemStyle: { color: 'blue' },
        name: "Android",
        value: 29
      },
      {
        itemStyle: { color: 'green' },
        name: "Mac OS X",
        value: 70
      },
      {
        itemStyle: { color: 'yellow' },
        name: "Windows",
        value: 150
      }
    ]
  },]
})




/**
 * 访问记录数据
 */
const recordData = reactive({
  list: [
    {
      equipment: (3)['Chrome/', 'Android/', 'MOBILE'],
      ip: "117.139.216.244",
      key: 1,
      region: "中国-四川省-成都市",
      source: "https://taobao.com",
      time: "2022-05-18 18:17:46",
      type: "老访客"
    },
    {
      equipment: (3)['Chrome/', 'Android/', 'MOBILE'],
      ip: "117.139.216.244",
      key: 1,
      region: "中国-四川省-成都市",
      source: "https://taobao.com",
      time: "2022-05-18 18:17:46",
      type: "老访客"
    },
    {
      equipment: (3)['Chrome/', 'Android/', 'MOBILE'],
      ip: "117.139.216.244",
      key: 1,
      region: "中国-四川省-成都市",
      source: "https://taobao.com",
      time: "2022-05-18 18:17:46",
      type: "老访客"
    },
  ],
  columns: [
    { title: '访问时间', dataIndex: 'time' },
    { title: '访问ip', dataIndex: 'ip' },
    { title: '访客地区', dataIndex: 'region' },
    { title: '设备信息', dataIndex: 'equipment' },
    { title: '访客类型', dataIndex: 'type' },
    { title: '访问来源', dataIndex: 'source' },
  ],
  recordTotal: 3,  //访问记录展示总页数
  recordPage: 1       //访问记录当前页数
})


/**
 * tab切换
 */
const tabChange = (val) => {
  if (val === '2') {
    data.dateShow = false
  } else {
    data.dateShow = true
  }
}




</script>
<style lang='less' scoped>
@import '~@/style/commonColor.less';

.pagination {
  margin-top: 20px;
  text-align: right;
}

.ant-picker {
  width: 20%;
  position: absolute;
  top: 188px;
  right: 50px;
}

:deep(.ant-tabs-top > .ant-tabs-nav::before) {
  border-bottom: 1px solid #eee;
}

.header {
  font-size: 16px;
  font-weight: bold;
  color: #000;
  display: inline-block;
  border-bottom: 1px solid #f0f0f0;

}

.boxsec {
  display: flex;
  flex: 1;
  align-items: center;
}

.wrapEchart {
  display: flex;
  flex-wrap: wrap;
  padding: 20px 0;

  .frr_item {
    width: 50%;
    overflow: hidden;
    display: flex;
    flex-direction: column;
  }
}

:deep(.ant-tabs-tab) {
  font-size: 16px !important;
}

.echart {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(55, 55, 55, .6);
  padding: 80px 0;
  overflow-y: scroll;
}

.echart_content {
  width: 1200px;
  margin: auto;
  background-color: #fff;
  padding: 50px;
  position: relative;

  .echart_header {
    display: flex;
    align-items: center;
    margin-bottom: 50px;
  }

  .img {
    display: flex;
    align-items: center;
    justify-content: center;

    img {
      width: 80px;
      height: 80px;
      border-radius: 50%;
      margin-right: 20px;
    }
  }

  .text {
    .title {
      font-size: 24px;
      font-weight: bold;
    }
  }

  .close {
    font-size: 20px;
    position: absolute;
    right: 0;
    right: 20px;
    top: 20px;
    color: @minorColor;
    cursor: pointer;
  }
}
</style>