<template>
  <div class="shortChain_right">
    <div class="content">
      <!-- 短链列表 -->
      <div v-if="listShow">
        <!-- 创建短链 -->
        <ShortChainFound :addShortChainData='addShortChainData' />
        <a-table :columns="data.columns" :data-source="shortChainList" :pagination="false">
          <template #bodyCell="{ column, record }">
            <template v-if="column.dataIndex === 'urlList'">
              <div class="urlList">
                <div class="urlList_text">
                  <div class="urlList_text_one">{{ record.urlList[0] }}</div>
                  <div id="shortChainText">{{ record.urlList[1] }}</div>
                </div>
                <div class="urlList_icon">
                  <qrcode-outlined class="iconCss" @click='qrcodeClick(record.urlList[1], record.key)' />
                  <div id="qrcode" class="qrcodes" v-if="record.key === qrcodeIndex"></div>
                  <copy-outlined class="copy" @click='copy()' :data-clipboard-text="record.urlList[1]" />
                </div>
              </div>
            </template>
            <template v-else-if="column.dataIndex === 'action'">
              <bar-chart-outlined class='iconCss' @click='openEchart(record)' />
              <edit-outlined class="iconCss" @click='showModal(record)' />
              <a-popconfirm title="确定要删除该短链吗？" @confirm="confirm(record.key)" ok-text="删除" cancel-text="取消">
                <close-outlined class="close" />
              </a-popconfirm>
            </template>
          </template>
        </a-table>
        <a-pagination v-model:current="shortChainPagination" :total="shortChainListTotal" show-less-items
          :show-size-changer="false" @change='pageChange' />
      </div>
      <div class="blank" v-if='!listShow'>
        <div class="blank_content">请选择分组展示短链列表!</div>
      </div>
      <ShortChainEditPop v-if="visible" :visible='visible' :closeModal="closeModal"
        :itemShortChainmess="shortChainMess.itemShortChainmess" :changeMess="changeMess" />
      <ShortChainEchart v-if='isShowEchart' :closeEchart='closeEchart' :shortChainItemData='shortChainItemData.data' />
    </div>
  </div>
</template>
<script setup>
import { EditOutlined, BarChartOutlined, CopyOutlined, QrcodeOutlined, CloseOutlined } from '@ant-design/icons-vue';
import ShortChainEchart from '././echart/ShortChainEchart.vue'
import ShortChainEditPop from './ShortChainEditPop.vue'
import ShortChainFound from './ShortChainFound.vue'
import { reactive, ref, defineProps, onMounted } from 'vue'
import { message } from 'ant-design-vue';
import emitter from '@/utils/eventBus';
import Clipboard from 'clipboard';
import QRcode from 'qrcodejs2'

const {
  shortChainList,
  shortChainListTotal,
  deleteShortChainHandle,
  changeShortChainHandle,
  addShortChainData,
  paginationHandle,
  shortChainPagination,
  listShow
} = defineProps([
  'shortChainList',
  'shortChainListTotal',
  'deleteShortChainHandle',
  'changeShortChainHandle',
  'addShortChainData',
  'paginationHandle',
  'shortChainPagination',
  'listShow'
])
/**
 * 数据初始化
 */
const data = reactive({
  columns: [
    { title: '短链信息', dataIndex: 'name' },
    { title: '短链网址', dataIndex: 'urlList' },
    { title: '创建时间', dataIndex: 'time' },
    { title: '操作', dataIndex: 'action' }
  ],
})


/**
 * 二维码生成逻辑
 */
const qrcodeIndex = ref(null)
const lock = ref(true)
const qrcode = (url) => {
  new QRcode('qrcode', {
    width: 100,
    height: 100,
    text: url
  })
}
const listenClick = () => {
  qrcodeIndex.value = null
  lock.value = true
  window.removeEventListener('click', listenClick)
}
const qrcodeClick = (url, key) => {
  if (lock.value) {
    qrcodeIndex.value = key
    setTimeout(() => {
      qrcode(`http://${url}`)
      window.addEventListener('click', listenClick)
    });
    lock.value = false
  }
}

/**
 * 复制逻辑
 */
const copy = async (val) => {
  const clipboard = new Clipboard('.copy')
  clipboard.on('success', e => {
    message.success("复制成功！")
    clipboard.destroy()// 释放内存
  })
  clipboard.on('error', e => {
    message.error('复制失败！')
    clipboard.destroy()// 释放内存
  })
}

/**
 * 删除短链
 */
const confirm = (val) => {
  deleteShortChainHandle(val - 1)
}


/**
 * 编辑逻辑
 */
const shortChainMess = reactive({
  itemShortChainmess: {}
})
const visible = ref(false);
// 打开弹窗
const showModal = (val) => {
  shortChainMess.itemShortChainmess = val
  visible.value = true;
};
// 关闭弹窗
const closeModal = e => {
  visible.value = false;
};
// 更改信息
const changeMess = (val) => {
  if (shortChainMess.itemShortChainmess.name === val.name) {
    message.error('没发现修改内容')
    return
  }
  if (!val.name) {
    message.error('标题不能为空')
    return
  }
  changeShortChainHandle({ index: val.key - 1, name: val.name })
  closeModal()
}



/**
 * Echart可视化逻辑
 */
const isShowEchart = ref(false)
const shortChainItemData = reactive({
  data: {}
})
const openEchart = (val) => {
  shortChainItemData.data = val
  isShowEchart.value = true
}
const closeEchart = () => {
  isShowEchart.value = false
}
onMounted(() => {
  emitter.on('closeEcharts', () => {
    console.log(111)
    closeEchart()
  })
})

/**
 * 分页切换
 */
const pageChange = (val) => {
  paginationHandle(val)
}
</script>
<style lang='less' scoped>
@import '~@/style/commonColor.less';

.blank {
  flex: 1;
  padding: 20px;
  display: flex;
  flex-shrink: 0;
  color: @minorColor;

  .blank_content {
    background-color: #fff;
    flex: 1;
    padding: 20px;
    display: flex;
    justify-content: center;
    align-items: center;
  }
}

:deep(.urlList_text_one) {
  width: 200px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.ant-pagination {
  text-align: right;
  margin-top: 20px;
}

:deep(.ant-table-tbody > tr > td) {
  padding: 10px 16px;
}

.qrcodes {
  width: 120px;
  height: 120px;
  padding: 5px;
  background-color: #fff;
  position: absolute;
  z-index: 10;
}

.urlList {
  display: flex;
  align-items: center;

  .urlList_text {
    margin-right: 20px;
  }

  .urlList_icon {
    position: relative;
  }
}

.close {
  font-size: 20px;
  color: @minorColor;
}

.iconCss {
  font-size: 20px;
  color: @minorColor;
  margin-right: 10px
}

.copy {
  font-size: 20px;
  color: @minorColor
}

.shortChain_right {
  flex: 1;
  padding: 20px;
  display: flex;
  flex-shrink: 0;

  .content {
    background-color: #fff;
    flex: 1;
    padding: 20px;
    display: flex;
    flex-direction: column;
  }
}
</style>