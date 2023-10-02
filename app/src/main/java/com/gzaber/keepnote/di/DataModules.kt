package com.gzaber.keepnote.di

import android.content.Context
import androidx.room.Room
import com.gzaber.keepnote.data.repository.DefaultFoldersRepository
import com.gzaber.keepnote.data.repository.DefaultNotesRepository
import com.gzaber.keepnote.data.repository.FoldersRepository
import com.gzaber.keepnote.data.repository.NotesRepository
import com.gzaber.keepnote.data.source.FoldersDataSource
import com.gzaber.keepnote.data.source.NotesDataSource
import com.gzaber.keepnote.data.source.RoomFoldersDataSource
import com.gzaber.keepnote.data.source.RoomNotesDataSource
import com.gzaber.keepnote.data.source.room.AppDatabase
import com.gzaber.keepnote.data.source.room.FolderDao
import com.gzaber.keepnote.data.source.room.NoteDao
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindFoldersRepository(repository: DefaultFoldersRepository): FoldersRepository

    @Singleton
    @Binds
    abstract fun bindNotesRepository(repository: DefaultNotesRepository): NotesRepository
}

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Singleton
    @Binds
    abstract fun bindFoldersDataSource(dataSource: RoomFoldersDataSource): FoldersDataSource

    @Singleton
    @Binds
    abstract fun bindNotesDataSource(dataSource: RoomNotesDataSource): NotesDataSource
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app-database"
        ).build()
    }

    @Singleton
    @Provides
    fun provideFolderDao(database: AppDatabase): FolderDao = database.folderDao()

    @Singleton
    @Provides
    fun provideNoteDao(database: AppDatabase): NoteDao = database.noteDao()
}