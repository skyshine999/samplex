package com.pro.simpsons.home


import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pro.simpsons.OnclickItem
import com.pro.simpsons.R
import com.pro.simpsons.databinding.ActivityMainBinding
import com.pro.simpsons.details.DetailsActivity
import com.pro.simpsons.home.models.RelatedTopic


class MainActivity : AppCompatActivity(), OnclickItem {

    var adapter: CustomListAdapter? = null
    var listItems: List<RelatedTopic>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        binding.lifecycleOwner = this
        initRecylerView(binding)

        viewModel.getCharacterLiveData()!!.observe(this, Observer {
            listItems = it.RelatedTopics
            adapter!!.setData(listItems!!)
        })

        viewModel.callWebService()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val searchView = MenuItemCompat.getActionView(menu.findItem(R.id.action_search)) as SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        doSearch(searchView)
        return true
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

    fun initRecylerView(binding: ActivityMainBinding) {
        listItems = arrayListOf()
        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.recycleView.layoutManager = linearLayoutManager
        adapter = CustomListAdapter(listOf(), this@MainActivity)
        binding.recycleView.adapter = adapter
        val dividerItemDecoration = DividerItemDecoration(
            binding.recycleView.getContext(),
            linearLayoutManager.getOrientation()
        )
        binding.recycleView.addItemDecoration(dividerItemDecoration)
    }

    override fun openNextScreen(pos: Int) {
        var intent = Intent(this@MainActivity, DetailsActivity::class.java)
        intent.putExtra("obj", listItems!!.get(pos))
        startActivity(intent)
    }


}
