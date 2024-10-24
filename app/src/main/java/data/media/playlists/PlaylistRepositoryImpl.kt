package data.media.playlists

import android.content.Context
import android.graphics.Bitmap.CompressFormat.JPEG
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment.DIRECTORY_PICTURES
import androidx.core.net.toUri
import data.convertors.PlaylistDbConvertor
import data.convertors.PlaylistTrackDbConvertor
import data.db.dao.PlaylistDao
import data.db.dao.PlaylistTracksDao
import domain.media.Playlist
import domain.player.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.FileOutputStream

class PlaylistRepositoryImpl(
    private val context: Context,
    private val playlistDao: PlaylistDao,
    private val trackDao: PlaylistTracksDao,
    private val playlistConvertor: PlaylistDbConvertor,
    private val trackConvertor: PlaylistTrackDbConvertor
) : PlaylistRepository {

    override suspend fun add(playlist: Playlist) =
        playlistDao.add(playlistConvertor.map(playlist))

    override suspend fun addPlaylistTrack(track: Track) =
        trackDao.add(trackConvertor.map(track))

    override suspend fun update(playlist: Playlist) =
        playlistDao.update(playlistConvertor.map(playlist))

    override suspend fun getById(id: Int): Playlist =
        playlistConvertor.map(playlistDao.getById(id))

    override fun getAll(): Flow<List<Playlist>> = flow {
        val playlists = playlistDao.getAll()
        emit(playlists.map { playlistConvertor.map(it) })
    }

    override fun saveImage(uri: Uri, image: String): String {
        val filePath = File(context.getExternalFilesDir(DIRECTORY_PICTURES), "playlists")
        if (!filePath.exists()) filePath.mkdirs()

        val file = File(filePath, image)
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory.decodeStream(inputStream)
            .compress(JPEG, 30, outputStream)

        return file.toUri().toString()
    }
}
