package com.example.marvelapp.model

data class CharacterResponse(
    val data: CharacterData
)

data class CharacterData(
    val results: List<Character>
)

data class Character(
    val name: String,
    val description: String,
    val thumbnail: Thumbnail
)

data class Thumbnail(
    val path: String,
    val extension: String
) {
    fun getUrl(): String = "$path.$extension"
}
