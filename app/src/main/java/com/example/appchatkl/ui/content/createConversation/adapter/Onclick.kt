package com.example.appchatkl.ui.content.createConversation.adapter


import com.example.appchatkl.data.CreateConversation

interface Onclick {
    fun select(conversation: CreateConversation)
    fun minus(conversation: CreateConversation)
}