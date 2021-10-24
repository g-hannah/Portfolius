package com.ghannah

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Test

@RunWith(AndroidJUnit4::class)
class InvestmentTest
{
    companion object
    {
        private const val CURRENCY_NAME : String = "BTC"
    }

    /*
     * Data for mock investment
     */
    private val amount1 : Double = 1.0
    private val rate1 : Double = 30000.0
    private val fee1 : Double = 5.0

    private val amount2 : Double = 1.7
    private val rate2 : Double = 43274.0
    private val fee2 : Double = 11.11

    private val inv1 = Investment(CURRENCY_NAME, amount1, rate1, fee1)
    private val inv2 = Investment(CURRENCY_NAME, amount2, rate2, fee2)

    @Test
    fun test_costIsCorrect()
    {
        assertEquals(30005.0, inv1.getCost(), 0.01)
        assertEquals(73576.91, inv2.getCost(), 0.01)
    }

    @Test
    fun test_effectiveRateIsCorrect()
    {
        /*
         * Effective rate is (AMOUNT * RATE + FEE) / AMOUNT
         * inv1 -> 30005.0
         * inv2 -> 43280.53529411765
         */
        assertEquals(30005.0, inv1.getEffectiveRate(),0.01)
        assertEquals(43280.53529411765, inv2.getEffectiveRate(), 0.01)
    }
}