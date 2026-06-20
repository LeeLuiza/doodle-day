package com.example.common

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.compose.ui.Modifier

interface FeatureApi {
    fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        modifier: Modifier = Modifier
    )
}