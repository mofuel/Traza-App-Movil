package com.bancocloud.traza.app

import android.app.Application

class TrazaApplication : Application() {
    val container: AppContainer by lazy { AppContainer(this) }
}
