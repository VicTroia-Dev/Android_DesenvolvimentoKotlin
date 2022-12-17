package com.example.androidfirebaseproject

class ModelCategory {

    var id:String = ""
    var categoria:String = ""
    var timestamp:Long = 0
    var uid:String = ""

    // requerido pelo Firebase
    constructor()

    constructor(id: String, categories: String, timestamp: Long, uid:String){
        this.id = id
        this.categoria = categories
        this.timestamp = timestamp
        this.uid = uid
    }


}