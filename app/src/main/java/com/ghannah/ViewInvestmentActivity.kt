package com.ghannah

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class ViewInvestmentActivity : AppCompatActivity()
{
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)

        val ctx : Context = this
        val inv : Investment? = PortfoliusState.getCurrentlySelectedInvestment()

        if (null == inv)
        {
            Notification.error(ctx, "No investment is selected")
            finish()
        }

        val investment : Investment = inv!!

        setContentView(R.layout.view_investment)

        val fmt : String = "£%.2f"

        val cur : String? = PortfoliusState.getCurrentlySelectedInvestmentCurrency()
        var rate : Rate? = null

        if (null != cur)
            rate = ExchangeRatesManager.getRateForCurrency(cur)

        findViewById<TextView>(R.id.textViewInvestmentIdValue).text = investment.getId()
        findViewById<TextView>(R.id.textViewInvestmentAmountValue).text = investment.getAmount().toString()
        findViewById<TextView>(R.id.textViewInvestmentRateThenValue).text = fmt.format(investment.getRate())
        findViewById<TextView>(R.id.textViewInvestmentFeeValue).text = fmt.format(investment.getFee())
        findViewById<TextView>(R.id.textViewInvestmentCurrentRateValue).text = fmt.format(rate?.getValue())
        findViewById<TextView>(R.id.textViewInvestmentCostValue).text = fmt.format(investment.getCost())
        findViewById<TextView>(R.id.textViewInvestmentCurrentValueValue).text = fmt.format(investment.getWorth())

        val tvNet : TextView? = findViewById<TextView>(R.id.textViewInvestmentNetValue)

        val netChange : Double = investment.net()
        val gain : Boolean = 0.0 <= netChange

        tvNet?.text = fmt.format(investment.net())

        if (gain)
            tvNet?.setTextColor(resources.getColor(R.color.colorPositive))
        else
            tvNet?.setTextColor(resources.getColor(R.color.colorNegative))

        findViewById<Button>(R.id.buttonDeleteInvestment)
            .setOnClickListener {

                val portfolio : Portfolio = PortfoliusState.getCurrentlySelectedPortfolio()!!
                portfolio.removeInvestment(investment)
                Notification.send(ctx, "Removed investment from portfolio")

                finish()
            }

        findViewById<Button>(R.id.buttonEditInvestment)
            .setOnClickListener {

                startActivity(Intent(ctx, EditInvestmentActivity::class.java))
            }

        findViewById<Button>(R.id.buttonInvestmentMarkAsSold)
            .setOnClickListener {

                startActivity(Intent(ctx, SoldInvestmentActivity::class.java))
            }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onRestart()
    {
        super.onRestart()

        if (null == PortfoliusState.getCurrentlySelectedInvestment())
            finish()
    }
}