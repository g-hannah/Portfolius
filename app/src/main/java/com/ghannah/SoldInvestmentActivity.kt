package com.ghannah

import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class SoldInvestmentActivity : AppCompatActivity()
{
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.sold_investment)

        findViewById<Button>(R.id.buttonSoldInvestmentSubmit)
            .setOnClickListener {

                val portfolio : Portfolio? = PortfoliusState.getCurrentlySelectedPortfolio()
                val inv : Investment? = PortfoliusState.getCurrentlySelectedInvestment()
                if (null != portfolio && null != inv)
                {
                    PortfoliusState.addToTotalGainOrLoss(inv.net())
                    portfolio.removeInvestment(inv)
                }
            }

        findViewById<Button>(R.id.buttonSoldInvestmentCancel)
            .setOnClickListener {

                finish()
            }
    }
}