package com.ghannah

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class AddInvestmentActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener
{
    private var selectedCurrency : String? = null

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

        findViewById<Button>(R.id.buttonSubmitNewInvestment)
            .setOnClickListener {

                val portfolio : Portfolio = PortfoliusState.getCurrentlySelectedPortfolio()

                if (null == portfolio)
                {
                    Toast.makeText(this, "Error retrieving selected portfolio", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }

                // get selected currency

                val amount : Double? = findViewById<EditText>(R.id.editTextAmountCurrency).text.toString().toDoubleOrNull()
                val rate : Double? = findViewById<EditText>(R.id.editTextRateCurrency).text.toString().toDoubleOrNull()
                val fee : Double? = findViewById<EditText>(R.id.editTextFeeCurrency).text.toString().toDoubleOrNull()

                if (null == selectedCurrency || null == amount || null == rate || null == fee)
                {
                    Toast.makeText(this, "Error with input values", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }

                val portfolioName : String = portfolio._name
                portfolio.addInvestment(Investment(selectedCurrency!!, amount, rate, fee))

                Toast.makeText(
                    this,
                    "Successfully added new investment to portfolio $portfolioName for currency $selectedCurrency",
                    Toast.LENGTH_LONG
                )
            }
    }

    override fun onItemSelected(parent : AdapterView<*>, view : View?, pos : Int, id : Long)
    {
        val s : Any = parent.getItemAtPosition(pos)

        if (s is String)
            selectedCurrency = s
    }

    override fun onNothingSelected(p0: AdapterView<*>?)
    {
        TODO("Not yet implemented")
    }
}