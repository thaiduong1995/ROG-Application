package com.duong.rog.extension

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.annotation.StringRes
import com.duong.rog.R

private fun TextView.getDotAnimator(
    dotCount: Int,
    list: List<CharSequence>
): ValueAnimator {
    val valueTo = dotCount + 1

    return ValueAnimator.ofInt(0, valueTo).apply {
        this.interpolator = LinearInterpolator()
        this.duration = context.resources.getInteger(R.integer.dots_anim_time).toLong()
        this.repeatCount = ObjectAnimator.INFINITE
        this.repeatMode = ObjectAnimator.RESTART

        addUpdateListener {
            val value = it.animatedValue as? Int

            /**
             * Sometimes [ValueAnimator] give a corner value.
             */
            if (value == null || value == valueTo) return@addUpdateListener

            text = list.getOrNull(value)
        }
    }
}

fun TextView?.getDotsSpanAnimator(@StringRes stringId: Int): ValueAnimator? {
    if (this == null) return null

    val simpleText = context.getString(stringId)
    val dotText = context.getString(R.string.dot)
    val dotCount = 3

    val resultText = StringBuilder(simpleText).apply {
        repeat(dotCount) { append(dotText) }
    }.toString()

    val textList = mutableListOf<SpannableString>()
    for (i in 0 until dotCount + 1) {
        val spannable = SpannableString(resultText)

        val start = resultText.length - (dotCount - i)
        val end = resultText.length
        val flag = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        spannable.setSpan(ForegroundColorSpan(Color.TRANSPARENT), start, end, flag)

        textList.add(spannable)
    }

    return this.getDotAnimator(dotCount, textList)
}