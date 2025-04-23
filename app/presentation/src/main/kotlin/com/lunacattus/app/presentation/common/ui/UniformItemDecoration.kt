package com.lunacattus.app.presentation.common.ui

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class UniformItemDecoration(
    val isVertical: Boolean = true,
    val maxItemCountShow: Int,
    val fixedSpacing: Int,
    val itemWidth: Int = 0,
    val itemHigh: Int = 0
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val adapter = parent.adapter ?: return
        val itemSize = adapter.itemCount
        val position = parent.getChildAdapterPosition(view)

        when {
            itemSize <= 1 -> {
                if (position == 0) {
                    if (isVertical) {
                        val totalSpace = parent.height - itemHigh
                        outRect.top = totalSpace / 2
                        outRect.bottom = totalSpace / 2
                    } else {
                        val totalSpace = parent.width - itemWidth
                        outRect.left = totalSpace / 2
                        outRect.right = totalSpace / 2
                    }
                }
            }

            itemSize <= maxItemCountShow -> {
                val spacing = if (isVertical) {
                    val totalSpace = parent.height - itemHigh * itemSize
                    totalSpace / (itemSize + 1)
                } else {
                    val totalSpace = parent.width - itemWidth * itemSize
                    totalSpace / (itemSize + 1)
                }
                when (position) {
                    0 -> {
                        if (isVertical) {
                            outRect.top = spacing
                            outRect.bottom = spacing / 2
                        } else {
                            outRect.left = spacing
                            outRect.right = spacing / 2
                        }
                    }

                    itemSize - 1 -> {
                        if (isVertical) {
                            outRect.top = spacing / 2
                            outRect.bottom = spacing
                        } else {
                            outRect.left = spacing / 2
                            outRect.right = spacing
                        }
                    }

                    else -> {
                        if (isVertical) {
                            outRect.top = spacing / 2
                            outRect.bottom = spacing / 2
                        } else {
                            outRect.left = spacing / 2
                            outRect.right = spacing / 2
                        }
                    }
                }
            }

            else -> {
                when (position) {
                    0 -> if (isVertical) {
                        outRect.top = 0
                    } else {
                        outRect.left = 0
                    }

                    else -> if (isVertical) {
                        outRect.top = fixedSpacing
                    } else {
                        outRect.left = fixedSpacing
                    }
                }
                if (isVertical) {
                    outRect.bottom = 0
                } else {
                    outRect.right = 0
                }
            }
        }
    }
}