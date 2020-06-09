package com.sysaxiom.dagger.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.sysaxiom.dagger.R
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var factory : ViewModelProvider.Factory

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DaggerPingComponent.builder()
            .networkModule(NetworkModule(this))
            .build()
            .inject(this)

        viewModel = ViewModelProviders.of(this, factory).get(MainViewModel::class.java)

        viewModel.getPing()
    }
}
