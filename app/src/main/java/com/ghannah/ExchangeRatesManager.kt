package com.ghannah

import android.os.Build
import androidx.annotation.RequiresApi
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import java.util.concurrent.CompletableFuture

/**
 * A singleton object for dealing with exchange rates
 */
object ExchangeRatesManager
{
    //private val rates : MutableMap<String,Rate?> = mutableMapOf<String,Rate?>()
    private var rates : MutableMap<String,MutableList<Rate>> = mutableMapOf<String,MutableList<Rate>>()
    private val server_ip : String = "192.168.0.20"
    private val server_port : Short = 34567.toShort()
    private val sleep_time : Long = 360000 // 6 minutes

    private fun writeRatesToDisk()
    {
        // TODO
    }

    fun setRatesData(map : MutableMap<String,MutableList<Rate>>)
    {
        this.rates = map
    }
/*
    fun getRateForCurrencyAtTimepoint(currency : String, at : Int) : Rate
    {
        val fakeRate = Rate(1711.96, System.currentTimeMillis())

        val list : MutableList<Rate>? = rates[currency]
        if (null == list)
            return fakeRate

        if (0 == list.size || 0 > at)
            return fakeRate

        if (at >= list.size)
            return list[list.size-1]
        else
            return list[at]
    }
 */

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
        val mapper : ObjectMapper = ObjectMapper()
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
        val mapper : ObjectMapper = ObjectMapper()
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
            this.rates.put(currency, mutableListOf<Rate>())

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

            Thread.sleep(36050)
        }
    }

    fun getRateForCurrency(currency : String) : Rate?
    {
        val list : MutableList<Rate>? = rates.get(currency)

        if (null == list)
            return null
        else
            return list.get(list.size - 1)
    }
}