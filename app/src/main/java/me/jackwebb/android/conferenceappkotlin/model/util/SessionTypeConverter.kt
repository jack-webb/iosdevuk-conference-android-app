package me.jackwebb.android.conferenceappkotlin.model.util

import androidx.room.TypeConverter
import me.jackwebb.android.conferenceappkotlin.model.session.SessionType

class SessionTypeConverter {
    @TypeConverter
    fun toSessionType(string: String): SessionType {
        return when (string) {
            "coffee" -> SessionType.COFFEE
            "dinner" -> SessionType.DINNER
            "lunch" -> SessionType.LUNCH
            "registration" -> SessionType.REGISTRATION
            "talk" -> SessionType.TALK
            "workshop" -> SessionType.WORKSHOP
            else -> throw Exception("Unknown sessionType")
        }
    }

    @TypeConverter
    fun toString(sessionType: SessionType): String {
        return when (sessionType) {
            SessionType.COFFEE -> "coffee"
            SessionType.DINNER -> "dinner"
            SessionType.LUNCH -> "lunch"
            SessionType.REGISTRATION -> "registration"
            SessionType.TALK -> "talk"
            SessionType.WORKSHOP -> "workshop"
        }
    }
}
