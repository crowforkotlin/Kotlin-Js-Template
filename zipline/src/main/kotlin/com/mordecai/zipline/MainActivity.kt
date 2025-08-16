package com.mordecai.zipline

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import app.cash.zipline.Zipline
import app.cash.zipline.loader.DefaultFreshnessCheckerNotFresh
import app.cash.zipline.loader.LoadResult
import app.cash.zipline.loader.ManifestVerifier.Companion.NO_SIGNATURE_CHECKS
import app.cash.zipline.loader.ZiplineLoader
import com.mordecai.zipline.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding

    private val scope = MainScope()
    private lateinit var worldClockAndroid: WorldClockAndroid
    private var accountZipline: AccountZiplineAndroid? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        accountZipline = AccountZiplineAndroid(applicationContext, scope)

        /*CoroutineScope(Dispatchers.IO).launch {
            try {
                // 注意：真机请把地址换为你局域网服务器 IP；模拟器访问本机用 10.0.2.2
                val manifestUrl = "http://192.168.137.1:8080/manifest.zipline.json"
                val account = accountZipline!!.fetchAccount(manifestUrl)
                Log.d("zipline", "Account loaded: $account")
                // TODO: 更新 UI 显示 account
            } catch (t: Throwable) {
                Log.e("zipline", "Failed to load Account", t)
            }
        }*/


        CoroutineScope(Dispatchers.IO).launch {
            worldClockAndroid = WorldClockAndroid(applicationContext, scope)
            worldClockAndroid.start()
            launch {
                repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    worldClockAndroid.events.collect {
                        "event is $it".info()
                    }
                }
            }
            launch {
                repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    worldClockAndroid.models.collect {
                        "model is $it".info()
                    }
                }
            }
        }
        init()
    }

    private fun init() = lifecycleScope.launch {


    }
    override fun onDestroy() {
        worldClockAndroid.close()
        scope.cancel()
        super.onDestroy()
    }
}

fun Any?.info() {
    Log.d("zipline", this.toString())
}