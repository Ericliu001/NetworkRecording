package com.example.networkrecording.network.data

import kotlinx.serialization.Serializable

@Serializable
data class Repository(val name: String, val description: String?)
