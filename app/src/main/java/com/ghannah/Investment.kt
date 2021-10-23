package com.ghannah

import kotlin.properties.Delegates

/**
 * A class to store information about
 * an individual investment which
 * belongs to some portfolio
 */
class Investment
{

//    private val amount : Double = _amount
//    private val rate : Double = _rate
//    private val fee : Double = _fee
    private var currency : String
    private var amount : Double
    private var rate : Double
    private var fee : Double

    constructor(_currency : String, _amount : Double, _rate : Double, _fee : Double)
    {
        this.currency = _currency
        this.amount = _amount
        this.rate = _rate
        this.fee = _fee
    }

    fun getCurrency() : String
    {
        return this.currency
    }

    fun getAmount() : Double
    {
        return this.amount
    }

    fun getRate() : Double
    {
        return this.rate
    }

    fun getFee() : Double
    {
        return this.fee
    }

    fun getEffectiveRate() : Double
    {
        return ((this.amount * this.rate) + this.fee) / this.amount
    }

    fun getCost() : Double
    {
        return ((this.amount * this.rate) + this.fee)
    }

    fun net() : Double
    {
        /*
         * Retrieve current rate for the currency
         * that this investment was for.
         *
         * e.g.,
         *
         * val r : Double = RatesManager.getRateForCurrency(this.currency)
         *
         * val currentValue : Double = r * this.amount
         * return currentValue - this.cost
         */
        val currentRate : Rate? = ExchangeRatesManager.getRateForCurrency("BTC")

        if (null != currentRate)
        {
            val currentWorth : Double = this.amount * currentRate.getValue()
            return currentWorth - getCost()
        }

        return 0.0
    }
}