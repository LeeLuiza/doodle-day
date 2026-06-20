package com.example.planner.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.common.FeatureApi
import com.example.common.Routes
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlin.jvm.java

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val features = EntryPointAccessors.fromApplication(
        context.applicationContext,
        FeatureApiEntryPoint::class.java
    ).features()

    NavHost(
        navController = navController,
        startDestination = Routes.CALENDAR,
        modifier = modifier
    ) {
        features.forEach { feature ->
            feature.registerGraph(
                navGraphBuilder = this,
                navController = navController,
                modifier = modifier
            )
        }
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface FeatureApiEntryPoint {
    fun features(): Set<@JvmSuppressWildcards FeatureApi>
}