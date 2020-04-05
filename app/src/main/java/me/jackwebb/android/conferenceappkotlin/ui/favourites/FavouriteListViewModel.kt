package me.jackwebb.android.conferenceappkotlin.ui.favourites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import me.jackwebb.android.conferenceappkotlin.datasource.ConferenceRepository
import me.jackwebb.android.conferenceappkotlin.model.session.Session

class FavouriteListViewModel(application: Application): AndroidViewModel(application) {
    private val repository: ConferenceRepository = ConferenceRepository(application)

    private var mSessions: LiveData<List<Session>>

    init {
        mSessions = repository.getFavouriteSessions()
    }

    fun sessions() = mSessions

    fun favouriteSession(session: Session) = repository.favouriteSession(session)
    fun unfavouriteSession(session: Session) = repository.unfavouriteSession(session)
}
