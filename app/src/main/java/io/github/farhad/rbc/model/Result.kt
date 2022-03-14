package io.github.farhad.rbc.model

sealed class Result<T> {
    class InputError<T> : Result<T>()
    class Failure<T> : Result<T>()
    data class Success<T>(val data: List<T>) : Result<T>()
}