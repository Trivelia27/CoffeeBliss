package com.example.coffeebliss.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.coffeebliss.ui.Screen
import com.example.coffeebliss.ui.theme.*

enum class MemberTab { HOME, CARD, TRANSACTIONS, REWARDS, PROFILE }

private data class NavItemData(
    val tab: MemberTab,
    val icon: ImageVector,
    val label: String
)

private val navItems = listOf(
    NavItemData(MemberTab.HOME,         Icons.Default.Home,         "Home"),
    NavItemData(MemberTab.CARD,         Icons.Default.CreditCard,   "Kartu"),
    NavItemData(MemberTab.TRANSACTIONS, Icons.Default.Receipt,      "Riwayat"),
    NavItemData(MemberTab.REWARDS,      Icons.Default.CardGiftcard, "Reward"),
    NavItemData(MemberTab.PROFILE,      Icons.Default.Person,       "Profil"),
)

@Composable
fun MemberBottomNav(
    navController: NavController,
    memberId: Int,
    currentTab: MemberTab
) {
    Surface(
        modifier  = Modifier
            .fillMaxWidth()
            .shadow(
                elevation    = 24.dp,
                spotColor    = EspressoDark.copy(0.15f),
                ambientColor = EspressoDark.copy(0.08f)
            ),
        color         = Color.White,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 8.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            navItems.forEach { item ->
                BottomNavItem(
                    data      = item,
                    isActive  = currentTab == item.tab,
                    onClick   = {
                        if (currentTab != item.tab) {
                            when (item.tab) {
                                MemberTab.HOME -> navController.navigate(Screen.MemberDetail.createRoute(memberId)) {
                                    popUpTo(Screen.MemberDetail.createRoute(memberId)) { inclusive = true }
                                    launchSingleTop = true
                                }
                                MemberTab.CARD         -> navController.navigate(Screen.MemberCard.createRoute(memberId)) { launchSingleTop = true }
                                MemberTab.TRANSACTIONS -> navController.navigate(Screen.History.createRoute(memberId)) { launchSingleTop = true }
                                MemberTab.REWARDS      -> navController.navigate(Screen.Rewards.createRoute(memberId)) { launchSingleTop = true }
                                MemberTab.PROFILE      -> navController.navigate(Screen.Profile.createRoute(memberId)) { launchSingleTop = true }
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun BottomNavItem(
    data: NavItemData,
    isActive: Boolean,
    onClick: () -> Unit
) {
    val bgColor by animateColorAsState(
        targetValue    = if (isActive) EspressoDark else Color.Transparent,
        animationSpec  = spring(stiffness = Spring.StiffnessMedium),
        label          = "navBg"
    )
    val iconTint by animateColorAsState(
        targetValue   = if (isActive) GoldFixed else MochaBrown,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label         = "navTint"
    )
    val pillWidth by animateDpAsState(
        targetValue    = if (isActive) 72.dp else 44.dp,
        animationSpec  = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
        label          = "pillWidth"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication        = null,
                onClick           = onClick
            )
            .padding(horizontal = 4.dp, vertical = 2.dp)
    ) {
        // Animated pill background
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .width(pillWidth)
                .height(36.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(bgColor)
                .padding(horizontal = 8.dp)
        ) {
            Icon(
                imageVector        = data.icon,
                contentDescription = data.label,
                tint               = iconTint,
                modifier           = Modifier.size(if (isActive) 22.dp else 20.dp)
            )
        }

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text       = data.label,
            fontSize   = 10.sp,
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
            color      = if (isActive) EspressoDark else MochaBrown
        )
    }
}
