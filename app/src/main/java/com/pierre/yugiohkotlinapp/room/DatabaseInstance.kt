package com.pierre.yugiohkotlinapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [CardEntity::class], version = 1, exportSchema = false)
public abstract class DatabaseInstance : RoomDatabase() {

    abstract fun cardDao(): CardDao

    private class CardDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    var cardDao = database.cardDao()

                    // Delete all content here.
                    cardDao.deleteAll()

                    // Add sample words.
                    var card = CardEntity(
                        0,
                        "Tornado Dragon",
                        "2 Level 4 monsters\nOnce per turn (Quick Effect): You can detach 1 material from this card, then target 1 Spell/Trap on the field; destroy it.",
                        6983839,
                        "https://storage.googleapis.com/ygoprodeck.com/pics/6983839.jpg",
                        2100,
                        2000,
                        4,
                        "Wyrm",
                        "2.99$"

                    )
                    cardDao.addCard(card)
                    card = CardEntity(
                        1,
                        "Tornado Dragon",
                        "2 Level 4 monsters\nOnce per turn (Quick Effect): You can detach 1 material from this card, then target 1 Spell/Trap on the field; destroy it.",
                        6983839,
                        "https://storage.googleapis.com/ygoprodeck.com/pics/6983839.jpg",
                        2100,
                        2000,
                        4,
                        "Wyrm",
                        "2.99$"
                    )
                    cardDao.addCard(card)
                }
            }
        }
    }


    companion object {
        @Volatile
        private var INSTANCE: DatabaseInstance? = null

        fun getDatabase(
            context: Context, scope: CoroutineScope
        ): DatabaseInstance {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseInstance::class.java,
                    "yugioh"
                )
                    .addCallback(CardDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}