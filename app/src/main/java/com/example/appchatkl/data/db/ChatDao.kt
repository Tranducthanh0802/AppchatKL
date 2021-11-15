package com.example.appchatkl.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.appchatkl.data.User
import com.example.appchatkl.data.db.data.Conversations
import com.example.appchatkl.data.db.data.Save

@Dao
interface ChatDao {
    @Insert(onConflict = REPLACE)
    fun insertUser(user: User)

    @Insert(onConflict = REPLACE)
    fun insertConversation(conversation: Conversations)

    @Insert(onConflict = REPLACE)
    fun insertSave(save: Save)

    @Query("SELECT * FROM User")
    fun loadUser(): List<User>

    @Query("SELECT * FROM Conversations")
    fun loadConversation(): List<Conversations>

    @Query("SELECT * FROM Save")
    fun loadSave(): List<Save>

    @Query("DELETE FROM Save")
    fun deleteSave()


}