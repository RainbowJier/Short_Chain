import { createRouter, createWebHashHistory } from 'vue-router';
import { message } from 'ant-design-vue';

const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    {
      path: '/',
      redirect: '/home',
    },
    {
      path: '/home',
      component: () => import('../views/home/Home.vue'),
    },
    {
      path: '/loginRegister',
      component: () => import('../views/loginRegister/LoginRegister'),
    },
    {
      path: '/shortChain',
      component: () => import('../views/shortChain/ShortChain'),
      meta: { isAuth: true },
    },
    {
      path: '/personal',
      component: () => import('../views/personal/Personal'),
      meta: { isAuth: true },
    },
    {
      path: '/scheme',
      component: () => import('../views/scheme/Scheme'),
    },
    {
      name: 'payPage',
      path: '/payPage',
      component: () => import('../views/payPage/PayPage'),
      meta: { isAuth: true },
    },
    {
      name: 'demo',
      path: '/demo',
      component: () =>
        import('../views/shortChain/components/echart/ShortChainEchartDemo'),
    },
  ],
});
// 路由拦截
router.beforeEach((to, from, next) => {
  //判断是否需要登录权限
  if (to.meta.isAuth) {
    if (localStorage.getItem('token')) {
      next();
    } else {
      message.error('请先登录!');
      router.push('./loginRegister');
    }
  } else {
    next();
  }
});

export default router;

