@file:Suppress("SpellCheckingInspection")

package com.mordecai.zipline

class PrintService : Print {
    override fun print(account: Account): Account {
        print(account)
        return account.copy(name = "wuya", age = 0)
    }
}