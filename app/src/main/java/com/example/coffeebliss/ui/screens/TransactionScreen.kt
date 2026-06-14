package com.example.coffeebliss.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.coffeebliss.ui.CoffeeViewModel
import com.example.coffeebliss.ui.Screen
import com.example.coffeebliss.ui.theme.*

private val paymentMethods = listOf("Cash", "Digital Wallet", "Kartu Kredit", "Bliss Balance")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(navController: NavController, viewModel: CoffeeViewModel, memberId: Int) {
    var amountText       by remember { mutableStateOf("") }
    var selectedPayment  by remember { mutableStateOf("Cash") }
    var showSuccess      by remember { mutableStateOf(false) }
    var lastEarned       by remember { mutableStateOf(0) }
    var totalAfter       by remember { mutableStateOf(0) }

    val member by viewModel.getMember(memberId).collectAsState(initial = null)

    val amount       = amountText.toDoubleOrNull()
    val pointsToEarn = amount?.let { (it / 10000).toInt() } ?: 0
    val isValid      = amount != null && amount > 0

    if (showSuccess) {
        PointUpdatedDialog(
            earned       = lastEarned,
            total        = totalAfter,
            onGoToReward = {
                showSuccess = false
                navController.navigate(Screen.Rewards.createRoute(memberId)) {
                    popUpTo(Screen.Transaction.createRoute(memberId)) { inclusive = true }
                }
            },
            onDismiss = {
                showSuccess = false
                navController.popBackStack()
            }
        )
    }

    Scaffold(
        containerColor = MochaCream,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Add Transaction",
                        fontWeight = FontWeight.Bold,
                        color      = Color.White,
                        fontSize   = 17.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = GoldBorder)
                    }
                },
                actions = {
                    // Coffee Bliss logo
                    Icon(
                        Icons.Default.LocalCafe,
                        contentDescription = null,
                        tint     = GoldBorder,
                        modifier = Modifier
                            .size(28.dp)
                            .padding(end = 12.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = EspressoDark)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // ── Member info card ──────────────────────────────────────────
            member?.let { m ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(Brush.linearGradient(listOf(EspressoDark, EspressoMedium)))
                        .border(1.dp, GoldBorder.copy(0.3f), RoundedCornerShape(18.dp))
                        .padding(18.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(GoldBorder.copy(alpha = 0.15f))
                                .border(1.dp, GoldBorder.copy(0.4f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text       = m.name.firstOrNull()?.uppercase() ?: "?",
                                color      = GoldFixed,
                                fontWeight = FontWeight.Black,
                                fontSize   = 22.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text       = m.name,
                                color      = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize   = 16.sp
                            )
                            Text(
                                text     = "ID: BLISS-${String.format("%05d", m.id)}-C",
                                color    = MochaText,
                                fontSize = 11.sp
                            )
                        }
                        // Verified badge
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Verified,
                                contentDescription = null,
                                tint     = GoldBorder,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text     = getMemberLevel(m.points),
                                color    = GoldFixed,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            // ── Amount input card ─────────────────────────────────────────
            Card(
                modifier  = Modifier.fillMaxWidth(),
                shape     = RoundedCornerShape(18.dp),
                colors    = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text       = "Total Pembelian",
                        fontWeight = FontWeight.Bold,
                        color      = EspressoDark,
                        fontSize   = 15.sp
                    )

                    OutlinedTextField(
                        value         = amountText,
                        onValueChange = { amountText = it.filter { c -> c.isDigit() } },
                        label         = { Text("Masukkan nominal") },
                        leadingIcon   = {
                            Text(
                                "Rp",
                                fontWeight = FontWeight.Bold,
                                color      = EspressoDark,
                                fontSize   = 14.sp,
                                modifier   = Modifier.padding(start = 4.dp)
                            )
                        },
                        modifier        = Modifier.fillMaxWidth(),
                        shape           = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine      = true,
                        colors          = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor   = EspressoDark,
                            focusedLabelColor    = EspressoDark,
                            cursorColor          = EspressoDark,
                            unfocusedBorderColor = MochaDivider
                        )
                    )

                    // Points calculation display
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (isValid) GoldBorder.copy(alpha = 0.10f)
                                else MochaContainer
                            )
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint     = if (isValid) GoldBorder else MochaBrown,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        if (isValid) {
                            Text("Kamu mendapat ", color = EspressoDark, fontSize = 13.sp)
                            Text(
                                text       = "$pointsToEarn Points",
                                color      = EspressoDark,
                                fontWeight = FontWeight.Black,
                                fontSize   = 14.sp
                            )
                        } else {
                            Text(
                                text     = "1 Point per Rp 10.000",
                                color    = MochaBrown,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            // ── Payment Method ────────────────────────────────────────────
            Card(
                modifier  = Modifier.fillMaxWidth(),
                shape     = RoundedCornerShape(18.dp),
                colors    = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text       = "Metode Pembayaran",
                        fontWeight = FontWeight.Bold,
                        color      = EspressoDark,
                        fontSize   = 15.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    // 2-column chip grid
                    val pairs = paymentMethods.chunked(2)
                    for (pair in pairs) {
                        Row(
                            modifier              = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            for (method in pair) {
                                val isSelected = selectedPayment == method
                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { selectedPayment = method },
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (isSelected) EspressoDark else MochaContainer
                                ) {
                                    Text(
                                        text       = method,
                                        color      = if (isSelected) Color.White else MochaBrown,
                                        fontSize   = 13.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        textAlign  = TextAlign.Center,
                                        modifier   = Modifier.padding(vertical = 10.dp, horizontal = 8.dp)
                                    )
                                }
                            }
                            // Fill remaining space if pair has 1 item
                            if (pair.size == 1) Spacer(modifier = Modifier.weight(1f))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // ── Save button ───────────────────────────────────────────────
            Button(
                onClick = {
                    if (isValid && amount != null) {
                        lastEarned = pointsToEarn
                        totalAfter = (member?.points ?: 0) + pointsToEarn
                        viewModel.addTransaction(memberId, amount)
                        amountText = ""
                        showSuccess = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                enabled = isValid,
                shape   = RoundedCornerShape(27.dp),
                colors  = ButtonDefaults.buttonColors(
                    containerColor         = EspressoDark,
                    disabledContainerColor = MochaBrown.copy(alpha = 0.3f)
                )
            ) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint     = if (isValid) GoldFixed else MochaText,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Save Transaction",
                    fontWeight = FontWeight.Bold,
                    fontSize   = 16.sp,
                    color      = if (isValid) GoldFixed else MochaText
                )
            }

            // Hint
            Text(
                text      = "Rp 10.000 = 1 Poin reward",
                color     = MochaBrown,
                fontSize  = 12.sp,
                textAlign = TextAlign.Center,
                modifier  = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun PointUpdatedDialog(
    earned: Int,
    total: Int,
    onGoToReward: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape     = RoundedCornerShape(24.dp),
            colors    = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Column(
                modifier = Modifier.padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .background(
                            Brush.linearGradient(listOf(EspressoDark, EspressoMedium)),
                            CircleShape
                        )
                        .border(2.dp, GoldBorder, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint     = GoldFixed,
                        modifier = Modifier.size(44.dp)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text       = "Transaction Saved! ✓",
                    fontWeight = FontWeight.Black,
                    fontSize   = 18.sp,
                    color      = EspressoDark,
                    textAlign  = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text      = "Poin reward telah dikreditkan ke akun kamu",
                    color     = MochaBrown,
                    fontSize  = 13.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Points badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Brush.linearGradient(listOf(EspressoDark, EspressoMedium)))
                        .border(1.dp, GoldBorder, RoundedCornerShape(16.dp))
                        .padding(horizontal = 32.dp, vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text       = "+$earned",
                            color      = GoldBorder,
                            fontWeight = FontWeight.Black,
                            fontSize   = 38.sp,
                            lineHeight = 40.sp
                        )
                        Text(
                            text          = "POIN REWARD",
                            color         = MochaText,
                            fontSize      = 10.sp,
                            letterSpacing = 1.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text     = "Total poin kamu: $total",
                    color    = MochaBrown,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick  = onGoToReward,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape    = RoundedCornerShape(24.dp),
                    colors   = ButtonDefaults.buttonColors(containerColor = EspressoDark)
                ) {
                    Text("Lihat Reward", fontWeight = FontWeight.Bold, color = GoldFixed)
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                    Text("Kembali ke Dashboard", color = MochaBrown)
                }
            }
        }
    }
}
