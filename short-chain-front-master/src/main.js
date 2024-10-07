import { createApp } from 'vue';
import App from './App.vue';
import './style/base.less';
import router from './router/index';
import antArr from './utils/antDesignVue';

const app = createApp(App);

for (const item of antArr) {
  app.use(item);
}

app.use(router).mount('#app');

