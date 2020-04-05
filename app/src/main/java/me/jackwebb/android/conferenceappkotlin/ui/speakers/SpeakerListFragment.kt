package me.jackwebb.android.conferenceappkotlin.ui.speakers

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_speaker_list.*
import me.jackwebb.android.conferenceappkotlin.R
import me.jackwebb.android.conferenceappkotlin.model.speaker.Speaker

class SpeakerListFragment : Fragment() {
    private val TAG = this::class.java.name

    private lateinit var viewModel: SpeakerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SpeakerViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_speaker_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val detailListener = object : SpeakerRecyclerViewAdapter.ClickListener {
            override fun onClick(speaker: Speaker) {
                findNavController().navigate(
                    SpeakerListFragmentDirections.showSpeakerDetails(speaker)
                )
            }
        }

        val adapter = SpeakerRecyclerViewAdapter(detailListener)
        speakerListView.layoutManager = LinearLayoutManager(context)
        speakerListView.adapter = adapter

        viewModel.speakers.observe(this, Observer { speakers ->
            Log.d(TAG, "${speakers.size} speakers(s) observed in viewModel")
            adapter.setData(speakers.sortedBy { it.name })
        })
    }
}
