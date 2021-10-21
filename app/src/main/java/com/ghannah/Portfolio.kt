package com.ghannah

/**
 * A class to store information for a
 * given portfolio. Contains a list of
 * investments made in this portfolio
 */
class Portfolio(var _name : String)
{
    private var name : String = _name
    private val created = System.currentTimeMillis()

    /*
     * For a given portfolio, a user can have investments
     * for different currencies. So the name of the currency
     * maps to the list of investments.
     *
     * CURRENCY -> LIST
     */
    private val mapInvestments : MutableMap<String,MutableList<Investment>> = mutableMapOf<String,MutableList<Investment>>()

    public fun net() : Double
    {
        var res : Double = 0.0

       // for (investment in investments)
           // res += investment.net()

        return res

    }

    public fun addInvestment(currency : String, inv : Investment)
    {
        var list : MutableList<Investment>? = mapInvestments.get(currency)

        if (null == list)
            list = mutableListOf<Investment>()

        list.add(inv)
        mapInvestments.put(currency, list)
    }

    public fun getInvestmentById(id : String) : Investment?
    {
        for (currency in mapInvestments.keys)
        {
            val list : MutableList<Investment>? = mapInvestments.get(currency)

            if (null == list)
                continue

            for (investment in list)
            {
                if (investment.getId().equals(id))
                    return investment
            }
        }

        return null
    }

    override fun toString(): String
    {
        val n : String = "£%.2f".format(net())
        return "Portfolio: $_name Value: $n"
    }
}