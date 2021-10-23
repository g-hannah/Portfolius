package com.ghannah

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ViewPortfolioActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.view_portfolio)

        val res = applicationContext.resources
        val key : String = res.getString(R.string.SELECTED_PORTFOLIO)
        val portfolioName : String? = savedInstanceState?.getString(res.getString(R.string.SELECTED_PORTFOLIO))
        findViewById<TextView>(R.id.textViewPortfolioName).setText(portfolioName)
    }
}