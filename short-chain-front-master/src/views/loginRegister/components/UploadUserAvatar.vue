<template>
  <div class="avatar">
    <div class="avatar_title"> <span>*</span> 请上传头像：</div>
    <a-upload v-model:file-list="fileList" name="file" list-type="picture-card" class="avatar-uploader" :max-count="1"
      :show-upload-list="true" action="http://api.link.open1024.com/account-server/api/account/v1/upload"
      :before-upload="beforeUpload" @change="handleChange">
      <!-- <img v-if="imageUrl" :src="imageUrl" alt="file"  /> -->
      <div>
        <loading-outlined v-if="loading"></loading-outlined>
        <plus-outlined v-else></plus-outlined>
        <div class="ant-upload-text">上传</div>
      </div>
    </a-upload>
  </div>
</template>
<script setup>
import { PlusOutlined, LoadingOutlined } from '@ant-design/icons-vue';
import { message } from 'ant-design-vue';
import { ref, defineProps } from 'vue';
const { changeAvatar } = defineProps(['changeAvatar'])
const getBase64 = (img, callback) => {
  const reader = new FileReader();
  reader.addEventListener('load', () => callback(reader.result));
  reader.readAsDataURL(img);
}

const fileList = ref([]);
const loading = ref(false);
const imageUrl = ref('');

const handleChange = info => {
  if (info.file.status === 'uploading') {
    loading.value = true;
    return;
  }

  if (info.file.status === 'done') {
    getBase64(info.file.originFileObj, base64Url => {
      imageUrl.value = base64Url;
      loading.value = false;
      changeAvatar(info.file.response.data)
      message.success('上传成功！');
    });
  }

  if (info.file.status === 'error') {
    loading.value = false;
    message.error('上传失败！');
  }
};

const beforeUpload = file => {
  const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';

  if (!isJpgOrPng) {
    message.error('只能上传JPG格式图片!');
  }

  const isLt2M = file.size / 1024 / 1024 < 2;

  if (!isLt2M) {
    message.error('Image must smaller than 2MB!');
  }

  return isJpgOrPng && isLt2M;
};



</script>
<style lang="less">
.avatar {
  display: flex;
  padding: 0 0 10px 49px;

  .avatar_title {
    white-space: nowrap;

    span {
      color: #ff4d4f;
    }
  }

}

.avatar-uploader>.ant-upload {
  width: 128px;
  height: 128px;
}

.ant-upload-select-picture-card i {
  font-size: 32px;
  color: #999;
}

.ant-upload-select-picture-card .ant-upload-text {
  margin-top: 8px;
  color: #666;
}

.ant-upload-list {
  position: relative;
}

.ant-upload-list-picture-card-container {
  position: absolute;
  right: 123px;
}


.img {
  width: 128px;
  height: 128px;
}
</style>