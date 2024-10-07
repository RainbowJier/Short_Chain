import axios from 'axios';
import JSONBIG from 'json-bigint';
/**
 * 创建axios实例
 */
const ENV = process.env.NODE_ENV;
const host =
  ENV === 'development'
    ? 'http://api.link.open1024.com'
    : 'http://api.link.open1024.com';
const service = axios.create({
  baseURL: host,
  timeout: '3000',
  transformResponse: [
    function (data) {
      try {
        return JSONBIG.parse(data);
      } catch (err) {
        return data;
      }
    },
  ],
});

/**
 * 请求拦截
 */
service.interceptors.request.use((config) => {
  if (!config.url.includes('register') && !config.url.includes('login')) {
    config.headers.token = localStorage.getItem('token');
  }
  if (config.url.includes('confirm')) {
    //设置下单令牌
    config.headers['request-token'] = localStorage.getItem('request-token');
  }
  return config;
});

/**
 * 响应拦截
 */
service.interceptors.response.use((res) => {
  return res.data;
});

/**
 * 封装请求函数
 */
const request = (options) => {
  if (options.method === 'get' || options.method === 'delete') {
    options.params = options.data;
  }
  return service(options);
};

export default request;

