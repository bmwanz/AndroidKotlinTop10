package com.maximumscrubbage.bw.androidkotlintop10

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import timber.log.Timber


class FeedAdapter(context: Context, private val resource: Int, private val applications: List<FeedEntry>)
    : ArrayAdapter<FeedEntry>(context, resource) {

    private val inflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        Timber.d("FeedAdapter - getCount() = %d", applications.size)
        return applications.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        Timber.d("FeedAdapter - getView()")

        // reuse view and giving it back to adapter, only create new view if adapter wasn't given one to reuse
        val view: View
        val viewHolder: ViewHolder

        // if convertView is null, inflate new View and create new viewHolder and storing it in view's tag
        // if given existing view, retrieving viewholder from its tag
        // tag is object casted as ViewHolder, we know it's ViewHolder because we're only ones who put it there
        if (convertView == null) {
            Timber.d("FeedAdapter - getView called with null convertView")
            view = inflater.inflate(resource, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val currentApp = applications[position]
        viewHolder.tvName.text = currentApp.name
        viewHolder.tvArtist.text = currentApp.artist
        viewHolder.tvSummary.text = currentApp.summary

        // naive implementation
        // inflates a new view each time, findViewById is slow
//        val view = inflater.inflate(resource, parent, false)
//        val tvName: TextView = view.findViewById(R.id.tvName)
//        val tvArtist: TextView = view.findViewById(R.id.tvArtist)
//        val tvSummary: TextView = view.findViewById(R.id.tvSummary)
//        val currentApp = applications[position]
//        tvName.text = currentApp.name
//        tvArtist.text = currentApp.artist
//        tvSummary.text = currentApp.summary

        return view
    }
}

class ViewHolder(v: View) {
    // hold textviews so we don't have to call findViewById every time
    val tvName: TextView = v.findViewById(R.id.tvName)
    val tvArtist: TextView = v.findViewById(R.id.tvArtist)
    val tvSummary: TextView = v.findViewById(R.id.tvSummary)
}