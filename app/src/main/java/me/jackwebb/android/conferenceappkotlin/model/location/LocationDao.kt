package me.jackwebb.android.conferenceappkotlin.model.location

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

@Dao
interface LocationDao {

    @Query("SELECT * FROM locations")
    fun all(): LiveData<List<Location>>

    @Query("SELECT * FROM locations WHERE id == :id")
    fun locationById(id:String): LiveData<Location>

}