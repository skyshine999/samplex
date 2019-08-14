package com.pro.simpsons.home

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pro.simpsons.OnclickItem
import com.pro.simpsons.R
import com.pro.simpsons.databinding.HomeFragmentLayoutBinding
import com.pro.simpsons.details.DetailsFragment
import com.pro.simpsons.home.models.RelatedTopic


class HomeFragment : Fragment(), OnclickItem {

    var binding: HomeFragmentLayoutBinding? = null
    var adapter: CustomListAdapter? = null
    var listItems: List<RelatedTopic>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment_layout, container, false)
        setHasOptionsMenu(true)
        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        binding!!.lifecycleOwner = this
        initRecylerView(binding!!)

        viewModel.getCharacterLiveData()!!.observe(this, Observer {
            listItems = it.RelatedTopics
            adapter!!.setData(listItems!!)
        })

        viewModel.callWebService()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity!!.menuInflater.inflate(R.menu.menu, menu)
        val searchView = MenuItemCompat.getActionView(menu.findItem(R.id.action_search)) as SearchView
        val searchManager = activity!!.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity!!.componentName))
        doSearch(searchView)
    }


    private fun doSearch(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                performSearch(newText, listItems!!)
                return false
            }
        })
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar!!.setTitle("Home")
        if (!activity!!.resources.getBoolean(R.bool.isTablet)) {
            (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
            (activity as AppCompatActivity).supportActionBar!!.setDisplayShowHomeEnabled(true)
        }
    }

    private fun performSearch(
        query: CharSequence?,
        relatedTopics: List<RelatedTopic>
    ) {

        if (relatedTopics.size == 0) {
            return
        }
        var newList: MutableList<RelatedTopic> = mutableListOf()
        var searchList: MutableList<RelatedTopic> = mutableListOf()

        for (topic in relatedTopics) {
            newList.add(topic)
        }


        for (topic in newList) {
            if (topic.Text.toLowerCase().contains(query.toString().toLowerCase()) || topic.Result.toLowerCase().contains(
                    query.toString().toLowerCase()
                )
            ) {
                var relatedTopic = RelatedTopic(topic.FirstURL, topic.Icon, topic.Result, topic.Text)
                searchList.add(relatedTopic)
            }
        }
        adapter!!.setData(searchList)
    }

    fun initRecylerView(binding: HomeFragmentLayoutBinding) {
        listItems = arrayListOf()
        val linearLayoutManager = LinearLayoutManager(activity!!, RecyclerView.VERTICAL, false)
        binding.recycleView.layoutManager = linearLayoutManager
        adapter = CustomListAdapter(listOf(), this)
        binding.recycleView.adapter = adapter
        val dividerItemDecoration = DividerItemDecoration(
            binding.recycleView.getContext(),
            linearLayoutManager.getOrientation()
        )
        binding.recycleView.addItemDecoration(dividerItemDecoration)
    }

    override fun openNextScreen(pos: Int) {

        if (activity!!.resources.getBoolean(R.bool.isTablet)) {
            var fragment: DetailsFragment = DetailsFragment()
            val bundle = Bundle()
            bundle.putParcelable("obj", listItems!!.get(pos))
            fragment!!.arguments = bundle
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.second_container, fragment).commit()

        } else {
            var detailsFragment = DetailsFragment()
            val bundle = Bundle()
            bundle.putParcelable("obj", listItems!!.get(pos))
            detailsFragment.arguments = bundle
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.container, detailsFragment)
                .addToBackStack(null).commit()
        }


    }


}