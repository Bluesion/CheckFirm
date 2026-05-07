package com.illusion.checkfirm.feature.bookmark.api

data object BookmarkRouteNavKey

data object BookmarkDetailNavKey

/**
 * Full-screen category editor (port of legacy CategoryEditActivity). Pass null
 * [categoryName] to create a new category, or an existing category name to rename
 * + reassign device membership.
 */
data class CategoryEditRouteNavKey(val categoryName: String? = null)
