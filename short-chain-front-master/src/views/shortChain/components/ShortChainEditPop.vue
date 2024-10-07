<template>
  <div>
    <a-modal v-model:visible="visible" title="编辑链接" @ok="handleOk" ok-text="确认" cancel-text="取消" @cancel="handleCancel">
      <div class="modalItem">
        <div class="itemTitle">被编辑的短链</div>
        <div>{{ data.editURL }}</div>
      </div>
      <div class="modalItem">
        <div class="itemTitle">跳转链接</div>
        <div>{{ data.linkUrl }}</div>
      </div>
      <div class="modalItem">
        <div class="itemTitle">短链标题</div>
        <a-input v-model:value="data.inputTitle" />
      </div>
      <!-- 更改短链时间有效期 -->
      <!-- <div class="modalItem">
        <div class="itemTitle">短链有效期</div>
        <div class="dateItem">
          <a-radio-group v-model:value="radioValue" @change="radioChange">
            <a-radio :value="1">永久</a-radio>
            <a-radio :value="2">自定义</a-radio>
          </a-radio-group>
          <a-date-picker v-model:value="dateValue" style="width: 50%" v-if='isShowDate' />
        </div>
      </div> -->
    </a-modal>
  </div>
</template>
<script setup>
import { defineProps, reactive, ref } from 'vue';

const { visible, closeModal, itemShortChainmess, changeMess } = defineProps(['visible', 'closeModal', 'itemShortChainmess', 'changeMess'])
/**
 * 数据初始化
 */
const data = reactive({
  editURL: itemShortChainmess.urlList[0],
  linkUrl: itemShortChainmess.urlList[1],
  inputTitle: itemShortChainmess.name,
})


/**
 * 确认
 */
const handleOk = () => {
  changeMess({ name: data.inputTitle, key: itemShortChainmess.key })
}


/**
 * 取消
 */
const handleCancel = () => {
  closeModal()
}


/**
 * 短链有效期
 */
// const radioValue = ref(1)
// const dateValue = ref(null)
// const isShowDate = ref(false)
// const radioChange = (val) => {
//   val.target.value === 2 ? isShowDate.value = true : isShowDate.value = false
// }
</script>
<style lang='less' scoped>
.modalItem {
  margin-bottom: 30px;

  .itemTitle {
    margin-bottom: 10px;
    font-weight: bold;
  }

  .dateItem {
    height: 32px;
    display: flex;
    align-items: center;
    margin-bottom: 300px;
  }
}

:deep(.ant-space-item) {
  flex: 1;
}

:deep(.ant-space-align-center) {
  width: 100%;
}

:deep(.ant-select) {
  width: 100%;
}
</style>