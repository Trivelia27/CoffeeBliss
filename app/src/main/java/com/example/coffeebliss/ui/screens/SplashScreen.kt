package com.example.coffeebliss.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.coffeebliss.ui.CoffeeViewModel
import com.example.coffeebliss.ui.Screen
import com.example.coffeebliss.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController, viewModel: CoffeeViewModel) {
    // ── Animation states ──────────────────────────────────────────────────
    val logoAlpha   = remember { Animatable(0f) }
    val logoScale   = remember { Animatable(0.5f) }
    val ringScale   = remember { Animatable(0.6f) }
    val textAlpha   = remember { Animatable(0f) }
    val textSlide   = remember { Animatable(30f) }
    val tagAlpha    = remember { Animatable(0f) }
    val btnAlpha    = remember { Animatable(0f) }
    val btnSlide    = remember { Animatable(40f) }
    val pulseAnim   = rememberInfiniteTransition(label = "pulse")
    val pulseScale  = pulseAnim.animateFloat(
        initialValue   = 1f,
        targetValue    = 1.08f,
        animationSpec  = infiniteRepeatable(
            animation  = tween(1800, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ringPulse"
    )

    LaunchedEffect(Unit) {
        delay(200)
        // Logo appears
        logoScale.animateTo(1f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium))
        logoAlpha.animateTo(1f, animationSpec = tween(400))
        ringScale.animateTo(1f, animationSpec = tween(500, easing = EaseOutCubic))
        delay(100)
        // Text slides up
        textAlpha.animateTo(1f, animationSpec = tween(500))
        textSlide.animateTo(0f, animationSpec = tween(500, easing = EaseOutCubic))
        delay(150)
        tagAlpha.animateTo(1f, animationSpec = tween(400))
        delay(300)
        // Tagline appears then auto-navigate
        btnAlpha.animateTo(1f, animationSpec = tween(400))
        btnSlide.animateTo(0f, animationSpec = tween(400, easing = EaseOutCubic))
        delay(600)
        // Validate session against DB (handles stale sessions after DB wipe)
        val validId = viewModel.validateSession()
        val destination = if (validId != -1)
            Screen.MemberDetail.createRoute(validId)
        else
            Screen.Login.route
        navController.navigate(destination) {
            popUpTo(Screen.Splash.route) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors  = listOf(Color(0xFF3C2A21), Color(0xFF1A0E0A)),
                    radius  = 1200f
                )
            )
    ) {
        // Ambient glow blobs
        Box(
            modifier = Modifier
                .size(300.dp)
                .offset(x = (-60).dp, y = (-80).dp)
                .blur(80.dp)
                .background(GoldBorder.copy(alpha = 0.07f), CircleShape)
        )
        Box(
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 60.dp, y = 60.dp)
                .blur(80.dp)
                .background(EspressoLight.copy(alpha = 0.3f), CircleShape)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 36.dp)
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // ── Outer pulse ring ──────────────────────────────────────────
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .scale(ringScale.value * pulseScale.value)
                    .alpha(logoAlpha.value)
                    .border(
                        1.dp,
                        Brush.sweepGradient(
                            listOf(GoldBorder.copy(0f), GoldBorder.copy(0.4f), GoldBorder.copy(0f))
                        ),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                // ── Inner logo circle ─────────────────────────────────────
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .scale(logoScale.value)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(EspressoLight, EspressoMedium, EspressoDark)
                            )
                        )
                        .border(1.5.dp, GoldBorder.copy(0.7f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector        = Icons.Default.LocalCafe,
                        contentDescription = null,
                        modifier           = Modifier.size(70.dp),
                        tint               = GoldBorder
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // ── Brand name ────────────────────────────────────────────────
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .alpha(textAlpha.value)
                    .offset(y = textSlide.value.dp)
            ) {
                Text(
                    text          = "COFFEE",
                    color         = Color.White,
                    fontWeight    = FontWeight.Black,
                    fontSize      = 38.sp,
                    letterSpacing = 10.sp,
                    lineHeight    = 40.sp
                )
                Text(
                    text          = "BLISS",
                    color         = GoldBorder,
                    fontWeight    = FontWeight.Black,
                    fontSize      = 38.sp,
                    letterSpacing = 10.sp,
                    lineHeight    = 40.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                // Thin gold line divider
                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(1.5.dp)
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color.Transparent, GoldBorder, Color.Transparent)
                            )
                        )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Tagline ───────────────────────────────────────────────────
            Text(
                text      = "Loyalty Membership",
                color     = MochaText,
                fontSize  = 14.sp,
                letterSpacing = 2.sp,
                textAlign = TextAlign.Center,
                modifier  = Modifier.alpha(tagAlpha.value)
            )

            Spacer(modifier = Modifier.weight(1f))

            // ── Loading indicator ─────────────────────────────────────────
            Column(
                modifier = Modifier
                    .alpha(btnAlpha.value)
                    .offset(y = btnSlide.value.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    color        = GoldBorder,
                    modifier     = Modifier.size(28.dp),
                    strokeWidth  = 2.dp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text          = "Kumpulkan poin • Nikmati reward",
                    color         = MochaText.copy(alpha = 0.7f),
                    fontSize      = 12.sp,
                    textAlign     = TextAlign.Center,
                    letterSpacing = 0.5.sp
                )

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}
