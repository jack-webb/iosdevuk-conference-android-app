package me.jackwebb.android.conferenceappkotlin.model.speaker

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "speakers")
data class Speaker(
    @PrimaryKey
    val id: String,
    val name: String?,
    @ColumnInfo(name = "biography")
    val bio: String?,
    @ColumnInfo(name = "twitter")
    val twitterName: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(bio)
        parcel.writeString(twitterName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Speaker> {
        override fun createFromParcel(parcel: Parcel): Speaker {
            return Speaker(parcel)
        }

        override fun newArray(size: Int): Array<Speaker?> {
            return arrayOfNulls(size)
        }
    }
}