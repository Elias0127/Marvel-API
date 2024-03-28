package com.example.marvelapi

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.marvelapp.model.Character
import com.example.marvelapp.network.RetrofitInstance
import com.example.marvelapp.utils.HashUtils
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private var characters: List<Character> = listOf()
    private var currentIndex: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Hardcoded API keys
        val publicKey = "ee58846ce29a2a2eb1af77882cdb76a1"
        val privateKey = "8bee10a3b6204537d5c06ccd0ae076c3d54f8ec1"

        CoroutineScope(Dispatchers.IO).launch {
            // Network request to get characters
            val timestamp = (System.currentTimeMillis() / 1000).toString()
            val hash = HashUtils.md5(timestamp + privateKey + publicKey)
            val response = RetrofitInstance.api.getCharacters(publicKey, hash, timestamp)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    characters = response.body()?.data?.results.orEmpty()
                    if (characters.isNotEmpty()) {
                        showNextCharacter() // Show the first character initially
                    }
                } else {
                    Log.e("MainActivity", "API Error: ${response.errorBody()?.string()}")
                }
            }
        }

        findViewById<Button>(R.id.nextButton).setOnClickListener {
            showNextCharacter()
        }
    }

    private fun showNextCharacter() {
        currentIndex = (currentIndex + 1) % characters.size // Increment and loop around the list
        val character = characters[currentIndex]

        val imageUrl = "${character.thumbnail.path.replace("http", "https")}/standard_medium.${character.thumbnail.extension}"

        findViewById<TextView>(R.id.characterNameTextView).text = character.name
        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.error_image)
            .into(findViewById<ImageView>(R.id.characterImageView))
    }
}
