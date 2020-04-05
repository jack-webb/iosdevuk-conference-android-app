package me.jackwebb.android.conferenceappkotlin.datasource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import me.jackwebb.android.conferenceappkotlin.model.location.Location
import me.jackwebb.android.conferenceappkotlin.model.location.LocationDao
import me.jackwebb.android.conferenceappkotlin.model.session.Session
import me.jackwebb.android.conferenceappkotlin.model.session.SessionDao
import me.jackwebb.android.conferenceappkotlin.model.speaker.Speaker
import me.jackwebb.android.conferenceappkotlin.model.speaker.SpeakerDao

@Database(entities = [Session::class, Location::class, Speaker::class], version = 1)
abstract class ConferenceDatabase : RoomDatabase() {
    private val TAG = this::class.java.name

    abstract fun sessionDao(): SessionDao
    abstract fun speakerDao(): SpeakerDao
    abstract fun locationDao(): LocationDao

    companion object {
        private val TAG = this::class.java.name

        private const val INSTANCE_NAME = "conference_database"
        private const val DATABASE_FILENAME = "conf-with-not-null-constraints.db"
        private const val DATABASE_FILEPATH = "databases/$DATABASE_FILENAME"

        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: ConferenceDatabase? = null

        fun getDatabase(context: Context): ConferenceDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ConferenceDatabase::class.java,
                    INSTANCE_NAME
                )
                    .createFromAsset(DATABASE_FILEPATH)
                    .build()
                INSTANCE = instance
                return instance
            }
        }

    }
}
