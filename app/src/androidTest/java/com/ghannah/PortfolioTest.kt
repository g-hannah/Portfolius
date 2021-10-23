package com.ghannah

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.After
import org.junit.runner.RunWith

import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class PortfolioTest
{
    companion object
    {
        private const val PORTFOLIO_NAME : String = "MainPortfolio"
        private const val CURRENCY_NAME : String = "BTC"
    }

//    @Test(expected=PortfolioExistsException::class)
//    fun portfolioExistsException_isThrown()
//    {
//        val portfolio1 = Portfolio(PORTFOLIO_NAME)
//        val portfolio2 = Portfolio(PORTFOLIO_NAME)
//
//        PortfolioManager.addPortfolio(portfolio1)
//        PortfolioManager.addPortfolio(portfolio2)
//    }

    @Test
    fun correctNetChangeCalculated()
    {
        /*
         * Create some mock exchange rates data
         */
        val data : MutableMap<String,MutableList<Rate>> = mutableMapOf<String,MutableList<Rate>>()
        val list : MutableList<Rate> = mutableListOf<Rate>()
        val mockRate : Double = 40000.0

        list.add(Rate(mockRate, System.currentTimeMillis()))
        data.put(CURRENCY_NAME, list)

        ExchangeRatesManager.setRatesData(data)

        /*
         * Choose values for mock investment data
         */
        val amount1 : Double = 1.0
        val rate1 : Double = 30000.0
        val fee1 : Double = 10.0
        val amount2 : Double = 2.0
        val rate2 : Double = 20000.0
        val fee2 : Double = 5.0

        val inv1 = Investment(CURRENCY_NAME, amount1, rate1, fee1)
        val inv2 = Investment(CURRENCY_NAME, amount2, rate2, fee2)

        val portfolio = Portfolio(PORTFOLIO_NAME)

        portfolio.addInvestment(inv1)
        portfolio.addInvestment(inv2)

        /*
         * 120,000
         */
        val currentValue : Double = (amount1 + amount2) * mockRate

        /*
         * 30,010
         */
        val cost1 : Double = amount1 * rate1 + fee1

        /*
         * 40,005
         */
        val cost2 : Double = amount2 * rate2 + fee2

        /*
         * 49,985
         */
        val netChangeInValue : Double = currentValue - (cost1 + cost2)

        assertEquals(netChangeInValue, portfolio.net(),0.01)
    }

//    @After
//    fun cleanup()
//    {
//        /*
//         * Remove the mock portfolio we added
//         */
//        PortfolioManager.removePortfolioByName(PORTFOLIO_NAME)
//
//        /*
//         * Reset the data to an empty map
//         */
//        ExchangeRatesManager.setRatesData(mutableMapOf<String,MutableList<Rate>>())
//    }
}