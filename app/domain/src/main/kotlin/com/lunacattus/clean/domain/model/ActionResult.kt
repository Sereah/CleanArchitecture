package com.lunacattus.clean.domain.model

sealed class ActionResult<out T> {
    data class Success<out T>(val data: T) : ActionResult<T>()
    data class Error(val code: Int, val description: String) : ActionResult<Nothing>()
}