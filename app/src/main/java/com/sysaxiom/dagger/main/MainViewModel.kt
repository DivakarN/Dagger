package com.sysaxiom.dagger.main

import androidx.lifecycle.ViewModel
import com.sysaxiom.kodein.util.PingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class MainViewModel @Inject constructor(private val repository: PingRepository) : ViewModel(), CoroutineScope {

    val job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = job

    fun getPing() {
        launch(Dispatchers.IO) {
            val result = repository.ping()
            println(result.body())
        }
    }

}