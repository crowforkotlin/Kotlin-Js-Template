@file:OptIn(ExperimentalJsExport::class)

package com.mordecai.zipline

import app.cash.zipline.Zipline

val zipline by lazy { Zipline.get() }

@JsExport
fun launchZipline() {
    val print = zipline.take<Print>("Print")

}


