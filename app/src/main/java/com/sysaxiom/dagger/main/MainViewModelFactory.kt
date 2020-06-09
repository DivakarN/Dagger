package com.sysaxiom.dagger.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sysaxiom.kodein.util.PingRepository
import javax.inject.Inject
import javax.inject.Provider

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory @Inject constructor(private val mainViewModelProvider: Provider<MainViewModel>) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return mainViewModelProvider.get() as T
    }
}