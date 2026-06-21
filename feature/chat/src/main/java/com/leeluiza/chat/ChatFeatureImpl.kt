package com.leeluiza.chat

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.leeluiza.chat.presentation.ChatScreen
import com.leeluiza.common.FeatureApi
import com.leeluiza.common.Routes
import javax.inject.Inject

class ChatFeatureImpl @Inject constructor(): FeatureApi {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        modifier: Modifier
    ) {
        navGraphBuilder.composable(Routes.CHAT) {
            ChatScreen(
                onBack = { navController.popBackStack() },
                modifier = modifier
            )
        }
    }
}