package com.acpdq.credit.app.system.exceptions

data class BusinessException(override val message: String?): RuntimeException(message)