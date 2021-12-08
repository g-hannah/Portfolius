package com.ghannah

import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
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
                    val rate : Double? = findViewById<EditText>(R.id.editTextDecimalRateValue).text.toString().toDoubleOrNull()
                    val fee : Double? = findViewById<EditText>(R.id.editTextDecimalFeeValue).text.toString().toDoubleOrNull()

                    if (null != rate && null != fee)
                    {
                        val r : Double = rate!!
                        val f : Double = fee!!
                        val net : Double = (inv.getAmount() * r) - inv.getCost() - f

                        PortfoliusState.addToTotalGainOrLoss(net)
                        PortfoliusState.unsetSelectedInvestment()
                        portfolio.removeInvestment(inv)

                        Notification.send(this, "Investment sold!")
                        finish()
                    }
                    else
                    {
                        Notification.error(this, "Invalid values")
                    }
                }
            }

        findViewById<Button>(R.id.buttonSoldInvestmentCancel)
            .setOnClickListener {

                finish()
            }
    }
}
