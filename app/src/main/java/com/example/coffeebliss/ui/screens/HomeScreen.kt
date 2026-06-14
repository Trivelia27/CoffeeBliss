package com.example.coffeebliss.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.coffeebliss.data.Member
import com.example.coffeebliss.ui.CoffeeViewModel
import com.example.coffeebliss.ui.Screen
import com.example.coffeebliss.ui.theme.*
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: CoffeeViewModel) {
    val members       by viewModel.allMembers.collectAsState(initial = emptyList())
    var showNotifs    by remember { mutableStateOf(false) }
    var showLogoutDlg by remember { mutableStateOf(false) }

    // Notifications dialog
    if (showNotifs) {
        NotificationsDialog(members = members, onDismiss = { showNotifs = false })
    }

    // Exit (back to splash) confirm dialog
    if (showLogoutDlg) {
        AlertDialog(
            onDismissRequest = { showLogoutDlg = false },
            containerColor   = Color.White,
            shape            = RoundedCornerShape(20.dp),
            icon             = {
                Box(
                    modifier = Modifier.size(56.dp).clip(CircleShape).background(EspressoDark),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.AutoMirrored.Filled.Logout, null, tint = GoldBorder, modifier = Modifier.size(28.dp))
                }
            },
            title = { Text("Keluar Aplikasi?", fontWeight = FontWeight.Black, color = EspressoDark) },
            text  = { Text("Kembali ke halaman awal Coffee Bliss?", color = MochaBrown) },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDlg = false
                        navController.navigate(Screen.Splash.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    shape  = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EspressoDark)
                ) { Text("Ya, Keluar", color = GoldFixed, fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDlg = false }) {
                    Text("Batal", color = MochaBrown)
                }
            }
        )
    }

    Scaffold(
        containerColor = MochaCream,
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocalCafe, null, tint = GoldBorder, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("Coffee Bliss", fontWeight = FontWeight.Black, color = Color.White, fontSize = 17.sp, letterSpacing = 0.5.sp)
                            Text("Loyalty Membership", color = MochaText, fontSize = 10.sp)
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { showNotifs = true }) {
                        BadgedBox(badge = {
                            if (members.isNotEmpty()) Badge(containerColor = GoldBorder) { Text("${members.size.coerceAtMost(9)}", color = EspressoDark, fontSize = 9.sp) }
                        }) {
                            Icon(Icons.Default.Notifications, "Notifikasi", tint = Color.White.copy(0.8f))
                        }
                    }
                    IconButton(onClick = { showLogoutDlg = true }) {
                        Icon(Icons.AutoMirrored.Filled.Logout, "Logout", tint = Color.White.copy(0.7f))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = EspressoDark)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick        = { navController.navigate(Screen.AddMember.route) },
                containerColor = GoldBorder,
                contentColor   = EspressoDark,
                shape          = CircleShape,
                modifier       = Modifier.shadow(10.dp, CircleShape)
            ) {
                Icon(Icons.Default.Add, "Tambah Member")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier       = Modifier.padding(padding).fillMaxSize(),
            contentPadding = PaddingValues(bottom = 88.dp)
        ) {
            // ── Hero Banner ──────────────────────────────────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(listOf(Color(0xFF1A0E0A), EspressoDark, EspressoMedium))
                        )
                        .padding(horizontal = 20.dp, vertical = 24.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Total Member Aktif", color = MochaText, fontSize = 12.sp, letterSpacing = 0.3.sp)
                            Text(
                                "${members.size}",
                                color      = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize   = 52.sp,
                                lineHeight = 56.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                val platinum = members.count { it.points >= 300 }
                                val gold     = members.count { it.points in 100..299 }
                                val silver   = members.count { it.points < 100 }
                                LevelPill("$platinum Platinum", PlatinumColor)
                                LevelPill("$gold Gold",         GoldColor)
                                LevelPill("$silver Silver",     SilverColor)
                            }
                        }
                        // Icon box
                        Box(
                            modifier = Modifier
                                .size(76.dp)
                                .clip(RoundedCornerShape(22.dp))
                                .background(Color.White.copy(0.06f))
                                .border(1.dp, GoldBorder.copy(0.25f), RoundedCornerShape(22.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.People, null, tint = GoldBorder.copy(0.85f), modifier = Modifier.size(38.dp))
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(
                            Brush.horizontalGradient(listOf(Color.Transparent, GoldBorder, GoldFixed, GoldBorder, Color.Transparent))
                        )
                )
            }

            // ── Section Header ────────────────────────────────────────────
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 20.dp, bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(GoldBorder.copy(0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.WorkspacePremium, null, tint = GoldBorder, modifier = Modifier.size(16.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Daftar Member", fontWeight = FontWeight.Bold, color = EspressoDark, fontSize = 15.sp)
                    Spacer(modifier = Modifier.weight(1f))
                    Surface(shape = RoundedCornerShape(20.dp), color = EspressoDark.copy(0.06f)) {
                        Text(
                            "${members.size} orang",
                            color    = EspressoDark,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            if (members.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier.size(80.dp).clip(CircleShape).background(MochaContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.LocalCafe, null, tint = MochaBrown.copy(0.4f), modifier = Modifier.size(36.dp))
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Belum ada member", color = EspressoDark, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyLarge)
                            Text("Tekan + untuk mendaftarkan member baru", color = MochaBrown, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 4.dp))
                        }
                    }
                }
            } else {
                items(members) { member ->
                    MemberListItem(member) { navController.navigate(Screen.MemberDetail.createRoute(member.id)) }
                }
            }
        }
    }
}

@Composable
fun NotificationsDialog(members: List<Member>, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape     = RoundedCornerShape(24.dp),
            colors    = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(16.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(EspressoDark),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Notifications, null, tint = GoldBorder, modifier = Modifier.size(18.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Notifikasi", fontWeight = FontWeight.Black, fontSize = 18.sp, color = EspressoDark, modifier = Modifier.weight(1f))
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, null, tint = MochaBrown, modifier = Modifier.size(18.dp))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = MochaDivider, thickness = 0.5.dp)
                Spacer(modifier = Modifier.height(12.dp))

                if (members.isEmpty()) {
                    Box(modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp), contentAlignment = Alignment.Center) {
                        Text("Belum ada notifikasi", color = MochaBrown, fontSize = 13.sp)
                    }
                } else {
                    // Recent 3 member joins
                    val recent = members.takeLast(3).reversed()
                    recent.forEach { m ->
                        NotifItem(
                            icon    = Icons.Default.PersonAdd,
                            title   = "Member Baru",
                            message = "${m.name} bergabung sebagai member baru"
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                    // Close-to-upgrade hints
                    members.filter { it.points in 90..99 }.take(2).forEach { m ->
                        NotifItem(
                            icon    = Icons.Default.Star,
                            title   = "Hampir Gold!",
                            message = "${m.name} butuh ${100 - m.points} poin lagi untuk level Gold"
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                    members.filter { it.points in 280..299 }.take(2).forEach { m ->
                        NotifItem(
                            icon    = Icons.Default.WorkspacePremium,
                            title   = "Hampir Platinum!",
                            message = "${m.name} butuh ${300 - m.points} poin lagi untuk Platinum"
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Button(
                    onClick  = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape    = RoundedCornerShape(12.dp),
                    colors   = ButtonDefaults.buttonColors(containerColor = EspressoDark)
                ) {
                    Text("Tutup", color = GoldFixed, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun NotifItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, message: String) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(GoldBorder.copy(0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = GoldCoffee, modifier = Modifier.size(18.dp))
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = EspressoDark)
            Text(message, fontSize = 12.sp, color = MochaBrown, lineHeight = 16.sp)
        }
    }
}

@Composable
private fun LevelPill(text: String, color: Color) {
    Surface(
        shape    = RoundedCornerShape(20.dp),
        color    = color.copy(0.15f),
        modifier = Modifier.border(1.dp, color.copy(0.3f), RoundedCornerShape(20.dp))
    ) {
        Text(text, color = color, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
    }
}

@Composable
fun MemberListItem(member: Member, onClick: () -> Unit) {
    val avatarPalette = listOf(
        Color(0xFF4E342E), Color(0xFF3E2723), Color(0xFF5D4037),
        Color(0xFF4A148C), Color(0xFFBF360C), Color(0xFF004D40),
    )
    val avatarColor = avatarPalette[member.name.hashCode().absoluteValue % avatarPalette.size]
    val initial     = member.name.firstOrNull()?.uppercase() ?: "?"
    val level       = getMemberLevel(member.points)
    val levelColor  = getLevelColor(member.points)

    Card(
        modifier  = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 5.dp)
            .clickable { onClick() },
        shape     = RoundedCornerShape(18.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            // Left accent bar
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(50.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(levelColor)
            )
            Spacer(modifier = Modifier.width(12.dp))

            // Avatar
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(avatarColor)
                    .border(1.5.dp, levelColor.copy(0.5f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(initial, color = Color.White, fontWeight = FontWeight.Black, fontSize = 22.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(member.name, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = MochaDark)
                Text(member.email, color = MochaBrown, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Surface(
                        shape    = RoundedCornerShape(20.dp),
                        color    = levelColor.copy(0.10f),
                        modifier = Modifier.border(1.dp, levelColor.copy(0.3f), RoundedCornerShape(20.dp))
                    ) {
                        Text(
                            level,
                            color      = if (level == "GOLD") GoldDark else levelColor,
                            fontSize   = 9.sp,
                            fontWeight = FontWeight.Bold,
                            modifier   = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                    Surface(shape = RoundedCornerShape(20.dp), color = GoldBorder.copy(0.08f)) {
                        Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, null, tint = GoldBorder, modifier = Modifier.size(10.dp))
                            Spacer(modifier = Modifier.width(3.dp))
                            Text("${member.points} pts", color = GoldDark, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }
                }
            }

            Icon(Icons.Default.ChevronRight, null, tint = MochaBrown.copy(0.35f), modifier = Modifier.size(20.dp))
        }
    }
}
