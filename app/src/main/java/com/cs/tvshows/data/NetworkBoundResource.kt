package com.cs.tvshows.data

import com.cs.tvshows.utils.Outcome
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flowOn

inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType?) -> Boolean,
    crossinline onFetchFailed: (Throwable) -> Unit = { },
) = flow {

    val data = query().firstOrNull()
    emit(Outcome.Loading(data))

    val flow = when {
        shouldFetch(data) -> {
            emit(Outcome.Loading(data))
            try {
                saveFetchResult(fetch())
                query().map { Outcome.Success(it) }
            } catch (throwable: Throwable) {
                onFetchFailed(throwable)
                query().map { Outcome.Error(throwable, it) }
            }
        }
        else -> {
            query().map { Outcome.Success(it) }
        }
    }
    emitAll(flow)

}.flowOn(Dispatchers.IO)
