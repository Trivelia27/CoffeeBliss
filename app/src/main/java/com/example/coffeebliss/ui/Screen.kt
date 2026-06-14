package com.example.coffeebliss.ui

sealed class Screen(val route: String) {
    object Splash      : Screen("splash")
    object Login       : Screen("login")
    object Register    : Screen("register")
    object MemberDetail: Screen("member_detail/{memberId}") {
        fun createRoute(memberId: Int) = "member_detail/$memberId"
    }
    object MemberCard  : Screen("member_card/{memberId}") {
        fun createRoute(memberId: Int) = "member_card/$memberId"
    }
    object Transaction : Screen("transaction/{memberId}") {
        fun createRoute(memberId: Int) = "transaction/$memberId"
    }
    object Rewards     : Screen("rewards/{memberId}") {
        fun createRoute(memberId: Int) = "rewards/$memberId"
    }
    object History     : Screen("history/{memberId}") {
        fun createRoute(memberId: Int) = "history/$memberId"
    }
    object Profile     : Screen("profile/{memberId}") {
        fun createRoute(memberId: Int) = "profile/$memberId"
    }
    // kept for backward compat with HomeScreen.kt file
    object Home        : Screen("home")
    object AddMember   : Screen("add_member")
}
