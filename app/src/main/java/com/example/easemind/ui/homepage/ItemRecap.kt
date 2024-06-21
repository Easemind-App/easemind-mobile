package com.example.easemind.ui.homepage

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.easemind.R

class ItemRecap @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    val imageView: ImageView
    val textView: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.item_recap, this, true)
        imageView = findViewById(R.id.iv_item_emoji)
        textView = findViewById(R.id.tv_item_date)
    }

    fun setEmojiDrawable(drawableId: Int) {
        Glide.with(this)
            .load(R.drawable.draw_overjoyed)
            .into(imageView)
//        imageView.setImageResource(drawableId)
    }

    fun setDateText(text: String) {
        textView.text = text
    }
}
