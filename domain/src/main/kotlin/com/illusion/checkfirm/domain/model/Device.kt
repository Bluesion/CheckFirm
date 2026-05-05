package com.illusion.checkfirm.domain.model

data class Device(
    val model: String,
    val csc: String,
) {
    fun asDevice(): String {
        return "$model$csc"
    }

    fun isValidDevice(): Boolean {
        return isValidModel() && isValidCsc()
    }

    fun isValidModel(): Boolean {
        // Hyphen이 하나만 있는지 확인
        if (model.count { it == '-' } != 1) {
            return false
        }

        // SM-A720S에서 SM이 prefix, A720S는 code
        val prefix = model.split("-")[0]
        val code = model.split("-")[1]

        // SM, SC, SCG, SCV 등 2-3글자 prefix만 지원
        if (prefix.length !in 2..3) {
            return false
        }

        // prefix가 영어 대문자만으로 구성되어 있는지 확인
        prefix.forEach {
            if (it.code !in 65..90) {
                return false
            }
        }

        // prefix가 S로 시작하는지 확인 (GT로 시작하는 구형 모델 미지원)
        if (prefix[0] != 'S') {
            return false
        }

        // 가장 짧은 건 code가 3자리 (SC-03L 등 라우터)
        // SM-A025VZKAVZW 같은 특수 케이스 존재
        // 위 보다 긴 건 못 봤는데, 혹시 몰라서 12자까지 제한
        if (code.length !in 3..12) {
            return false
        }

        // 코드가 영어 대문자나 숫자가 아닌 경우 걸러내기
        for (ch in code) {
            if (ch.code !in 65..90 && ch.code !in 48..57) {
                return false
            }
        }

        return true
    }

    fun isValidCsc(): Boolean {
        if (csc.length != 3) {
            return false
        }

        // 코드가 영어 대문자나 숫자가 아닌 경우 걸러내기
        for (ch in csc) {
            if (ch.code !in 65..90 && ch.code !in 48..57) {
                return false
            }
        }

        return true
    }
}
