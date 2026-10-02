package com.illusion.checkfirm.core.domain.model

data class Bookmark(
    val name: String,
    val device: Device,
    val category: String,
    val id: Long? = null,
    val position: Int = 0,
)
