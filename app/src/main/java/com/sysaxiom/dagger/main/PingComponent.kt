package com.sysaxiom.dagger.main

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.sysaxiom.dagger.util.NetworkConnectionInterceptor
import com.sysaxiom.dagger.util.RetrofitHandler
import com.sysaxiom.kodein.util.NetworkApis
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Inject

@Component(modules = [PingModule::class,NetworkModule::class])
interface PingComponent {

    fun inject(mainActivity: MainActivity)

}


@Module
abstract class PingModule {

    @Binds
    abstract fun bindViewModelFactory(factory: MainViewModelFactory) :ViewModelProvider.Factory

}

@Module
class NetworkModule(context: Context) {

    var context: Context = context

    @Provides
    fun provideContext(): Context {
        return context
    }

    @Provides
    @Inject
    fun bindRetrofitHandler(networkConnectionInterceptor: NetworkConnectionInterceptor) : NetworkApis {
        return RetrofitHandler(networkConnectionInterceptor)
    }

}