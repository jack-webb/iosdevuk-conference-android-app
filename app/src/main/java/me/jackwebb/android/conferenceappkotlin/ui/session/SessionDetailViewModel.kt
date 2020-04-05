package me.jackwebb.android.conferenceappkotlin.ui.session

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import me.jackwebb.android.conferenceappkotlin.datasource.ConferenceRepository
import me.jackwebb.android.conferenceappkotlin.model.session.Session

class SessionDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ConferenceRepository = ConferenceRepository(application)

    fun location(id: String) = repository.getLocationById(id)
    fun speaker(id: String) = repository.getSpeakerById(id)

    fun favouriteSession(session: Session) = repository.favouriteSession(session)
    fun unfavouriteSession(session: Session) = repository.unfavouriteSession(session)
}
