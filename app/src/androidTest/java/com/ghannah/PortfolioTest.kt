package com.ghannah

import android.os.Handler
import android.os.Looper
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.Before
import org.junit.After
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PortfolioTest
{
    companion object
    {
        private const val PORTFOLIO_NAME : String = "MainPortfolio"
        private const val CURRENCY_NAME : String = "BTC"
    }

    private val data : MutableMap<String,MutableList<Rate>> = mutableMapOf<String,MutableList<Rate>>()
    private val list : MutableList<Rate> = mutableListOf<Rate>()

    /*
     * Mock exchange rate
     */
    private val mockRate : Double = 40000.0

    /*
     * Data for first mock investment
     */
    private val amount1 : Double = 1.0
    private val rate1 : Double = 30000.0
    private val fee1 : Double = 10.0

    /*
     * Data for second mock investment
     */
    private val amount2 : Double = 2.0
    private val rate2 : Double = 20000.0
    private val fee2 : Double = 5.0

    private lateinit var inv1 : Investment
    private lateinit var inv2 : Investment
    private lateinit var portfolio : Portfolio
    private lateinit var secondPortfolio : Portfolio

    /**
     * Set up mock data for the test context
     */
    @Before
    fun setupMockData()
    {
        list.add(Rate(mockRate, System.currentTimeMillis()))
        data.put(CURRENCY_NAME, list)
        ExchangeRatesManager.setRatesData(data)

        inv1 = Investment(CURRENCY_NAME, amount1, rate1, fee1)
        inv2 = Investment(CURRENCY_NAME, amount2, rate2, fee2)

        portfolio = Portfolio(PORTFOLIO_NAME)
        portfolio.addInvestment(inv1)
        portfolio.addInvestment(inv2)

        Handler(Looper.getMainLooper()).post {

            PortfolioManager.addPortfolioNoWrite(portfolio)
        }
    }

    /**
     * Add a portfolio that has the same name as
     * one already added.
     *
     * XXX
     *
     * Not working since to use PortfolioManager we
     * have to enclose it in a thread and the thrown
     * exception isn't caught here.
     */
//    @Test(expected=PortfolioExistsException::class)
//    fun portfolioExistsException_isThrown()
//    {
//        Handler(Looper.getMainLooper()).post {
//
//            secondPortfolio = Portfolio(PORTFOLIO_NAME)
//            PortfolioManager.addPortfolioNoWrite(secondPortfolio)
//        }
//    }

    /**
     * Test if the correct net value is
     * calculated for a given rate.
     */
    @Test
    fun test_forGivenRateCorrectNetValueIsCalculated()
    {
        val otherMockRate : Double = 25000.0
        val rate = Rate(otherMockRate, System.currentTimeMillis())

        val paid1 : Double = amount1 * rate1 + fee1
        val valueNow1 : Double = amount1 * mockRate
        val paid2 : Double = amount2 * rate2 + fee2
        val valueNow2 : Double = amount2 * mockRate

        val netForRate : Double = ((amount1 * otherMockRate) - paid1) + ((amount2 * otherMockRate) - paid2)
        val netNow : Double = (valueNow1 - paid1) + (valueNow2 - paid2)

        assertEquals(netNow - netForRate, netNow - portfolio.netForRate(rate), 0.01)
    }

    /**
     * Test that the correct net change
     * in value is calculated
     */
    @Test
    fun test_correctNetChangeIsCalculated()
    {
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

    @After
    fun cleanup()
    {
        Handler(Looper.getMainLooper()).post {
            /*
             * Remove the mock portfolio we added
             */
            PortfolioManager.removePortfolioByName(PORTFOLIO_NAME)

            /*
             * Reset the data to an empty map
             */
            ExchangeRatesManager.setRatesData(mutableMapOf<String,MutableList<Rate>>())
        }
    }
}