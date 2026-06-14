package com.example.coffeebliss.ui.screens

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.coffeebliss.data.Transaction
import com.example.coffeebliss.ui.CoffeeViewModel
import com.example.coffeebliss.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(navController: NavController, viewModel: CoffeeViewModel, memberId: Int) {
    val member       by viewModel.getMember(memberId).collectAsState(initial = null)
    val transactions by viewModel.getTransactions(memberId).collectAsState(initial = emptyList())

    var selectedTx by remember { mutableStateOf<Transaction?>(null) }
    val sheetState  = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Detail bottom sheet
    selectedTx?.let { tx ->
        ModalBottomSheet(
            onDismissRequest  = { selectedTx = null },
            sheetState        = sheetState,
            containerColor    = Color.White,
            shape             = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
        ) {
            TransactionDetailSheet(tx = tx, onClose = { selectedTx = null })
        }
    }

    Scaffold(
        containerColor = MochaCream,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Riwayat Transaksi", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
                        member?.let { m ->
                            val level = getMemberLevel(m.points)
                            val nextTarget = if (m.points >= 300) 0 else if (m.points >= 100) 300 - m.points else 100 - m.points
                            if (nextTarget > 0) {
                                Text("$nextTarget pts to $level upgrade", color = MochaText, fontSize = 11.sp)
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = GoldBorder)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = EspressoDark)
            )
        },
        bottomBar = {
            MemberBottomNav(navController = navController, memberId = memberId, currentTab = MemberTab.TRANSACTIONS)
        }
    ) { padding ->
        LazyColumn(
            modifier       = Modifier.padding(padding).fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // ── Summary Banner ────────────────────────────────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(espressoGradient)
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                ) {
                    member?.let { m ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Total Poin", color = MochaText, fontSize = 12.sp)
                                Text(
                                    "${String.format("%,d", m.points)} Points",
                                    color      = GoldBorder,
                                    fontWeight = FontWeight.Black,
                                    fontSize   = 26.sp,
                                    lineHeight = 30.sp
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("${transactions.size}", color = Color.White, fontWeight = FontWeight.Black, fontSize = 22.sp)
                                Text("Transaksi", color = MochaText, fontSize = 12.sp)
                            }
                        }
                    }
                }
                Box(
                    modifier = Modifier.fillMaxWidth().height(2.dp)
                        .background(Brush.horizontalGradient(listOf(GoldBorder, GoldFixed, GoldBorder)))
                )
            }

            // ── Section Label ─────────────────────────────────────────────
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Receipt, null, tint = GoldBorder, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Semua Transaksi", fontWeight = FontWeight.Bold, color = EspressoDark, fontSize = 15.sp)
                    }
                    Surface(shape = RoundedCornerShape(20.dp), color = EspressoDark.copy(0.06f)) {
                        Text(
                            "${transactions.size} total",
                            color      = EspressoDark,
                            fontSize   = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier   = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                // hint tap
                if (transactions.isNotEmpty()) {
                    Text(
                        "Ketuk transaksi untuk melihat detail",
                        color    = MochaBrown.copy(0.6f),
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 20.dp).padding(bottom = 4.dp)
                    )
                }
            }

            if (transactions.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 48.dp, horizontal = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier.size(72.dp).clip(CircleShape).background(MochaContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.LocalCafe, null, tint = MochaBrown.copy(0.4f), modifier = Modifier.size(32.dp))
                            }
                            Spacer(modifier = Modifier.height(14.dp))
                            Text("Belum ada transaksi", color = EspressoDark, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
                            Text("Lakukan transaksi pertama kamu!", color = MochaBrown, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 4.dp))
                        }
                    }
                }
            } else {
                val grouped = transactions.groupBy { tx ->
                    tx.date.split(",").firstOrNull()?.trim() ?: tx.date
                }

                for ((dateLabel, txList) in grouped) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(GoldBorder))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(dateLabel, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = MochaBrown, letterSpacing = 0.3.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            HorizontalDivider(modifier = Modifier.weight(1f), thickness = 0.5.dp, color = MochaDivider)
                        }
                    }
                    for (tx in txList) {
                        item(key = tx.id) {
                            HistoryTransactionCard(tx = tx, onClick = { selectedTx = tx })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoryTransactionCard(tx: Transaction, onClick: () -> Unit) {
    val isRedeem = tx.type == "redeem"
    val iconBg   = if (isRedeem) Color(0xFFFFF3E0) else EspressoDark.copy(0.07f)
    val iconTint = if (isRedeem) Color(0xFFE65100) else EspressoDark
    val icon     = if (isRedeem) Icons.Default.CardGiftcard else Icons.Default.LocalCafe

    Card(
        modifier  = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { onClick() },
        shape     = RoundedCornerShape(14.dp),
        colors    = CardDefaults.cardColors(
            containerColor = if (isRedeem) Color(0xFFFFFBF5) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            // Left accent bar
            Box(
                modifier = Modifier
                    .width(3.dp).height(44.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(if (isRedeem) Color(0xFFE65100) else GoldBorder)
            )
            Spacer(modifier = Modifier.width(10.dp))

            Box(
                modifier = Modifier.size(42.dp).clip(CircleShape).background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = iconTint, modifier = Modifier.size(20.dp))
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text       = if (isRedeem) tx.note.ifEmpty { "Redeem Reward" } else "Rp ${String.format("%,.0f", tx.amount)}",
                    fontWeight = FontWeight.Bold,
                    fontSize   = 15.sp,
                    color      = MochaDark
                )
                val timePart = tx.date.split(",").getOrNull(1)?.trim() ?: ""
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(if (timePart.isNotEmpty()) timePart else tx.date, color = MochaBrown, fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = if (isRedeem) Color(0xFFE65100).copy(0.08f) else EspressoDark.copy(0.06f)
                    ) {
                        Text(
                            if (isRedeem) "Penukaran" else "In-store",
                            color    = if (isRedeem) Color(0xFFE65100) else MochaBrown,
                            fontSize = 10.sp,
                            fontWeight = if (isRedeem) FontWeight.SemiBold else FontWeight.Normal,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (isRedeem) Color(0xFFE65100).copy(0.1f) else GoldBorder.copy(0.12f)
                ) {
                    Text(
                        text       = if (isRedeem) "${tx.pointEarned} Pts" else "+${tx.pointEarned} Pts",
                        color      = if (isRedeem) Color(0xFFE65100) else GoldDark,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 13.sp,
                        modifier   = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Icon(Icons.Default.ChevronRight, null, tint = MochaBrown.copy(0.35f), modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
private fun TransactionDetailSheet(tx: Transaction, onClose: () -> Unit) {
    val isRedeem  = tx.type == "redeem"
    val datePart  = tx.date.split(",").firstOrNull()?.trim() ?: tx.date
    val timePart  = tx.date.split(",").getOrNull(1)?.trim() ?: ""
    val receiptNo = "TRX-${tx.id.toString().padStart(6, '0')}"
    val accentColor = if (isRedeem) Color(0xFFE65100) else GoldBorder

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 32.dp)
    ) {
        Box(modifier = Modifier.width(40.dp).height(4.dp).clip(RoundedCornerShape(2.dp)).background(MochaDivider).align(Alignment.CenterHorizontally))

        Spacer(modifier = Modifier.height(20.dp))

        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Brush.linearGradient(
                        if (isRedeem) listOf(Color(0xFFBF360C), Color(0xFFE64A19))
                        else listOf(EspressoDark, EspressoMedium)
                    )),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    if (isRedeem) Icons.Default.CardGiftcard else Icons.Default.Receipt,
                    null, tint = if (isRedeem) Color.White else GoldBorder,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    if (isRedeem) "Detail Penukaran" else "Detail Transaksi",
                    fontWeight = FontWeight.Black, fontSize = 18.sp, color = EspressoDark
                )
                Text(receiptNo, color = MochaBrown, fontSize = 13.sp, letterSpacing = 0.5.sp)
            }
            Surface(shape = RoundedCornerShape(20.dp), color = Color(0xFF2E7D32).copy(0.1f)) {
                Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFF2E7D32)))
                    Spacer(modifier = Modifier.width(5.dp))
                    Text("Selesai", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        HorizontalDivider(color = MochaDivider, thickness = 0.5.dp)
        Spacer(modifier = Modifier.height(20.dp))

        // Hero box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.linearGradient(
                        if (isRedeem) listOf(Color(0xFF4E1500), Color(0xFF6D2100))
                        else listOf(EspressoDark, Color(0xFF4A2C20))
                    )
                )
                .border(1.dp, accentColor.copy(0.35f), RoundedCornerShape(16.dp))
                .padding(vertical = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    if (isRedeem) "Reward Ditukar" else "Total Pembayaran",
                    color = MochaText, fontSize = 12.sp, letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text       = if (isRedeem) tx.note.ifEmpty { "Reward" } else "Rp ${String.format("%,.0f", tx.amount)}",
                    color      = accentColor,
                    fontWeight = FontWeight.Black,
                    fontSize   = if (isRedeem) 26.sp else 32.sp,
                    lineHeight = 36.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                Surface(shape = RoundedCornerShape(20.dp), color = accentColor.copy(0.18f)) {
                    Row(modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            if (isRedeem) Icons.Default.Remove else Icons.Default.Star,
                            null, tint = accentColor, modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            if (isRedeem) "${tx.pointEarned} Poin Digunakan"
                            else "+${tx.pointEarned} Poin Didapat",
                            color = accentColor, fontWeight = FontWeight.Bold, fontSize = 13.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Detail rows
        DetailCard {
            DetailRow(label = "No. Resi", value = receiptNo, icon = Icons.Default.ConfirmationNumber)
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = MochaDivider)
            DetailRow(label = "Tanggal", value = datePart, icon = Icons.Default.CalendarToday)
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = MochaDivider)
            DetailRow(label = "Waktu", value = if (timePart.isNotEmpty()) timePart else "-", icon = Icons.Default.Schedule)
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = MochaDivider)
            if (isRedeem) {
                DetailRow(label = "Jenis", value = "Penukaran Reward", icon = Icons.Default.CardGiftcard, valueColor = Color(0xFFE65100))
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = MochaDivider)
                DetailRow(label = "Reward", value = tx.note.ifEmpty { "-" }, icon = Icons.Default.LocalCafe)
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = MochaDivider)
                DetailRow(label = "Poin Digunakan", value = "${tx.pointEarned} pts", icon = Icons.Default.Star, valueColor = Color(0xFFE65100))
            } else {
                DetailRow(label = "Metode Bayar", value = "In-store", icon = Icons.Default.Store)
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = MochaDivider)
                DetailRow(label = "Poin Diperoleh", value = "+${tx.pointEarned} pts", icon = Icons.Default.Star, valueColor = GoldDark)
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = MochaDivider)
                DetailRow(label = "Rate Poin", value = "Rp 10.000 = 1 poin", icon = Icons.Default.Info)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

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
private fun DetailCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier  = Modifier.fillMaxWidth()
    ) {
        Column(content = content)
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    icon: ImageVector,
    valueColor: Color = MochaDark
) {
    Row(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(32.dp).clip(RoundedCornerShape(8.dp)).background(EspressoDark.copy(0.07f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = EspressoDark.copy(0.7f), modifier = Modifier.size(16.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(label, color = MochaBrown, fontSize = 13.sp, modifier = Modifier.weight(1f))
        Text(value, color = valueColor, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
    }
}
