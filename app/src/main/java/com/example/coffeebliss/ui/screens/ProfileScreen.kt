package com.example.coffeebliss.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.coffeebliss.ui.CoffeeViewModel
import com.example.coffeebliss.ui.Screen
import com.example.coffeebliss.ui.theme.*
import java.util.Calendar
import kotlin.math.absoluteValue


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, viewModel: CoffeeViewModel, memberId: Int) {
    val member       by viewModel.getMember(memberId).collectAsState(initial = null)
    val transactions by viewModel.getTransactions(memberId).collectAsState(initial = emptyList())

    val avatarPalette = listOf(
        Color(0xFF4E342E), Color(0xFF3E2723), Color(0xFF5D4037),
        Color(0xFF4A148C), Color(0xFFBF360C), Color(0xFF004D40),
    )

    Scaffold(
        containerColor = MochaCream,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "My Profile",
                        fontWeight = FontWeight.Bold,
                        color      = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = GoldBorder)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = EspressoDark)
            )
        },
        bottomBar = {
            MemberBottomNav(
                navController = navController,
                memberId      = memberId,
                currentTab    = MemberTab.PROFILE
            )
        }
    ) { padding ->
        member?.let { m ->
            val level      = getMemberLevel(m.points)
            val levelColor = getLevelColor(m.points)
            val initial    = m.name.firstOrNull()?.uppercase() ?: "?"
            val avatarBg   = avatarPalette[m.name.hashCode().absoluteValue % avatarPalette.size]
            val year       = Calendar.getInstance().get(Calendar.YEAR)

            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // ── Hero header ──────────────────────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Brush.linearGradient(listOf(EspressoDark, EspressoMedium)))
                        .padding(vertical = 36.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(96.dp)
                                .clip(CircleShape)
                                .background(avatarBg)
                                .border(2.dp, GoldBorder, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text       = initial,
                                color      = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize   = 40.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text       = m.name,
                            color      = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize   = 22.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text     = "ID: BLISS-${String.format("%05d", m.id)}-C",
                            color    = MochaText,
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Level badge
                        Surface(
                            shape    = RoundedCornerShape(20.dp),
                            color    = GoldBorder.copy(alpha = 0.15f),
                            modifier = Modifier.border(1.dp, GoldBorder, RoundedCornerShape(20.dp))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.Verified,
                                    contentDescription = null,
                                    tint     = GoldBorder,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text          = "$level MEMBER",
                                    color         = GoldFixed,
                                    fontWeight    = FontWeight.Bold,
                                    fontSize      = 12.sp,
                                    letterSpacing = 1.sp
                                )
                            }
                        }
                    }
                }

                // Gold accent line
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(
                            Brush.horizontalGradient(listOf(GoldBorder, GoldFixed, GoldBorder))
                        )
                )

                Spacer(modifier = Modifier.height(20.dp))

                // ── Stats row ────────────────────────────────────────────
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ProfileStatCard(
                        label    = "Total Poin",
                        value    = String.format("%,d", m.points),
                        color    = EspressoDark,
                        modifier = Modifier.weight(1f)
                    )
                    ProfileStatCard(
                        label    = "Transaksi",
                        value    = "${transactions.size}",
                        color    = GoldCoffee,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // ── Level Status card ─────────────────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Brush.linearGradient(listOf(levelColor.copy(0.12f), levelColor.copy(0.06f))))
                        .border(1.dp, levelColor.copy(0.3f), RoundedCornerShape(16.dp))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Level icon
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(levelColor.copy(0.15f))
                                .border(1.dp, levelColor.copy(0.4f), RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                when (level) {
                                    "PLATINUM" -> Icons.Default.WorkspacePremium
                                    "GOLD"     -> Icons.Default.Star
                                    else       -> Icons.Default.Verified
                                },
                                contentDescription = null,
                                tint     = levelColor,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text       = "$level MEMBER",
                                color      = if (level == "GOLD") GoldDark else levelColor,
                                fontWeight = FontWeight.Black,
                                fontSize   = 16.sp,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text     = when (level) {
                                    "PLATINUM" -> "Level tertinggi • Nikmati semua privilege"
                                    "GOLD"     -> "${300 - m.points} poin lagi menuju Platinum"
                                    else       -> "${100 - m.points} poin lagi menuju Gold"
                                },
                                color    = MochaBrown,
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )
                        }

                        // Right badge
                        Surface(
                            shape    = RoundedCornerShape(20.dp),
                            color    = levelColor.copy(0.12f),
                            modifier = Modifier.border(1.dp, levelColor.copy(0.35f), RoundedCornerShape(20.dp))
                        ) {
                            Text(
                                text       = level,
                                color      = if (level == "GOLD") GoldDark else levelColor,
                                fontWeight = FontWeight.Black,
                                fontSize   = 11.sp,
                                letterSpacing = 0.5.sp,
                                modifier   = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ── Info card ────────────────────────────────────────────
                Card(
                    modifier  = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    shape     = RoundedCornerShape(18.dp),
                    colors    = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(modifier = Modifier.padding(4.dp)) {
                        ProfileInfoRow(
                            icon  = Icons.Default.Person,
                            label = "Nama Lengkap",
                            value = m.name
                        )
                        HorizontalDivider(
                            modifier  = Modifier.padding(horizontal = 16.dp),
                            thickness = 0.5.dp,
                            color     = MochaDivider
                        )
                        ProfileInfoRow(
                            icon  = Icons.Default.Email,
                            label = "Email",
                            value = m.email
                        )
                        HorizontalDivider(
                            modifier  = Modifier.padding(horizontal = 16.dp),
                            thickness = 0.5.dp,
                            color     = MochaDivider
                        )
                        ProfileInfoRow(
                            icon  = Icons.Default.Phone,
                            label = "Nomor HP",
                            value = m.phone
                        )
                        HorizontalDivider(
                            modifier  = Modifier.padding(horizontal = 16.dp),
                            thickness = 0.5.dp,
                            color     = MochaDivider
                        )
                        ProfileInfoRow(
                            icon  = Icons.Default.CreditCard,
                            label = "ID Member",
                            value = "BLISS-${String.format("%05d", m.id)}-C"
                        )
                        HorizontalDivider(
                            modifier  = Modifier.padding(horizontal = 16.dp),
                            thickness = 0.5.dp,
                            color     = MochaDivider
                        )
                        ProfileInfoRow(
                            icon       = Icons.Default.Star,
                            label      = "Level Member",
                            value      = level,
                            valueColor = if (level == "GOLD") GoldDark else levelColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ── Level progress ───────────────────────────────────────
                val (nextLevel, ptsNeeded) = when {
                    m.points >= 300 -> "PLATINUM" to 0
                    m.points >= 100 -> "PLATINUM" to (300 - m.points)
                    else            -> "GOLD"     to (100 - m.points)
                }

                if (ptsNeeded > 0) {
                    Card(
                        modifier  = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        shape     = RoundedCornerShape(14.dp),
                        colors    = CardDefaults.cardColors(
                            containerColor = GoldBorder.copy(alpha = 0.08f)
                        ),
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.AutoMirrored.Filled.TrendingUp,
                                    contentDescription = null,
                                    tint     = GoldBorder,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text       = "Menuju Level $nextLevel",
                                    fontWeight = FontWeight.Bold,
                                    color      = GoldDark,
                                    fontSize   = 13.sp
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text     = "$ptsNeeded pts lagi",
                                    color    = GoldDark,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            val progress = if (m.points >= 100) {
                                (m.points - 100f) / (300f - 100f)
                            } else {
                                m.points / 100f
                            }
                            LinearProgressIndicator(
                                progress  = { progress.coerceIn(0f, 1f) },
                                modifier  = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color      = GoldBorder,
                                trackColor = GoldBorder.copy(alpha = 0.2f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ── Digital card shortcut ────────────────────────────────
                Card(
                    modifier  = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    shape     = RoundedCornerShape(14.dp),
                    colors    = CardDefaults.cardColors(containerColor = EspressoDark),
                    elevation = CardDefaults.cardElevation(0.dp),
                    onClick   = { navController.navigate(Screen.MemberCard.createRoute(memberId)) }
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.CreditCard,
                            contentDescription = null,
                            tint     = GoldFixed,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text       = "Lihat Kartu Digital",
                                color      = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize   = 14.sp
                            )
                            Text(
                                text     = "Tunjukkan QR code ke kasir",
                                color    = MochaText,
                                fontSize = 12.sp
                            )
                        }
                        Icon(
                            Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint     = GoldBorder,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text     = "Member sejak $year • coffeebliss.id",
                    color    = MochaBrown,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // ── Logout ───────────────────────────────────────────────
                var showLogout by remember { mutableStateOf(false) }
                if (showLogout) {
                    AlertDialog(
                        onDismissRequest = { showLogout = false },
                        containerColor   = Color.White,
                        shape            = RoundedCornerShape(20.dp),
                        icon = {
                            Box(
                                modifier = Modifier.size(52.dp).clip(CircleShape).background(EspressoDark),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.AutoMirrored.Filled.Logout, null, tint = GoldBorder, modifier = Modifier.size(26.dp))
                            }
                        },
                        title = { Text("Keluar?", fontWeight = FontWeight.Black, color = EspressoDark) },
                        text  = { Text("Anda akan keluar dari akun membership ini.", color = MochaBrown) },
                        confirmButton = {
                            Button(
                                onClick = {
                                    showLogout = false
                                    viewModel.logout()
                                    navController.navigate(Screen.Login.route) { popUpTo(0) { inclusive = true } }
                                },
                                shape  = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = EspressoDark)
                            ) { Text("Ya, Keluar", color = GoldFixed, fontWeight = FontWeight.Bold) }
                        },
                        dismissButton = {
                            TextButton(onClick = { showLogout = false }) { Text("Batal", color = MochaBrown) }
                        }
                    )
                }

                OutlinedButton(
                    onClick  = { showLogout = true },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                    shape    = RoundedCornerShape(14.dp),
                    border   = androidx.compose.foundation.BorderStroke(1.dp, EspressoDark.copy(0.3f)),
                    colors   = ButtonDefaults.outlinedButtonColors(contentColor = EspressoDark)
                ) {
                    Icon(Icons.AutoMirrored.Filled.Logout, null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Keluar dari Akun", fontWeight = FontWeight.SemiBold)
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        } ?: Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = GoldBorder)
        }
    }
}

@Composable
private fun ProfileStatCard(label: String, value: String, color: Color, modifier: Modifier) {
    Card(
        modifier  = modifier,
        shape     = RoundedCornerShape(14.dp),
        colors    = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.08f)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = value, fontWeight = FontWeight.Black, fontSize = 20.sp, color = color)
            Text(text = label, fontSize = 11.sp, color = MochaBrown)
        }
    }
}

@Composable
private fun ProfileInfoRow(
    icon: ImageVector,
    label: String,
    value: String,
    valueColor: Color = MochaDark
) {
    Row(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(EspressoDark.copy(alpha = 0.07f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint     = EspressoDark.copy(0.8f),
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, fontSize = 11.sp, color = MochaBrown)
            Text(text = value, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = valueColor)
        }
    }
}
