/**
 * echart视图相关接口
 */
import request from '../utils/request';

// 地区访问分布数据
export const getRegion = (data) => {
  return request({
    method: 'post',
    url: '/data-server/api/visit_stats/v1/region_day',
    data,
  });
};

// 获取曲线图数据
export const getCurve = (data) => {
  return request({
    method: 'post',
    url: '/data-server/api/visit_stats/v1/trend',
    data,
  });
};

// TOP10来源访问数据
export const getSourceTop = (data) => {
  return request({
    method: 'post',
    url: '/data-server/api/visit_stats/v1/frequent_source',
    data,
  });
};

// 访问入口数据
export const getEntrance = (data) => {
  return request({
    method: 'post',
    url: '/data-server/api/visit_stats/v1/device_info',
    data,
  });
};

// 访问记录数据
export const getRecord = (data) => {
  return request({
    method: 'post',
    url: '/data-server/api/visit_stats/v1/page_record',
    data,
  });
};

