package com.ghannah

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class ViewInvestmentActivity : AppCompatActivity()
{
    @RequiresApi(Build.VERSION_CODES.N)
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

        val fmt : String = "£%.2f"
        findViewById<TextView>(R.id.textViewInvestmentIdValue).text = investment.getId()
        findViewById<TextView>(R.id.textViewInvestmentAmountValue).text = investment.getAmount().toString()
        findViewById<TextView>(R.id.textViewInvestmentRateValue).text = fmt.format(investment.getRate())
        findViewById<TextView>(R.id.textViewInvestmentCostValue).text = fmt.format(investment.getCost())
        findViewById<TextView>(R.id.textViewInvestmentWorthValue).text = fmt.format(investment.getWorth())

        val tvNet : TextView? = findViewById<TextView>(R.id.textViewInvestmentNetValue)

        val netChange : Double = investment.net()
        val gain : Boolean = 0.0 <= netChange

        tvNet?.text = fmt.format(investment.net())

        if (gain)
            tvNet?.setTextColor(resources.getColor(R.color.colorPositive))
        else
            tvNet?.setTextColor(resources.getColor(R.color.colorNegative))

        findViewById<ImageButton>(R.id.buttonDeleteInvestment)
            .setOnClickListener {

                val portfolio : Portfolio = PortfoliusState.getCurrentlySelectedPortfolio()!!
                portfolio.removeInvestment(investment)
                Notification.send(this, "Removed investment from portfolio")

                finish()
            }

        findViewById<ImageButton>(R.id.buttonEditInvestment)
            .setOnClickListener {

                startActivity(Intent(this, EditInvestmentActivity::class.java))
            }

        findViewById<Button>(R.id.buttonInvestmentMarkAsSold)
            .setOnClickListener {

                startActivity(Intent(this, SoldInvestmentActivity::class.java))
            }
    }
}