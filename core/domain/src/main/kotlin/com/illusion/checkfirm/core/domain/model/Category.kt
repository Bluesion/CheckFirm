package com.illusion.checkfirm.core.domain.model

data class Category(
    val name: String,
    val id: Long? = null,
    val position: Int = 0,
)
