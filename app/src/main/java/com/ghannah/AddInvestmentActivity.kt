package com.ghannah

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class AddInvestmentActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener
{
    private var selectedCurrency : String = "BTC" // default

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.add_investment)

        val spinner : Spinner = findViewById(R.id.spinnerCurrencyChoices)

        ArrayAdapter.createFromResource(
            this,
            R.array.valid_cryptocurrencies,
            android.R.layout.simple_spinner_item
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = it
        }

        spinner.onItemSelectedListener = this

        findViewById<Button>(R.id.buttonSubmitEditedInvestment)
            .setOnClickListener {

                val portfolio : Portfolio? = PortfoliusState.getCurrentlySelectedPortfolio()

                if (null == portfolio)
                {
                    Toast.makeText(this, "Error retrieving selected portfolio", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }

                // get selected currency

                val amount : Double? = findViewById<EditText>(R.id.editTextAmountCurrency).text.toString().toDoubleOrNull()
                val rate : Double? = findViewById<EditText>(R.id.editTextRateCurrency).text.toString().toDoubleOrNull()
                val fee : Double? = findViewById<EditText>(R.id.editTextFeeCurrency).text.toString().toDoubleOrNull()

                if (null == amount || null == rate || null == fee)
                {
                    Notification.error(this, "An error occurred parsing input values")
                    return@setOnClickListener
                }

                val portfolioName : String = portfolio._name

//                println("INVESTMENT: $message")
                val inv = Investment(selectedCurrency, amount, rate, fee)
                portfolio.addInvestment(inv)

                Notification.send(
                    this,
                    "Successfully added new investment to " + portfolio._name
                )

                //finish()
            }
    }

    override fun onItemSelected(parent : AdapterView<*>, view : View?, pos : Int, id : Long)
    {
        val s : Any = parent.getItemAtPosition(pos)

        if (s is String)
        {
            selectedCurrency = s
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?)
    {
        TODO("Not yet implemented")
    }
}