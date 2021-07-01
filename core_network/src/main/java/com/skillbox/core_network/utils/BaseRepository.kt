package com.skillbox.core_network.utils

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class BaseRepository(val errorHandler: ErrorHandler) {

    protected suspend inline fun <T> execute(
        crossinline onLocal: (Boolean) -> Unit,
        crossinline onState: (Failure) -> Unit,
        noinline func: suspend () -> T,
        noinline funcLocal: suspend () -> T,
        noinline funcOther: suspend (result: T) -> T
    ) : T? {
        return try {
            val result = withContext(Dispatchers.IO) { func.invoke() }
            withContext(Dispatchers.IO) { result ?.let { funcOther.invoke(result) } }
            withContext(Dispatchers.Main) {
                onLocal.invoke(false)
                result
            }
        } catch (e: Exception) {
            Log.e("BaseRepository", e.localizedMessage)
            withContext(Dispatchers.Main) {
                val failure = errorHandler.proceedException(e)
                if(failure == Failure.UnknownError){
                    onLocal.invoke(true)
                    funcLocal.invoke()
                } else {
                    onState.invoke(failure)
                    onLocal.invoke(false)
                    null
                }
            }
        }
    }
}