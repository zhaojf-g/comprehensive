import SockJS from 'sockjs-client'
import Stomp from 'stompjs'

const websocket = {
    url: '',
    checkInterval: null, // 断线重连时 检测连接是否建立成功
    websocket: null,
    stompClient: null,
    listenerList: [], // 监听器列表，断线重连时 用于重新注册监听


    init(url) {
        if (this.stompClient == null || !this.stompClient.connected) {
            this.url = url || this.url
            if (this.stompClient != null && this.websocket.readyState === SockJS.OPEN) {
                this.stompClient.disconnect(() => {
                    this.connect()
                })
            } else if (this.stompClient != null && this.websocket.readyState === SockJS.CONNECTING) {
                console.log('连接正在建立')
                return
            } else {
                this.connect()
            }

            if (!this.checkInterval) {
                this.checkInterval = setInterval(() => {
                    console.log('检测连接：' + this.websocket.readyState)
                    if (this.stompClient != null && this.stompClient.connected) {
                        clearInterval(this.checkInterval)
                        this.checkInterval = null
                        console.log('重连成功')
                    } else if (this.stompClient != null && this.websocket.readyState !== SockJS.CONNECTING) {
                        // websocket的状态为open 但是stompClient的状态却是未连接状态，故此处需要把连接断掉 然后重连
                        this.stompClient.disconnect(() => {
                            this.connect()
                        })
                    }
                }, 2000)
            }
        } else {
            console.log('连接已建立成功，不再执行')
        }
    },
    connect() {
        const _this = this
        const websocket = new SockJS(this.url + "?token=1111111111")
        this.websocket = websocket
        // 获取STOMP子协议的客户端对象
        const stompClient = Stomp.over(websocket)
        stompClient.debug = null // 关闭控制台打印
        stompClient.heartbeat.outgoing = 20000
        stompClient.heartbeat.incoming = 10000// 0: 客户端不从服务端接收心跳包
        // 向服务器发起websocket连接
        let headers = {
            token: "92c31bd5-ae43-4f25-9aad-c4eb1a92d61d"
            //          Authorization: ''
        }
        stompClient.connect(
            headers, // 此处注意更换自己的用户名，最好以参数形式带入
            frame => {
                console.log('连接成功！')
                _this.listenerList.forEach(item => {
                    _this.stompClient.subscribe(item.topic, item.callback)
                })

            },
            () => { // 第一次连接失败和连接后断开连接都会调用这个函数 此处调用重连
                console.log('连接断开')
                if (!this.checkInterval) {
                    setTimeout(() => {
                        _this.init()
                    }, 1000)
                }

            }
        )
        this.stompClient = stompClient
    },
    send(topic, data) {
        console.log('发送信息', data)
        this.stompClient.send(topic, {}, data)
    },
    subscribe(topic, callback) {
        console.log('添加订阅', topic)
        if (this.listenerList.indexOf()) {

        }
        this.stompClient.subscribe(topic, callback);
        this.listenerList.push({topic: topic, callback: callback})
    },
    unsubscribe(topic) {
        this.stompClient.unsubscribe(topic)
        for (let i = 0; i < this.listenerList.length; i++) {
            if (this.listenerList[i].topic === p) {
                this.listenerList.splice(i, 1)
                console.log('解除订阅：' + p + ' size:' + this.listenerList.length)
                break
            }
        }
    }
}

export default websocket
