package com.example.cs551fitnessapp.repository

import com.example.cs551fitnessapp.database.member.MemberDao
import com.example.cs551fitnessapp.database.member.MemberEntity
import kotlinx.coroutines.flow.Flow

class MemberRepository(
    private val memberDao: MemberDao
) {
    fun getAllMembers(): Flow<List<MemberEntity>> = memberDao.getAllMembers()

    fun getMemberById(memberId: Long): Flow<MemberEntity?> =
        memberDao.getMemberById(memberId)

    fun searchMembers(query: String): Flow<List<MemberEntity>> =
        memberDao.searchMembers(query)

    suspend fun addMember(member: MemberEntity): Long =
        memberDao.insertMember(member)

    suspend fun updateMember(member: MemberEntity) =
        memberDao.updateMember(member)

    suspend fun deleteMember(member: MemberEntity) =
        memberDao.deleteMember(member)
}