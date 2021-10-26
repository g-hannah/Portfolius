package com.ghannah

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ViewInvestmentActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)

        val inv : Investment? = PortfoliusState.getCurrentlySelectedInvestment()
        if (null == inv)
        {
            Notification.error(this, "No investment is selected")
            finish()
        }

        val investment : Investment = inv!!

        setContentView(R.layout.view_investment)

        findViewById<TextView>(R.id.textViewInvestmentAmountValue).text = investment.getAmount().toString()
        findViewById<TextView>(R.id.textViewInvestmentRateValue).text = investment.getRate().toString()
        findViewById<TextView>(R.id.textViewInvestmentCostValue).text = investment.getCost().toString()
        findViewById<TextView>(R.id.textViewInvestmentWorthValue).text = investment.getWorth().toString()
        findViewById<TextView>(R.id.textViewInvestmentNetValue).text = investment.net().toString()

        findViewById<Button>(R.id.buttonDeleteInvestment)
            .setOnClickListener {

                val portfolio : Portfolio = PortfoliusState.getCurrentlySelectedPortfolio()!!
                portfolio.removeInvestment(investment)
                Notification.send(this, "Removed investment from portfolio")

                finish()
            }
    }
}