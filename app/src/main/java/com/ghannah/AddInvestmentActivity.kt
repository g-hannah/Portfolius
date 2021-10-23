package com.ghannah

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddInvestmentActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)

        findViewById<Button>(R.id.buttonSubmitNewInvestment)
            .setOnClickListener {

                val ctx : Context = applicationContext
                val portfolio : Portfolio? = PortfolioManager
                    .getPortfolioWithName(ctx.resources.getString(R.string.SELECTED_PORTFOLIO))

                if (null == portfolio)
                {
                    Toast.makeText(this, "Error retrieving selected portfolio", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }

                // get selected currency

                val currency : String = "ETH"
                val amount : Double? = findViewById<EditText>(R.id.editTextAmountCurrency).text.toString().toDoubleOrNull()
                val rate : Double? = findViewById<EditText>(R.id.editTextRateCurrency).text.toString().toDoubleOrNull()
                val fee : Double? = findViewById<EditText>(R.id.editTextFeeCurrency).text.toString().toDoubleOrNull()

                if (null == amount || null == rate || null == fee)
                {
                    Toast.makeText(this, "Error with input values", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }

                portfolio.addInvestment(Investment("BTC", amount, rate, fee))
                Toast.makeText(this, "Successfully added new investment", Toast.LENGTH_LONG)
            }

        setContentView(R.layout.add_investment)
    }
}