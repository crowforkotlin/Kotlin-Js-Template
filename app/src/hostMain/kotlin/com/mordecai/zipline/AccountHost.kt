package com.mordecai.zipline

import app.cash.zipline.Zipline
import app.cash.zipline.loader.DefaultFreshnessCheckerNotFresh
import app.cash.zipline.loader.LoadResult
import app.cash.zipline.loader.ZiplineLoader
import kotlinx.coroutines.flow.flowOf

/**
 * 一次性加载 manifest，取得 Account 并返回；不做轮询。
 */
suspend fun loadAccountOnce(
    ziplineLoader: ZiplineLoader,
    manifestUrl: String,
): Account {
    val loadResultFlow = ziplineLoader.load(
        applicationName = "account-app",
        freshnessChecker = DefaultFreshnessCheckerNotFresh, // 开发期方便热更；上线请替换为签名校验
        manifestUrlFlow = flowOf(manifestUrl),              // 只发一次 URL，不轮询
        initializer = { _: Zipline ->
            // 本例不需要向 JS 绑定 Host；留空
        },
    )

    var resultAccount: Account? = null
    loadResultFlow.collect { result ->
        if (result is LoadResult.Success) {
            val zipline = result.zipline
            val service = zipline.take<AccountService>("AccountService")
            try {
                resultAccount = service.account()
            } finally {
                service.close()
                // 也可以 zipline.close()，但要确保没有其他服务在用；此处交给上层控制
            }
        } else if (result is LoadResult.Failure) {
            throw result.exception
        }
    }

    return resultAccount ?: error("Failed to load Account from Zipline manifest: $manifestUrl")
}