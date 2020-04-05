package me.jackwebb.android.conferenceappkotlin.ui.speakers

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import me.jackwebb.android.conferenceappkotlin.datasource.ConferenceRepository

class SpeakerViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ConferenceRepository = ConferenceRepository(application)
    val speakers = repository.getAllSpeakers()

}
