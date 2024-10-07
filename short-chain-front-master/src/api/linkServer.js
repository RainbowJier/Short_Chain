/**
 * 短链相关接口
 */
import request from '../utils/request';

// 获取全部短链分组
export const getGroupList = () => {
  return request({
    method: 'get',
    url: '/link-server/api/group/v1/list',
  });
};

// 创建短链分组
export const addGroup = (data) => {
  return request({
    method: 'post',
    url: '/link-server/api/group/v1/add',
    data,
  });
};

// 修改短链分组名字
export const changeGroupName = (data) => {
  return request({
    method: 'put',
    url: '/link-server/api/group/v1/update',
    data,
  });
};

// 删除短链分组
export const deleteGroup = (data) => {
  return request({
    method: 'delete',
    url: `/link-server/api/group/v1/del/${data}`,
  });
};

// 分页获取全部短链列表
export const getShortChain = (data) => {
  return request({
    method: 'post',
    url: '/link-server/api/link/v1/page',
    data,
  });
};

// 创建短链
export const addShortChain = (data) => {
  return request({
    method: 'post',
    url: '/link-server/api/link/v1/add',
    data,
  });
};

// 修改短链
export const changeShortChain = (data) => {
  return request({
    method: 'post',
    url: '/link-server//api/link/v1/update',
    data,
  });
};

// 删除短链
export const deleteShortChain = (data) => {
  return request({
    method: 'post',
    url: '/link-server/api/link/v1/del',
    data,
  });
};

