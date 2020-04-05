package me.jackwebb.android.conferenceappkotlin.model.session

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import me.jackwebb.android.conferenceappkotlin.model.util.DateTimeConverter
import me.jackwebb.android.conferenceappkotlin.model.util.SessionTypeConverter
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName = "sessions")
@TypeConverters(DateTimeConverter::class, SessionTypeConverter::class)
data class Session(
    @PrimaryKey val id: String, val title: String?,
    val content: String?,
    val locationId: String?,
    val sessionDate: LocalDate?,
    val sessionOrder: Int,
    val timeStart: LocalTime?,
    val timeEnd: LocalTime?,
    val sessionType: SessionType?,
    val speakerId: String?,
    var isFavourite: Boolean?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        DateTimeConverter().stringToDate(parcel.readString()!!),
        parcel.readInt(),
        DateTimeConverter().stringToTime(parcel.readString()!!),
        DateTimeConverter().stringToTime(parcel.readString()!!),
        SessionTypeConverter().toSessionType(parcel.readString()!!),
        parcel.readString(),
        parcel.readInt() != 0
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(content)
        parcel.writeString(locationId)
        parcel.writeString(DateTimeConverter().dateToString(sessionDate!!))
        parcel.writeInt(sessionOrder)
        parcel.writeString(DateTimeConverter().timeToString(timeStart!!))
        parcel.writeString(DateTimeConverter().timeToString(timeEnd!!))
        parcel.writeString(SessionTypeConverter().toString(sessionType!!))
        parcel.writeString(speakerId)
        parcel.writeInt(if (isFavourite!!) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Session> {
        override fun createFromParcel(parcel: Parcel): Session {
            return Session(parcel)
        }

        override fun newArray(size: Int): Array<Session?> {
            return arrayOfNulls(size)
        }
    }
}

//class SessionWithSpeakerNameAndLocationName (
//    @Embedded
//    val session: Session,
//    @Nullable  // e.g. socials, meals
//    @Relation(parentColumn = "speakerId", entityColumn = "id", entity = Speaker::class)
//    val speakerName: String,
//    @Relation(parentColumn = "locationId", entityColumn = "id", entity = Location::class)
//    val locationName: String
//)