package com.leeluiza.calendar

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.leeluiza.calendar.presentation.CalendarScreen
import com.leeluiza.common.FeatureApi
import com.leeluiza.common.Routes
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