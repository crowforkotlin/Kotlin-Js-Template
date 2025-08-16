package com.mordecai.zipline

import app.cash.zipline.Zipline

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

private val zipline by lazy { Zipline.get() }

@OptIn(ExperimentalJsExport::class)
@JsExport
fun main() {
  val worldClockHost = zipline.take<WorldClockHost>("WorldClockHost")
  zipline.bind<WorldClockPresenter>(
    name = "WorldClockPresenter",
    instance = RealWorldClockPresenter(worldClockHost),
  )
}

class RealWorldClockPresenter(
  private val host: WorldClockHost,
) : WorldClockPresenter {
  override fun models(
    events: Flow<WorldClockEvent>,
  ): Flow<WorldClockModel> {
    return flow {
      while (true) {
        emit(
          WorldClockModel(
            label = TimeFormatter().formatLocalTime(),
          ),
        )
        delay(16)
      }
    }
  }
}


/*
private val zipline by lazy { Zipline.get() }

@OptIn(ExperimentalJsExport::class)
@JsExport
fun main() {
  // JS 侧只绑定一个服务，返回静态/动态 Account 信息
  zipline.bind<AccountService>(
    name = "AccountService",
    instance = AccountServiceImpl(),
  )
}

class AccountServiceImpl : AccountService {
  override fun account(): Account {
    // 这里可以接入你的业务逻辑来动态生成
    return Account(id = "u_001", name = "Alice")
  }
}
*/
