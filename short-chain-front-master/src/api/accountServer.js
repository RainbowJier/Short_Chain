/**
 * 用户相关接口
 */
import request from '../utils/request';

//获取手机验证码
export const getCaptcha = (data) => {
  return request({
    method: 'post',
    url: '/account-server/api/notify/v1/send_code',
    data,
  });
};

//注册
export const getRegister = (data) => {
  return request({
    method: 'post',
    url: '/account-server/api/account/v1/register',
    data,
  });
};

//登录接口
export const getLogin = (data) => {
  return request({
    method: 'post',
    url: '/account-server/api/account/v1/login',
    data,
  });
};

//获取用户信息
export const getUserInfo = () => {
  return request({
    method: 'get',
    url: '/account-server/api/account/v1/detail',
  });
};

//获取用户信息
export const getTraffic = (data) => {
  return request({
    method: 'post',
    url: '/account-server/api/traffic/v1/page',
    data,
  });
};

