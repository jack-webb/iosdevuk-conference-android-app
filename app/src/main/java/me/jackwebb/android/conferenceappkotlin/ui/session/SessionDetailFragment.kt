package me.jackwebb.android.conferenceappkotlin.ui.session

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_session_detail.*
import me.jackwebb.android.conferenceappkotlin.R
import me.jackwebb.android.conferenceappkotlin.databinding.FragmentSessionDetailBinding
import me.jackwebb.android.conferenceappkotlin.model.session.Session
import me.jackwebb.android.conferenceappkotlin.model.session.SessionType


class SessionDetailFragment : Fragment() {
    private val TAG = this::class.java.name

    private lateinit var viewModel: SessionDetailViewModel
    private lateinit var session: Session
    private lateinit var mBinding: FragmentSessionDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SessionDetailViewModel::class.java)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_session_detail, container, false)

        arguments?.let {
            val safeArgs = SessionDetailFragmentArgs.fromBundle(it)
            session = safeArgs.session
        }

        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mBinding.session = session
        mBinding.sessionHasSpeaker = session.speakerId != ""

        if (session.locationId == null) {
            btnLocation.visibility = View.GONE
        } else {
            viewModel.location(session.locationId!!).observe(this, Observer { location ->
                Log.d(TAG, "Setting location button for ${location.name}")
                mBinding.btnLocation.text = getString(R.string.more_about, location.name)
                mBinding.btnLocation.setOnClickListener {
                    findNavController().navigate(
                        SessionDetailFragmentDirections.showSessionLocationDetails(location)
                    )
                }

                Log.d(TAG, "Setting location string for ${location.name}")
                mBinding.txtLocation.text = location.name
            })
        }

        if (session.speakerId == null || session.speakerId!!.isEmpty()) {
            btnSpeaker.visibility = View.GONE
        } else {
            viewModel.speaker(session.speakerId!!).observe(this, Observer { speaker ->
                Log.d(TAG, "Setting speaker button for ${speaker.name}")
                mBinding.btnSpeaker.text = getString(R.string.more_about, speaker.name)
                mBinding.btnSpeaker.setOnClickListener {
                    findNavController().navigate(
                        SessionDetailFragmentDirections.showSessionSpeakerDetails(speaker)
                    )
                }

                Log.d(TAG, "Setting speaker string for ${speaker.name}")
                mBinding.txtSpeaker.text = speaker.name
            })
        }


    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (session.sessionType != SessionType.TALK && session.sessionType != SessionType.WORKSHOP)
            return
        
        inflater.inflate(R.menu.session_detail_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        if (session.sessionType != SessionType.TALK && session.sessionType != SessionType.WORKSHOP)
            return

        val favourite = menu.findItem(R.id.action_favourite)
        val unfavourite = menu.findItem(R.id.action_unfavourite)

        favourite.isVisible = !session.isFavourite!!
        unfavourite.isVisible = session.isFavourite!!
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_favourite -> {
                Toast.makeText(context, R.string.add_fav, Toast.LENGTH_SHORT).show()
                viewModel.favouriteSession(session)
            }
            R.id.action_unfavourite -> {
                Toast.makeText(context, R.string.remove_fav, Toast.LENGTH_SHORT).show()
                viewModel.unfavouriteSession(session)
            }
        }
        (activity as AppCompatActivity).invalidateOptionsMenu()

        return super.onOptionsItemSelected(item)
    }
}