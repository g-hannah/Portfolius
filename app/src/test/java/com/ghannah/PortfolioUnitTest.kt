package com.ghannah

import org.junit.Test
import org.junit.Assert.*

class PortfolioUnitTest
{
    private val name : String = "MainPortfolio"
    private val portfolio = Portfolio(name)

    @Test
    fun name_isCorrect()
    {
        assertEquals(portfolio._name, name)
    }
}