import axios from 'axios'
import {Message} from 'element-ui'
import store from '@/store'
import {getToken, removeToken} from '@/utils/auth'
import qs from 'qs'

// create an axios instance
const service = axios.create({
    baseURL: '/tpms',
    // baseURL: process.env.VUE_APP_BASE_API // url = base url + request url
    // withCredentials: true, // send cookies when cross-domain requests
    // timeout: 10000 // request timeout
    paramsSerializer: function (params) {
        return qs.stringify(params, { arrayFormat: "indices" })
    }
})

// request interceptor
service.interceptors.request.use(
    config => {
        // do something before request is sent
        if (store.getters.token) {
            config.headers['Accept-Language'] = store.getters.language
            config.headers['jyby-tpms-token'] = getToken()
        }
        return config
    },
    error => {
        return Promise.reject(error)
    }
)

// response interceptor
service.interceptors.response.use(
    /**
     * If you want to get http information such as headers or status
     * Please return  response => response
     */

    /**
     * Determine the request status by custom code
     * Here is just an example
     * You can also judge the status by HTTP Status Code
     */
    response => {

        const res = response.data
    // 下载文件，直接返回response
    if (!res.code && response.headers['content-disposition']) {
        return response
    }
        
        if (!res.code|| res.code !== 200) {

            

            if (res.code === 207) {
                removeToken()
                location.reload()
            }
            Message({
                message: res.message,
                type: 'error',
                duration: 5 * 1000
            })

            return Promise.reject(new Error(res.message || 'Error'))
        } else {
        
            return res
        }
    },
    error => {
        console.log('err' + error) // for debug
        Message({
            message: error.message,
            type: 'error',
            duration: 5 * 1000
        })
        return Promise.reject(error)
    }
)

export default service
