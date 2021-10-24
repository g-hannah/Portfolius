package com.ghannah

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.selects.select

class ViewPortfolioActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)

        val selectedPortfolio : Portfolio = PortfoliusState.getCurrentlySelectedPortfolio()

        findViewById<TextView>(R.id.textViewPortfolioName).setText(selectedPortfolio._name)

        val ll : LinearLayout = findViewById<LinearLayout>(R.id.linearLayoutPortfolioList)
        val map : MutableMap<String,MutableList<Investment>> = selectedPortfolio.getInvestments()
        val res : Resources = applicationContext.resources
        val fmt : String = res.getString(R.string.investment_view_format_string)

        for (key in map.keys)
        {
            val tv = TextView(this)
            tv.setText(String.format(fmt, key, selectedPortfolio.net()))
            ll.addView(tv)
        }

        setContentView(R.layout.view_portfolio)
    }
}