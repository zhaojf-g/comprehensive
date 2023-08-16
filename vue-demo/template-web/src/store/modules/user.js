import {login, logout, getInfo, getUserInfo} from '@/api/user'
import {getToken, setToken, removeToken} from '@/utils/auth'
import router, {resetRouter} from '@/router'

const state = {
    token: getToken(),
    name: '',
    menus: [],
    userNodeId: '',
    userType: '',
}

const mutations = {
    SET_TOKEN: (state, token) => {
        state.token = token
    },
    SET_NAME: (state, name) => {
        state.name = name
    },
    SET_ROLES: (state, roles) => {
        state.roles = roles
    },
    SET_MENUS: (state, menus) => {
        state.menus = menus
    },
    SET_USERNODEID: (state, userNodeId) => {
        state.userNodeId = userNodeId
    },
    SET_USERTYPE: (state, userType) => {
        state.userType = userType
    }
}

const actions = {
    // user login
    login({commit}, userInfo) {
        const {username, password} = userInfo
        return new Promise((resolve, reject) => {
            login({username: username.trim(), password: password}).then(res => {
                commit('SET_TOKEN', res.data.token)
                setToken(res.data.token)
                resolve()
            }).catch(error => {
                reject(error)
            })
        })
    },

    // get user info
    getInfo({commit}) {
        return new Promise((resolve, reject) => {
            getUserInfo().then(res => {
                const {code} = res
                if (code !== 200) {
                    reject('验证失败，请重新登录！')
                }
                const {data} = res
                const {userName, roleId, menus} = data

                // roles must be a non-empty array
                // if (!roles || roles.length <= 0) {
                //     reject('getInfo: roles must be a non-null array!')
                // }
                commit('SET_NAME', userName)
                commit('SET_ROLES', [roleId])
                commit('SET_MENUS', menus)
                // if (user.userNodeId) {
                //     commit('SET_USERNODEID', user.userNodeId)
                // }
                // if (user.userType) {
                //     commit('SET_USERTYPE', user.userType)
                // }
                resolve(data)
            }).catch(error => {
                reject(error)
            })
        })
    },

    // user logout
    logout({commit, state, dispatch}) {
        return new Promise((resolve, reject) => {
            logout(state.token).then(() => {
                commit('SET_TOKEN', '')
                commit('SET_ROLES', [])
                removeToken()
                resetRouter()

                // reset visited views and cached views
                // to fixed https://github.com/PanJiaChen/vue-element-admin/issues/2485
                dispatch('tagsView/delAllViews', null, {root: true})

                resolve()
            }).catch(error => {
                reject(error)
            })
        })
    },

    // remove token
    resetToken({commit}) {
        return new Promise(resolve => {
            commit('SET_TOKEN', '')
            commit('SET_ROLES', [])
            removeToken()
            resolve()
        })
    },

    // dynamically modify permissions
    async changeRoles({commit, dispatch}, role) {
        const token = role + '-token'

        commit('SET_TOKEN', token)
        setToken(token)

        const {roles} = await dispatch('getInfo')

        resetRouter()

        // generate accessible routes map based on roles
        const accessRoutes = await dispatch('permission/generateRoutes', roles, {root: true})
        // dynamically add accessible routes
        router.addRoutes(accessRoutes)

        // reset visited views and cached views
        dispatch('tagsView/delAllViews', null, {root: true})
    }
}

export default {
    namespaced: true,
    state,
    mutations,
    actions
}