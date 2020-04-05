package me.jackwebb.android.conferenceappkotlin.ui.favourites

import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.daimajia.swipe.util.Attributes
import kotlinx.android.synthetic.main.fragment_session_list.*
import me.jackwebb.android.conferenceappkotlin.R
import me.jackwebb.android.conferenceappkotlin.model.session.Session
import me.jackwebb.android.conferenceappkotlin.model.session.SessionType
import me.jackwebb.android.conferenceappkotlin.ui.util.SessionRecyclerViewAdapter

class FavouriteListFragment : Fragment() {
    private val TAG = this::class.java.name

    private lateinit var viewModel: FavouriteListViewModel
    private lateinit var adapter: SessionRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FavouriteListViewModel::class.java)
        setHasOptionsMenu(true) // enable our menu (search, settings etc.)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_session_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val detailListener = object : SessionRecyclerViewAdapter.ClickListener {
            override fun onClick(session: Session) {
                findNavController().navigate(
                    FavouriteListFragmentDirections.showFavouriteDetails(session)
                )
            }
        }
        val favouriteListener = object : SessionRecyclerViewAdapter.ClickListener {
            override fun onClick(session: Session) {
                Toast.makeText(context, R.string.add_fav, Toast.LENGTH_SHORT).show()
                viewModel.favouriteSession(session)
            }
        }
        val unfavouriteListener = object : SessionRecyclerViewAdapter.ClickListener {
            override fun onClick(session: Session) {
                Toast.makeText(context, R.string.remove_fav, Toast.LENGTH_SHORT).show()
                viewModel.unfavouriteSession(session)
            }
        }

        adapter = SessionRecyclerViewAdapter(
            detailListener,
            favouriteListener,
            unfavouriteListener
        )
        adapter.mode = Attributes.Mode.Single // Allow only one swipe action open

        sessionListRecyclerView.layoutManager = LinearLayoutManager(context)
        sessionListRecyclerView.adapter = adapter

        viewModel.sessions().observe(this, Observer {
            adapter.setData(it)
        })

        // Don't need to show the favouritePrompt here
        favouritePrompt.visibility = View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favourite_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.imeOptions =
            EditorInfo.IME_ACTION_DONE  // Replace unused "search" icon on keyboard

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // We use live filtering, and thus never submit
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d(TAG, "onQueryTextChange with string ${newText!!}")
                adapter.filter.filter(newText)
                return false
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_filter_everything -> adapter.filter.filter("")
            R.id.action_filter_talks -> adapter.filter.filter(SessionType.TALK.name)
            R.id.action_filter_workshops -> adapter.filter.filter(SessionType.WORKSHOP.name)
        }
        return true
    }
}
