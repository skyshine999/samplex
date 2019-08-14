package com.pro.simpsons.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.pro.simpsons.databinding.DetailFragmentLayoutBinding
import com.pro.simpsons.home.models.RelatedTopic
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask

import android.widget.ImageView
import com.pro.simpsons.R
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL





class DetailsFragment : Fragment() {

    var binding: DetailFragmentLayoutBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.detail_fragment_layout, container, false)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
            var relatedTopic: RelatedTopic = it.getParcelable("obj")
            binding!!.title.text = relatedTopic.Text
            binding!!.desc.text = relatedTopic.Result
            var arraylist = relatedTopic.Text.split("-")
            (activity as AppCompatActivity).supportActionBar!!.setTitle(arraylist.get(0))
            if (!relatedTopic.Icon.URL.isEmpty()) {
                DownloadImageTask(binding!!.image).execute(relatedTopic.Icon.URL);
//                GlideApp.with(activity!!)
//                    .load(relatedTopic.Icon.URL)
//                    .placeholder(R.mipmap.start)
//                    .fitCenter()
//                    .into(binding!!.imageView);
            }
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (!activity!!.resources.getBoolean(R.bool.isTablet)) {
            (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            (activity as AppCompatActivity).supportActionBar!!.setDisplayShowHomeEnabled(true)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                activity!!.supportFragmentManager.popBackStack()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
    private inner class DownloadImageTask(internal var bmImage: ImageView) : AsyncTask<String, Void, Bitmap>() {

        override fun doInBackground(vararg urls: String): Bitmap? {
            val urldisplay = urls[0]
//            var bmp: Bitmap? = null
//            try {
//              bmp = getBitmapFromURL(urldisplay)
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
            val bitmap = getBitmapFromURL(urldisplay)

            return bitmap
        }

        override fun onPostExecute(result: Bitmap) {
            bmImage.setImageBitmap(result)
        }
    }

    fun getBitmapFromURL(src: String): Bitmap? {
        try {
            val url = URL(src)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            return BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            // Log exception
            return null
        }

    }
}