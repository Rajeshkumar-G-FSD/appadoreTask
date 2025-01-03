package com.example.quizgame

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myquiztest.R

class CountryAdapter(
    private val ctx: Context,
    private val countries: List<Country>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {
    private var correctAnswerId = -1 // Store the correct answer position

    private var selectedAnswerId = -1 // Store the selected answer position
    private var score = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_country, parent, false)

        return CountryViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: CountryViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        val country = countries[position]
        holder.tvCountryName.text = country.countryName
        // Reset initial state
        holder.tvCountryName.setBackgroundResource(R.drawable.answer_border)
        holder.tvCountryName.setTextColor(ctx.resources.getColor(android.R.color.black))
        holder.tvCountryResult.visibility = View.GONE
        // Highlight the correct answer
        if (position == correctAnswerId) {
            holder.tvCountryName.setBackgroundResource(R.drawable.answered_border)
            if (position != selectedAnswerId) {
                holder.tvCountryResult.text = ctx.resources.getString(R.string.tv_correct)
                holder.tvCountryResult.setTextColor(ctx.resources.getColor(R.color.green_color))
                holder.tvCountryResult.visibility = View.VISIBLE
            }
        }
        // Set the view based on the selected answer
        if (position == selectedAnswerId) {
            if (countries[position].id == countries[position].answerId) {
                holder.tvCountryName.setBackgroundResource(R.drawable.answered_border)
                holder.tvCountryResult.text = ctx.resources.getString(R.string.tv_correct)
                holder.tvCountryResult.setTextColor(ctx.resources.getColor(R.color.green_color))
                holder.tvCountryResult.visibility = View.VISIBLE
                score += 1 //incre correct answer
            } else {
                holder.tvCountryName.setBackgroundResource(R.drawable.wronganswer_border)
                holder.tvCountryName.setTextColor(ctx.resources.getColor(android.R.color.white))
                holder.tvCountryResult.text = ctx.resources.getString(R.string.tv_wrong)
                holder.tvCountryResult.setTextColor(ctx.resources.getColor(R.color.red_color))
                holder.tvCountryResult.visibility = View.VISIBLE
            }
            listener.onItemClick(countries[position].id == countries[position].answerId, score)
        }
        /*  holder.tvCountryName.setOnClickListener(view -> {
            if
            (selectedAnswerId == -1) {
// Allow only one selection
                selectedAnswerId = position;
                if
                (countries.get(position).getId() == countries.get(position).getanswerId()) {
                    correctAnswerId = position;
                    listener.onItemClick(true);
                } else {
                    listener.onItemClick(false);
                }
                notifyDataSetChanged();
// Refresh the list to show the updated views
            }
        });*/
        holder.tvCountryName.setOnClickListener { view: View? ->
            if (selectedAnswerId == -1) {
// Allow only one selection
                selectedAnswerId = position
                if (countries[position].id == countries[position].answerId) {
                    correctAnswerId = position
                }
                notifyDataSetChanged()
                // Refresh the list to show the updated views
            }
        }
        // Identify the correct answer id
        if (country.id == country.answerId && correctAnswerId == -1) {
            correctAnswerId = position
        }
        Log.e(
            "onBindViewHolder",
            country.countryName +
                    ">>"
                    + country.id +
                    ">>"
                    + country.answerId
        )
    }
    override fun getItemCount(): Int {
        return countries.size
    }

    class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvCountryName: TextView = itemView.findViewById(R.id.tvCountryName)
        var tvCountryResult: TextView = itemView.findViewById(R.id.tvCountryResult)
    }

    interface OnItemClickListener {
        fun onItemClick(isCorrect: Boolean, newScore: Int)
    }
}

