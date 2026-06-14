package com.example.coffeebliss.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.coffeebliss.ui.CoffeeViewModel
import com.example.coffeebliss.ui.Screen
import com.example.coffeebliss.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(navController: NavController, viewModel: CoffeeViewModel) {
    var name            by remember { mutableStateOf("") }
    var email           by remember { mutableStateOf("") }
    var phone           by remember { mutableStateOf("") }
    var password        by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showPassword    by remember { mutableStateOf(false) }
    var showConfirm     by remember { mutableStateOf(false) }
    var errorMsg        by remember { mutableStateOf("") }
    var isLoading       by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val scope        = rememberCoroutineScope()

    val contentAlpha = remember { Animatable(0f) }
    val contentSlide = remember { Animatable(30f) }
    LaunchedEffect(Unit) {
        delay(80)
        contentAlpha.animateTo(1f, tween(450))
        contentSlide.animateTo(0f, tween(450, easing = EaseOutCubic))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.radialGradient(listOf(Color(0xFF3C2A21), Color(0xFF1A0E0A)), radius = 1200f))
    ) {
        Box(modifier = Modifier.size(260.dp).offset((-60).dp, (-80).dp).blur(80.dp).background(GoldBorder.copy(0.07f), CircleShape))
        Box(modifier = Modifier.size(200.dp).align(Alignment.BottomEnd).offset(60.dp, 60.dp).blur(70.dp).background(EspressoLight.copy(0.25f), CircleShape))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp)
                .alpha(contentAlpha.value)
                .offset(y = contentSlide.value.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // Logo
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(Brush.radialGradient(listOf(EspressoLight, EspressoMedium, EspressoDark)))
                    .border(1.5.dp, GoldBorder.copy(0.7f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.LocalCafe, null, tint = GoldBorder, modifier = Modifier.size(36.dp))
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text("COFFEE BLISS", color = Color.White, fontWeight = FontWeight.Black, fontSize = 20.sp, letterSpacing = 4.sp)
            Text("Daftar Member Baru", color = GoldBorder, fontSize = 12.sp, letterSpacing = 1.sp)

            Spacer(modifier = Modifier.height(28.dp))

            // Card
            Card(
                shape  = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.05f)),
                modifier = Modifier.fillMaxWidth().border(1.dp, GoldBorder.copy(0.2f), RoundedCornerShape(24.dp))
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text("Buat Akun", color = Color.White, fontWeight = FontWeight.Black, fontSize = 22.sp)
                    Text("Daftarkan diri dan mulai kumpulkan poin", color = MochaText, fontSize = 13.sp, modifier = Modifier.padding(top = 2.dp, bottom = 20.dp))

                    // Nama
                    OutlinedTextField(
                        value         = name,
                        onValueChange = { name = it; errorMsg = "" },
                        label         = { Text("Nama Lengkap", color = MochaText) },
                        leadingIcon   = { Icon(Icons.Default.Person, null, tint = GoldBorder.copy(0.7f), modifier = Modifier.size(20.dp)) },
                        singleLine    = true,
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words, imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                        shape    = RoundedCornerShape(14.dp),
                        colors   = inputColors(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Email
                    OutlinedTextField(
                        value         = email,
                        onValueChange = { email = it; errorMsg = "" },
                        label         = { Text("Email", color = MochaText) },
                        leadingIcon   = { Icon(Icons.Default.Email, null, tint = GoldBorder.copy(0.7f), modifier = Modifier.size(20.dp)) },
                        singleLine    = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                        shape    = RoundedCornerShape(14.dp),
                        colors   = inputColors(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Nomor HP
                    OutlinedTextField(
                        value         = phone,
                        onValueChange = { phone = it; errorMsg = "" },
                        label         = { Text("Nomor HP", color = MochaText) },
                        leadingIcon   = { Icon(Icons.Default.Phone, null, tint = GoldBorder.copy(0.7f), modifier = Modifier.size(20.dp)) },
                        singleLine    = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                        shape    = RoundedCornerShape(14.dp),
                        colors   = inputColors(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Password
                    OutlinedTextField(
                        value         = password,
                        onValueChange = { password = it; errorMsg = "" },
                        label         = { Text("Password", color = MochaText) },
                        leadingIcon   = { Icon(Icons.Default.Lock, null, tint = GoldBorder.copy(0.7f), modifier = Modifier.size(20.dp)) },
                        trailingIcon  = {
                            IconButton(onClick = { showPassword = !showPassword }) {
                                Icon(if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility, null, tint = MochaText)
                            }
                        },
                        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        singleLine    = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                        shape    = RoundedCornerShape(14.dp),
                        colors   = inputColors(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Konfirmasi password
                    OutlinedTextField(
                        value         = confirmPassword,
                        onValueChange = { confirmPassword = it; errorMsg = "" },
                        label         = { Text("Konfirmasi Password", color = MochaText) },
                        leadingIcon   = { Icon(Icons.Default.LockOpen, null, tint = GoldBorder.copy(0.7f), modifier = Modifier.size(20.dp)) },
                        trailingIcon  = {
                            IconButton(onClick = { showConfirm = !showConfirm }) {
                                Icon(if (showConfirm) Icons.Default.VisibilityOff else Icons.Default.Visibility, null, tint = MochaText)
                            }
                        },
                        visualTransformation = if (showConfirm) VisualTransformation.None else PasswordVisualTransformation(),
                        singleLine    = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                        shape    = RoundedCornerShape(14.dp),
                        colors   = inputColors(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (errorMsg.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.ErrorOutline, null, tint = Color(0xFFEF5350), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(errorMsg, color = Color(0xFFEF5350), fontSize = 13.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Daftar button
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .clip(RoundedCornerShape(26.dp))
                            .background(Brush.horizontalGradient(listOf(GoldBorder, GoldFixed, GoldBorder))),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            onClick = {
                                if (isLoading) return@Button
                                when {
                                    name.isBlank()  -> { errorMsg = "Nama tidak boleh kosong"; return@Button }
                                    email.isBlank() -> { errorMsg = "Email tidak boleh kosong"; return@Button }
                                    phone.isBlank() -> { errorMsg = "Nomor HP tidak boleh kosong"; return@Button }
                                    password.length < 6 -> { errorMsg = "Password minimal 6 karakter"; return@Button }
                                    password != confirmPassword -> { errorMsg = "Password tidak cocok"; return@Button }
                                }
                                isLoading = true
                                scope.launch {
                                    val id = viewModel.registerMember(name, email, phone, password)
                                    when (id) {
                                        -1   -> { errorMsg = "Email sudah terdaftar"; isLoading = false }
                                        -2   -> { errorMsg = "Gagal mendaftar, coba lagi"; isLoading = false }
                                        else -> {
                                            navController.navigate(Screen.MemberDetail.createRoute(id)) {
                                                popUpTo(Screen.Register.route) { inclusive = true }
                                                popUpTo(Screen.Login.route)    { inclusive = true }
                                            }
                                        }
                                    }
                                }
                            },
                            modifier  = Modifier.fillMaxSize(),
                            shape     = RoundedCornerShape(26.dp),
                            colors    = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            elevation = ButtonDefaults.buttonElevation(0.dp)
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(color = EspressoDark, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                            } else {
                                Text("DAFTAR SEKARANG", fontWeight = FontWeight.Black, fontSize = 14.sp, letterSpacing = 1.5.sp, color = EspressoDark)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Login link
            Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                Text("Sudah punya akun? ", color = MochaText, fontSize = 14.sp)
                Text(
                    text       = "Masuk",
                    color      = GoldBorder,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 14.sp,
                    modifier   = Modifier.clickable { navController.popBackStack() }
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
