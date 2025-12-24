package com.illusion.checkfirm

import android.app.Application
import com.google.firebase.messaging.FirebaseMessaging
import com.illusion.checkfirm.data.repository.RepositoryProvider
import com.illusion.checkfirm.data.source.local.DatabaseProvider

class CheckFirm : Application() {

    val repositoryProvider: RepositoryProvider by lazy {
        RepositoryProvider(this)
    }

    override fun onCreate() {
        super.onCreate()
        DatabaseProvider.initialize(this)
        repositoryProvider.initialize()

        FirebaseMessaging.getInstance().subscribeToTopic("update")
    }
}
