package com.ghannah

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.Dispatchers.Default
import java.util.concurrent.CompletableFuture
import kotlinx.coroutines.sync.withLock

/**
 * A singleton object for dealing with exchange rates
 */
object ExchangeRatesManager
{
    //private val rates : MutableMap<String,Rate?> = mutableMapOf<String,Rate?>()
    private var rates : MutableMap<String,MutableList<Rate>> = mutableMapOf<String,MutableList<Rate>>()
    private val server_ip : String = "192.168.0.16"
    private val server_port : Short = 34567.toShort()
    private val sleep_time : Int = 360000 // 6 minutes
    private val mapper : ObjectMapper = ObjectMapper()
    private val mutex : Mutex = Mutex()

    private fun writeRatesToDisk()
    {
        // TODO
    }

    fun setRatesData(map : MutableMap<String,MutableList<Rate>>)
    {
        this.rates = map
    }

    /**
     * Send request to the backend server to obtain
     * the list of currencies that are currently handled
     */
    @RequiresApi(Build.VERSION_CODES.N)
    private fun _get_available_currencies() : MutableList<String>
    {
        val req : ServerRequestHandler =
            ServerRequestHandler(server_ip, server_port)

        val request : String = "{ \"type\" : \"information\", \"request\" : \"currencies\" }"
        val future : CompletableFuture<String> = req.request(request);
        val response : String = future.get()

        val root : JsonNode = mapper.readTree(response)

        /*
            Response from server will be of type:

            {
                "status" : "ok",
                "data" : [
                    "ETH",
                    "BTC"
                ]
            }
         */

        val list : MutableList<String> = mutableListOf<String>()
        val status : JsonNode = root.at("status")

        if (status.isNull() || status.isEmpty() || status.asText().equals("error"))
            return list

        val data : JsonNode = root.at("data")
        if (data.isNull || data.isEmpty)
            return list

        if (data.isArray)
        {
            for (node in data)
            {
                val elem : String = node.asText()
                list.add(elem)
            }
        }

        return list
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun _get_rate(currency : String) : Rate?
    {
        val req : ServerRequestHandler =
            ServerRequestHandler(server_ip, server_port)

        val request : String = "{ \"type\" : \"rate\", \"currency\" : \"$currency\" }"
        val future : CompletableFuture<String> = req.request(request)
        val response : String = future.get()

        val root : JsonNode = mapper.readTree(response)

        /*
            Response from server will be of format:

            {
                "status" : "ok",
                "currency" : "<currency>",
                "data" : {
                    "timestamp" : 2833928420,
                    "rate" : 3478.65
                }
            }
         */

        val statusNode : JsonNode = root.at("status")

        if (statusNode.isNull() || statusNode.isEmpty() || statusNode.asText().equals("error"))
            return null

        val data : JsonNode = root.at("data")
        if (data.isNull || data.isEmpty)
            return null

        val tsNode : JsonNode = data.at("timestamp")
        val rateNode : JsonNode = data.at("rate")

        if (tsNode.isNull || rateNode.isNull || tsNode.isEmpty || rateNode.isEmpty)
            return null

        return Rate(rateNode.asDouble(), tsNode.asLong())
    }

    @RequiresApi(Build.VERSION_CODES.N)
    public fun start()
    {
        val currencies : MutableList<String> = _get_available_currencies();

        for (currency in currencies)
            rates.put(currency, mutableListOf<Rate>())

        while (true)
        {
            for (currency in currencies)
            {
                val list: MutableList<Rate> = rates.get(currency)!!
                val rate : Rate? = _get_rate(currency)
                if (null != rate)
                {
                    list.add(rate!!)
                }
            }

            Thread.sleep(1000L * 60L * 60L)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    public fun getRateForCurrencyAtTimepoint(currency : String, at : Int) : Rate?
    {
        val _at = at - 1

        if (0 > _at || 24 <= _at)
            return null

        var ret : Rate? = null

        val list : MutableList<Rate>? = rates[currency]

        if (null != list && 0 < list.size)
        {
            ret = list[_at]
        }

        return ret
    }

    public fun getRateForCurrency(currency : String) : Rate?
    {
        val list : MutableList<Rate>? = rates.get(currency)
        var rate : Rate? = null

        /*
            Lock the mutex to have exclusive
            access to the rates data structures
         */
        //mutex.lock()

        if (null != list)
        {
            rate = list.get(0)
        }

        //mutex.unlock()

        return rate
    }
}