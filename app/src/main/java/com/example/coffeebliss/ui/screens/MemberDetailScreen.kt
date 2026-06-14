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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.coffeebliss.data.Member
import com.example.coffeebliss.data.Transaction
import com.example.coffeebliss.ui.CoffeeViewModel
import com.example.coffeebliss.ui.Screen
import com.example.coffeebliss.ui.theme.*
import kotlin.math.absoluteValue

// ─── Shared helpers ───────────────────────────────────────────────────────
val espressoGradient: Brush get() = Brush.linearGradient(listOf(EspressoDark, EspressoMedium))

fun getMemberLevel(points: Int) = when {
    points >= 300 -> "PLATINUM"
    points >= 100 -> "GOLD"
    else          -> "SILVER"
}

fun getLevelColor(points: Int) = when {
    points >= 300 -> PlatinumColor
    points >= 100 -> GoldColor
    else          -> SilverColor
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberDetailScreen(navController: NavController, viewModel: CoffeeViewModel, memberId: Int) {
    val member       by viewModel.getMember(memberId).collectAsState(initial = null)
    val transactions by viewModel.getTransactions(memberId).collectAsState(initial = emptyList())
    var showLogout   by remember { mutableStateOf(false) }
    var showNotif    by remember { mutableStateOf(false) }
    val notifSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (showNotif) {
        ModalBottomSheet(
            onDismissRequest = { showNotif = false },
            sheetState       = notifSheetState,
            containerColor   = Color.White,
            shape            = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
        ) {
            member?.let { m ->
                NotificationSheet(m = m, transactions = transactions, onClose = { showNotif = false })
            }
        }
    }

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

    Scaffold(
        containerColor = MochaCream,
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocalCafe, null, tint = GoldBorder, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Coffee Bliss", fontWeight = FontWeight.Black, color = Color.White, fontSize = 17.sp)
                    }
                },
                actions = {
                    IconButton(onClick = { showNotif = true }) {
                        BadgedBox(badge = {
                            Badge(containerColor = GoldBorder) {
                                Text("!", color = EspressoDark, fontSize = 8.sp, fontWeight = FontWeight.Black)
                            }
                        }) {
                            Icon(Icons.Default.Notifications, "Notif", tint = Color.White.copy(0.85f))
                        }
                    }
                    IconButton(onClick = { showLogout = true }) {
                        Icon(Icons.AutoMirrored.Filled.Logout, "Keluar", tint = Color.White.copy(0.7f))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = EspressoDark)
            )
        },
        bottomBar = {
            MemberBottomNav(navController = navController, memberId = memberId, currentTab = MemberTab.HOME)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick        = { navController.navigate(Screen.Transaction.createRoute(memberId)) },
                containerColor = EspressoDark,
                contentColor   = GoldFixed,
                shape          = CircleShape,
                modifier       = Modifier.shadow(12.dp, CircleShape)
            ) {
                Icon(Icons.Default.Add, "Tambah Transaksi")
            }
        }
    ) { padding ->
        member?.let { m ->
            val level       = getMemberLevel(m.points)
            val levelColor  = getLevelColor(m.points)
            val initial     = m.name.firstOrNull()?.uppercase() ?: "?"
            val avatarPal   = listOf(Color(0xFF4E342E), Color(0xFF3E2723), Color(0xFF5D4037), Color(0xFF4A148C), Color(0xFFBF360C), Color(0xFF004D40))
            val avatarBg    = avatarPal[m.name.hashCode().absoluteValue % avatarPal.size]
            val recentTx    = transactions.take(3)
            val totalVisits = transactions.size

            LazyColumn(
                modifier       = Modifier.padding(padding).fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                // ── Hero Banner ───────────────────────────────────────────
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.linearGradient(listOf(Color(0xFF1A0E0A), EspressoDark, EspressoMedium))
                            )
                            .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 24.dp)
                    ) {
                        Column {
                            // Greeting row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Selamat datang kembali,", color = MochaText, fontSize = 12.sp)
                                    Text(
                                        m.name.split(" ").first() + " 👋",
                                        color      = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize   = 18.sp
                                    )
                                }
                                // Avatar
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .background(avatarBg)
                                        .border(1.5.dp, GoldBorder, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(initial, color = Color.White, fontWeight = FontWeight.Black, fontSize = 18.sp)
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // ── Premium member card ───────────────────────
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(16.dp, RoundedCornerShape(20.dp), ambientColor = EspressoDark.copy(0.4f))
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(
                                        Brush.linearGradient(listOf(Color(0xFF2D1A12), EspressoMedium))
                                    )
                                    .border(1.5.dp, GoldBorder.copy(0.7f), RoundedCornerShape(20.dp))
                                    .clickable { navController.navigate(Screen.MemberCard.createRoute(memberId)) }
                                    .padding(18.dp)
                            ) {
                                Column {
                                    // Top row: level badge + card hint
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(20.dp),
                                            color = GoldBorder.copy(0.15f),
                                            modifier = Modifier.border(1.dp, GoldBorder.copy(0.5f), RoundedCornerShape(20.dp))
                                        ) {
                                            Text(
                                                "⭐ $level",
                                                color      = GoldFixed,
                                                fontWeight = FontWeight.Bold,
                                                fontSize   = 11.sp,
                                                modifier   = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                            )
                                        }
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.CreditCard, null, tint = MochaText, modifier = Modifier.size(12.dp))
                                            Spacer(modifier = Modifier.width(3.dp))
                                            Text("Tap untuk kartu", color = MochaText, fontSize = 10.sp)
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(20.dp))

                                    // Points big number
                                    Row(verticalAlignment = Alignment.Bottom) {
                                        Text(
                                            "${m.points}",
                                            color      = GoldBorder,
                                            fontWeight = FontWeight.Black,
                                            fontSize   = 44.sp,
                                            lineHeight = 46.sp
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            "pts",
                                            color      = MochaText,
                                            fontSize   = 16.sp,
                                            modifier   = Modifier.padding(bottom = 8.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(14.dp))

                                    // Divider
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(0.5.dp)
                                            .background(GoldBorder.copy(0.25f))
                                    )

                                    Spacer(modifier = Modifier.height(14.dp))

                                    // Bottom row: name + visits
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text("NAMA", color = MochaText, fontSize = 9.sp, letterSpacing = 1.sp)
                                            Text(
                                                m.name.uppercase(),
                                                color      = Color.White,
                                                fontWeight = FontWeight.Bold,
                                                fontSize   = 13.sp,
                                                letterSpacing = 0.5.sp
                                            )
                                        }
                                        Column(horizontalAlignment = Alignment.End) {
                                            Text("KUNJUNGAN", color = MochaText, fontSize = 9.sp, letterSpacing = 1.sp)
                                            Text(
                                                "$totalVisits",
                                                color      = Color.White,
                                                fontWeight = FontWeight.Bold,
                                                fontSize   = 13.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Gold shimmer divider
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(
                                Brush.horizontalGradient(listOf(Color.Transparent, GoldBorder, GoldFixed, GoldBorder, Color.Transparent))
                            )
                    )
                }

                // ── Bento Stats ───────────────────────────────────────────
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    val rewardsAvail = listOf(50, 100, 150).count { m.points >= it }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        BentoCard(
                            icon     = Icons.Default.Star,
                            iconTint = GoldBorder,
                            bgBrush  = Brush.linearGradient(listOf(GoldBorder.copy(0.12f), GoldBorder.copy(0.04f))),
                            value    = "${m.points}",
                            label    = "Total Poin",
                            modifier = Modifier.weight(1f)
                        )
                        BentoCard(
                            icon     = Icons.Default.WorkspacePremium,
                            iconTint = levelColor,
                            bgBrush  = Brush.linearGradient(listOf(levelColor.copy(0.14f), levelColor.copy(0.04f))),
                            value    = level,
                            label    = "Level",
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        BentoCard(
                            icon     = Icons.Default.CardGiftcard,
                            iconTint = EspressoLight,
                            bgBrush  = Brush.linearGradient(listOf(EspressoDark.copy(0.10f), EspressoDark.copy(0.03f))),
                            value    = "$rewardsAvail Item",
                            label    = "Reward",
                            modifier = Modifier
                                .weight(1f)
                                .clickable { navController.navigate(Screen.Rewards.createRoute(memberId)) }
                        )
                        BentoCard(
                            icon     = Icons.Default.Receipt,
                            iconTint = MochaBrown,
                            bgBrush  = Brush.linearGradient(listOf(MochaBrown.copy(0.10f), MochaBrown.copy(0.03f))),
                            value    = "${transactions.size}x",
                            label    = "Transaksi",
                            modifier = Modifier
                                .weight(1f)
                                .clickable { navController.navigate(Screen.History.createRoute(memberId)) }
                        )
                    }
                }

                // ── Recent Bliss ──────────────────────────────────────────
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    SectionHeader(
                        icon      = Icons.Default.History,
                        title     = "Recent Bliss",
                        action    = "Lihat Semua",
                        onAction  = { navController.navigate(Screen.History.createRoute(memberId)) }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

                if (transactions.isEmpty()) {
                    item { EmptyTransactionCard() }
                } else {
                    items(recentTx) { tx -> RecentTransactionRow(tx) }
                }

                // ── Limited Offers ────────────────────────────────────────
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    SectionHeader(
                        icon     = Icons.Default.LocalOffer,
                        title    = "Limited Offers",
                        action   = null,
                        onAction = null
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    LimitedOfferCard(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        onClick  = { navController.navigate(Screen.Rewards.createRoute(memberId)) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        } ?: Box(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentAlignment = Alignment.Center
        ) { CircularProgressIndicator(color = GoldBorder) }
    }
}

@Composable
private fun BentoCard(
    icon: ImageVector,
    iconTint: Color,
    bgBrush: Brush,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(bgBrush)
            .border(1.dp, iconTint.copy(0.15f), RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(iconTint.copy(0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = iconTint, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(value, fontWeight = FontWeight.Black, fontSize = 17.sp, color = iconTint, lineHeight = 20.sp)
            Text(label, fontSize = 11.sp, color = MochaBrown)
        }
    }
}

@Composable
private fun SectionHeader(
    icon: ImageVector,
    title: String,
    action: String?,
    onAction: (() -> Unit)?
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(GoldBorder.copy(0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = GoldBorder, modifier = Modifier.size(16.dp))
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(title, fontWeight = FontWeight.Bold, color = EspressoDark, fontSize = 15.sp)
        }
        if (action != null && onAction != null) {
            TextButton(onClick = onAction, contentPadding = PaddingValues(0.dp)) {
                Text(action, color = GoldCoffee, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun RecentTransactionRow(tx: Transaction) {
    Card(
        modifier  = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
        shape     = RoundedCornerShape(14.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(EspressoDark.copy(0.07f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.LocalCafe, null, tint = EspressoDark, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Rp ${String.format("%,.0f", tx.amount)}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MochaDark)
                Text(tx.date, color = MochaBrown, fontSize = 11.sp)
            }
            Surface(shape = RoundedCornerShape(20.dp), color = GoldBorder.copy(0.12f)) {
                Text(
                    "+${tx.pointEarned} pts",
                    color      = GoldDark,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 12.sp,
                    modifier   = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }
        }
    }
}

@Composable
private fun EmptyTransactionCard() {
    Card(
        modifier  = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(56.dp).clip(CircleShape).background(MochaContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.LocalCafe, null, tint = MochaBrown.copy(0.4f), modifier = Modifier.size(26.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text("Belum ada transaksi", color = EspressoDark, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Text("Tekan + untuk catat transaksi", color = MochaBrown, fontSize = 12.sp)
        }
    }
}

@Composable
private fun LimitedOfferCard(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .shadow(8.dp, RoundedCornerShape(18.dp), ambientColor = EspressoDark.copy(0.3f))
            .clip(RoundedCornerShape(18.dp))
            .background(Brush.linearGradient(listOf(Color(0xFF2D1A12), EspressoDark, EspressoMedium)))
            .border(1.dp, GoldBorder.copy(0.45f), RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .padding(20.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(GoldBorder.copy(0.2f))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text("DOUBLE POINTS", color = GoldFixed, fontSize = 9.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text("Weekend Reward", color = Color.White, fontWeight = FontWeight.Black, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Kumpulkan 2× poin di setiap\ntransaksi akhir pekan ini!",
                    color      = MochaText,
                    fontSize   = 12.sp,
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(14.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(GoldBorder)
                        .padding(horizontal = 16.dp, vertical = 7.dp)
                ) {
                    Text("Klaim Sekarang →", color = EspressoDark, fontWeight = FontWeight.Black, fontSize = 12.sp)
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(0.05f))
                    .border(1.dp, GoldBorder.copy(0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("☕", fontSize = 36.sp)
            }
        }
    }
}

// ─── Kept for compatibility ───────────────────────────────────────────────
@Composable
fun TransactionItemCard(tx: Transaction) {
    Card(
        modifier  = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
        shape     = RoundedCornerShape(12.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(modifier = Modifier.size(40.dp), shape = CircleShape, color = EspressoDark.copy(0.08f)) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.ShoppingBag, null, tint = EspressoDark, modifier = Modifier.size(20.dp))
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Rp ${String.format("%,.0f", tx.amount)}", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = MochaDark)
                Text(tx.date, color = MochaBrown, fontSize = 12.sp)
            }
            Surface(shape = RoundedCornerShape(8.dp), color = GoldBorder.copy(0.15f)) {
                Text("+${tx.pointEarned} pts", color = GoldDark, fontWeight = FontWeight.Bold, fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
            }
        }
    }
}

// ─── Notification Sheet ───────────────────────────────────────────────────

private data class NotifEntry(
    val icon: ImageVector,
    val iconBg: Color,
    val iconTint: Color,
    val title: String,
    val message: String,
    val isNew: Boolean = true
)

@Composable
private fun NotificationSheet(m: Member, transactions: List<Transaction>, onClose: () -> Unit) {
    val level      = getMemberLevel(m.points)
    val levelColor = getLevelColor(m.points)

    // Build contextual notifications
    val items = buildList {
        // 1. Level progress / achievement
        when {
            m.points >= 300 -> add(NotifEntry(
                Icons.Default.WorkspacePremium, PlatinumColor.copy(0.15f), PlatinumColor,
                "Level Platinum Tercapai! 🎉",
                "Selamat! Kamu sudah berada di level tertinggi Coffee Bliss. Nikmati semua privilege eksklusif."
            ))
            m.points >= 100 -> add(NotifEntry(
                Icons.Default.Star, GoldColor.copy(0.15f), GoldColor,
                "Menuju Level Platinum",
                "Hanya ${300 - m.points} poin lagi untuk naik ke Platinum! Terus bertransaksi."
            ))
            else -> add(NotifEntry(
                Icons.Default.Star, SilverColor.copy(0.15f), SilverColor,
                "Menuju Level Gold",
                "Kamu butuh ${100 - m.points} poin lagi untuk naik ke Gold. Ayo tambah transaksi!"
            ))
        }

        // 2. Reward tersedia
        val rewardList = listOf(50 to "Espresso", 100 to "Cappuccino", 150 to "Cafe Latte")
        val best = rewardList.filter { (pts, _) -> m.points >= pts }.maxByOrNull { it.first }
        if (best != null) {
            add(NotifEntry(
                Icons.Default.CardGiftcard, GoldBorder.copy(0.12f), GoldCoffee,
                "Reward Siap Ditukar!",
                "Kamu bisa menukar ${best.first} poin untuk 1 cup ${best.second} gratis sekarang. Cek tab Reward!"
            ))
        }

        // 3. Transaksi terakhir
        if (transactions.isNotEmpty()) {
            val last = transactions.first()
            val dateShort = last.date.split(",").firstOrNull()?.trim() ?: last.date
            add(NotifEntry(
                Icons.Default.Receipt, EspressoDark.copy(0.08f), EspressoDark,
                "Transaksi Terakhir",
                "Rp ${String.format("%,.0f", last.amount)} pada $dateShort. Kamu mendapat +${last.pointEarned} poin.",
                isNew = false
            ))
        }

        // 4. Tips
        add(NotifEntry(
            Icons.Default.Info, MochaBrown.copy(0.08f), MochaBrown,
            "Tips Kumpulkan Poin",
            "Setiap Rp 10.000 transaksi = 1 poin. Semakin sering transaksi, semakin cepat naik level!",
            isNew = false
        ))
    }

    val newCount = items.count { it.isNew }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 32.dp)
    ) {
        // Handle bar
        Box(
            modifier = Modifier
                .width(40.dp).height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(MochaDivider)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Brush.linearGradient(listOf(EspressoDark, EspressoMedium))),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Notifications, null, tint = GoldBorder, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Pemberitahuan", fontWeight = FontWeight.Black, fontSize = 18.sp, color = EspressoDark)
                Text(
                    if (newCount > 0) "$newCount pesan baru" else "Semua sudah dibaca",
                    color = MochaBrown, fontSize = 12.sp
                )
            }
            if (newCount > 0) {
                Surface(shape = RoundedCornerShape(20.dp), color = GoldBorder) {
                    Text(
                        "$newCount baru",
                        color = EspressoDark, fontWeight = FontWeight.Black, fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = MochaDivider, thickness = 0.5.dp)
        Spacer(modifier = Modifier.height(12.dp))

        // Notification items
        items.forEach { entry ->
            NotifSheetItem(entry)
            Spacer(modifier = Modifier.height(10.dp))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick  = onClose,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape    = RoundedCornerShape(26.dp),
            colors   = ButtonDefaults.buttonColors(containerColor = EspressoDark)
        ) {
            Text("Tutup", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = GoldFixed)
        }
    }
}

@Composable
private fun NotifSheetItem(entry: NotifEntry) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(if (entry.isNew) entry.iconBg else Color(0xFFF8F5F2))
            .then(
                if (entry.isNew)
                    Modifier.border(1.dp, entry.iconTint.copy(0.2f), RoundedCornerShape(14.dp))
                else
                    Modifier
            )
            .padding(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(entry.iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(entry.icon, null, tint = entry.iconTint, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text       = entry.title,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 13.sp,
                    color      = EspressoDark,
                    modifier   = Modifier.weight(1f)
                )
                if (entry.isNew) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(GoldBorder)
                    )
                }
            }
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text       = entry.message,
                color      = MochaBrown,
                fontSize   = 12.sp,
                lineHeight = 17.sp
            )
        }
    }
}

@Composable
fun DashboardMenuButton(
    icon: ImageVector, label: String, tint: Color, containerColor: Color,
    modifier: Modifier = Modifier, onClick: () -> Unit
) {
    Card(modifier = modifier, shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(0.dp), onClick = onClick) {
        Row(modifier = Modifier.padding(horizontal = 14.dp, vertical = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = tint, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Text(label, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = tint)
        }
    }
}
