package com.ghannah

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * A class to store information about
 * an individual investment which
 * belongs to some portfolio
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class Investment(
    var _currency : String,
    var _amount : Double,
    var _rate : Double,
    var _fee : Double
)
{

    private var currency : String = _currency
    private var amount : Double = _amount
    private var rate : Double = _rate
    private var fee : Double = _fee

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

    @JsonIgnore
    fun getEffectiveRate() : Double
    {
        return ((this.amount * this.rate) + this.fee) / this.amount
    }

    @JsonIgnore
    fun getCost() : Double
    {
        return ((this.amount * this.rate) + this.fee)
    }

    @JsonIgnore
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