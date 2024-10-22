package di

import androidx.room.Room
import data.convertors.FavoriteTrackDbConvertor
import data.db.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "database.db"
        ).build()
    }

    factory { FavoriteTrackDbConvertor() }

}
