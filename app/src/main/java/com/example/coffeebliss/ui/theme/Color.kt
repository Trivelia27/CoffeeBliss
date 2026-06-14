package com.example.coffeebliss.ui.theme

import androidx.compose.ui.graphics.Color

// ─── Espresso / Deep Coffee ────────────────────────────────────────────────
val EspressoDark       = Color(0xFF25160E)  // primary – deepest espresso
val EspressoMedium     = Color(0xFF3C2A21)  // primary-container
val EspressoLight      = Color(0xFF5A3E32)  // lighter espresso tint

// ─── Gold Accent ───────────────────────────────────────────────────────────
val GoldBorder         = Color(0xFFE9C349)  // gold border / secondary-fixed-dim
val GoldContainer      = Color(0xFFFED65B)  // secondary-container / active pill
val GoldFixed          = Color(0xFFFFE088)  // secondary-fixed / light badge
val GoldCoffee         = Color(0xFF735C00)  // secondary – muted gold text
val GoldDark           = Color(0xFFA08200)  // deeper gold label

// ─── Surface / Cream ───────────────────────────────────────────────────────
val MochaCream         = Color(0xFFFBF9F7)  // surface / background
val MochaContainer     = Color(0xFFF2EDEA)  // surface-container-low
val MochaContainerHigh = Color(0xFFEAE4E1)  // surface-container
val MochaDivider       = Color(0xFFD9D3D0)  // outline-variant

// ─── Text / Mocha ──────────────────────────────────────────────────────────
val MochaText          = Color(0xFFAA9084)  // on-primary-container (dim)
val MochaBrown         = Color(0xFF81756F)  // outline
val MochaDark          = Color(0xFF1C1816)  // on-surface
val MochaLabel         = Color(0xFF6B5C56)  // label text

// ─── Status ────────────────────────────────────────────────────────────────
val SuccessGreen       = Color(0xFF2E7D32)
val ErrorRed           = Color(0xFFB00020)

// ─── Member Level Colors ───────────────────────────────────────────────────
val SilverColor        = Color(0xFF90A4AE)  // silver member
val GoldColor          = Color(0xFFE9C349)  // gold member  (same as GoldBorder)
val PlatinumColor      = Color(0xFF90CAF9)  // platinum member

// Legacy aliases (so old references compile while we update gradually)
val CoffeeGreen        = EspressoDark
val CoffeeGreenLight   = EspressoMedium
val CoffeeGreenContainer = MochaContainer
val CoffeeBrown        = EspressoLight
val CoffeeLightBrown   = MochaBrown
val CoffeeCream        = MochaCream
val CoffeeCreamDark    = MochaContainer
val CoffeeGold         = GoldBorder
val CoffeeSilver       = SilverColor
val CoffeePlatinum     = PlatinumColor
val CoffeeSuccess      = SuccessGreen
val CoffeeError        = ErrorRed
val CoffeeOnGreen      = Color(0xFFFFFFFF)
