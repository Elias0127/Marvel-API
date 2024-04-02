package com.example.marvelapi

import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.marvelapi.model.Character

class CharacterAdapter(characters: List<Character>) : RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    private var characters: MutableList<Character> = characters.toMutableList()
    private var charactersFull: List<Character> = ArrayList(characters)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_character, parent, false)
        return CharacterViewHolder(view, this)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = characters[position]
        holder.bind(character)
    }

    override fun getItemCount() = characters.size

    fun filter(query: String) {
        characters = if (query.isEmpty()) {
            charactersFull.toMutableList()
        } else {
            charactersFull.filter {
                it.name.contains(query, ignoreCase = true)
            }.toMutableList()
        }
        notifyDataSetChanged()
    }

    inner class CharacterViewHolder(itemView: View, private val adapter: CharacterAdapter) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.characterNameTextView)
        private val imageView: ImageView = itemView.findViewById(R.id.characterImageView)

        fun bind(character: Character) {
            nameTextView.text = character.name
            Glide.with(itemView.context)
                .load(character.thumbnail.getUrl().replace("http://", "https://"))
                .placeholder(R.drawable.placeholder_image) // Ensure these resources exist
                .error(R.drawable.error_image)
                .into(imageView)

            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    Toast.makeText(itemView.context, "${character.name} clicked!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
