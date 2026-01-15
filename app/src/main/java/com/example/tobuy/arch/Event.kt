package com.example.tobuy.arch

open class Event <out T>(private val content :T){
    var hasBeenHandled = false
        private set //Allow external read but not write

    //returns the content and prevent its use again
    fun getContent() : T?{
        return if(hasBeenHandled){
            null
        }else{
            hasBeenHandled = true
            content
        }
    }

    //returns the content, even if its been handled
    fun peekContent(): T = content
}