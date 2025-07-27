package com.illusion.checkfirm.data.model

/**
 * 디바이스 정보를 담는 클래스입니다.
 * @param model 모델명 (ex. SM-A720S)
 * @param csc CSC (ex. SKC)
 */
data class DeviceItem(val model: String, val csc: String)