package com.cherry.cherrypie

class FoodItem {
    var url : String = "https://avatanplus.com/files/resources/original/57eebd005736f1577c924965.png"
    var name : String = ""

    constructor (url : String, name : String) {
        this.url = url
        this.name = name
    }

    constructor (name : String) {
        this.name = name
    }

    override fun toString(): String {
        return "FoodItem(url='$url', name='$name')"
    }


}