package com.ghannah

import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

/**
 * Activity for creating a new portfolio
 */
class CreatePortfolioActivity : AppCompatActivity()
{
    private fun clearName()
    {
        findViewById<EditText>(R.id.newPortfolioName).setText("")
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_portfolio)

        findViewById<Button>(R.id.buttonCreateNewPortfolio)
            .setOnClickListener {

                val tv : TextView = findViewById<TextView>(R.id.newPortfolioName)
                val portfolio = Portfolio(tv.text.toString())
                var result : Boolean = false

                try
                {
                    result = PortfolioManager.addPortfolio(portfolio)
                }
                catch (e : PortfolioExistsException)
                {
                    Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                }

                if (result)
                {
                    Toast.makeText(
                        this,
                        String.format("Successfully created portfolio \"%s\"", portfolio._name),
                        Toast.LENGTH_LONG
                    ).show()
                }

                clearName()
            }
    }
}