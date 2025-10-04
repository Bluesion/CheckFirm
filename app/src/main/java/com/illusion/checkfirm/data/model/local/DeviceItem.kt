package com.illusion.checkfirm.data.model.local

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 디바이스 정보를 담는 클래스입니다.
 * @param model 모델명 (ex. SM-A720S)
 * @param csc CSC (ex. SKC)
 */
@Parcelize
data class DeviceItem(val model: String, val csc: String) : Parcelable