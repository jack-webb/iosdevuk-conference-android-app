package me.jackwebb.android.conferenceappkotlin.ui.util


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.swipe.SwipeLayout
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter
import kotlinx.android.synthetic.main.fragment_session_list_item.view.*
import me.jackwebb.android.conferenceappkotlin.R
import me.jackwebb.android.conferenceappkotlin.databinding.FragmentSessionListItemBinding
import me.jackwebb.android.conferenceappkotlin.model.session.Session
import me.jackwebb.android.conferenceappkotlin.model.session.SessionType
//import me.jackwebb.android.conferenceappkotlin.model.session.SessionWithSpeakerAndLocation
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

class SessionRecyclerViewAdapter(
    private val mOnClickDetails: ClickListener,
    private val mOnClickFavourite: ClickListener,
    private val mOnClickUnfavourite: ClickListener
) : RecyclerSwipeAdapter<SessionRecyclerViewAdapter.SessionViewHolder>(), Filterable {
    private val TAG = this::class.java.name

    private var allSessionsData: List<Session> = emptyList() // Every session from the VM
    private var mSessionsData: MutableList<Session> =
        mutableListOf() // Sessions we should show when filtering

    override fun getSwipeLayoutResourceId(position: Int): Int = R.id.sessionListSwipeLayout

    interface ClickListener {
        fun onClick(session: Session)
    }

    inner class SessionViewHolder(view: View, private val binding: FragmentSessionListItemBinding) :
        RecyclerView.ViewHolder(view) {

        fun bind(
            session: Session,
            mOnClickDetails: ClickListener,
            mOnClickFavourite: ClickListener,
            mOnClickUnfavourite: ClickListener
        ) {
            binding.setSession(session)
            binding.sessionHasSpeaker = session.speakerId != ""
            bindExtraData(session) // Bind some things that android can't
            bindSwipeActions(
                session,
                mOnClickDetails,
                mOnClickFavourite,
                mOnClickUnfavourite
            ) // Bind click and swipe click listeners
            mItemManger.closeAllItems() // Ensure all the swipes are closed
            binding.executePendingBindings()
        }

        private fun bindExtraData(session: Session) { //todo move these to custom binding adapters
            binding.txtDay.text = session.sessionDate!!.dayOfWeek.getDisplayName(
                TextStyle.SHORT_STANDALONE,
                Locale.UK
            )

            binding.txtStartTime.text =
                session.timeStart?.format(DateTimeFormatter.ofPattern("HH:mm"))

            binding.txtSessionTypeEmoji.text = getEmoji(session.sessionType)

            setIndicatorColour(session)
        }

        private fun getEmoji(sessionType: SessionType?): CharSequence {
            return itemView.context.getString(
                when (sessionType) {
                    SessionType.COFFEE -> R.string.emoji_coffee
                    SessionType.DINNER -> R.string.emoji_food
                    SessionType.LUNCH -> R.string.emoji_food
                    SessionType.REGISTRATION -> R.string.emoji_registration
                    SessionType.TALK -> R.string.emoji_talk
                    SessionType.WORKSHOP -> R.string.emoji_workshop
                    null -> throw Exception("Session is null")

                }
            )
        }

        private fun bindSwipeActions(
            session: Session,
            mOnClickDetails: ClickListener,
            mOnClickFavourite: ClickListener,
            mOnClickUnfavourite: ClickListener
        ) {
            // Add swipe functionality, set bg colour and icon, add toast for disabled type

            with(itemView as SwipeLayout) {
                // Initialise
                showMode = SwipeLayout.ShowMode.LayDown
                addDrag(SwipeLayout.DragEdge.Left, favourite)

                // Detail listener - always add this
                surfaceView.setOnClickListener{
                    mOnClickDetails.onClick(session)
                }

                if (currentBottomView == null) throw Exception("No bottom view to set up")

                if (session.sessionType == SessionType.TALK || session.sessionType == SessionType.WORKSHOP) {
                    if (session.isFavourite == true) { // Unfavourite, == true for null checking
                        currentBottomView!!.favourite.setBackgroundColor(context.getColor(R.color.warning))
                        currentBottomView!!.favouriteIcon.setImageDrawable(context.getDrawable(R.drawable.icon_unfavourite))
                        currentBottomView!!.setOnClickListener {
                            mOnClickUnfavourite.onClick(session)
                            itemView.close()
                        }
                    } else { // favourite
                        currentBottomView!!.favourite.setBackgroundColor(context.getColor(R.color.favourite))
                        currentBottomView!!.favouriteIcon.setImageDrawable(context.getDrawable(R.drawable.icon_favourite))
                        currentBottomView!!.setOnClickListener {
                            mOnClickFavourite.onClick(session)
                            itemView.close()
                        }
                    }
                } else { // Disabled icon and add toast
                    currentBottomView!!.favourite.setBackgroundColor(context.getColor(R.color.disabled))
                    currentBottomView!!.favouriteIcon.setImageDrawable(context.getDrawable(R.drawable.icon_cross))
                    val sessionString =
                        session.sessionType.toString().toLowerCase(Locale.getDefault())
                    currentBottomView!!.setOnClickListener {
                        Toast.makeText(
                            context,
                            "You can't favourite a $sessionString session!",
                            Toast.LENGTH_LONG
                        ).show()
                        itemView.close()
                    }
                }
            }


        }

        private fun setIndicatorColour(session: Session) {
            val context = itemView.context

            when {
                sessionIsNow(session) -> { // set "Current session" colour
                    binding.indicator.setBackgroundColor(context.getColor(R.color.session_current))
                    binding.txtDay.setTextColor(context.getColor(R.color.session_current_secondary_text))
                    binding.txtStartTime.setTextColor(context.getColor(R.color.session_current_primary_text))
                }
                session.isFavourite == true -> { // set "Favourite session" colour, == true for null check
                    binding.indicator.setBackgroundColor(context.getColor(R.color.session_favourite))
                    binding.txtDay.setTextColor(context.getColor(R.color.session_favourite_secondary_text))
                    binding.txtStartTime.setTextColor(context.getColor(R.color.session_favourite_primary_text))
                }
                else -> { // set the standard colour
                    binding.indicator.setBackgroundColor(context.getColor(R.color.session_standard))
                    binding.txtDay.setTextColor(context.getColor(R.color.session_standard_secondary_text))
                    binding.txtStartTime.setTextColor(context.getColor(R.color.session_standard_primary_text))
                }
            }
        }
    }

    private fun sessionIsNow(session: Session): Boolean {
        return LocalDate.now().isEqual(session.sessionDate) &&
                session.timeStart!! <= LocalTime.now() &&
                session.timeEnd!! >= LocalTime.now()
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SessionViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding: FragmentSessionListItemBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_session_list_item,
            parent,
            false
        )

        return SessionViewHolder(binding.root, binding)
    }

    override fun getItemCount() = mSessionsData.size

    override fun onBindViewHolder(
        holder: SessionViewHolder,
        position: Int
    ) {
        val session = mSessionsData[position]
        holder.bind(session, mOnClickDetails, mOnClickFavourite, mOnClickUnfavourite)
        mItemManger.bindView(
            holder.itemView,
            position
        ) // Bind to the swipe view, track which are open/closed (+ more stuff)
    }

    internal fun setData(sessions: List<Session>) {
        Log.d(TAG, "Session data updated")
//        this.allSessionsData = sessions.filterIsInstance(Session::class.java)
        this.allSessionsData = sessions

        this.mSessionsData.clear()
        this.mSessionsData.addAll(sessions)

        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                val sessionTypes = SessionType.values().map { it.name }

                results.values = when {
                    constraint == null -> allSessionsData
                    constraint.isEmpty() -> allSessionsData
                    constraint in sessionTypes -> allSessionsData.filter { it.sessionType!!.name == constraint }
                    else -> allSessionsData.filter {
                        it.title!!.contains(
                            constraint.trim(),
                            ignoreCase = true
                        )
                    }
                }

                return results
            }

            @Suppress("UNCHECKED_CAST") // The cast is fine...
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                mSessionsData.clear()
                mSessionsData.addAll(results!!.values as MutableList<Session>)
                notifyDataSetChanged()
            }

        }
    }
}

