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
    private val investments : MutableMap<String,MutableList<Investment>?> = mutableMapOf<String,MutableList<Investment>?>()

    public fun net() : Double
    {
        var res : Double = 0.0

       // for (investment in investments)
           // res += investment.net()

        return res
    }

    public fun addInvestment(currency : String, inv : Investment)
    {
        var list : MutableList<Investment>? = investments.get(currency)

        if (null == list)
            list = mutableListOf<Investment>()

        list.add(inv)
        investments.put(currency, list)
    }

    override fun toString(): String
    {
        val size : String = investments.size.toString()
        return "$_name:$created:$size"
    }
}