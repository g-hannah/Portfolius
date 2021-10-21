package com.ghannah

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.SurfaceView
import android.view.View
import androidx.core.content.res.ResourcesCompat

class GraphCanvas(ctx : Context) : SurfaceView(ctx)
{
    private lateinit var canvas : Canvas
    private lateinit var bitmap : Bitmap
    private val colorBackground = ResourcesCompat.getColor(resources, R.color.colorCanvas, null)
}