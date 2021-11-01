package kevin.jo.ramos.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Expression::class], version = 1)
abstract class AppDatabase() : RoomDatabase() {
    abstract fun expressionDao(): ExpressionDAO
}