import './assets/main.css'

import { createApp } from 'vue'

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

import App from './App.vue'
import router from './router'
import axios from "axios";

// axios 配置
// 配置 axios 默认的根路径
axios.defaults.baseURL = import.meta.env.VITE_API_HOST;

// 可以在此处配置请求头、超时等
// axios.defaults.headers.common['Authorization'] = 'Bearer token';
axios.defaults.timeout = 10000;

const app = createApp(App)

app.use(router)
app.use(ElementPlus)

app.mount('#app')
