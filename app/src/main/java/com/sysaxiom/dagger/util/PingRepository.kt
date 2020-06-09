package com.sysaxiom.kodein.util

import com.sysaxiom.dagger.util.RetrofitHandler
import retrofit2.Response
import javax.inject.Inject

class PingRepository @Inject constructor(val networkApis: NetworkApis) {

    suspend fun ping(): Response<PingResponse> {
        return networkApis.ping()
    }
}