package com.example.dexter.interactors.base

import com.example.dexter.errors.Failure
import com.example.dexter.types.Either
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * Abstract execution unit for different use cases.
 *
 * @param <T> request type
 * @param <I> parameter
 */
abstract class UseCase<T, I>(private val foreground: CoroutineContext): CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = foreground + job

    /**
     * Method to execute the use case
     */
    fun execute(parameter: I? = null, block: (Either<Failure, T>) -> Unit) {
        launch {
            block(run(parameter))
        }
    }

    /**
     * Cancel execution
     */
    fun cancel() {
        job.cancel()
    }

    /**
     * Abstract method to implement the use case
     */
    abstract suspend fun run(parameter: I?): Either<Failure, T>

}
