package me.jackwebb.android.conferenceappkotlin.model.session

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.TypeConverters
import androidx.room.Update
import me.jackwebb.android.conferenceappkotlin.model.util.DateTimeConverter
import me.jackwebb.android.conferenceappkotlin.model.util.SessionTypeConverter

@Dao
@TypeConverters(DateTimeConverter::class, SessionTypeConverter::class)
interface SessionDao {
    @Query("SELECT * FROM sessions")
    fun allSessions(): LiveData<List<Session>>

    @Query("SELECT * FROM sessions WHERE sessions.sessionType = 'talk'")
    fun allTalks(): LiveData<List<Session>>

//    @Query("SELECT * FROM (sessions LEFT JOIN speakers ON sessions.speakerId = speakers.id) AS sessionsspeakers INNER JOIN locations ON sessionsspeakers.locationId = locations.id")
//    @Query("SELECT * FROM sessions LEFT JOIN speakers ON sessions.speakerId = speakers.id")
//    fun allSessionsForList(): LiveData<List<SessionWithSpeakerAndLocation>>

    @Query("SELECT * FROM sessions WHERE sessions.isFavourite = 1")
    fun favouriteSessions(): LiveData<List<Session>>

    @Query("SELECT * FROM sessions WHERE id == :id")
    fun sessionById(id: String): LiveData<Session>

    @Update
    fun update(session: Session)

//    @Query("SELECT * FROM sessions")
//    fun getAllSessionsWithSpeakerAndLocation(): LiveData<List<SessionWithSpeakerAndLocation>>
}