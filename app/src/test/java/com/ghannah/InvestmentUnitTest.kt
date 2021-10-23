package com.ghannah

import org.junit.Assert.*
import org.junit.Test

/**
 * Unit test for Investment class
 */
class InvestmentUnitTest
{
    private val amount : Double = 1.0
    private val rate : Double = 100.0
    private val fee : Double = 5.0
    private val inv = Investment(amount, rate, fee)

    @Test
    fun cost_isCorrect()
    {
        assertEquals(inv.getCost(), 105.0, 0.01)
    }

    @Test
    fun effectiveRate_isCorrect()
    {
        val er : Double = 105.0 / amount
        assertEquals(inv.getEffectiveRate(), er, 0.01)
    }
}