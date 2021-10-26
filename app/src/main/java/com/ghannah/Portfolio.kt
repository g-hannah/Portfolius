package com.ghannah

import android.os.Build
import androidx.annotation.RequiresApi
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * A class to store information for a
 * given portfolio. Contains a list of
 * investments made in this portfolio
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class Portfolio(var _name : String)
{
    private val name : String = _name
    private val created = System.currentTimeMillis()

    /*
     * For a given portfolio, a user can have investments
     * for different currencies. So the name of the currency
     * maps to the list of investments.
     *
     * CURRENCY -> LIST
     */
    private val mapInvestments : MutableMap<String,MutableList<Investment>> = mutableMapOf<String,MutableList<Investment>>()

    fun getInvestments() : MutableMap<String,MutableList<Investment>>
    {
        return this.mapInvestments
    }

    /**
     * Return the net gain / loss
     * over the whole portfolio
     */
    fun net() : Double
    {
        var res : Double = 0.0

        for (key in mapInvestments.keys)
        {
            val list : MutableList<Investment>? = mapInvestments[key]

            if (null == list)
                continue

            for (investment in list)
                res += investment.net()
        }

        return res

    }

    @Throws(NoInvestmentListForCurrencyException::class)
    fun netForCurrency(currency : String) : Double
    {
        var list : MutableList<Investment>? = mapInvestments[currency]

        if (null == list)
            throw NoInvestmentListForCurrencyException("No investments in portfolio for $currency")

        var res : Double = 0.0

        for (investment in list)
            res += investment.net()

        return res
    }

    /**
     * Calculate the net change in
     * value for a given rate
     */
    fun netForRate(rate : Rate) : Double
    {
        var ret : Double = 0.0

        for (key in mapInvestments.keys)
        {
            val list : MutableList<Investment> = mapInvestments[key] ?: continue

            for (investment in list)
            {
                val amount : Double = investment.getAmount()
                val fee : Double = investment.getFee()
                val rateThen : Double = investment.getRate()
                val paid : Double = amount * rateThen + fee
                val valueNow : Double = amount * rate.getValue()

                ret += (valueNow - paid)
            }
        }

        return ret
    }

    /**
     * Add an investment to list of investments
     * for a given cryptocurrency
     */
    fun addInvestment(inv : Investment)
    {
        var list : MutableList<Investment>? = mapInvestments[inv.getCurrency()]

        if (null == list)
            list = mutableListOf<Investment>()

        list.add(inv)
        mapInvestments.put(inv.getCurrency(), list)

        PortfolioManager.write()
    }

    /**
     * Remove an investment from the list
     * of investments for a given cryptocurrency
     */
    @RequiresApi(Build.VERSION_CODES.N)
    fun removeInvestment(inv : Investment)
    {
        var list : MutableList<Investment>? = mapInvestments[inv.getCurrency()]

        if (null != list)
        {
            list.remove(inv)
            mapInvestments.put(inv.getCurrency(), list)

            PortfolioManager.write()
        }
    }

//    public fun getInvestmentById(id : String) : Investment?
//    {
//        for (currency in mapInvestments.keys)
//        {
//            val list : MutableList<Investment>? = mapInvestments.get(currency)
//
//            if (null == list)
//                continue
//
//            for (investment in list)
//            {
//                if (investment.getId().equals(id))
//                    return investment
//            }
//        }
//
//        return null
//    }

    @JsonIgnore
    fun getValue() : Double
    {
        var ret : Double = 0.0

        for (key in mapInvestments.keys)
        {
            val rate : Rate = ExchangeRatesManager.getRateForCurrency(key) ?: continue
            val list : MutableList<Investment> = mapInvestments[key] ?: continue

            for (investment in list)
            {
                ret += (investment.getAmount() * rate.getValue())
            }
        }

        return ret
    }

    override fun toString(): String
    {
        val value : String = "£%.2f".format(getValue())
        val n : String = "£%.2f".format(net())

        return "$name, value $value, net $n"
    }
}