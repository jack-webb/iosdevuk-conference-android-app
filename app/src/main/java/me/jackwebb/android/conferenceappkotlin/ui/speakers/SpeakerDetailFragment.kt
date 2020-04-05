package me.jackwebb.android.conferenceappkotlin.ui.speakers

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import me.jackwebb.android.conferenceappkotlin.R
import me.jackwebb.android.conferenceappkotlin.databinding.FragmentSpeakerDetailBinding
import me.jackwebb.android.conferenceappkotlin.model.speaker.Speaker

class SpeakerDetailFragment : Fragment() {
    private val TAG = this::class.java.name

    private lateinit var speaker: Speaker
    private lateinit var mBinding: FragmentSpeakerDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_speaker_detail, container, false)

        arguments?.let {
            val safeArgs = SpeakerDetailFragmentArgs.fromBundle(it)
            speaker = safeArgs.speaker
        }

        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mBinding.speaker = speaker
        mBinding.btnTwitter.setOnClickListener { openTwitterLink(speaker.twitterName!!) }
    }

    private fun openTwitterLink(twitterName: String) {
        try { // Open in the twitter app with the twitter URI
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=${twitterName}"))
            startActivity(intent)
        } catch (e: Exception) { // Open in the browser with a standard URL
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/${twitterName}"))
            startActivity(intent)
        }
    }
}
