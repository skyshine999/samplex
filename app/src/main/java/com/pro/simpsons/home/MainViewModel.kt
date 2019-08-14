package com.pro.simpsons.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pro.simpsons.AppConstants
import com.pro.simpsons.home.models.Character
import com.pro.simpsons.networkrepo.ApiClient
import com.pro.simpsons.networkrepo.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private var characterLiveData: MutableLiveData<Character>? = null

    fun getCharacterLiveData(): MutableLiveData<Character>? {
        if (characterLiveData == null) {
            characterLiveData = MutableLiveData<Character>()
        }
        return characterLiveData
    }


    fun callWebService() {
        var call: Call<Character>? = ApiClient.client?.create(ApiInterface::class.java)
            ?.getCharacter(AppConstants.SIMPSON_CHARACTER, AppConstants.JSON_FORMAT)
        call?.let {
            it.enqueue(object : Callback<Character> {
                override fun onFailure(call: Call<Character>?, t: Throwable?) {
                    print(t!!.localizedMessage)
                }

                override fun onResponse(call: Call<Character>?, response: Response<Character>?) {
                    response?.let {
                        getCharacterLiveData()!!.postValue(it.body())
                    }
                }

            })
        }


    }
}