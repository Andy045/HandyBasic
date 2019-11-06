package com.handy.basic.eventbus

/**
 * @title: BaseEventMessage
 * @projectName HandyBasicKT
 * @description: EventBus消息体通用超类
 * @author LiuJie https://github.com/Handy045
 * @date Created in 2019-10-11 16:30
 */
open class BaseEventMessage {
    var result = false
    var message = ""
    var any: Any? = null

    constructor(result: Boolean) {
        this.result = result
    }

    constructor(message: String) {
        this.message = message
    }

    constructor(result: Boolean, message: String) {
        this.result = result
        this.message = message
    }

    constructor(any: Any) {
        this.any = any
    }

    constructor(result: Boolean, any: Any) {
        this.result = result
        this.any = any
    }
}