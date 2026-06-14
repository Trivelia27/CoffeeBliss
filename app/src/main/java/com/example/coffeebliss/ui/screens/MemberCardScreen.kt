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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.coffeebliss.data.Member
import com.example.coffeebliss.ui.CoffeeViewModel
import com.example.coffeebliss.ui.theme.*
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberCardScreen(navController: NavController, viewModel: CoffeeViewModel, memberId: Int) {
    val member       by viewModel.getMember(memberId).collectAsState(initial = null)
    val transactions by viewModel.getTransactions(memberId).collectAsState(initial = emptyList())

    Scaffold(
        containerColor = MochaCream,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.LocalCafe,
                            contentDescription = null,
                            tint     = GoldBorder,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "COFFEE BLISS",
                            fontWeight = FontWeight.Black,
                            color      = Color.White,
                            letterSpacing = 2.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = GoldBorder)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = EspressoDark)
            )
        },
        bottomBar = {
            MemberBottomNav(
                navController = navController,
                memberId      = memberId,
                currentTab    = MemberTab.CARD
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text       = "Kartu Member Digital",
                style      = MaterialTheme.typography.titleMedium,
                color      = MochaBrown,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(20.dp))

            member?.let { m ->
                val rewardsAvailable = listOf(50, 100, 150).count { m.points >= it }
                val totalVisits = transactions.size

                DigitalMemberCard(m)

                Spacer(modifier = Modifier.height(20.dp))

                // Stats row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CardStatBox(
                        label    = "Reward Tersedia",
                        value    = "$rewardsAvailable Item",
                        icon     = Icons.Default.CardGiftcard,
                        modifier = Modifier.weight(1f)
                    )
                    CardStatBox(
                        label    = "Total Kunjungan",
                        value    = "$totalVisits",
                        icon     = Icons.Default.LocalCafe,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Promotional text card
                Card(
                    modifier  = Modifier.fillMaxWidth(),
                    shape     = RoundedCornerShape(16.dp),
                    colors    = CardDefaults.cardColors(
                        containerColor = EspressoDark.copy(alpha = 0.05f)
                    ),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            tint     = EspressoDark,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text       = "Temukan Espresso Roast musiman terbaru dari dataran tinggi Sumatra. Tunjukkan kartu ini kepada kasir untuk mengumpulkan poin.",
                            style      = MaterialTheme.typography.bodySmall,
                            color      = EspressoDark,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CardStatBox(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier  = modifier,
        shape     = RoundedCornerShape(14.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint     = EspressoDark,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text       = value,
                fontWeight = FontWeight.Black,
                fontSize   = 18.sp,
                color      = EspressoDark,
                textAlign  = TextAlign.Center
            )
            Text(
                text      = label,
                fontSize  = 11.sp,
                color     = MochaBrown,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun DigitalMemberCard(member: Member) {
    val level      = getMemberLevel(member.points)
    val levelColor = getLevelColor(member.points)
    val year       = Calendar.getInstance().get(Calendar.YEAR)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation     = 20.dp,
                shape         = RoundedCornerShape(24.dp),
                ambientColor  = EspressoDark.copy(alpha = 0.4f)
            )
            .clip(RoundedCornerShape(24.dp))
            .background(Brush.linearGradient(listOf(EspressoDark, EspressoMedium)))
            .border(1.5.dp, GoldBorder.copy(alpha = 0.7f), RoundedCornerShape(24.dp))
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ── Card Header ──────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text          = "COFFEE BLISS",
                        color         = Color.White,
                        fontWeight    = FontWeight.Black,
                        fontSize      = 18.sp,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text          = "MEMBERSHIP CARD",
                        color         = GoldBorder.copy(alpha = 0.8f),
                        fontSize      = 9.sp,
                        letterSpacing = 2.sp
                    )
                }
                Icon(
                    Icons.Default.LocalCafe,
                    contentDescription = null,
                    tint     = GoldBorder,
                    modifier = Modifier.size(32.dp)
                )
            }

            // Gold divider
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color.Transparent, GoldBorder.copy(0.6f), Color.Transparent)
                        )
                    )
            )

            // ── Card Body ────────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text          = member.name.uppercase(Locale.getDefault()),
                    color         = Color.White,
                    fontWeight    = FontWeight.Black,
                    fontSize      = 20.sp,
                    letterSpacing = 1.sp,
                    textAlign     = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text       = "ID: ${String.format(Locale.getDefault(), "%04d %04d %04d", member.id, 1002, 4491)}",
                    color      = MochaText,
                    fontSize   = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Level badge
                Surface(
                    shape    = RoundedCornerShape(20.dp),
                    color    = GoldBorder.copy(alpha = 0.15f),
                    modifier = Modifier.border(1.dp, GoldBorder, RoundedCornerShape(20.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Verified,
                            contentDescription = null,
                            tint     = GoldBorder,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text          = "$level MEMBER",
                            color         = GoldFixed,
                            fontWeight    = FontWeight.Bold,
                            fontSize      = 11.sp,
                            letterSpacing = 1.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // QR placeholder
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White.copy(alpha = 0.92f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector        = Icons.Default.QrCode2,
                            contentDescription = "QR Code",
                            modifier           = Modifier.size(100.dp),
                            tint               = EspressoDark
                        )
                        Text(
                            text      = "Scan to earn rewards",
                            fontSize  = 9.sp,
                            color     = MochaBrown,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Points row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    PointStatItem("TOTAL POIN", "${member.points}", GoldBorder)
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(40.dp)
                            .background(GoldBorder.copy(alpha = 0.25f))
                    )
                    PointStatItem("STATUS", level, levelColor)
                }
            }

            // ── Card Footer ──────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.2f))
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Verified,
                        contentDescription = null,
                        tint     = GoldBorder.copy(0.7f),
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text          = "Active Member Since $year • coffeebliss.id",
                        fontSize      = 10.sp,
                        color         = MochaText,
                        letterSpacing = 0.3.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun PointStatItem(label: String, value: String, valueColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontWeight = FontWeight.Black, fontSize = 22.sp, color = valueColor)
        Text(text = label, fontSize = 9.sp, color = MochaText, letterSpacing = 0.8.sp)
    }
}
