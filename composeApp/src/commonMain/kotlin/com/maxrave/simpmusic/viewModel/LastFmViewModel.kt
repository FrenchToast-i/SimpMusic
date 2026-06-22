package com.maxrave.simpmusic.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxrave.simpmusic.service.lastfm.LastFmDataStore
import com.maxrave.simpmusic.service.lastfm.LastFmService
import com.maxrave.simpmusic.service.lastfm.LastFmSession
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * ViewModel for Last.fm integration
 * Handles authentication, scrobbling, and retrieving user data
 */
class LastFmViewModel(
    private val lastFmDataStore: LastFmDataStore,
) : ViewModel() {
    // Last.fm API credentials (these should be stored securely in production)
    private val apiKey = "YOUR_LAST_FM_API_KEY" // Replace with actual API key
    private val apiSecret = "YOUR_LAST_FM_API_SECRET" // Replace with actual API secret
    
    private val httpClient = HttpClient()
    private val lastFmService = LastFmService(httpClient, apiKey, apiSecret)

    private val _uiState = MutableStateFlow<LastFmUiState>(LastFmUiState.Idle)
    val uiState: StateFlow<LastFmUiState> = _uiState.asStateFlow()

    val isLoggedIn = lastFmDataStore.isLoggedIn
    val userName = lastFmDataStore.userName

    init {
        viewModelScope.launch {
            lastFmDataStore.isLoggedIn.collect { loggedIn ->
                if (loggedIn) {
                    _uiState.value = LastFmUiState.LoggedIn
                } else {
                    _uiState.value = LastFmUiState.Idle
                }
            }
        }
    }

    /**
     * Get authentication URL for OAuth flow
     */
    fun getAuthUrl(callbackUrl: String): String {
        return lastFmService.getAuthUrl(callbackUrl)
    }

    /**
     * Authenticate with Last.fm using token
     */
    fun authenticate(token: String) {
        viewModelScope.launch {
            _uiState.value = LastFmUiState.Loading
            lastFmService.getSession(token).fold(
                onSuccess = { session ->
                    lastFmDataStore.saveSession(session.key, session.name)
                    _uiState.value = LastFmUiState.LoggedIn
                },
                onFailure = { error ->
                    _uiState.value = LastFmUiState.Error(error.message ?: "Authentication failed")
                },
            )
        }
    }

    /**
     * Logout from Last.fm
     */
    fun logout() {
        viewModelScope.launch {
            lastFmDataStore.clearCredentials()
            _uiState.value = LastFmUiState.Idle
        }
    }

    /**
     * Scrobble a track
     */
    fun scrobble(
        artist: String,
        track: String,
        album: String? = null,
        timestamp: Long,
        duration: Int? = null,
    ) {
        viewModelScope.launch {
            val sessionKey = lastFmDataStore.sessionKey.first()
            if (!sessionKey.isNullOrEmpty()) {
                lastFmService.scrobble(
                    sessionKey = sessionKey,
                    artist = artist,
                    track = track,
                    album = album,
                    timestamp = timestamp,
                    duration = duration,
                )
            }
        }
    }

    /**
     * Update now playing
     */
    fun updateNowPlaying(
        artist: String,
        track: String,
        album: String? = null,
        duration: Int? = null,
    ) {
        viewModelScope.launch {
            val sessionKey = lastFmDataStore.sessionKey.first()
            if (!sessionKey.isNullOrEmpty()) {
                lastFmService.updateNowPlaying(
                    sessionKey = sessionKey,
                    artist = artist,
                    track = track,
                    album = album,
                    duration = duration,
                )
            }
        }
    }

    /**
     * Get user's top artists
     */
    fun getTopArtists(limit: Int = 50, period: String = "overall") {
        viewModelScope.launch {
            val userName = lastFmDataStore.userName.first()
            if (!userName.isNullOrEmpty()) {
                _uiState.value = LastFmUiState.Loading
                lastFmService.getTopArtists(userName, limit, period).fold(
                    onSuccess = { artists ->
                        _uiState.value = LastFmUiState.TopArtistsLoaded(artists)
                    },
                    onFailure = { error ->
                        _uiState.value = LastFmUiState.Error(error.message ?: "Failed to load top artists")
                    },
                )
            }
        }
    }

    /**
     * Get user's top tracks
     */
    fun getTopTracks(limit: Int = 50, period: String = "overall") {
        viewModelScope.launch {
            val userName = lastFmDataStore.userName.first()
            if (!userName.isNullOrEmpty()) {
                _uiState.value = LastFmUiState.Loading
                lastFmService.getTopTracks(userName, limit, period).fold(
                    onSuccess = { tracks ->
                        _uiState.value = LastFmUiState.TopTracksLoaded(tracks)
                    },
                    onFailure = { error ->
                        _uiState.value = LastFmUiState.Error(error.message ?: "Failed to load top tracks")
                    },
                )
            }
        }
    }

    /**
     * Get similar tracks for recommendations
     */
    fun getSimilarTracks(artist: String, track: String? = null, limit: Int = 20) {
        viewModelScope.launch {
            _uiState.value = LastFmUiState.Loading
            lastFmService.getSimilarTracks(artist, track, limit).fold(
                onSuccess = { tracks ->
                    _uiState.value = LastFmUiState.SimilarTracksLoaded(tracks)
                },
                onFailure = { error ->
                    _uiState.value = LastFmUiState.Error(error.message ?: "Failed to load similar tracks")
                },
            )
        }
    }
}

sealed class LastFmUiState {
    object Idle : LastFmUiState()
    object Loading : LastFmUiState()
    object LoggedIn : LastFmUiState()
    data class Error(val message: String) : LastFmUiState()
    data class TopArtistsLoaded(val artists: List<com.maxrave.simpmusic.service.lastfm.LastFmArtist>) : LastFmUiState()
    data class TopTracksLoaded(val tracks: List<com.maxrave.simpmusic.service.lastfm.LastFmTrack>) : LastFmUiState()
    data class SimilarTracksLoaded(val tracks: List<com.maxrave.simpmusic.service.lastfm.LastFmTrack>) : LastFmUiState()
}
