package com.example.marvelapi

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelapp.network.RetrofitInstance
import com.example.marvelapp.utils.HashUtils
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CharacterAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.charactersRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val dividerItemDecoration = DividerItemDecoration(recyclerView.context, LinearLayoutManager.VERTICAL)
        recyclerView.addItemDecoration(dividerItemDecoration)


        loadCharacters()

        val searchEditText: EditText = findViewById(R.id.searchEditText)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                adapter.filter(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun loadCharacters() {
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
                    val characters = response.body()?.data?.results.orEmpty()
                    adapter = CharacterAdapter(characters)
                    recyclerView.adapter = adapter
                } else {
                    Log.e("MainActivity", "API Error: ${response.errorBody()?.string()}")
                }
            }
        }
    }
}
