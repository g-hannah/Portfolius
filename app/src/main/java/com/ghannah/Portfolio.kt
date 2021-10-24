package com.ghannah

/**
 * A class to store information for a
 * given portfolio. Contains a list of
 * investments made in this portfolio
 */
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
    }

    /**
     * Remove an investment from the list
     * of investments for a given cryptocurrency
     */
    fun removeInvestment(inv : Investment)
    {
        var list : MutableList<Investment>? = mapInvestments[inv.getCurrency()]

        if (null != list)
        {
            list.remove(inv)
            mapInvestments.put(inv.getCurrency(), list)
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

    override fun toString(): String
    {
        val n : String = "£%.2f".format(net())
        return "Portfolio: $_name Value: $n"
    }
}