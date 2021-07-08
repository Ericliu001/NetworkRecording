package com.example.networkrecording

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.networkrecording.network.data.Repository

class ItemViewModel : ViewModel() {
    private val mutableRepos = MutableLiveData<List<Repository>>()
    val repos: LiveData<List<Repository>> get() = mutableRepos

    fun populateRepos(repos: List<Repository>) {
        mutableRepos.value = repos
    }
}