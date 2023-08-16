import {constantRoutes} from '@/router'
import Layout from '../../views/layout/index'

/**
 * 递归异步菜单，返回路由表
 * @param menus
 * @param level
 */
function filterAsyncRouter(menus, level) {

    if (!menus || !menus.length > 0) {
        return []
    }
    const addRouters = []
    menus.forEach(route => {
        const name = route.menuName
        const addRouter = {
            path: route.urlSuffix,
            name: name
        }
        if (level === 1) {
            addRouter.component = Layout
        }
        if (route.children) {
            // 有子菜单
            addRouter.redirect = route.fullPath
            addRouter.children = filterAsyncRouter(route.children, level + 1)
            addRouter.meta = {
                title: route.menuName || name,
                icon: route.menuIcon || name
            }
        } else {
            if (level > 1) {
                addRouter.component = resolve => require([`@/views${route.fullPath}`], resolve)
                addRouter.name = route.menuName
                addRouter.meta = {title: route.menuName, icon: route.menuIcon}
            } else {
                // 没有子菜单
                addRouter.children = [
                    {
                        path: 'index',
                        component: resolve => require([`@/views${route.fullPath}`], resolve),
                        name: route.menuName,
                        meta: {title: route.menuName, icon: route.menuIcon}
                    }
                ]
            }
        }
        addRouter.alwaysShow = !!route.children
        addRouters.push(addRouter)
    })
    return addRouters

}


const state = {
    routes: [],
    addRoutes: []
}


const mutations = {
    SET_ROUTERS: (state, routers) => {
        state.addRouters = routers
        state.routes = constantRoutes.concat(routers)
    }
}


const actions = {
    generateRoutes({commit}, data) {
        return new Promise(resolve => {
            const accessedRouters = filterAsyncRouter(data, 1)

            accessedRouters.push(
                {path: '*', redirect: '/404', hidden: true}
            )

            commit('SET_ROUTERS', accessedRouters)
            resolve(accessedRouters)
        })
    }
}

export default {
    namespaced: true,
    state,
    mutations,
    actions
}
