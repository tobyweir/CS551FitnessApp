package com.example.cs551fitnessapp.database.member
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MemberDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMember(member: MemberEntity): Long

    @Update
    suspend fun updateMember(member: MemberEntity)

    @Delete
    suspend fun deleteMember(member: MemberEntity)

    @Query("SELECT * FROM members ORDER BY name ASC")
    fun getAllMembers(): Flow<List<MemberEntity>>

    @Query("SELECT * FROM members WHERE memberId = :memberId")
    fun getMemberById(memberId: Long): Flow<MemberEntity?>

    @Query("SELECT * FROM members WHERE name LIKE '%' || :query || '%' ORDER BY name ASC")
    fun searchMembers(query: String): Flow<List<MemberEntity>>
}