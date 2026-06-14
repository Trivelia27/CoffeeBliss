package com.example.coffeebliss.data

import kotlinx.coroutines.flow.Flow

class CoffeeRepository(
    private val memberDao: MemberDao,
    private val transactionDao: TransactionDao
) {
    val allMembers: Flow<List<Member>> = memberDao.getAllMembers()

    fun getMemberById(id: Int): Flow<Member?> = memberDao.getMemberById(id)

    suspend fun loginMember(email: String, password: String): Member? =
        memberDao.findByCredentials(email.trim().lowercase(), password)

    suspend fun emailExists(email: String): Boolean =
        memberDao.findByEmail(email.trim().lowercase()) != null

    suspend fun memberExists(id: Int): Boolean =
        memberDao.findById(id) != null

    suspend fun insertMember(member: Member) = memberDao.insertMember(member)

    suspend fun insertTransaction(transaction: Transaction) {
        transactionDao.insertTransaction(transaction)
        memberDao.addPoints(transaction.memberId, transaction.pointEarned)
    }

    fun getTransactionsForMember(memberId: Int): Flow<List<Transaction>> =
        transactionDao.getTransactionsForMember(memberId)

    suspend fun redeemReward(memberId: Int, points: Int, rewardName: String, date: String) {
        memberDao.deductPoints(memberId, points)
        transactionDao.insertTransaction(
            Transaction(
                memberId    = memberId,
                amount      = 0.0,
                pointEarned = -points,
                date        = date,
                type        = "redeem",
                note        = rewardName
            )
        )
    }
}
