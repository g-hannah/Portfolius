package com.ghannah

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.size

class ViewInvestmentsActivity : AppCompatActivity()
{
    private fun showInvestments()
    {
        val selectedCurrency : String? = PortfoliusState.getCurrentlySelectedInvestmentCurrency()

        if (null == selectedCurrency)
        {
            Notification.error(this, "No currency in state")
            finish()
        }

        val selectedPortfolio : Portfolio? = PortfoliusState.getCurrentlySelectedPortfolio()

        if (null == selectedPortfolio)
        {
            Notification.error(this, "No portfolio selected (ViewInvestmentsActivity)")
            finish()
        }

        val res = applicationContext.resources
        findViewById<TextView>(R.id.textViewPortfolioNameViewInvestments).text = selectedPortfolio!!._name
        findViewById<TextView>(R.id.textViewInvestmentListForCurrencyLabel).text = String.format(res.getString(R.string.investment_list_label_format_string), selectedCurrency)

        val list : MutableList<Investment> = selectedPortfolio!!.getInvestments()[selectedCurrency]!!
        val ll : LinearLayout? = findViewById<LinearLayout>(R.id.linearLayoutInvestmentListForCurrency)

        if (null == ll)
        {
            Notification.error(this, "No investments found for this currency")
            //finish()
        }

        if (0 < ll!!.size)
        {
            ll.removeAllViews()
        }

        val buttonInvestmentMapping : MutableMap<Button,Investment> = mutableMapOf<Button,Investment>()

        for (investment in list)
        {
            //  val buttonStyle : Int = R.style.Button_Investment
            //  val ctw : ContextThemeWrapper = ContextThemeWrapper(this, buttonStyle)
            val button : Button = Button(this)

            button.setBackgroundColor(0xffff88)
            button.text = investment.toString()
            buttonInvestmentMapping[button] = investment
            ll?.addView(button)

            button.setOnClickListener {

                if (it is Button)
                {
                    PortfoliusState.setCurrentlySelectedInvestment(buttonInvestmentMapping.get(it)!!)
                    startActivity(Intent(this, ViewInvestmentActivity::class.java))
                }
            }
        }
    }

    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.view_investments)
        showInvestments()
    }

    override fun onRestart()
    {
        super.onRestart()
        showInvestments()
    }
}