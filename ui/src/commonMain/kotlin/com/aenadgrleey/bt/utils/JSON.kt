package com.aenadgrleey.bt.utils

import kotlinx.serialization.json.Json

val json by lazy { Json { ignoreUnknownKeys = true } }