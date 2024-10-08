<template>
  <div class="newBuild">
    <a-input v-model:value="inputUrl" placeholder="请输入要创建的短链" class="input" />
    <a-button type="primary" class="button" @click="showModal">创建短链</a-button>
  </div>
  <!-- 创建短链弹窗 -->
  <div>
    <a-modal v-model:visible="visible" title="新建链接" @ok="handleOk" ok-text="创建" cancel-text="取消">
      <div class="modalItem">
        <div class="itemTitle">跳转链接</div>
        <a-input v-model:value="inputUrl" placeholder="请输入要创建的短链" />
      </div>
      <div class="modalItem">
        <div class="itemTitle">短链标题</div>
        <a-input v-model:value="inputTitle" placeholder="请输入短链的标题" />
      </div>
      <!-- 更改短链时间有效期 -->
      <div class="modalItem">
        <div class="itemTitle">短链有效期</div>
        <div class="dateItem">
          <a-radio-group v-model:value="radioValue" @change="radioChange">
            <a-radio :value="1">永久</a-radio>
            <a-radio :value="2">自定义</a-radio>
          </a-radio-group>
          <a-date-picker v-model:value="dateValue" style="width: 50%" v-if='isShowDate' format="YYYY-MM-DD HH:mm:ss"
            :disabled-date="disabledDate" />
        </div>
      </div>
    </a-modal>
  </div>
</template>
<script setup>
import { message } from 'ant-design-vue';
import { ref } from 'vue'
import { defineProps } from 'vue';
import dayjs from 'dayjs'

const { addShortChainData } = defineProps(["addShortChainData"])

/**
 * 创建短链逻辑
 */
const inputTitle = ref('')
const inputUrl = ref('')
const visible = ref(false);
const showModal = () => {
  visible.value = true;
};
const handleOk = e => {
  if (!inputTitle.value || !inputUrl.value) {
    message.error('短链或者标题不能为空！')
    return
  }
  let expired = ''
  if (radioValue.value == 1) {
    expired = -1
  } else {
    expired = dayjs(dateValue.value).format('YYYY-MM-DD HH:mm:ss')
  }
  addShortChainData({ inputTitle: inputTitle.value, inputUrl: inputUrl.value, expired: expired })
  visible.value = false;
};



/**
 * 短链有效期
 */
const radioValue = ref(1)
const dateValue = ref(null)
const isShowDate = ref(false)
const radioChange = (val) => {
  val.target.value === 2 ? isShowDate.value = true : isShowDate.value = false
}

/**
 * 禁止选择的日期
 */
const disabledDate = current => {
  return current && current < dayjs().endOf('day');
};
</script>
<style lang='less' scoped>
@import '~@/style/commonColor.less';

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

.newBuild {
  display: flex;
  margin-bottom: 30px;

  .input {
    margin-right: 20px;
    height: 40px;
  }

  .ant-input:focus {
    border-color: @minorColor ;
  }

  .ant-input:hover {
    border-color: @minorColor ;
  }

  .button {
    height: 40px;
    background-color: @minorColor;
    border-color: @minorColor;
  }
}
</style>