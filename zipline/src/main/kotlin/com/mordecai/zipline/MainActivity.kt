package com.mordecai.zipline

import android.os.Bundle
import android.printservice.PrintService
import android.util.Log.e
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import app.cash.zipline.Zipline
import app.cash.zipline.loader.DefaultFreshnessCheckerNotFresh
import app.cash.zipline.loader.LoadResult
import app.cash.zipline.loader.ManifestVerifier.Companion.NO_SIGNATURE_CHECKS
import app.cash.zipline.loader.ZiplineLoader
import com.mordecai.zipline.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        CoroutineScope(Dispatchers.IO).launch {
            val executorService = Executors.newFixedThreadPool(1) { runnable -> Thread(runnable, "zipline") }
            val dispatcher = executorService.asCoroutineDispatcher()
            launchZipline(dispatcher)
        }
        init()
    }

    private fun init() = lifecycleScope.launch {


    }

    suspend fun launchZipline(dispatcher: CoroutineDispatcher): Zipline {
        val manifestUrl = "http://192.168.137.1:8080/manifest.zipline.json"
        val loader = ZiplineLoader(
            dispatcher = dispatcher,
            manifestVerifier = NO_SIGNATURE_CHECKS,
            httpClient = OkHttpClient(),
        )
        return when (val result = loader.loadOnce(
            applicationName = "AccountPrint",
            freshnessChecker = DefaultFreshnessCheckerNotFresh,
            manifestUrl = manifestUrl,
            initializer = { zipline ->
                zipline.bind("Print", PrintService())
            }
        )) {
            is LoadResult.Success -> result.zipline
            is LoadResult.Failure -> error(result.exception)
        }
    }
}