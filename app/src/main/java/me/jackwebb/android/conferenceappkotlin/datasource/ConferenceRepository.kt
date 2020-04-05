package me.jackwebb.android.conferenceappkotlin.datasource

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import me.jackwebb.android.conferenceappkotlin.model.location.Location
import me.jackwebb.android.conferenceappkotlin.model.location.LocationDao
import me.jackwebb.android.conferenceappkotlin.model.session.Session
import me.jackwebb.android.conferenceappkotlin.model.session.SessionDao
//import me.jackwebb.android.conferenceappkotlin.model.session.SessionWithSpeakerAndLocation
import me.jackwebb.android.conferenceappkotlin.model.speaker.Speaker
import me.jackwebb.android.conferenceappkotlin.model.speaker.SpeakerDao

class ConferenceRepository(app: Application) {
    private val TAG = this::class.java.name

    private var sessionDao: SessionDao
    private var speakerDao: SpeakerDao
    private var locationDao: LocationDao

    init {
        val db = ConferenceDatabase.getDatabase(app)
        sessionDao = db.sessionDao()
        speakerDao = db.speakerDao()
        locationDao = db.locationDao()
    }


    fun getAllSessions(): LiveData<List<Session>> {
        return sessionDao.allSessions()
    }

    fun getFavouriteSessions(): LiveData<List<Session>> {
        return sessionDao.favouriteSessions()
    }

    fun getAllSpeakers(): LiveData<List<Speaker>> {
        return speakerDao.all()
    }

    fun getAllLocations(): LiveData<List<Location>> {
        return locationDao.all()
    }

    fun getSpeakerById(id: String): LiveData<Speaker> {
        return speakerDao.speakerById(id)
    }

    fun getLocationById(id: String): LiveData<Location> {
        return locationDao.locationById(id)
    }

    fun favouriteSession(session: Session) {
        session.isFavourite = true
        UpdateAsyncTask(sessionDao).execute(session)
    }

    fun unfavouriteSession(session: Session) {
        session.isFavourite = false
        UpdateAsyncTask(sessionDao).execute(session)
    }


    private class UpdateAsyncTask internal constructor(private val mAsyncTaskDao: SessionDao) :
        AsyncTask<Session, Void, Void>() {

        override fun doInBackground(vararg params: Session): Void? {
            mAsyncTaskDao.update(params[0]) // This line throws the exception
            return null
        }
    }
}