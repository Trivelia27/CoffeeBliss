package com.example.coffeebliss.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.coffeebliss.ui.CoffeeViewModel
import com.example.coffeebliss.ui.Screen
import com.example.coffeebliss.ui.theme.*

data class Reward(val name: String, val description: String, val points: Int, val emoji: String)

private val rewards = listOf(
    Reward("Espresso",        "1 cup Espresso gratis pilihan kamu",   50,  "☕"),
    Reward("Cappuccino",      "1 cup Cappuccino creamy gratis",        100, "🥛"),
    Reward("Cafe Latte",      "1 cup Latte signature gratis",          150, "🍵"),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RewardScreen(navController: NavController, viewModel: CoffeeViewModel, memberId: Int) {
    val member by viewModel.getMember(memberId).collectAsState(initial = null)

    var confirmReward  by remember { mutableStateOf<Reward?>(null) }
    var successReward  by remember { mutableStateOf<Reward?>(null) }
    var showNotifSheet by remember { mutableStateOf(false) }

    confirmReward?.let { reward ->
        ConfirmRedeemDialog(
            reward     = reward,
            currentPts = member?.points ?: 0,
            onConfirm  = {
                viewModel.redeemReward(memberId, reward.points, reward.name)
                successReward = reward
                confirmReward = null
            },
            onDismiss  = { confirmReward = null }
        )
    }

    successReward?.let { reward ->
        RedeemSuccessDialog(
            reward        = reward,
            remaining     = member?.points ?: 0,
            onGoToProfile = {
                successReward = null
                navController.navigate(Screen.Profile.createRoute(memberId))
            },
            onDismiss = { successReward = null }
        )
    }

    val context = LocalContext.current

    if (showNotifSheet) {
        NotificationsDialog(
            members   = listOfNotNull(member),
            onDismiss = { showNotifSheet = false }
        )
    }

    Scaffold(
        containerColor = MochaCream,
        topBar = {
            TopAppBar(
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
                            "Coffee Bliss",
                            fontWeight = FontWeight.Black,
                            color      = Color.White,
                            fontSize   = 17.sp
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showNotifSheet = true }) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = "Notifikasi",
                            tint = Color.White.copy(alpha = 0.8f)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = EspressoDark)
            )
        },
        bottomBar = {
            MemberBottomNav(
                navController = navController,
                memberId      = memberId,
                currentTab    = MemberTab.REWARDS
            )
        }
    ) { padding ->
        LazyColumn(
            modifier       = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // ── Points Balance Header ─────────────────────────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(espressoGradient)
                        .padding(horizontal = 20.dp, vertical = 24.dp)
                ) {
                    member?.let { m ->
                        val level = getMemberLevel(m.points)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text     = "Saldo Poin",
                                    color    = MochaText,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text       = "${String.format("%,d", m.points)} Points",
                                    color      = GoldBorder,
                                    fontWeight = FontWeight.Black,
                                    fontSize   = 30.sp,
                                    lineHeight = 34.sp
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = GoldBorder.copy(alpha = 0.15f),
                                modifier = Modifier.border(1.dp, GoldBorder.copy(0.5f), RoundedCornerShape(20.dp))
                            ) {
                                Text(
                                    text       = level,
                                    color      = GoldFixed,
                                    fontWeight = FontWeight.Bold,
                                    fontSize   = 12.sp,
                                    modifier   = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(
                            Brush.horizontalGradient(listOf(GoldBorder, GoldFixed, GoldBorder))
                        )
                )
            }

            // ── Catalog Header ────────────────────────────────────────────
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.LocalCafe,
                            contentDescription = null,
                            tint     = GoldBorder,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text       = "Rewards Catalog",
                            fontWeight = FontWeight.Bold,
                            color      = EspressoDark,
                            fontSize   = 16.sp
                        )
                    }
                    TextButton(
                        onClick = { navController.navigate(Screen.History.createRoute(memberId)) }
                    ) {
                        Text(
                            text     = "Lihat Riwayat",
                            color    = GoldCoffee,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // ── Reward Cards ──────────────────────────────────────────────
            items(rewards.size) { index ->
                val reward    = rewards[index]
                val canAfford = (member?.points ?: 0) >= reward.points

                StitchRewardCard(
                    reward    = reward,
                    canAfford = canAfford,
                    onRedeem  = { confirmReward = reward },
                    modifier  = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }

            // ── Refer a Friend ────────────────────────────────────────────
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(EspressoDark, Color(0xFF4A2C20))
                            )
                        )
                        .border(1.dp, GoldBorder.copy(0.3f), RoundedCornerShape(18.dp))
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = null,
                                    tint     = GoldBorder,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text       = "Refer a Friend",
                                    color      = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize   = 16.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text     = "Ajak teman dan dapatkan\n50 poin bonus untuk kamu dan temanmu!",
                                color    = MochaText,
                                fontSize = 12.sp,
                                lineHeight = 18.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Surface(
                                shape    = RoundedCornerShape(20.dp),
                                color    = GoldBorder,
                                modifier = Modifier.clickable {
                                    val memberCode = member?.id?.let { "BLISS-${String.format("%05d", it)}-C" } ?: "BLISS-XXXXX-C"
                                    val shareText = "☕ Bergabunglah dengan Coffee Bliss Loyalty Program!\n\nGunakan kode referral saya: $memberCode\nDapatkan 50 poin bonus untuk kamu dan temanmu!\n\nhttps://coffeebliss.id/referral"
                                    val intent = Intent(Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        putExtra(Intent.EXTRA_SUBJECT, "Gabung Coffee Bliss Loyalty!")
                                        putExtra(Intent.EXTRA_TEXT, shareText)
                                    }
                                    context.startActivity(Intent.createChooser(intent, "Bagikan via"))
                                }
                            ) {
                                Text(
                                    text       = "Bagikan Link",
                                    color      = EspressoDark,
                                    fontWeight = FontWeight.Bold,
                                    fontSize   = 12.sp,
                                    modifier   = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.06f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Share,
                                contentDescription = null,
                                tint     = GoldBorder,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StitchRewardCard(
    reward: Reward,
    canAfford: Boolean,
    onRedeem: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier  = modifier,
        shape     = RoundedCornerShape(18.dp),
        colors    = CardDefaults.cardColors(
            containerColor = if (canAfford) Color.White else MochaContainer.copy(alpha = 0.7f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (canAfford) 3.dp else 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Emoji icon
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        if (canAfford) EspressoDark.copy(alpha = 0.06f)
                        else MochaBrown.copy(alpha = 0.06f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = reward.emoji, fontSize = 32.sp)
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text       = reward.name,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 15.sp,
                    color      = if (canAfford) MochaDark else MochaBrown
                )
                Text(
                    text     = reward.description,
                    fontSize = 12.sp,
                    color    = MochaBrown,
                    lineHeight = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Point cost badge
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (canAfford) GoldBorder.copy(alpha = 0.15f) else MochaDivider.copy(0.5f)
                ) {
                    Text(
                        text       = "redeem ${reward.points} Points",
                        color      = if (canAfford) GoldDark else MochaBrown,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 11.sp,
                        modifier   = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            if (canAfford) {
                Button(
                    onClick        = onRedeem,
                    shape          = RoundedCornerShape(12.dp),
                    colors         = ButtonDefaults.buttonColors(containerColor = EspressoDark),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Text(
                        text       = "Redeem",
                        fontSize   = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color      = GoldFixed
                    )
                }
            } else {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MochaDivider.copy(alpha = 0.4f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = null,
                            tint     = MochaBrown,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Terkunci", color = MochaBrown, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun ConfirmRedeemDialog(
    reward: Reward,
    currentPts: Int,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor   = Color.White,
        shape            = RoundedCornerShape(24.dp),
        icon             = { Text(reward.emoji, fontSize = 44.sp) },
        title = {
            Text(
                "Konfirmasi Redeem",
                fontWeight = FontWeight.Black,
                color      = EspressoDark,
                textAlign  = TextAlign.Center
            )
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier            = Modifier.fillMaxWidth()
            ) {
                Text("Tukar poin untuk:", color = MochaBrown, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    reward.name,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 18.sp,
                    textAlign  = TextAlign.Center,
                    color      = EspressoDark
                )
                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Poin Kamu", fontSize = 11.sp, color = MochaBrown)
                        Text("$currentPts", fontWeight = FontWeight.Black, color = EspressoDark, fontSize = 20.sp)
                    }
                    Text("→", fontSize = 22.sp, color = GoldBorder, modifier = Modifier.padding(top = 10.dp))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Sisa Poin", fontSize = 11.sp, color = MochaBrown)
                        Text(
                            "${currentPts - reward.points}",
                            fontWeight = FontWeight.Black,
                            color      = EspressoDark,
                            fontSize   = 20.sp
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick  = onConfirm,
                shape    = RoundedCornerShape(20.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = EspressoDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ya, Redeem!", fontWeight = FontWeight.Bold, color = GoldFixed)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                Text("Batal", color = MochaBrown)
            }
        }
    )
}

@Composable
private fun RedeemSuccessDialog(
    reward: Reward,
    remaining: Int,
    onGoToProfile: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape     = RoundedCornerShape(28.dp),
            colors    = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Confetti-style success icon
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(EspressoDark, EspressoMedium)))
                        .border(2.dp, GoldBorder, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint     = GoldFixed,
                        modifier = Modifier.size(48.dp)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text       = "Redeem Success! 🎉",
                    fontWeight = FontWeight.Black,
                    fontSize   = 20.sp,
                    color      = EspressoDark,
                    textAlign  = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text      = "Poin telah dipotong dan reward siap diambil.",
                    color     = MochaBrown,
                    textAlign = TextAlign.Center,
                    fontSize  = 13.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Reward box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Brush.linearGradient(listOf(EspressoDark, EspressoMedium)))
                        .border(1.dp, GoldBorder.copy(0.5f), RoundedCornerShape(16.dp))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment    = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(reward.emoji, fontSize = 28.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(reward.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text     = "Remaining Balance: $remaining PTS",
                    color    = EspressoDark,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick  = onGoToProfile,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape    = RoundedCornerShape(24.dp),
                    colors   = ButtonDefaults.buttonColors(containerColor = EspressoDark)
                ) {
                    Text("Go to My Card →", fontWeight = FontWeight.Bold, color = GoldFixed)
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                    Text("Lihat Riwayat Transaksi", color = MochaBrown)
                }
            }
        }
    }
}
