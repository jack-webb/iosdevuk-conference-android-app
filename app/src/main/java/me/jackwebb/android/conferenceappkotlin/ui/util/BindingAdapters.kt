package me.jackwebb.android.conferenceappkotlin.ui.util

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import me.jackwebb.android.conferenceappkotlin.model.session.SessionType
import java.util.*

object BindingAdapters {
    /**
     * Makes the View [View.GONE] unless the condition is met.
     */
    @BindingAdapter("goneUnless")
    @JvmStatic
    fun View.goneUnless(visible: Boolean) {
        visibility = if (visible) View.VISIBLE else View.GONE
    }

    @BindingAdapter("sessionTypeText")
    @JvmStatic
    fun TextView.sessionTypeText(sessionType: SessionType) {
        text = sessionType.toString().toLowerCase(Locale.ROOT).capitalize(Locale.ROOT)
    }

    @BindingAdapter("setAvatar")
    @JvmStatic
    fun CircleImageView.setAvatar(speakerId: String) {
        Picasso.get().load("file:///android_asset/avatars/${speakerId}.jpg")
            .into(this)
    }
}