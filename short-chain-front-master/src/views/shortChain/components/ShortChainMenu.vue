<template>
  <div class="shortChain_left">
    <div class="group">
      <div>
        <span class="title">短链分组</span>
        <span>共{{ data.groupList.length }}组</span>
      </div>
      <a-space>
        <plus-square-outlined :style="{ color: '#FF9600', fontSize: '24px' }" @click="showModal" />
      </a-space>
    </div>
    <div class="group_item" :class="{ tabColor: tabColor == index }" v-for="i, index in data.groupList"
      @click="tabClick(index)">
      <div>
        <a-space class="edit" @click="groupEdit(i.title, index)">
          <holder-outlined />
        </a-space>
        <span class="name">{{ i.title }}</span>
      </div>
      <div class="group_item_right">
        <!-- <span class="num">{{ i.num }}</span> -->
        <a-popconfirm title="确定要删除该分组吗？" @confirm="confirm(index)" ok-text="删除" cancel-text="取消">
          <close-outlined />
        </a-popconfirm>
      </div>
    </div>
    <div v-if="menuList.length == 0" class="blank">
      分组为空，请创建！
    </div>
  </div>
  <!-- 创建分组 -->
  <ShortChainGroupPop :handleOk='handleOk' :closeGroup="closeGroup" v-if='visible' :visible='visible'
    :groupName='groupName' :Edit='Edit' />
</template>
<script setup>
import { PlusSquareOutlined, HolderOutlined, CloseOutlined } from '@ant-design/icons-vue';
import { ref, reactive, defineProps } from 'vue'
import { message } from 'ant-design-vue';
import ShortChainGroupPop from './ShortChainGroupPop'
import { addGroup, changeGroupName, deleteGroup } from '../../../api/linkServer'
const { menuList, tabClick, tabColor, closeShortChainMain, getGroupListData, changetabColor } = defineProps(['menuList', 'tabClick', 'tabColor', 'closeShortChainMain', 'getGroupListData', 'changetabColor'])
// 数据初始化
const data = reactive({
  groupList: menuList,
})


/**
 * 创建分组逻辑
 */
const visible = ref(false);
const showModal = () => {
  visible.value = true;
  groupName.value = ''
};
const handleOk = (e) => {
  visible.value = false;
  data.groupList.push({ title: e })
  //创建接口调用
  addGroupData(e)

};
const closeGroup = () => {
  visible.value = false
}
//创建分组接口
const addGroupData = async (val) => {
  const res = await addGroup({ title: val })
  if (res.code === 0) {
    message.success('创建成功！')
    getGroupListData()
  }
}



/**
 * 编辑分组逻辑
 */
const groupName = ref('')     //每项分组名字
const groupIndex = ref(null)  //每项分组索引值
const groupEdit = (name, i) => {
  visible.value = true;
  groupName.value = name
  groupIndex.value = i
}
const Edit = (val) => {
  if (groupName.value === val) {
    message.error('没有修改内容！')
    return
  }
  data.groupList.map((i, index) => {
    if (groupIndex.value === index) {
      i.title = val
      changeGroupNameData(val, i.id)
    }
  })
  closeGroup()
  message.success('修改成功！')
}
//修改组名接口
const changeGroupNameData = async (title, id) => {
  await changeGroupName({ title, id })
}



/**
 * 删除分组逻辑
 */
const confirm = (i) => {
  data.groupList = data.groupList.filter((item, index) => {
    if (i === index) {
      deleteGroupData(item.id)
    }
    return i !== index
  })
  closeShortChainMain()
}
//删除分组接口
const deleteGroupData = async (val) => {
  const res = await deleteGroup(val)
  if (res.code === 0) {
    message.success('删除成功！')
    changetabColor()
  }
}

</script>
<style lang='less' scoped>
@import '~@/style/commonColor.less';

.blank {
  text-align: center;
  color: @minorColor;
}

.edit {
  color: #b6b6b6;
  cursor: pointer;
}

.num {
  margin-right: 10px;
}

.shortChain_left {
  width: 200px;
  background-color: #fff;
  flex-shrink: 0;

  .group {
    height: 80px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 20px;

    .title {
      font-size: 16px;
      font-weight: bold;
      margin-right: 5px;
    }
  }

  .tabColor {
    background-color: #ff960030 !important;
    color: @minorColor;
  }

  .group_item {
    height: 40px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 20px;

    .group_item_right {
      display: none;
    }

    .delete {
      display: none;
    }

    &:hover {
      background-color: #f5f5f5;

      .edit {
        color: red
      }

      .group_item_right {
        display: inline-block;
        color: red;
      }
    }

    .name {
      margin-left: 5px;
    }
  }
}
</style>