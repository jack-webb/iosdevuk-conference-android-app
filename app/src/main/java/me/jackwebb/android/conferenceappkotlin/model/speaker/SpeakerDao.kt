package me.jackwebb.android.conferenceappkotlin.model.speaker


import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

@Dao
interface SpeakerDao {

    @Query("SELECT * FROM speakers")
    fun all(): LiveData<List<Speaker>>

    @Query("SELECT * FROM speakers WHERE id == :id")
    fun speakerById(id:String) : LiveData<Speaker>

}