package com.illusion.checkfirm.database.catcher

import androidx.lifecycle.LiveData

class InfoCatcherRepository(private val catcherDao: InfoCatcherDao) {

    val allDevices: LiveData<List<InfoCatcherEntity>> = catcherDao.getAll()

    fun insert(entity: InfoCatcherEntity) {
        catcherDao.insert(entity)
    }

    fun delete(device: String) {
        catcherDao.delete(device)
    }
}