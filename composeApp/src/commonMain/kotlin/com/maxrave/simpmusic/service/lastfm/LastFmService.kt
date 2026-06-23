package com.maxrave.simpmusic.service.lastfm

import com.maxrave.domain.data.model.lastfm.LastFmArtist
import com.maxrave.domain.data.model.lastfm.LastFmAuthResponse
import com.maxrave.domain.data.model.lastfm.LastFmResponse
import com.maxrave.domain.data.model.lastfm.LastFmSession
import com.maxrave.domain.data.model.lastfm.LastFmSessionResponse
import com.maxrave.domain.data.model.lastfm.LastFmSimilarTracksResponse
import com.maxrave.domain.data.model.lastfm.LastFmTopArtistsResponse
import com.maxrave.domain.data.model.lastfm.LastFmTopTracksResponse
import com.maxrave.domain.data.model.lastfm.LastFmTrack
import com.maxrave.logger.Logger
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.parameters
import kotlinx.serialization.json.Json
import java.security.MessageDigest

class LastFmService(
    private val httpClient: HttpClient,
    private val apiKey: String,
    private val apiSecret: String,
) {
    companion object {
        private const val BASE_URL = "https://ws.audioscrobbler.com/2.0/"
        private const val AUTH_URL = "https://www.last.fm/api/auth/"
        
        private const val METHOD_AUTH_GET_TOKEN = "auth.gettoken"
        private const val METHOD_AUTH_GET_SESSION = "auth.getsession"
        private const val METHOD_TRACK_SCROBBLE = "track.scrobble"
        private const val METHOD_TRACK_UPDATE_NOW_PLAYING = "track.updateNowPlaying"
        private const val METHOD_USER_GET_TOP_ARTISTS = "user.gettopartists"
        private const val METHOD_USER_GET_TOP_TRACKS = "user.gettoptracks"
        private const val METHOD_TRACK_GET_SIMILAR = "track.getSimilar"
    }
    
    private val json = Json {
        ignoreUnknownKeys = true
    }
    
    private var sessionKey: String? = null
    private var username: String? = null
    
    fun setSession(sessionKey: String, username: String) {
        this.sessionKey = sessionKey
        this.username = username
        Logger.d("LastFmService", "Session set for user: $username")
    }
    
    fun clearSession() {
        sessionKey = null
        username = null
        Logger.d("LastFmService", "Session cleared")
    }
    
    fun isLoggedIn(): Boolean = sessionKey != null
    
    suspend fun authenticate(): Result<String> {
        return try {
            val sig = generateApiSignature(mapOf(
                "api_key" to apiKey,
                "method" to METHOD_AUTH_GET_TOKEN
            ))
            
            val response = httpClient.get(BASE_URL) {
                parameter("method", METHOD_AUTH_GET_TOKEN)
                parameter("api_key", apiKey)
                parameter("api_sig", sig)
                parameter("format", "json")
            }
            
            val authResponse = response.body<LastFmAuthResponse>()
            if (authResponse.token.isNotEmpty()) {
                Result.success(authResponse.token)
            } else {
                Result.failure(Exception("Failed to get auth token"))
            }
        } catch (e: Exception) {
            Logger.e("LastFmService", "Authentication failed", e)
            Result.failure(e)
        }
    }
    
    suspend fun getSession(token: String): Result<LastFmSession> {
        return try {
            val params = mapOf(
                "api_key" to apiKey,
                "method" to METHOD_AUTH_GET_SESSION,
                "token" to token
            )
            val sig = generateApiSignature(params)
            
            val response = httpClient.get(BASE_URL) {
                parameter("method", METHOD_AUTH_GET_SESSION)
                parameter("api_key", apiKey)
                parameter("token", token)
                parameter("api_sig", sig)
                parameter("format", "json")
            }
            
            val sessionResponse = response.body<LastFmSessionResponse>()
            val session = sessionResponse.session
            
            if (session != null && session.key.isNotEmpty()) {
                setSession(session.key, session.name)
                Result.success(session)
            } else {
                Result.failure(Exception("Failed to get session"))
            }
        } catch (e: Exception) {
            Logger.e("LastFmService", "Get session failed", e)
            Result.failure(e)
        }
    }
    
    suspend fun scrobble(
        artist: String,
        track: String,
        album: String? = null,
        timestamp: Long = System.currentTimeMillis() / 1000,
        duration: Int? = null,
    ): Result<Unit> {
        val currentSession = sessionKey
        if (currentSession == null) {
            return Result.failure(Exception("Not logged in"))
        }
        
        return try {
            val params = mutableMapOf(
                "api_key" to apiKey,
                "method" to METHOD_TRACK_SCROBBLE,
                "artist" to artist,
                "track" to track,
                "timestamp" to timestamp.toString(),
                "sk" to currentSession,
            )
            
            album?.let { params["album"] = it }
            duration?.let { params["duration"] = it.toString() }
            
            val sig = generateApiSignature(params)
            
            val response = httpClient.post(BASE_URL) {
                setBody(parameters {
                    append("method", METHOD_TRACK_SCROBBLE)
                    append("artist", artist)
                    append("track", track)
                    append("timestamp", timestamp.toString())
                    append("api_key", apiKey)
                    append("api_sig", sig)
                    append("sk", currentSession)
                    append("format", "json")
                    album?.let { append("album", it) }
                    duration?.let { append("duration", it.toString()) }
                })
            }
            
            val errorResponse = response.body<LastFmResponse>()
            if (errorResponse.error != null) {
                Result.failure(Exception(errorResponse.message))
            } else {
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Logger.e("LastFmService", "Scrobble failed", e)
            Result.failure(e)
        }
    }
    
    suspend fun updateNowPlaying(
        artist: String,
        track: String,
        album: String? = null,
        duration: Int? = null,
    ): Result<Unit> {
        val currentSession = sessionKey
        if (currentSession == null) {
            return Result.failure(Exception("Not logged in"))
        }
        
        return try {
            val params = mutableMapOf(
                "api_key" to apiKey,
                "method" to METHOD_TRACK_UPDATE_NOW_PLAYING,
                "artist" to artist,
                "track" to track,
                "sk" to currentSession,
            )
            
            album?.let { params["album"] = it }
            duration?.let { params["duration"] = it.toString() }
            
            val sig = generateApiSignature(params)
            
            val response = httpClient.post(BASE_URL) {
                setBody(parameters {
                    append("method", METHOD_TRACK_UPDATE_NOW_PLAYING)
                    append("artist", artist)
                    append("track", track)
                    append("api_key", apiKey)
                    append("api_sig", sig)
                    append("sk", currentSession)
                    append("format", "json")
                    album?.let { append("album", it) }
                    duration?.let { append("duration", it.toString()) }
                })
            }
            
            val errorResponse = response.body<LastFmResponse>()
            if (errorResponse.error != null) {
                Result.failure(Exception(errorResponse.message))
            } else {
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Logger.e("LastFmService", "Update now playing failed", e)
            Result.failure(e)
        }
    }
    
    suspend fun getTopArtists(
        user: String? = null,
        limit: Int = 50,
        period: String = "overall",
    ): Result<List<LastFmArtist>> {
        val targetUser = user ?: username
        if (targetUser == null) {
            return Result.failure(Exception("No user specified"))
        }
        
        return try {
            val response = httpClient.get(BASE_URL) {
                parameter("method", METHOD_USER_GET_TOP_ARTISTS)
                parameter("user", targetUser)
                parameter("limit", limit)
                parameter("period", period)
                parameter("api_key", apiKey)
                parameter("format", "json")
            }
            
            val topArtistsResponse = response.body<LastFmTopArtistsResponse>()
            val artists = topArtistsResponse.topArtists?.artists ?: emptyList()
            Result.success(artists)
        } catch (e: Exception) {
            Logger.e("LastFmService", "Get top artists failed", e)
            Result.failure(e)
        }
    }
    
    suspend fun getTopTracks(
        user: String? = null,
        limit: Int = 50,
        period: String = "overall",
    ): Result<List<LastFmTrack>> {
        val targetUser = user ?: username
        if (targetUser == null) {
            return Result.failure(Exception("No user specified"))
        }
        
        return try {
            val response = httpClient.get(BASE_URL) {
                parameter("method", METHOD_USER_GET_TOP_TRACKS)
                parameter("user", targetUser)
                parameter("limit", limit)
                parameter("period", period)
                parameter("api_key", apiKey)
                parameter("format", "json")
            }
            
            val topTracksResponse = response.body<LastFmTopTracksResponse>()
            val tracks = topTracksResponse.topTracks?.tracks ?: emptyList()
            Result.success(tracks)
        } catch (e: Exception) {
            Logger.e("LastFmService", "Get top tracks failed", e)
            Result.failure(e)
        }
    }
    
    suspend fun getSimilarTracks(
        artist: String,
        track: String,
        limit: Int = 50,
    ): Result<List<LastFmTrack>> {
        return try {
            val response = httpClient.get(BASE_URL) {
                parameter("method", METHOD_TRACK_GET_SIMILAR)
                parameter("artist", artist)
                parameter("track", track)
                parameter("limit", limit)
                parameter("api_key", apiKey)
                parameter("format", "json")
            }
            
            val similarTracksResponse = response.body<LastFmSimilarTracksResponse>()
            val tracks = similarTracksResponse.similarTracks?.tracks ?: emptyList()
            Result.success(tracks)
        } catch (e: Exception) {
            Logger.e("LastFmService", "Get similar tracks failed", e)
            Result.failure(e)
        }
    }
    
    fun getAuthUrl(token: String): String {
        return "$AUTH_URL?api_key=$apiKey&token=$token"
    }
    
    private fun generateApiSignature(params: Map<String, String>): String {
        val sortedParams = params.toSortedMap()
        val signatureString = sortedParams.entries.joinToString("") { "${it.key}${it.value}" } + apiSecret
        
        val md = MessageDigest.getInstance("MD5")
        val digest = md.digest(signatureString.toByteArray())
        return digest.joinToString("") { "%02x".format(it) }
    }
}
