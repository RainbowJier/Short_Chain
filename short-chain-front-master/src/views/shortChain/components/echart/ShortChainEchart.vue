<template>
  <div class="echart">
    <div class="echart_content">
      <div class="echart_header">
        <div class="img">
          <img :src="data.userInfo.headImg">
        </div>
        <div class="text">
          <div class="title">{{ data.userInfo.username }}</div>
          <div>{{ shortChainItemData.urlList[0] }}</div>
        </div>
        <close-outlined class="close" @click='closeEchart' />
      </div>
      <!-- 数据表 -->
      <a-tabs v-model:activeKey="activeKey" @change="tabChange">
        <a-tab-pane key="1" tab="数据图表" size="large">
          <div>
            <!-- 地图数据图 -->
            <MapEchart :list="mapData.list" v-if='data.mapShow' />

            <!-- 天曲线数据图 -->
            <CurveEchartDay :curData="curveData.curData" v-if="data.curveShow" />

            <!-- 小时曲线数据图 -->
            <CurveEchartHour :curData="curveDatas.curData" v-if="data.curveShows && data.curveShowss" />

            <div class="wrapEchart">
              <!-- 来源数据图 -->
              <SourceEchart :sourceData="sourceData" v-if='data.sourceShow' />

              <!-- 入口数据图 -->
              <div class="frr_item">
                <div class="header">访问入口</div>
                <div class="boxsec">
                  <EntranceEchart v-for='item, index in entranceData.list' :item='item' :key='index'
                    v-if='data.entranceShow' />
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
            :show-size-changer="false" @change='recordPageChange' class="pagination" />
        </a-tab-pane>
      </a-tabs>
      <a-space direction="vertical" :size="12" v-if="data.dateShow">
        <a-range-picker v-model:value="data.dataValue" :ranges="data.ranges" @change="dateChange"
          @calendarChange="onCalendarChange" :disabled-date="disabledDate" />
      </a-space>
    </div>
  </div>
</template>
<script setup>
import { getEntrance, getCurve, getSourceTop, getRegion, getRecord } from '../../../../api/dataServer'
import CurveEchartHour from './components/CurveEchartHour.vue'
import EntranceEchart from './components/EntranceEchart.vue'
import CurveEchartDay from './components/CurveEchartDay.vue'
import { ref, reactive, defineProps, onMounted } from 'vue';
import { getUserInfo } from '../../../../api/accountServer'
import SourceEchart from './components/SourceEchart.vue'
import { CloseOutlined } from '@ant-design/icons-vue';
import noHeader from '../../../../assets/noHeader.png'
import MapEchart from './components/MapEchart.vue'
import dayjs from 'dayjs';

const { closeEchart, shortChainItemData } = defineProps(['closeEchart', 'shortChainItemData'])
const activeKey = ref('1')
/**
 * 时间范围选择
 */
const dayHandle = (date) => {
  return dayjs(date).format('YYYYMMDD')
}
const data = reactive({
  dataList: [],
  dates: null,
  dataValue: null,
  ranges: {
    '当天': [dayjs(), dayjs()],                   //今天
    '当月': [dayjs(), dayjs().endOf('month')],    //这个月
  },
  mapShow: false,         //控制地图展示
  sourceShow: true,     //控制来源展示
  entranceShow: true,   //控制入口展示
  curveShowss: true,    //控制曲线-小时级别展示
  curveShows: true,     //控制曲线-小时级别展示
  curveShow: true,      //控制曲线-天级别展示
  userInfo: {
    headImg: '',
    username: ''
  },
  dateShow: true,     //打开访问记录时关闭时间选择器    
})
// 固定展示一个月内数据
const disabledDate = current => {
  if (!data.dates || data.dates.length === 0) {
    return false;
  }
  const tooLate = data.dates[0] && current.diff(data.dates[0], 'days') > 30;
  const tooEarly = data.dates[1] && data.dates[1].diff(current, 'days') > 30;
  return tooEarly || tooLate;
};
const onCalendarChange = val => {
  data.dates = val;
};


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

  //曲线图数据接口-天级别
  data.curveShow = false
  getCurveData()

  //曲线图数据接口-小时级别
  if (data.dataList[0] == data.dataList[1]) {
    data.curveShowss = true
    data.curveShows = false
    getCurveDatas()
  } else {
    data.curveShowss = false
  }

  //访问来源top10
  data.sourceShow = false
  getSourceTopData()

  //地图数据展示
  data.mapShow = false
  getRegionData()
}


/**
 * 地图数据接口
 */
const mapData = reactive({
  list: []
})
const getRegionData = async () => {
  const deleteCityText = (val) => {
    if (val.includes('市')) {
      return val.split('市')[0]
    }
  }
  const res = await getRegion({
    // code: '13iSdUWa',
    code: shortChainItemData.code,
    startTime: data.dataList[0],
    endTime: data.dataList[1],
  })
  if (res.code === 0) {
    const newList = []
    res.data.map((i) => {
      if (!i.city.includes('[]') && i.city) {
        newList.push({ name: deleteCityText(i.city), value: i.pvCount })
      }
    })
    mapData.list = newList
    if (newList.length > 0) {
      data.mapShow = true
    }
  }
}




/**
 * TOP10来源访问数据
 */
const sourceData = reactive({
  regionList: [],
  pvCountList: []
})
const getSourceTopData = async () => {
  const res = await getSourceTop({
    // code: '13iSdUWa',
    code: shortChainItemData.code,
    startTime: data.dataList[0],
    endTime: data.dataList[1],
  })
  if (res.code === 0) {
    const newList = []
    const newLists = []
    res.data.map((i) => {
      newList.push(i.referer || '直接访问')
    })
    res.data.map((i) => {
      newLists.push(i.pvCount)
    })
    sourceData.regionList = newList
    sourceData.pvCountList = newLists
    data.sourceShow = true
  }
}






/**
 * 访问曲线数据
 */
const curveData = reactive({
  curData: {
    // 一周访问次数
    pvCount: [],
    // 访问人数
    uvCount: [],
    // IP数
    ipCount: [],
    dateList: [],
    pvSum: '',
    uvSum: '',
    ipSum: '',
  },
});
//一个月数组
// const countDay = (dateTimeStr, val, type) => {
//   const dateList = []
//   if (type === 'time') {
//     for (let i = 0; i < 30; i++) {
//       dateList.push(i.toString())
//     }
//     return dateList
//   } else {
//     for (let i = 0; i < 30; i++) {
//       dateList.push(0)
//     }
//     dateList.forEach((item, index) => {
//       dateTimeStr.forEach((subItem, subIndex) => {
//         if (dayjs(subItem).format('DD') == index) {
//           dateList[index] = val[subIndex]
//         }
//       })
//     })
//     return dateList
//   }
// }
//曲线图数据接口-天级别
const getCurveData = async () => {
  const res = await getCurve({
    code: shortChainItemData.code,
    // code: '13iSdUWa',
    type: 'day',
    startTime: data.dataList[0],
    endTime: data.dataList[1],
  })
  const pvCount = []
  const uvCount = []
  const ipCount = []
  const dateList = []
  if (res.code === 0) {
    res.data.map((i) => {
      pvCount.push(i.pvCount)
      uvCount.push(i.uvCount)
      ipCount.push(i.ipCount)
      curveData.curData.pvSum = Number(curveData.curData.pvSum) + i.pvCount
      curveData.curData.uvSum = Number(curveData.curData.uvSum) + i.uvCount
      curveData.curData.ipSum = Number(curveData.curData.ipSum) + i.ipCount
      dateList.push(dayjs(i.dateTimeStr).format('MM-DD'))
    })
    curveData.curData.pvCount = pvCount
    curveData.curData.uvCount = uvCount
    curveData.curData.ipCount = ipCount
    curveData.curData.dateList = dateList
    data.curveShow = true
  }
}




//曲线图数据接口-小时级别
const curveDatas = reactive({
  curData: {
    // 一周访问次数
    pvCount: [],
    // 访问人数
    uvCount: [],
    // IP数
    ipCount: [],
    dateList: [],
    pvSum: '',
    uvSum: '',
    ipSum: '',
  },
});

//24小时数组
const countHour = (dateTimeStr, val, type) => {
  const dateList = []
  if (type === 'time') {
    for (let i = 0; i < 24; i++) {
      dateList.push(i.toString())
    }
    return dateList
  } else {
    for (let i = 0; i < 24; i++) {
      dateList.push(0)
    }
    dateList.forEach((item, index) => {
      dateTimeStr.forEach((subItem, subIndex) => {
        if (subItem == index) {
          dateList[index] = val[subIndex]
        }
      })
    })
    return dateList
  }
}

const getCurveDatas = async () => {
  const res = await getCurve({
    code: shortChainItemData.code,
    // code: '13iSdUWa',
    type: 'hour',
    startTime: data.dataList[0],
  })
  const pvCount = []
  const uvCount = []
  const ipCount = []
  const dateList = []
  if (res.code === 0) {
    res.data.map((i) => {
      pvCount.push(i.pvCount)
      uvCount.push(i.uvCount)
      ipCount.push(i.ipCount)
      curveDatas.curData.pvSum = Number(curveDatas.curData.pvSum) + i.pvCount
      curveDatas.curData.uvSum = Number(curveDatas.curData.uvSum) + i.uvCount
      curveDatas.curData.ipSum = Number(curveDatas.curData.ipSum) + i.ipCount
      dateList.push(i.dateTimeStr)
    })
    curveDatas.curData.pvCount = countHour(dateList, pvCount)
    curveDatas.curData.uvCount = countHour(dateList, uvCount)
    curveDatas.curData.ipCount = countHour(dateList, ipCount)
    curveDatas.curData.dateList = countHour(dateList, '', 'time')
    data.curveShows = true
  }
}



/**
 * 访问入口数据
 */
const entranceData = reactive({
  list: [{
    equipment: '设备',
    equipmentList: []
  },
  {
    equipment: '系统',
    equipmentList: []
  },]
})
const getEntranceData = async () => {
  const res = await getEntrance({
    // code: '13iSdUWa',
    code: shortChainItemData.code,
    startTime: data.dataList[0],
    endTime: data.dataList[1],
  })
  if (res.code === 0) {
    const colorList = [{ color: 'red' }, { color: 'blue' }, { color: 'green' }, { color: 'yellow' }, { color: 'orange' }]
    //设备
    const list = res.data.device.map((i, index) => {
      return {
        value: i.pvCount,
        name: i.deviceType,
        itemStyle: colorList[index]
      }
    })
    entranceData.list[0].equipmentList = list
    //手机
    const lists = res.data.os.map((i, index) => {
      return {
        value: i.pvCount,
        name: i.os,
        itemStyle: colorList[index]
      }
    })
    entranceData.list[1].equipmentList = lists
    data.entranceShow = true
  }
}




/**
 * 访问记录数据
 */
const recordData = reactive({
  list: [],
  columns: [
    { title: '访问时间', dataIndex: 'time' },
    { title: '访问ip', dataIndex: 'ip' },
    { title: '访客地区', dataIndex: 'region' },
    { title: '设备信息', dataIndex: 'equipment' },
    { title: '访客类型', dataIndex: 'type' },
    { title: '访问来源', dataIndex: 'source' },
  ],
  recordTotal: null,  //访问记录展示总页数
  recordPage: 1       //访问记录当前页数
})
const getRecordData = async (val) => {
  const res = await getRecord({
    // code: '026m8O3a',
    code: shortChainItemData.code,
    size: 10,
    page: val || 1
  })
  if (res.code === 0) {
    const list = res.data.data.map((i, index) => {
      return {
        key: index + 1,
        time: dayjs(i.visitTime).format('YYYY-MM-DD HH:mm:ss'),
        ip: i.ip,
        region: `中国-${i.province}-${i.city}`,
        equipment: [i.browserName + '/', i.os + '/', i.deviceType],
        type: i.isNew === 1 ? "新访客" : '老访客',
        source: i.referer || "直接访问"
      }
    })
    recordData.list = list
    recordData.recordTotal = res.data.total
    console.log(list, res.data.total)
  }
}
onMounted(() => {
  getRecordData()
})
//分页切换
const recordPageChange = (val) => {
  getRecordData(val)
}

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


/**
 * 获取用户信息
 */
const getUserInfoData = async () => {
  const res = await getUserInfo()
  if (res.code === 0) {
    data.userInfo.headImg = res.data.headImg || noHeader
    data.userInfo.username = res.data.username || '测试角色'
  }
}
onMounted(() => {
  getUserInfoData()
})



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
    font-size: 30px;
    position: absolute;
    right: 0;
    right: 20px;
    top: 20px;
  }
}
</style>