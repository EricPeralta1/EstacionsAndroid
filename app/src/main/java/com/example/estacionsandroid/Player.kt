package com.example.estacionsandroid

class Player (
    var name: String,

    var errors1: Int,
    var usedHints1: Int,
    var time1: Int,

    var errors2: Int,
    var usedHints2: Int,
    var time2: Int,

    var errors3: Int,
    var usedHints3: Int,
    var time3: Int,

    var date: String
){

    constructor(name: String) : this(name, 0, 0, 0, 0, 0, 0, 0, 0, 0, "")
}