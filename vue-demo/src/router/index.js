import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
// import Layout from '@/views/layout/Layout'

export const constantRouterMap = [
    {
        path: '/',
        component: () => import('@/views/index.vue'),
        children: [
            {
                path: '/', // 嵌套路由里默认是哪个网页
                redirect: '/websocket'
            },
            // 菜单界面路由
            {
                path: '/websocket',
                name: 'websocket',
                component: () => import('../views/websocket/index.vue')
            },
            // 菜单界面路由
            {
                path: '/websocket2',
                name: 'websocket2',
                component: () => import('../views/websocket/index2.vue')
            }
        ]
    }
]

export default new Router({
    scrollBehavior: () => ({
        y: 0
    }),
    routes: constantRouterMap
})
