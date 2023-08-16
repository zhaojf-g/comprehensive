import Vue from 'vue'
import Router from 'vue-router'
import Layout from '@/views/layout'

Vue.use(Router)

export const constantRoutes = [
    {
        path: '/login',
        component: () => import('../views/login/index'),
        meta: {title: '登录', icon: 'home', affix: false},
        hidden: true
    },

    {
        path: '/404',
        component: () => import('@/views/error-page/404'),
        meta: {title: '404', icon: '404', affix: false},
        hidden: true
    },
    {
        path: '/',
        name: 'layout',
        component: Layout,
        redirect: '/dashboard',
        children: [
            {
                path: '/dashboard',
                component: () => import('@/views/dashboard/index'),
                name: 'dashboard',
                meta: {title: '首页', icon: 'home', affix: true}
            }
        ]
    },
    {
        path: '/documentation1',
        component: Layout,
        children: [
            {
                path: 'index',
                component: () => import('@/views/dashboard/index'),
                name: 'Documentation1',
                meta: {title: '文档', icon: 'settings', affix: false}
            }
        ]
    },
    {
        path: '/documentation2',
        component: Layout,
        children: [
            {
                path: 'index',
                component: () => import('@/views/dashboard/index'),
                name: 'Documentation2',
                meta: {title: '文档', icon: 'settings', affix: false}
            }
        ]
    },
    {
        path: '/test',
        component: Layout,
        meta: {title: '测试', icon: 'settings', affix: false},
        children: [
            {
                path: 'test1',
                component: () => import('@/views/dashboard/index'),
                name: 'Documentation3',
                meta: {title: '测试1', icon: 'settings', affix: false}
            },
            {
                path: 'test2',
                component: () => import('@/views/dashboard/index'),
                name: 'Documentation4',
                meta: {title: '测试2', icon: 'settings', affix: false}
            }
        ]
    },


    {
        path: '/404',
        component: () => import('@/views/error-page/404'),
        hidden: true
    },
    // 404 page must be placed at the end !!!
    // { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
    // mode: 'history', // require service support
    scrollBehavior: () => ({y: 0}),
    routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
    const newRouter = createRouter()
    router.matcher = newRouter.matcher // reset router
}

export default router
