package com.cherry.cherrypie

import java.util.*

class Message {
    var textMessage: String? = null
    var userName: String? = null
    var messageTime: Long = 0

    constructor(textMessage: String?, userName: String?) {
        this.textMessage = textMessage
        this.userName = userName
        messageTime = Date().time
    }

    constructor()
}