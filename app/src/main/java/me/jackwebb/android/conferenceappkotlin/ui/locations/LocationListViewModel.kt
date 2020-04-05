package me.jackwebb.android.conferenceappkotlin.ui.locations

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import me.jackwebb.android.conferenceappkotlin.datasource.ConferenceRepository

class LocationListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ConferenceRepository = ConferenceRepository(application)
    val locations = repository.getAllLocations()

}
