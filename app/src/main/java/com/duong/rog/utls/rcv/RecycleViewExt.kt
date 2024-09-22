package com.duong.rog.utls.rcv

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by duong_tt on 9/18/2023.
 * Email: tranthaiduong.kailoren@gmail.com
 * Github: https://github.com/thaiduong1995
 */
fun RecyclerView.addItemDecoration(marginHorizontal: Int, marginVertical: Int) {
    this.addItemDecoration(object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State,
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            if (parent.getChildAdapterPosition(view) >= 0) {
                outRect.right = marginHorizontal
                outRect.left = marginHorizontal
                outRect.top = marginVertical
                outRect.bottom = marginVertical
            }
        }
    })
}
