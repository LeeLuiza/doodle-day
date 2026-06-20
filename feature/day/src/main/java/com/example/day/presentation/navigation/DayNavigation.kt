package com.example.day.presentation.navigation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.common.Routes
import com.example.day.presentation.DayBoardScreen

fun NavGraphBuilder.dayScreen(navController: NavHostController) {
    composable(
        route = Routes.DAY,
        arguments = listOf(
            navArgument("date") { type = NavType.StringType }
        ),
        enterTransition = {
            fadeIn(animationSpec = tween(250)) +
                    scaleIn(
                        initialScale = 0.98f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioNoBouncy,
                            stiffness = Spring.StiffnessMediumLow
                        )
                    ) +
                    slideInVertically(
                        initialOffsetY = { 40 },
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioNoBouncy,
                            stiffness = Spring.StiffnessMediumLow
                        )
                    )
        },
        exitTransition = {
            fadeOut(animationSpec = tween(250)) +
                    scaleOut(
                        targetScale = 0.98f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioNoBouncy,
                            stiffness = Spring.StiffnessMediumLow
                        )
                    ) +
                    slideOutVertically(
                        targetOffsetY = { 40 },
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioNoBouncy,
                            stiffness = Spring.StiffnessMediumLow
                        )
                    )
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(250)) +
                    scaleIn(
                        initialScale = 0.98f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioNoBouncy,
                            stiffness = Spring.StiffnessMediumLow
                        )
                    )
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(250)) +
                    scaleOut(
                        targetScale = 0.98f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioNoBouncy,
                            stiffness = Spring.StiffnessMediumLow
                        )
                    ) +
                    slideOutVertically(
                        targetOffsetY = { 40 },
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioNoBouncy,
                            stiffness = Spring.StiffnessMediumLow
                        )
                    )
        }
    ) {
        DayBoardScreen(
            onBack = { navController.popBackStack() }
        )
    }
}