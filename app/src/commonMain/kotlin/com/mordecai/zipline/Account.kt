package com.mordecai.zipline

import app.cash.zipline.ZiplineService
import kotlinx.serialization.Serializable


@Serializable
data class Account(
    val name: String,
    val age: Int
)

interface Print : ZiplineService {
    fun print(account: Account): Account
}