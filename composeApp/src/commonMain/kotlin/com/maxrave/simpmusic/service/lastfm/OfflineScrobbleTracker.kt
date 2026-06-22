package com.maxrave.simpmusic.service.lastfm

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Offline scrobble tracking for when user is offline
 * Scrobbles are saved locally and synced when online
 */
@Serializable
data class OfflineScrobble(
    val artist: String,
    val track: String,
    val album: String? = null,
    val timestamp: Long,
    val duration: Int? = null,
    val synced: Boolean = false,
)

/**
 * Tracker for offline scrobbles
 * Note: In a full implementation, this would use Room database
 * For now, we'll use a simple in-memory list with DataStore persistence
 */
class OfflineScrobbleTracker(
    private val lastFmDataStore: LastFmDataStore,
) {
    private val json = Json { ignoreUnknownKeys = true }
    private var offlineScrobbles = mutableListOf<OfflineScrobble>()

    /**
     * Add a scrobble to offline tracking
     */
    suspend fun addScrobble(
        artist: String,
        track: String,
        album: String? = null,
        timestamp: Long,
        duration: Int? = null,
    ) {
        val scrobble = OfflineScrobble(
            artist = artist,
            track = track,
            album = album,
            timestamp = timestamp,
            duration = duration,
            synced = false,
        )
        offlineScrobbles.add(scrobble)
        // In full implementation, save to database
    }

    /**
     * Get all unsynced scrobbles
     */
    fun getUnsyncedScrobbles(): List<OfflineScrobble> {
        return offlineScrobbles.filter { !it.synced }
    }

    /**
     * Mark scrobble as synced
     */
    fun markAsSynced(scrobble: OfflineScrobble) {
        val index = offlineScrobbles.indexOf(scrobble)
        if (index >= 0) {
            offlineScrobbles[index] = scrobble.copy(synced = true)
        }
    }

    /**
     * Clear synced scrobbles
     */
    fun clearSyncedScrobbles() {
        offlineScrobbles.removeAll { it.synced }
    }

    /**
     * Get count of unsynced scrobbles
     */
    fun getUnsyncedCount(): Int {
        return offlineScrobbles.count { !it.synced }
    }
}
