package com.mordecai.zipline

import android.content.Context
import app.cash.zipline.loader.ManifestVerifier.Companion.NO_SIGNATURE_CHECKS
import app.cash.zipline.loader.ZiplineLoader
import java.util.concurrent.Executors
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import okhttp3.OkHttpClient

/**
 * Android 侧的小封装：创建 ZiplineLoader，并一次性拉取 Account。
 */
class AccountZiplineAndroid(
  private val applicationContext: Context,
  private val scope: CoroutineScope,
) {
  private val ziplineExecutorService = Executors.newSingleThreadExecutor { r -> Thread(r, "Zipline") }
  private val ziplineDispatcher: CoroutineDispatcher = ziplineExecutorService.asCoroutineDispatcher()
  private val okHttpClient = OkHttpClient()

  private val ziplineLoader = ZiplineLoader(
    dispatcher = ziplineDispatcher,
    manifestVerifier = NO_SIGNATURE_CHECKS, // 开发阶段方便；生产请使用签名校验
    httpClient = okHttpClient,
  )

  suspend fun fetchAccount(manifestUrl: String): Account {
    return loadAccountOnce(
      ziplineLoader = ziplineLoader,
      manifestUrl = manifestUrl,
    )
  }

  fun close() {
    ziplineExecutorService.shutdown()
  }
}