package com.severett.archunitdemo.hexagonal.port

import kotlinx.coroutines.flow.Flow

interface ListenerPort {
    fun listen(): Flow<ULong>
}