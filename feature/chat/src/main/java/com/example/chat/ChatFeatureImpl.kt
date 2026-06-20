package com.example.chat

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.chat.presentation.ChatScreen
import com.example.common.FeatureApi
import com.example.common.Routes
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