package kevin.jo.ramos.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Expression::class], version = 1, exportSchema = false)
abstract class AppDatabase() : RoomDatabase() {
    abstract fun expressionDao(): ExpressionDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null


        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
               val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "Expression"
                    ).build()

                INSTANCE = instance
                return instance
            }
        }

    }
}