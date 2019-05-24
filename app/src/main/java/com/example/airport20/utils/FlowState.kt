package com.example.airport20.utils

data class FlowState<T>(
    val data: T? = null,
    val throwable: Throwable? = null,
    val status: FlowStatus = FlowStatus.NEUTRAL
){
    companion object {
        inline fun <reified T> success() = FlowState<T>(status = FlowStatus.SUCCESS)
        inline fun <reified T> loading() = FlowState<T>(status = FlowStatus.LOADING)
    }
}