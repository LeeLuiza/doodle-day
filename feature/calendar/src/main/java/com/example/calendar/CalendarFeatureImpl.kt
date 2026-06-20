package com.example.calendar

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.calendar.presentation.CalendarScreen
import com.example.common.FeatureApi
import com.example.common.Routes
import javax.inject.Inject

class CalendarFeatureImpl @Inject constructor(): FeatureApi {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        modifier: Modifier
    ) {
        navGraphBuilder.composable(Routes.CALENDAR) {
            CalendarScreen(
                onDayClick = { dayId ->
                    navController.navigate(Routes.createDayRoute(dayId))
                }
            )
        }
    }
}