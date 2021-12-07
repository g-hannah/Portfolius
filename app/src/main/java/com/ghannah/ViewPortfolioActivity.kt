package com.ghannah

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isEmpty
import kotlinx.coroutines.selects.select

class ViewPortfolioActivity : AppCompatActivity()
{
    private var state : Bundle? = null
    private var selectedCurrency : String? = null
    private var confirmationDelete : Int = 0
    /**
     * Help function to display the investments
     * for the currently selected portfolio
     */
    @RequiresApi(Build.VERSION_CODES.N)
    private fun showInvestments()
    {
        val selectedPortfolio : Portfolio? = PortfoliusState.getCurrentlySelectedPortfolio()

        if (null == selectedPortfolio)
        {
            Notification.error(this, "No selected portfolio (showInvestments())")
            finish()
        }

        val ll : LinearLayout? = findViewById<LinearLayout>(R.id.linearLayoutPortfolioList)
        val map : MutableMap<String,MutableList<Investment>> = selectedPortfolio!!.getInvestments()
        val res : Resources = applicationContext.resources
        val fmt : String = res.getString(R.string.investment_view_format_string)

        /*
         * We need to remove all the views from the layout
         * as this method will be called from onRestart(),
         * refilling the LinearLayout with TextViews of
         * investments (so they would appear >1 time)
         */
        if (!ll?.isEmpty()!!)
            ll?.removeAllViews()

        if (map.isEmpty())
        {
            val tv = TextView(this)
            tv.text = applicationContext.resources.getString(R.string.no_investments_string)
            ll?.addView(tv)
        }
        else
        {
            val listsToRemove : MutableList<String> = mutableListOf<String>()

            for (key in map.keys)
            {
                val button = Button(this)
                button.setBackgroundColor(0xffee88)

                if (map[key]!!.isEmpty())
                {
                    listsToRemove.add(key)
                    continue
                }

                try
                {
                    button.text = String.format(fmt, key, selectedPortfolio.netForCurrency(key))
                    ll?.addView(button)

                    button.setOnClickListener {

                        if (it is Button)
                        {
                            val tmp = it.text.split(" ")
                            this.selectedCurrency = tmp[0]
                            PortfoliusState.setCurrentlySelectedInvestmentCurrency(tmp[0])
                            startActivity(Intent(this, ViewInvestmentsActivity::class.java))
                        }
                    }
                }
                catch (e : NoInvestmentListForCurrencyException)
                {
                    Notification.error(this, e.message)
                }
            }

            for (key in listsToRemove)
            {
                map.remove(key)
            }
        }

        findViewById<Button>(R.id.buttonDeletePortfolio)
            .setOnClickListener {

                PortfolioManager.removePortfolioByName(selectedPortfolio._name)
                Notification.send(this, "Removed portfolio")

                finish()
            }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)

        val selectedPortfolio : Portfolio? = PortfoliusState.getCurrentlySelectedPortfolio()

        if (null == selectedPortfolio)
        {
            Notification.error(this, "No portfolio is selected (onCreate() in ViewPortfolioActivity)")
            finish()
        }

        setContentView(R.layout.view_portfolio)

        findViewById<TextView>(R.id.textViewPortfolioName)?.setText(selectedPortfolio!!._name)

        showInvestments()


//        for (i in 1..10)
//        {
//            val tv = TextView(this)
//            tv.text = "Investment $i : +£1711.96"
//            ll?.addView(tv)
//        }

        findViewById<Button>(R.id.buttonAddNewInvestment)
            ?.setOnClickListener {

                startActivity(Intent(this, AddInvestmentActivity::class.java))
            }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onRestart()
    {
        super.onRestart()

        showInvestments()
    }
}