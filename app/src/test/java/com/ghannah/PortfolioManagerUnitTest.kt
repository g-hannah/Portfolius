package com.ghannah

import org.junit.Test
import org.junit.Assert.*

class PortfolioManagerUnitTest
{
//    companion object {
//        private const val CURRENCY_NAME : String = "BTC"
//        private const val PORTFOLIO_NAME : String = "TestPortfolio"
//    }
//
//    private lateinit var ctx : Context
//    private val mockRate : Double = 100.0
//    private val amount : Double = 1.0
//    private val rate : Double = 100.0
//    private val fee : Double = 0.0
//    private val inv = Investment(CURRENCY_NAME, amount, rate, fee)
//    private val portfolio = Portfolio(PORTFOLIO_NAME)

//    @Before
//    fun setupContext()
//    {
//        val data : MutableMap<String,MutableList<Rate>> = mutableMapOf<String,MutableList<Rate>>()
//        val rate = Rate(mockRate, System.currentTimeMillis())
//        val list : MutableList<Rate> = mutableListOf<Rate>()
//
//        list.add(rate)
//        data.put(CURRENCY_NAME, list)
//
//        ExchangeRatesManager.setRatesData(data)
//
//        portfolio.addInvestment(inv)
//
//        ExchangeRatesManager.setRatesData()
//        ctx = InstrumentationRegistry.getInstrumentation().targetContext
//        PortfolioManager.read(ctx.dataDir.toString())
//        PortfolioManager.addPortfolio(portfolio)
//    }

    @Test
    fun correctPercentageDifference_isCalculated()
    {
        /*
        200 / 100
        -200 / 100
        200 / -100
        -200 / -100

        100 / 200
        -100 / 200
        100 / -200
        -100 / -200
         */
        assertEquals(100.0, PortfolioManager.getPercentageDifferenceBetweenValues(200.0, 100.0), 0.01)
        assertEquals(-300.0, PortfolioManager.getPercentageDifferenceBetweenValues(-200.0, 100.0), 0.01)
        assertEquals(300.0, PortfolioManager.getPercentageDifferenceBetweenValues(200.0, -100.0), 0.01)
        assertEquals(-100.0, PortfolioManager.getPercentageDifferenceBetweenValues(-200.0, -100.0), 0.01)
        assertEquals(-50.0, PortfolioManager.getPercentageDifferenceBetweenValues(100.0, 200.0), 0.01)
        assertEquals(-150.0, PortfolioManager.getPercentageDifferenceBetweenValues(-100.0, 200.0), 0.01)
        assertEquals(150.0, PortfolioManager.getPercentageDifferenceBetweenValues(100.0, -200.0), 0.01)
        assertEquals(50.0, PortfolioManager.getPercentageDifferenceBetweenValues(-100.0, -200.0), 0.01)
    }
}