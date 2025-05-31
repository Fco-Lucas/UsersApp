package com.lcsz.first

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp // Fundamental: Habilita a geração de código do Hilt para o app
class MyApplication : Application() {

}