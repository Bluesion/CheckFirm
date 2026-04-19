package com.illusion.checkfirm.domain.model

/**
 * 디바이스 정보를 담는 클래스입니다.
 *
 * @property model 모델명 (ex. SM-A720S)
 * @property csc CSC (ex. SKC)
 */
data class Device(val model: String, val csc: String) {
    override fun toString(): String {
        return "$model$csc"
    }
}
