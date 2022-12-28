package com.example.scheduleappdb.UIZone.group

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface GroupOperatorDao
{
    @Query("SELECT * FROM groupoperator")
    fun getAll(): List<GroupOperator?>?

    @Query("SELECT * FROM groupoperator WHERE id = :id")
    fun getById(id: Int): GroupOperator

    @Insert
    fun insert(go: GroupOperator?)

    @Delete
    fun delete(go: GroupOperator?)
}