<template>
  <div>
    <a-modal v-model:visible="visible" :title="groupName ? '编辑链接分组' : '新建链接分组'" @ok="submit"
      :ok-text="groupName ? '修改' : '创建'" cancel-text="取消" @cancel="closeGroup">
      <div :style="{ marginBottom: '10px' }">分组名称</div>
      <a-form-item name="inputValue" :rules="[{ required: true, message: '请输入分组组名!', trigger: 'blur' }]">
        <a-input v-model:value="inputValue" :placeholder="groupName ? '' : '请输入分组组名'" />
      </a-form-item>
    </a-modal>
  </div>
</template>
<script setup>
import { defineProps, ref } from 'vue'
import { message } from 'ant-design-vue';

const { handleOk, visible, groupName, Edit, closeGroup } = defineProps(['handleOk', 'visible', 'groupName', 'Edit', 'closeGroup'])
const inputValue = ref(groupName || '')
const submit = () => {
  if (inputValue.value) {
    groupName ? Edit(inputValue.value) : handleOk(inputValue.value)
  } else {
    message.error('组名不能为空！')
  }
}
</script>
<style lang='less' scoped>
</style>