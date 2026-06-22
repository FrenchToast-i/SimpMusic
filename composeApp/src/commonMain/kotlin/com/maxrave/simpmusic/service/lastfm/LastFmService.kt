package com.maxrave.simpmusic.service.lastfm

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * Last.fm API Service
 * Handles authentication, scrobbling, and retrieving user data
 */
class LastFmService(
    private val httpClient: HttpClient,
    private val apiKey: String,
    private val apiSecret: String,
) {
    companion object {
        private const val BASE_URL = "https://ws.audioscrobbler.com/2.0/"
        private const val AUTH_URL = "https://www.last.fm/api/auth/"
        private val json = Json { ignoreUnknownKeys = true }
    }

    /**
     * Get authentication URL for OAuth flow
     */
    fun getAuthUrl(callbackUrl: String): String {
        return "$AUTH_URL?api_key=$apiKey&cb=$callbackUrl"
    }

    /**
     * Get session token from authentication token
     */
    suspend fun getSession(token: String): Result<LastFmSession> {
        return try {
            val response = httpClient.get(BASE_URL) {
                parameter("method", "auth.getSession")
                parameter("api_key", apiKey)
                parameter("token", token)
                parameter("format", "json")
                parameter("api_sig", generateSignature("auth.getSession", mapOf("token" to token)))
            }
            
            val responseBody = response.bodyAsText()
            val jsonResponse = json.parseToJsonElement(responseBody)
            val sessionJson = jsonResponse.jsonObject["session"]?.jsonObject
            
            if (sessionJson != null) {
                Result.success(
                    LastFmSession(
                        key = sessionJson["key"]?.jsonPrimitive?.content ?: "",
                        name = sessionJson["name"]?.jsonPrimitive?.content ?: "",
                    ),
                )
            } else {
                val error = jsonResponse.jsonObject["error"]?.jsonPrimitive?.content?.toIntOrNull() ?: -1
                val message = jsonResponse.jsonObject["message"]?.jsonPrimitive?.content ?: "Unknown error"
                Result.failure(LastFmException(error, message))
            }
        } catch (e: Exception) {
            Result.failure(LastFmException(-1, e.message ?: "Network error"))
        }
    }

    /**
     * Scrobble a track
     */
    suspend fun scrobble(
        sessionKey: String,
        artist: String,
        track: String,
        album: String? = null,
        timestamp: Long,
        duration: Int? = null,
    ): Result<Boolean> {
        return try {
            val params = mutableMapOf(
                "artist" to artist,
                "track" to track,
                "timestamp" to timestamp.toString(),
            )
            album?.let { params["album"] = it }
            duration?.let { params["duration"] = it.toString() }
            
            val response = httpClient.get(BASE_URL) {
                parameter("method", "track.scrobble")
                parameter("api_key", apiKey)
                parameter("sk", sessionKey)
                params.forEach { (key, value) ->
                    parameter(key, value)
                }
                parameter("format", "json")
                parameter("api_sig", generateSignature("track.scrobble", params + mapOf("sk" to sessionKey)))
            }
            
            val responseBody = response.bodyAsText()
            val jsonResponse = json.parseToJsonElement(responseBody)
            val scrobbles = jsonResponse.jsonObject["scrobbles"]?.jsonObject
            val accepted = scrobbles?.get("@attr")?.jsonObject?.get("accepted")?.jsonPrimitive?.content?.toIntOrNull() ?: 0
            
            Result.success(accepted > 0)
        } catch (e: Exception) {
            Result.failure(LastFmException(-1, e.message ?: "Network error"))
        }
    }

    /**
     * Update now playing
     */
    suspend fun updateNowPlaying(
        sessionKey: String,
        artist: String,
        track: String,
        album: String? = null,
        duration: Int? = null,
    ): Result<Boolean> {
        return try {
            val params = mutableMapOf(
                "artist" to artist,
                "track" to track,
            )
            album?.let { params["album"] = it }
            duration?.let { params["duration"] = it.toString() }
            
            val response = httpClient.get(BASE_URL) {
                parameter("method", "track.updateNowPlaying")
                parameter("api_key", apiKey)
                parameter("sk", sessionKey)
                params.forEach { (key, value) ->
                    parameter(key, value)
                }
                parameter("format", "json")
                parameter("api_sig", generateSignature("track.updateNowPlaying", params + mapOf("sk" to sessionKey)))
            }
            
            val responseBody = response.bodyAsText()
            val jsonResponse = json.parseToJsonElement(responseBody)
            val nowPlaying = jsonResponse.jsonObject["nowplaying"]?.jsonObject
            
            Result.success(nowPlaying != null)
        } catch (e: Exception) {
            Result.failure(LastFmException(-1, e.message ?: "Network error"))
        }
    }

    /**
     * Get user's top artists
     */
    suspend fun getTopArtists(
        user: String,
        limit: Int = 50,
        period: String = "overall",
    ): Result<List<LastFmArtist>> {
        return try {
            val response = httpClient.get(BASE_URL) {
                parameter("method", "user.gettopartists")
                parameter("user", user)
                parameter("limit", limit)
                parameter("period", period)
                parameter("api_key", apiKey)
                parameter("format", "json")
            }
            
            val responseBody = response.bodyAsText()
            val jsonResponse = json.parseToJsonElement(responseBody)
            val topartists = jsonResponse.jsonObject["topartists"]?.jsonObject
            val artistsArray = topartists?.get("artist")?.jsonArray
            
            val artists = artistsArray?.mapNotNull { element ->
                val artistObj = element.jsonObject
                LastFmArtist(
                    name = artistObj["name"]?.jsonPrimitive?.content ?: "",
                    mbid = artistObj["mbid"]?.jsonPrimitive?.content ?: "",
                    playCount = artistObj["playcount"]?.jsonPrimitive?.content?.toIntOrNull() ?: 0,
                    url = artistObj["url"]?.jsonPrimitive?.content ?: "",
                )
            } ?: emptyList()
            
            Result.success(artists)
        } catch (e: Exception) {
            Result.failure(LastFmException(-1, e.message ?: "Network error"))
        }
    }

    /**
     * Get user's top tracks
     */
    suspend fun getTopTracks(
        user: String,
        limit: Int = 50,
        period: String = "overall",
    ): Result<List<LastFmTrack>> {
        return try {
            val response = httpClient.get(BASE_URL) {
                parameter("method", "user.gettoptracks")
                parameter("user", user)
                parameter("limit", limit)
                parameter("period", period)
                parameter("api_key", apiKey)
                parameter("format", "json")
            }
            
            val responseBody = response.bodyAsText()
            val jsonResponse = json.parseToJsonElement(responseBody)
            val toptracks = jsonResponse.jsonObject["toptracks"]?.jsonObject
            val tracksArray = toptracks?.get("track")?.jsonArray
            
            val tracks = tracksArray?.mapNotNull { element ->
                val trackObj = element.jsonObject
                val artistObj = trackObj["artist"]?.jsonObject
                LastFmTrack(
                    name = trackObj["name"]?.jsonPrimitive?.content ?: "",
                    artist = artistObj?.get("name")?.jsonPrimitive?.content ?: "",
                    mbid = trackObj["mbid"]?.jsonPrimitive?.content ?: "",
                    playCount = trackObj["playcount"]?.jsonPrimitive?.content?.toIntOrNull() ?: 0,
                    url = trackObj["url"]?.jsonPrimitive?.content ?: "",
                )
            } ?: emptyList()
            
            Result.success(tracks)
        } catch (e: Exception) {
            Result.failure(LastFmException(-1, e.message ?: "Network error"))
        }
    }

    /**
     * Get track recommendations based on artist
     */
    suspend fun getSimilarTracks(
        artist: String,
        track: String? = null,
        limit: Int = 20,
    ): Result<List<LastFmTrack>> {
        return try {
            val response = httpClient.get(BASE_URL) {
                parameter("method", if (track != null) "track.getSimilar" else "artist.getSimilar")
                parameter("artist", artist)
                track?.let { parameter("track", it) }
                parameter("limit", limit)
                parameter("autocorrect", 1)
                parameter("api_key", apiKey)
                parameter("format", "json")
            }
            
            val responseBody = response.bodyAsText()
            val jsonResponse = json.parseToJsonElement(responseBody)
            val similartracks = jsonResponse.jsonObject["similartracks"]?.jsonObject
            val tracksArray = similartracks?.get("track")?.jsonArray
            
            val tracks = tracksArray?.mapNotNull { element ->
                val trackObj = element.jsonObject
                val artistObj = trackObj["artist"]?.jsonObject
                val artistName = artistObj?.get("name")?.jsonPrimitive?.content ?: ""
                LastFmTrack(
                    name = trackObj["name"]?.jsonPrimitive?.content ?: "",
                    artist = artistName,
                    mbid = trackObj["mbid"]?.jsonPrimitive?.content ?: "",
                    playCount = 0,
                    url = trackObj["url"]?.jsonPrimitive?.content ?: "",
                )
            } ?: emptyList()
            
            Result.success(tracks)
        } catch (e: Exception) {
            Result.failure(LastFmException(-1, e.message ?: "Network error"))
        }
    }

    /**
     * Generate API signature for authenticated requests
     */
    private fun generateSignature(method: String, params: Map<String, String>): String {
        val sortedParams = params.toSortedMap()
        val signatureString = sortedParams.entries.joinToString("") { "${it.key}${it.value}" } + "api_key$apiKey$method$method"
        return md5(signatureString)
    }

    /**
     * Simple MD5 hash implementation
     */
    private fun md5(input: String): String {
        val md = java.security.MessageDigest.getInstance("MD5")
        val digest = md.digest(input.toByteArray())
        return digest.joinToString("") { "%02x".format(it) }
    }
}

@Serializable
data class LastFmSession(
    val key: String,
    val name: String,
)

@Serializable
data class LastFmArtist(
    val name: String,
    val mbid: String,
    val playCount: Int,
    val url: String,
)

@Serializable
data class LastFmTrack(
    val name: String,
    val artist: String,
    val mbid: String,
    val playCount: Int,
    val url: String,
)

class LastFmException(
    val errorCode: Int,
    override val message: String,
) : Exception(message)
