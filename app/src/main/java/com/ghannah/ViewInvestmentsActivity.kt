package com.ghannah

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ViewInvestmentsActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)

        val selectedCurrency : String? = PortfoliusState.getCurrentlySelectedInvestmentCurrency()

        if (null == selectedCurrency)
        {
            Notification.error(this, "No currency in state")
            finish()
        }

        val selectedPortfolio : Portfolio? = PortfoliusState.getCurrentlySelectedPortfolio()

        if (null == selectedPortfolio)
        {
            Notification.error(this, "No portfolio selected")
            finish()
        }

        setContentView(R.layout.view_investments)

        val res = applicationContext.resources
        findViewById<TextView>(R.id.textViewInvestmentListForCurrencyLabel).text = String.format(res.getString(R.string.investment_list_label_format_string), selectedCurrency)

        val list : MutableList<Investment> = selectedPortfolio!!.getInvestments()[selectedCurrency]!!
        val ll : LinearLayout? = findViewById<LinearLayout>(R.id.linearLayoutInvestmentListForCurrency)

        if (null == ll)
        {
            Notification.error(this, "No investments found for this currency")
            //finish()
        }

        val buttonInvestmentMapping : MutableMap<Button,Investment> = mutableMapOf<Button,Investment>()

        val ourThis : Context = this
        for (investment in list)
        {
            val button = Button(this)
            button.text = investment.toString()
            buttonInvestmentMapping[button] = investment
            ll?.addView(button)

            button.setOnClickListener {

                if (it is Button)
                {
                    PortfoliusState.setCurrentlySelectedInvestment(buttonInvestmentMapping.get(it)!!)
                    startActivity(Intent(this, ViewInvestmentActivity::class.java))
                }
            }
        }
    }
}