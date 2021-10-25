package com.ghannah

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.widget.Button
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

        setContentView(R.layout.view_portfolio)

        findViewById<TextView>(R.id.textViewPortfolioName)?.setText(selectedPortfolio._name)

        val ll : LinearLayout? = findViewById<LinearLayout>(R.id.linearLayoutPortfolioList)
        val map : MutableMap<String,MutableList<Investment>> = selectedPortfolio.getInvestments()
        val res : Resources = applicationContext.resources
        val fmt : String = res.getString(R.string.investment_view_format_string)

        if (0 == map.size)
        {
            val tv = TextView(this)
            tv.text = "No investments for this portfolio! Use the button below to add one!"
            ll?.addView(tv)
        }
        else
        {
            for (key in map.keys)
            {
                val tv = TextView(this)

                try
                {
                    tv.text = String.format(fmt, key, selectedPortfolio.netForCurrency(key))
                }
                catch (e : NoInvestmentListForCurrencyException)
                {
                    Notification.send(this, e.message ?: "An error occurred...")
                }
            }
        }

//        for (i in 1..10)
//        {
//            val tv = TextView(this)
//            tv.text = "Investment $i : +£1711.96"
//            ll?.addView(tv)
//        }

        findViewById<Button>(R.id.buttonAddNewInvestment)
            ?.setOnClickListener {

                startActivity(Intent(this, AddInvestmentActivity::class.java))
            }
    }
}