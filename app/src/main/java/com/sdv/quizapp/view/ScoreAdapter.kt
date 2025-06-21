package com.sdv.quizapp.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sdv.quizapp.databinding.ItemScoreBinding
import com.sdv.quizapp.model.Score

class ScoreAdapter : ListAdapter<Score, ScoreAdapter.ScoreViewHolder>(ScoreDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {
        val binding = ItemScoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScoreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
        holder.bind(getItem(position), position + 4) // +4 car les 3 premiers sont dans le podium
    }

    class ScoreViewHolder(private val binding: ItemScoreBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(score: Score, position: Int) {
            binding.apply {
                tvPosition.text = position.toString()
                tvUsername.text = score.username
                tvCategory.text = score.category
                tvScore.text = "${score.score}/10"
            }
        }
    }

    private class ScoreDiffCallback : DiffUtil.ItemCallback<Score>() {
        override fun areItemsTheSame(oldItem: Score, newItem: Score): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Score, newItem: Score): Boolean {
            return oldItem == newItem
        }
    }
}