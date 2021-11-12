package com.example.appchatkl.ui.content.createConversation.adapter

import com.example.appchatkl.data.Conversation
import com.example.appchatkl.data.CreateConversation

interface onclick {
    fun select(conversation: CreateConversation)
    fun minus(conversation: CreateConversation)
}