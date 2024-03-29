package com.pathik.ride

import android.app.Application
import android.content.Context
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class PathikApp : Application() {

    companion object {
        lateinit var instance: PathikApp
        fun context() = instance.applicationContext
    }

    override fun getApplicationContext(): Context {
        instance = this
        return super.getApplicationContext()
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(if (BuildConfig.DEBUG) Timber.DebugTree() else CrashReportingTree())
    }

    private class CrashReportingTree : Timber.Tree() {

        override fun isLoggable(tag: String?, priority: Int): Boolean {
            return priority >= Log.ERROR
        }

        override fun log(priority: Int, tag: String?, message: String, throwable: Throwable?) {

        }
    }
}