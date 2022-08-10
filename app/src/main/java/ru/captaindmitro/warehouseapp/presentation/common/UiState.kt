package ru.captaindmitro.warehouseapp.presentation.common

sealed class UiState<out R> {
    object Loading : UiState<Nothing>()
    object Empty : UiState<Nothing>()
    data class Success<out T>(val data: T) : UiState<T>()
    data class Error(val exception: Exception) : UiState<Nothing>()
}