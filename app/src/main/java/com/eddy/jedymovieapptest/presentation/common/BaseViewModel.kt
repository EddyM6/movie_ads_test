package com.eddy.jedymovieapptest.presentation.common

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

abstract class BaseViewModel<INTENT, STATE, EFFECT>(
    protected val initialState: STATE
) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<STATE> = _state.asStateFlow()
    val currentState: STATE
        get() = state.value

    private val _effect = Channel<EFFECT>(BUFFERED)
    val effect = _effect.receiveAsFlow()

    open fun handleIntent(intent: INTENT) = Unit

    protected fun updateState(updater: (STATE) -> STATE) = _state.update { updater.invoke(it) }

    protected suspend fun sendEffect(effect: EFFECT) = _effect.send(effect)
}
