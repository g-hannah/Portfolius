package com.ghannah

import kotlinx.coroutines.sync.Mutex

class ExchangeRatesManager()
{
    //private val rates : MutableMap<String,Rate?> = mutableMapOf<String,Rate?>()
    private val historic_rates : MutableMap<String,MutableList<Rate>?> = mutableMapOf<String,MutableList<Rate>?>()

    private fun writeRatesToDisk()
    {

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

    public fun getRate(currency : String) : Rate?
    {
        val list : MutableList<Rate>? = historic_rates.get(currency)

        if (null == list)
            return null
        else
            return list.get(list.size - 1)
    }
}