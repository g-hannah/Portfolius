package com.ghannah

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class ViewInvestmentActivity : AppCompatActivity()
{
    @RequiresApi(Build.VERSION_CODES.N)
    private fun _deleteInvestment()
    {
        val p : Portfolio? = PortfoliusState.getCurrentlySelectedPortfolio()
        if (null != p)
        {
            val i : Investment? = PortfoliusState.getCurrentlySelectedInvestment()
            if (null != i)
            {
                p.removeInvestment(i)
                finish()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun showDetails()
    {
        val inv : Investment? = PortfoliusState.getCurrentlySelectedInvestment()

        if (null == inv)
        {
           // Notification.error(this, "No investment is selected")
            finish()
        }

        val investment : Investment = inv!!

        val fmt : String = "£%.2f"

        val cur : String? = PortfoliusState.getCurrentlySelectedInvestmentCurrency()
        var rate : Rate? = null

        if (null != cur)
            rate = ExchangeRatesManager.getRateForCurrency(cur)

        findViewById<TextView>(R.id.textViewInvestmentIdValue).text = investment.getId()
        findViewById<TextView>(R.id.textViewInvestmentAmountValue).text = investment.getAmount().toString()
        findViewById<TextView>(R.id.textViewInvestmentRateThenValue).text = fmt.format(investment.getRate())
        findViewById<TextView>(R.id.textViewInvestmentFeeValue).text = fmt.format(investment.getFee())
        findViewById<TextView>(R.id.textViewInvestmentCurrentRateValue).text = fmt.format(rate?.getValue())
        findViewById<TextView>(R.id.textViewInvestmentCostValue).text = fmt.format(investment.getCost())
        findViewById<TextView>(R.id.textViewInvestmentCurrentValueValue).text = fmt.format(investment.getWorth())

        val tvNet : TextView? = findViewById<TextView>(R.id.textViewInvestmentNetValue)

        val netChange : Double = investment.net()
        val gain : Boolean = 0.0 <= netChange

        tvNet?.text = fmt.format(investment.net())

        if (gain)
            tvNet?.setTextColor(resources.getColor(R.color.colorPositive))
        else
            tvNet?.setTextColor(resources.getColor(R.color.colorNegative))

        findViewById<Button>(R.id.buttonDeleteInvestment)
            .setOnClickListener {

                val builder : AlertDialog.Builder = AlertDialog.Builder(this)

                builder
                    .setMessage(resources.getString(R.string.confirmation_delete_investment))
                    .setCancelable(true)
                    .setPositiveButton("Yes", DialogInterface.OnClickListener {
                        dialog, id -> _deleteInvestment()
//                        dialog, id -> {
//                            val portfolio : Portfolio? = PortfoliusState.getCurrentlySelectedPortfolio()
//                            if (null != portfolio)
//                            {
//                                portfolio.removeInvestment(investment)
//                                Notification.send(this, "Deleted investment")
//                                finish()
//                            }
//                    }
                    })
                    .setNegativeButton("No", DialogInterface.OnClickListener {
                        dialog, id -> { }
                    })

                builder.create().show()
            }

        findViewById<Button>(R.id.buttonEditInvestment)
            .setOnClickListener {

                startActivity(Intent(this, EditInvestmentActivity::class.java))
            }

        findViewById<Button>(R.id.buttonInvestmentMarkAsSold)
            .setOnClickListener {

                startActivity(Intent(this, SoldInvestmentActivity::class.java))
            }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.view_investment)
        showDetails()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onRestart()
    {
        super.onRestart()
        showDetails()
    }
}