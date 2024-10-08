/**
 * 商品相关接口
 */
import request from '../utils/request';

//商品列表
export const getProductList = () => {
  return request({
    method: 'get',
    url: '/shop-server/api/product/v1/list',
  });
};

// 订单列表
export const getOrderList = (data) => {
  return request({
    method: 'post',
    url: '/shop-server/api/order/v1/page',
    data,
  });
};

// 获取下单令牌
export const getOrderToken = () => {
  return request({
    method: 'get',
    url: '/shop-server/api/order/v1/token',
  });
};

// 下单接口
export const getOrder = (data) => {
  return request({
    method: 'post',
    url: '/shop-server/api/order/v1/confirm',
    data,
  });
};

//查询订单支付状态
export const getOrderState = (data) => {
  return request({
    method: 'get',
    url: '/shop-server/api/order/v1/query_state',
    data,
  });
};

