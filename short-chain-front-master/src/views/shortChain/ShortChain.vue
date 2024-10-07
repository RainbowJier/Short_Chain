<template>
  <div class="shortChain">
    <!-- 短链导航菜单栏 -->
    <ShortChainMenu v-if='data.menuShow' :tabClick="tabClick" :menuList='data.menuList' :tabColor='tabColor'
      :changetabColor="changetabColor" :closeShortChainMain="closeShortChainMain"
      :getGroupListData='getGroupListData' />
    <div class="shortChain_left" v-if='!data.menuShow' />
    <!-- 短链主要内容 -->
    <ShortChainMain :shortChainList='data.shortChainList' :shortChainListTotal="data.shortChainListTotal"
      :deleteShortChainHandle="deleteShortChainHandle" :changeShortChainHandle="changeShortChainHandle"
      :addShortChainData='addShortChainData' :paginationHandle="paginationHandle"
      :shortChainPagination="shortChainPagination" :listShow="data.listShow" />
  </div>
</template>
<script setup>
import ShortChainMain from './components/ShortChainMain.vue'
import ShortChainMenu from './components/ShortChainMenu.vue'
import { onMounted, reactive, ref } from 'vue'
import { getGroupList } from '../../api/linkServer'
import { getShortChain, changeShortChain, deleteShortChain, addShortChain } from '../../api/linkServer'
import dayjs from 'dayjs'
import { message } from 'ant-design-vue';

/**
 * 数据定义
 */
const data = reactive({
  menuList: [],
  menuShow: false,
  listShow: false,
  shortChainList: [],
  shortChainListTotal: ''
})
const closeShortChainMain = () => {
  data.listShow = false
}

/**
 * 获取全部分组列表
 */
const getGroupListData = async () => {
  const res = await getGroupList()
  if (res.code === 0) {
    data.menuList = res.data
    data.menuShow = true
  }
}
onMounted(() => {
  getGroupListData()
})


/**
 * tab相关逻辑
 */
const tabColor = ref(null)  //选中tab颜色
const groupId = ref(null)   //选中分组id
const shortChainPagination = ref(1)  //分页默认展示页数
//menu菜单tab切换
const tabClick = (val) => {
  shortChainPagination.value = 1
  if (tabColor.value == val) {
    return
  }
  tabColor.value = val
  groupId.value = data.menuList[val].id
  getShortChainData({ id: data.menuList[val].id })
}
//去除tab选中颜色
const changetabColor = () => {
  tabColor.value = null
}



/**
 * 获取短链列表数据
 */
// 表格时间展示数据格式处理
const urlListhandle = (originalUrl, domain, code) => {
  originalUrl = originalUrl.split('&')[1]
  return [originalUrl, domain + '/' + code]
}
const getShortChainData = async (val) => {
  const res = await getShortChain({ page: val.page || 1, size: 10, groupId: val.id })
  if (res.code === 0) {
    const list = res.data.current_data.map((item, index) => {
      return {
        groupId: item.groupId,
        code: item.code,
        mappingId: item.id,
        key: index + 1,
        name: item.title,
        urlList: urlListhandle(item.originalUrl, item.domain, item.code),
        time: dayjs(item.time).format('YYYY/MM/DD HH:mm:ss')
      }
    })
    // 短链列表
    data.shortChainList = list
    // 短链列表总数
    data.shortChainListTotal = res.data.total_record
    data.listShow = true
  }
}


/**
 * 修改短链列表数据
 */
const changeShortChainHandle = (val) => {
  data.shortChainList[val.index].name = val.name
  changeShortChainData(val)
}
//修改短链接口
const changeShortChainData = async (val) => {
  const res = await changeShortChain({
    groupId: data.shortChainList[val.index].groupId,
    mappingId: data.shortChainList[val.index].mappingId,
    title: val.name,
    domainId: 1,
    domainType: 'OFFICIAL'
  })
  if (res.code === 0) {
    message.success('修改成功！')
  }
}


/**
 * 删除短链列表数据
 */
const deleteShortChainHandle = (val) => {
  deleteShortChainData(val)
  data.shortChainList = data.shortChainList.filter((i, index) => {
    return !(index === val)
  })
}
//删除短链接口
const deleteShortChainData = async (val) => {
  const res = await deleteShortChain({
    groupId: data.shortChainList[val].groupId,
    mappingId: data.shortChainList[val].mappingId,
    code: data.shortChainList[val].code
  })
  if (res.code === 0) {
    message.success('删除成功！')
  }
}



/**
 * 创建短链列表
 */
const addShortChainData = async (val) => {
  const res = await addShortChain({
    groupId: groupId.value,
    title: val.inputTitle,
    originalUrl: val.inputUrl,
    domainId: 1,
    domainType: 'OFFICIAL',
    expired: val.expired
  })
  if (res.code === 0) {
    getShortChainData({ id: groupId.value })
  } else if (res.code === 600102) {
    message.error('流量不足，扣减失败')
  }
}


/**
 * 分页切换
 */
const paginationHandle = (val) => {
  shortChainPagination.value = val
  getShortChainData({ page: val, id: groupId.value })
}

</script>
<style lang='less' scoped>
@import '~@/style/commonColor.less';

.shortChain {
  width: 1200px;
  display: flex;
  flex: 1;
  margin: auto;
  overflow-y: auto;

  .shortChain_left {
    width: 200px;
    background-color: #fff;
    flex-shrink: 0;
  }
}
</style>