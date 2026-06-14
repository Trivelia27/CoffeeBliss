package com.example.coffeebliss.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.coffeebliss.ui.CoffeeViewModel
import com.example.coffeebliss.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMemberScreen(navController: NavController, viewModel: CoffeeViewModel) {
    var name  by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    val nameError  = name.isNotBlank()  && name.trim().length < 2
    val emailError = email.isNotBlank() && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val phoneError = phone.isNotBlank() && phone.trim().length < 8
    val formValid  = name.isNotBlank() && email.isNotBlank() && phone.isNotBlank()
                     && !nameError && !emailError && !phoneError

    Scaffold(
        containerColor = MochaCream,
        topBar = {
            TopAppBar(
                title = {
                    Text("Daftar Member", fontWeight = FontWeight.Bold, color = Color.White)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = GoldBorder)
                    }
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // ── Hero icon ──────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .clip(CircleShape)
                    .background(Brush.linearGradient(listOf(EspressoDark, EspressoMedium)))
                    .border(2.dp, GoldBorder.copy(alpha = 0.6f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.LocalCafe,
                    contentDescription = null,
                    tint     = GoldBorder,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text  = "Registrasi Member Baru",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = EspressoDark
            )
            Text(
                text  = "Lengkapi data untuk bergabung",
                style = MaterialTheme.typography.bodyMedium,
                color = MochaBrown,
                modifier = Modifier.padding(top = 4.dp, bottom = 28.dp)
            )

            // ── Form Card ─────────────────────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape  = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    FormField(
                        value = name,
                        onValueChange = { name = it },
                        label = "Nama Lengkap",
                        icon  = Icons.Default.Person,
                        isError = nameError,
                        errorMessage = "Nama minimal 2 karakter",
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words)
                    )
                    FormField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Alamat Email",
                        icon  = Icons.Default.Email,
                        isError = emailError,
                        errorMessage = "Format email tidak valid",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )
                    FormField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = "Nomor HP",
                        icon  = Icons.Default.Phone,
                        isError = phoneError,
                        errorMessage = "Nomor HP minimal 8 digit",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // ── CTA Button ─────────────────────────────────────────────
            Button(
                onClick = {
                    if (formValid) {
                        viewModel.addMember(name.trim(), email.trim(), phone.trim())
                        navController.popBackStack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                enabled = formValid,
                shape   = RoundedCornerShape(27.dp),
                colors  = ButtonDefaults.buttonColors(
                    containerColor         = EspressoDark,
                    disabledContainerColor = MochaBrown.copy(alpha = 0.3f)
                )
            ) {
                Text(
                    text  = "Daftar Sekarang",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = if (formValid) GoldFixed else MochaText
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text  = "Gratis • Poin reward setiap transaksi",
                color = MochaBrown,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun FormField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    isError: Boolean,
    errorMessage: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            leadingIcon = {
                Icon(icon, contentDescription = null, tint = EspressoDark)
            },
            isError = isError,
            modifier = Modifier.fillMaxWidth(),
            shape    = RoundedCornerShape(12.dp),
            keyboardOptions = keyboardOptions,
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor   = EspressoDark,
                focusedLabelColor    = EspressoDark,
                cursorColor          = EspressoDark,
                unfocusedBorderColor = MochaDivider
            )
        )
        if (isError) {
            Text(
                text  = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 16.dp, top = 2.dp)
            )
        }
    }
}
