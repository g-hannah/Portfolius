package com.ghannah

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class InvestmentViewActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.view_investment)

        val ctx = getApplicationContext()

        val keyPortfolio : String = ctx.getResources().getString(R.string.SELECTED_PORTFOLIO)
        val keyInvestment : String = ctx.getResources().getString(R.string.SELECTED_INVESTMENT)
        val portfolioName : String? = savedInstanceState?.getString(keyPortfolio)
        val investmentId : String? = savedInstanceState?.getString(keyInvestment)

        if (null == portfolioName || null == investmentId)
            return

        val portfolio : Portfolio? = PortfolioManager.getPortfolioWithName(portfolioName)

        if (null == portfolio)
            return

        val investment : Investment? = portfolio.getInvestmentById(investmentId)
    }
}