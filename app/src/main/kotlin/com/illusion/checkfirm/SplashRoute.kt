package com.illusion.checkfirm

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SplashRoute(
    navigateToRoute: (Any) -> Unit,
    viewModel: SplashViewModel = hiltViewModel(),
) {
    val nextRoute by viewModel.nextRoute.collectAsStateWithLifecycle()

    LaunchedEffect(nextRoute) {
        nextRoute?.let { navigateToRoute(it) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
    )
}
