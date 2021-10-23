package com.ghannah

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class InvestmentViewActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        val inv : Investment = PortfoliusState.getCurrentlySelectedInvestment()

        setContentView(R.layout.view_investment)

        findViewById<TextView>(R.id.textViewInvestmentAmountValue).setText(inv.getAmount().toString())
        findViewById<TextView>(R.id.textViewInvestmentCostValue).setText(inv.getCost().toString())
        findViewById<TextView>(R.id.textViewInvestmentRateValue).setText(inv.getRate().toString())
        findViewById<TextView>(R.id.textViewInvestmentNetValue).setText(inv.net().toString())
    }
}