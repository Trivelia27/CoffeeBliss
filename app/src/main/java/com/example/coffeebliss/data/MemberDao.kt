package com.example.coffeebliss.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MemberDao {
    @Query("SELECT * FROM members")
    fun getAllMembers(): Flow<List<Member>>

    @Query("SELECT * FROM members WHERE id = :id")
    fun getMemberById(id: Int): Flow<Member?>

    @Query("SELECT * FROM members WHERE email = :email AND password = :password LIMIT 1")
    suspend fun findByCredentials(email: String, password: String): Member?

    @Query("SELECT * FROM members WHERE email = :email LIMIT 1")
    suspend fun findByEmail(email: String): Member?

    @Query("SELECT * FROM members WHERE id = :id LIMIT 1")
    suspend fun findById(id: Int): Member?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMember(member: Member)

    @Update
    suspend fun updateMember(member: Member)

    @Query("UPDATE members SET points = points + :points WHERE id = :memberId")
    suspend fun addPoints(memberId: Int, points: Int)

    @Query("UPDATE members SET points = points - :points WHERE id = :memberId")
    suspend fun deductPoints(memberId: Int, points: Int)
}
