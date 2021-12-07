package com.ghannah

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

import java.io.File
import java.io.FileOutputStream
import java.io.IOException

data class UserData(var totalGainOrLoss : Double)
{
    private val _totalGainOrLoss : Double = totalGainOrLoss
}

/**
 * A singleton class to manage specific application state
 * and also manage persistent data such as all-time
 * net gain or loss.
 */
object PortfoliusState
{
    /*
     * Unfortunately, there seems to be no way of retreiving
     * 'filesDir' variable from within this object (or
     * seemingly from any class that isn't somehow part
     * of the application GUI). So we have to explicitly
     * set this variable on application startup.
     */
    private var DATA_DIRECTORY : String? = null
    private val mapper = jacksonObjectMapper()

    fun setDataDirectory(path : String)
    {
        this.DATA_DIRECTORY = path
    }

    fun getDataDirectory() : String
    {
        return this.DATA_DIRECTORY!!
    }

    /*
     * At the start of several activities, we need access
     * to a specific portfolio or investment from a portfolio.
     * This is where we save pointers to those for later use.
     */
    private var selectedPortfolio : Portfolio? = null
    private var selectedInvestment : Investment? = null
    private var selectedInvestmentCurrency : String? = null

    /*
     * Persistent data
     *
     * Everytime a user marks an investment as sold, the
     * net gain or loss is added to this variable and
     * stored on disk
     */
    private var totalGainOrLoss : Double? = null

    @Throws(IOException::class, JsonProcessingException::class)
    private fun writeUserData()
    {
        if (null != this.DATA_DIRECTORY && null != this.totalGainOrLoss)
        {
            val obj = UserData(this.totalGainOrLoss!!)
            val json : String = mapper.writeValueAsString(obj)
            FileOutputStream(File("$DATA_DIRECTORY/data.json")).use {
                it.write(json.toByteArray())
            }
        }
    }

    @Throws(IOException::class, JsonProcessingException::class)
    private fun readUserData()
    {
        if (null != this.DATA_DIRECTORY)
        {
            val file = File("$DATA_DIRECTORY/data.json")

            if (file.exists())
            {
                val json : String = file.readText(Charsets.UTF_8)
                val obj : UserData = mapper.readValue<UserData>(json)
                this.totalGainOrLoss = obj.totalGainOrLoss
            }
        }
    }

    @Throws(IOException::class)
    fun addToTotalGainOrLoss(value : Double)
    {
        if (null == totalGainOrLoss)
        {
            totalGainOrLoss = value
        }
        else
        {
            /*
             * Have to add this if-statement to make
             * the compiler happy as it will not
             * smart-cast to Double since it's mutable
             * and could have change by this point.
             */
            if (totalGainOrLoss is Double)
               totalGainOrLoss = totalGainOrLoss!! + value
        }

        writeUserData()
    }

    fun getTotalGainOrLoss() : Double
    {
        if (null == totalGainOrLoss)
            readUserData()

        if (null == totalGainOrLoss)
            return 0.0

        return totalGainOrLoss!!
    }

    fun resetTotalGainOrLoss()
    {
        this.totalGainOrLoss = 0.0
    }

    fun setCurrentlySelectedPortfolio(portfolio : Portfolio)
    {
        this.selectedPortfolio = portfolio
    }

    fun setCurrentlySelectedInvestment(investment : Investment)
    {
        this.selectedInvestment = investment
    }

    fun setCurrentlySelectedInvestmentCurrency(value : String)
    {
        this.selectedInvestmentCurrency = value
    }

    fun getCurrentlySelectedPortfolio() : Portfolio?
    {
        return this.selectedPortfolio
    }

    fun getCurrentlySelectedInvestment() : Investment?
    {
        return this.selectedInvestment
    }

    fun getCurrentlySelectedInvestmentCurrency() : String?
    {
        return this.selectedInvestmentCurrency
    }

    fun unsetSelectedPortfolio()
    {
        this.selectedPortfolio = null
    }

    fun unsetSelectedInvestment()
    {
        this.selectedInvestment = null
    }
}