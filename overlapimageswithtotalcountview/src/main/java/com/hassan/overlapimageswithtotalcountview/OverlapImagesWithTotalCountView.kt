package com.hassan.overlapimageswithtotalcountview

import android.content.Context
import android.widget.Toast

class OverlapImagesWithTotalCountView {
    companion object {
        fun showMessage(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }
}