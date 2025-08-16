package com.mordecai.zipline

import app.cash.zipline.ZiplineService
import kotlinx.serialization.Serializable

@Serializable
data class Account(
  val id: String,
  val name: String,
)

interface AccountService : ZiplineService {
  fun account(): Account
}