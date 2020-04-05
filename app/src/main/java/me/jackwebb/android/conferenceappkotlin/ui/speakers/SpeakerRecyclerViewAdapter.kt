package me.jackwebb.android.conferenceappkotlin.ui.speakers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import me.jackwebb.android.conferenceappkotlin.R
import me.jackwebb.android.conferenceappkotlin.databinding.FragmentSpeakerListItemBinding
import me.jackwebb.android.conferenceappkotlin.model.speaker.Speaker

class SpeakerRecyclerViewAdapter(
    private val mOnClickDetails: ClickListener
) : RecyclerView.Adapter<SpeakerRecyclerViewAdapter.SpeakerViewHolder>() {

    private var speakersData: List<Speaker> = emptyList()

    interface ClickListener {
        fun onClick(speaker: Speaker)
    }

    class SpeakerViewHolder(val view: View, private val binding: FragmentSpeakerListItemBinding) :
        RecyclerView.ViewHolder(view) {
        fun bind(speakerData: Speaker, mOnClickDetails: ClickListener) {
            binding.speaker = speakerData
            binding.speakerAvatarPath = "file:///android_asset/avatars/${speakerData.id.trim()}.jpg"
            binding.speakerListItem.setOnClickListener {
                mOnClickDetails.onClick(speakerData)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpeakerViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding: FragmentSpeakerListItemBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_speaker_list_item,
            parent,
            false
        )

        return SpeakerViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: SpeakerViewHolder, position: Int) {
        holder.bind(speakersData[position], mOnClickDetails)
    }

    override fun getItemCount() = speakersData.size

    internal fun setData(speakers: List<Speaker>) {
        this.speakersData = speakers
        notifyDataSetChanged()
    }

}
