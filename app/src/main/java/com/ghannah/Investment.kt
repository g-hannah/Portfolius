package com.ghannah

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
    private val added : Long = System.currentTimeMillis()
    private val id : String = generateInvestmentId()

    private fun generateInvestmentId() : String
    {
        var fmt : String = "%4X-%4X"
        val endRange : Int = 4

        val part1 : String = String.format("%4X", added).substring(0, endRange)
        val part2 : String = String.format("%4X", (currency.hashCode() xor amount.hashCode() xor rate.hashCode() xor fee.hashCode())).substring(0, endRange)
        return "$part1-$part2"
    }

    fun getCurrency() : String
    {
        return this.currency
    }

    fun getAmount() : Double
    {
        return this.amount
    }

    fun setAmount(value : Double)
    {
        this.amount = value
    }

    fun getRate() : Double
    {
        return this.rate
    }

    fun setRate(value : Double)
    {
        this.rate = value
    }

    fun getFee() : Double
    {
        return this.fee
    }

    fun setFee(value : Double)
    {
        this.fee = value
    }

    fun getId() : String
    {
        return this.id
    }

    @JsonIgnore
    fun getWorth() : Double
    {
        var rate : Rate? = null
        val currency : String = this.currency

        rate = ExchangeRatesManager.getRateForCurrency(currency)
       // val rate : Rate? = ExchangeRatesManager.getRateForCurrency(this.currency)
        if (null != rate)
        {
            return this.amount * rate!!.getValue()
        }

        return 0.0
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
        var currentRate : Rate? = null
        val currency : String = this.currency
        currentRate = ExchangeRatesManager.getRateForCurrency(currency)

        if (null != currentRate)
        {
            val currentWorth : Double = this.amount * currentRate!!.getValue()
            return currentWorth - getCost()
        }

        return 0.0
    }

    @JsonIgnore
    override fun toString() : String
    {
        val r : String = "£%.2f".format(rate)

        return "$amount $currency @ $r"
    }
}