package com.leeluiza.day

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.leeluiza.common.FeatureApi
import com.leeluiza.day.presentation.navigation.dayScreen
import javax.inject.Inject

class DayFeatureImpl @Inject constructor(): FeatureApi {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        modifier: Modifier
    ) {
        navGraphBuilder.dayScreen(navController)
    }
}