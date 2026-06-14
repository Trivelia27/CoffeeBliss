# ☕ Coffee Bliss — Loyalty Membership App

Aplikasi Android loyalty membership untuk pelanggan kedai kopi. Kumpulkan poin dari setiap transaksi pembelian, tukar dengan reward menarik, dan pantau riwayat aktivitasmu, semua dalam satu app.

> **EAS — Pemrograman Mobile**  
> Triana Velia Hutabalian (5025231190) · Rafaela Shyra Ashma' Ramadhani (5025231217)

---

## ✨ Fitur

| Fitur | Deskripsi |
|---|---|
| 📝 Registrasi & Login | Daftar akun dengan email + password, sesi tersimpan otomatis |
| 💳 Kartu Member Digital | Kartu member dengan nama, ID, dan total poin |
| ⭐ Poin & Level | Rp 10.000 = 1 poin · Silver → Gold → Platinum |
| 🛒 Tambah Transaksi | Input nominal, poin langsung dihitung real-time |
| 🎁 Tukar Reward | Redeem poin: Free Coffee, Diskon 20%, Free Pastry |
| 📋 Riwayat Transaksi | Histori pembelian & redeem, tap untuk detail lengkap |
| 🔔 Notifikasi | Bell icon dengan pesan kontekstual sesuai level member |
| 👤 Profil | Statistik poin & transaksi, status level, logout |

---

## 🛠️ Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose + Material3
- **Architecture:** MVVM (ViewModel + Repository)
- **Database:** Room (SQLite)
- **Navigation:** Navigation Compose
- **State:** StateFlow + Coroutines
- **Session:** SharedPreferences

---

## 🚀 Cara Menjalankan

1. Clone repo ini
   ```bash
   git clone https://github.com/Trivelia27/CoffeeBliss.git
   ```
2. Buka di **Android Studio**
3. Tunggu Gradle sync selesai
4. Jalankan di emulator / device (min SDK 24)

---

## 📁 Struktur Project

```
app/src/main/java/com/example/coffeebliss/
├── data/
│   ├── AppDatabase.kt       # Room database
│   ├── Member.kt            # Entity member
│   ├── Transaction.kt       # Entity transaksi
│   ├── MemberDao.kt         # DAO queries member
│   ├── TransactionDao.kt    # DAO queries transaksi
│   ├── CoffeeRepository.kt  # Repository
│   └── SessionManager.kt    # SharedPreferences session
├── ui/
│   ├── CoffeeViewModel.kt   # ViewModel utama
│   ├── Screen.kt            # Route navigasi
│   └── screens/
│       ├── SplashScreen.kt
│       ├── LoginScreen.kt
│       ├── RegisterScreen.kt
│       ├── MemberDetailScreen.kt
│       ├── MemberCardScreen.kt
│       ├── TransactionScreen.kt
│       ├── RewardScreen.kt
│       ├── HistoryScreen.kt
│       └── ProfileScreen.kt
├── CoffeeApplication.kt
└── MainActivity.kt
```

---


## 📄 Lisensi

Project ini dibuat untuk keperluan akademik EAS Pemorgraman Perangkat Bergerak.
