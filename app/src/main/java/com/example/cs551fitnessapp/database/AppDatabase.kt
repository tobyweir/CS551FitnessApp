package com.example.cs551fitnessapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.cs551fitnessapp.database.member.MemberDao
import com.example.cs551fitnessapp.database.member.MemberEntity

@Database(
    entities = [
        MemberEntity::class,
        SessionEntity::class,
        ExerciseEntity::class,
        SessionExerciseEntity::class
    ],
    version = 2,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun memberDao(): MemberDao
    abstract fun sessionDao(): SessionDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun sessionExerciseDao(): SessionExerciseDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS sessions_new (
                        sessionId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        ownerMemberId INTEGER NOT NULL,
                        sessionName TEXT NOT NULL,
                        dtStartSession INTEGER NOT NULL,
                        dtEndSession INTEGER NOT NULL,
                        duration INTEGER NOT NULL,
                        FOREIGN KEY(ownerMemberId) REFERENCES members(memberId) ON DELETE CASCADE
                    )
                    """.trimIndent()
                )

                db.execSQL(
                    """
                    CREATE INDEX IF NOT EXISTS index_sessions_ownerMemberId
                    ON sessions_new(ownerMemberId)
                    """.trimIndent()
                )

                db.execSQL(
                    """
                    INSERT INTO sessions_new (
                        sessionId,
                        ownerMemberId,
                        sessionName,
                        dtStartSession,
                        dtEndSession,
                        duration
                    )
                    SELECT
                        sessionId,
                        ownerUserId,
                        sessionName,
                        dtStartSession,
                        dtEndSession,
                        CAST(duration AS INTEGER)
                    FROM sessions
                    WHERE EXISTS (
                        SELECT 1 FROM members
                        WHERE members.memberId = sessions.ownerUserId
                    )
                    """.trimIndent()
                )

                db.execSQL("DROP TABLE sessions")
                db.execSQL("ALTER TABLE sessions_new RENAME TO sessions")
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fitness_app_db"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}