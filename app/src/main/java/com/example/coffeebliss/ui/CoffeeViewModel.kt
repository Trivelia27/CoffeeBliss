package com.example.coffeebliss.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.coffeebliss.data.CoffeeRepository
import com.example.coffeebliss.data.Member
import com.example.coffeebliss.data.SessionManager
import com.example.coffeebliss.data.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CoffeeViewModel(
    private val repository: CoffeeRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    val allMembers: Flow<List<Member>> = repository.allMembers

    private val _loggedInMemberId = MutableStateFlow(sessionManager.getMemberId())
    val loggedInMemberId: StateFlow<Int> = _loggedInMemberId.asStateFlow()

    /**
     * Checks whether the stored session memberId still exists in the database.
     * If not (e.g. after a destructive DB migration), clears the session.
     * Returns the valid memberId, or -1 if no valid session.
     */
    suspend fun validateSession(): Int {
        val id = sessionManager.getMemberId()
        if (id == -1) return -1
        return if (repository.memberExists(id)) id else { logout(); -1 }
    }

    /** Returns memberId on success, -1 on wrong credentials */
    suspend fun loginMember(email: String, password: String): Int {
        val member = repository.loginMember(email, password) ?: return -1
        sessionManager.saveSession(member.id)
        _loggedInMemberId.value = member.id
        return member.id
    }

    /**
     * Registers a new member and auto-logs in.
     * Returns memberId on success, -1 if email already taken.
     */
    suspend fun registerMember(name: String, email: String, phone: String, password: String): Int {
        val normalizedEmail = email.trim().lowercase()
        if (repository.emailExists(normalizedEmail)) return -1
        repository.insertMember(
            Member(name = name.trim(), email = normalizedEmail, phone = phone.trim(), password = password)
        )
        val member = repository.loginMember(normalizedEmail, password) ?: return -2
        sessionManager.saveSession(member.id)
        _loggedInMemberId.value = member.id
        return member.id
    }

    fun logout() {
        sessionManager.logout()
        _loggedInMemberId.value = -1
    }

    fun getMember(id: Int): Flow<Member?> = repository.getMemberById(id)

    fun getTransactions(memberId: Int): Flow<List<Transaction>> =
        repository.getTransactionsForMember(memberId)

    fun addTransaction(memberId: Int, amount: Double) {
        viewModelScope.launch {
            val points = (amount / 10000).toInt()
            val date = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date())
            repository.insertTransaction(
                Transaction(memberId = memberId, amount = amount, pointEarned = points, date = date)
            )
        }
    }

    // kept for AddMemberScreen compat (not in primary flow)
    fun addMember(name: String, email: String, phone: String) {
        viewModelScope.launch {
            repository.insertMember(Member(name = name, email = email, phone = phone))
        }
    }

    fun redeemReward(memberId: Int, rewardPoints: Int, rewardName: String) {
        viewModelScope.launch {
            val date = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date())
            repository.redeemReward(memberId, rewardPoints, rewardName, date)
        }
    }
}

class CoffeeViewModelFactory(
    private val repository: CoffeeRepository,
    private val sessionManager: SessionManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CoffeeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CoffeeViewModel(repository, sessionManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
