package com.ghannah

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Test

@RunWith(AndroidJUnit4::class)
class InvestmentTest
{
    private val amount : Double = 1.0
    private val rate : Double = 30000.0
    private val fee : Double = 5.0
    private val inv = Investment("BTC", amount, rate, fee)

    @Test
    fun cost_isCorrect()
    {
        assertEquals(30005.0, inv.getCost(), 0.01)
    }

    @Test
    fun effectiveRate_isCorrect()
    {
        /*
         * Effective rate is (AMOUNT * RATE + FEE) / AMOUNT
         * == 30005 / 1
         * == 30005
         */
        assertEquals(30005.0, inv.getEffectiveRate(),0.01)
    }
}