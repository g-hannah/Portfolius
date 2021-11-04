package com.ghannah

import kotlinx.coroutines.sync.Mutex

/**
 * A singleton object for dealing with exchange rates
 */
object ExchangeRatesManager
{
    //private val rates : MutableMap<String,Rate?> = mutableMapOf<String,Rate?>()
    private var historic_rates : MutableMap<String,MutableList<Rate>> = mutableMapOf<String,MutableList<Rate>>()

    private fun writeRatesToDisk()
    {

    }

    fun setRatesData(map : MutableMap<String,MutableList<Rate>>)
    {
        this.historic_rates = map
    }

    fun getRateForCurrencyAtTimepoint(currency : String, at : Int) : Rate
    {
        val fakeRate = Rate(1711.96, System.currentTimeMillis())

        val list : MutableList<Rate>? = historic_rates[currency]
        if (null == list)
            return fakeRate

        if (0 == list.size || 0 > at)
            return fakeRate

        if (at >= list.size)
            return list[list.size-1]
        else
            return list[at]
    }

    public fun start()
    {
        /*
            Test code
         */
        val list : MutableList<String> = mutableListOf("BTC", "ETH", "SOL", "ALGO")

        for (currency in list)
        {
            val rate : Rate = Rate(2764.87, System.currentTimeMillis())
            var history : MutableList<Rate>? = historic_rates.get(currency)

            if (null == history)
                history = mutableListOf<Rate>()

            history.add(rate)
        }

        /*
            Run in infinite loop, sleeping every X units of time,
            retrieving fresh rates from the backend server

            while (true)
            {
                // sleep
                // retrieve rates from backend server
                // lock mutex for map of rates
                // update the rates
                // unlock the mutex for map of rates
                // lock the mutex for the array of historic rates
                // add to the array and write data to disk
                // unlock the mutex for the array of historic rates
            }
         */
    }

    fun getRateForCurrency(currency : String) : Rate?
    {
        val list : MutableList<Rate>? = historic_rates.get(currency)

        if (null == list)
            return null
        else
            return list.get(list.size - 1)
    }
}