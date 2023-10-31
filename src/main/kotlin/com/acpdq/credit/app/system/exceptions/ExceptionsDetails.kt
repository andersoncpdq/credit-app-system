package com.acpdq.credit.app.system.exceptions

import java.time.LocalDateTime

data class ExceptionsDetails(
    val title: String,
    val timestamp: LocalDateTime,
    val status: Int,
    val exception: String,
    val messages: MutableMap<String, String?>
)
