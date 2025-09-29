package com.eddy.jedymovieapptest.data.repository.util

import com.eddy.jedymovieapptest.domain.resource.Resource
import com.eddy.jedymovieapptest.domain.resource.RootError
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.Response

fun<RESPONSE, DOMAIN> safeApiCall(
    apiCall: suspend () -> Response<RESPONSE>,
    dtoMapper: (RESPONSE) -> DOMAIN,
) = flow {
    emit(Resource.Loading)
    val response = apiCall()
    if (response.isSuccessful) {
        response.body()?.let { data ->
            val result = dtoMapper(data)
            emit(Resource.Success(result))
        }
    } else {
        emit(Resource.Error(RootError.NetworkError()))
    }
}.catch { throwable ->
    emit(Resource.Error(RootError.Error(throwable)))
}

