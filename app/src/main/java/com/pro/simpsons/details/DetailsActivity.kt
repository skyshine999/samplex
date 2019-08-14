package com.pro.simpsons.details


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.pro.simpsons.R
import com.pro.simpsons.databinding.DetailsActivityBinding
import com.pro.simpsons.home.models.RelatedTopic


class DetailsActivity : AppCompatActivity() {

    var binding: DetailsActivityBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.details_activity)
        var relatedTopic: RelatedTopic = intent.extras.getParcelable("obj")
        binding!!.title.text = relatedTopic.Text
        binding!!.desc.text = relatedTopic.Result
        val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
        requestOptions.placeholder(R.mipmap.start)
        Glide.with(this).load(relatedTopic.Icon.URL).apply(requestOptions).into(binding!!.imageView)


    }


}