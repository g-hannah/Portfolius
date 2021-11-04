package com.ghannah

import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class EditInvestmentActivity : AppCompatActivity()
{
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)

        val investment : Investment? = PortfoliusState.getCurrentlySelectedInvestment()
        if (null == investment)
        {
            Notification.error(this, "No investment is selected")
            return
        }

        val inv : Investment = investment!!

        setContentView(R.layout.edit_investment)

        findViewById<TextView>(R.id.textViewEditInvestmentCurrency).text = inv.getCurrency()

        findViewById<Button>(R.id.buttonSubmitEditedInvestment)
            .setOnClickListener {

                val portfolio : Portfolio? = PortfoliusState.getCurrentlySelectedPortfolio()

                if (null == portfolio)
                {
                    Notification.error(this, "Failed to retrieve selected portfolio")
                    return@setOnClickListener
                }

                // get selected currency

                val amount : Double? = findViewById<EditText>(R.id.editTextEditAmountCurrency).text.toString().toDoubleOrNull()
                val rate : Double? = findViewById<EditText>(R.id.editTextEditRateCurrency).text.toString().toDoubleOrNull()
                val fee : Double? = findViewById<EditText>(R.id.editTextEditFeeCurrency).text.toString().toDoubleOrNull()

                if (null == amount || null == rate || null == fee)
                {
                    Notification.error(this, "An error occurred parsing input values")
                    return@setOnClickListener
                }

                portfolio.editInvestment(inv, amount, rate, fee)
//                inv._amount = amount
//                inv._rate = rate
//                inv._fee = fee
//
//                PortfolioManager.write()

//                val message = "Adding investment: amount $amount, rate $rate, fee $fee, currency $selectedCurrency"
//
//                Notification.send(
//                    this,
//                    message)

//                val portfolioName : String = portfolio._name

//                portfolio.replaceInvestment(inv)

                Notification.send(
                    this,
                    "Successfully modified investment " + inv.getId()
                )
            }
    }
}