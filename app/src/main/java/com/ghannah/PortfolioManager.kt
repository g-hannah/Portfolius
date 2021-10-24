package com.ghannah

import android.os.Environment
import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

import java.io.File
import java.io.FileOutputStream

/**
 * Singleton class to manage portfolios
 */
object PortfolioManager : AppCompatActivity()
{
    private val portfolios = mutableListOf<Portfolio>()
    private val mapper = jacksonObjectMapper()
    private lateinit var DATA_DIRECTORY : String

    /*
     * In a typical class (not a singleton in Kotlin), to have
     * the equivalent of static variables that are stored in
     * static memory and shared by all instances of the class,
     * we need to do companion object { <shared variable here> }
     *
     * Since this is already a singleton class, we cannot do this;
     * but we don't need to since there will only be once instance
     * of this class anyway.
     */
    private const val DATA_FILE_NAME : String = "portfolios.json"

    /**
     * Unfortunately, we cannot call read() from here because
     * we need to access filesDir, but a NullPointerException
     * error is thrown as this is unfortunately not defined
     * within this singleton (despite the fact we inherited
     * AppCompatActivity so that it's visible).
     */
    init
    {
    }

    /**
     * Private method to read into memory the
     * user's portfolios (stored in a JSON file)
     *
     * The path to the application's private
     * data directory is passed as an argument,
     * which is then stored in an instance var.
     */
    fun read(dir : String)
    {
        this.DATA_DIRECTORY = dir
        val pathToFile = "$dir/$DATA_FILE_NAME"
        val file = File(pathToFile)

        /*
         * Just return if it doesn't exist
         */
        if (!file.exists())
            return

        val json : String = File(pathToFile).readText(Charsets.UTF_8)
        val list : MutableList<Portfolio> = mapper.readValue<MutableList<Portfolio>>(json)

        for (portfolio in list)
            portfolios.add(portfolio)

        for (portfolio in this.portfolios)
            println("PORTFOLIO: $portfolio")
    }

    /**
     * Private method to convert the array of
     * portfolios into a JSON string and write
     * it to disk
     */
    private fun write()
    {
        val json : String = mapper.writeValueAsString(portfolios)
        FileOutputStream(File("$DATA_DIRECTORY/$DATA_FILE_NAME")).use {
            it.write(json.toByteArray())
        }
    }

    /**
     * private helper function to check if a portfolio
     * with the given name already exists
     *
     * @return boolean
     */
    private fun portfolioWithNameExists(name : String) : Boolean
    {
        for (portfolio in portfolios)
            if (portfolio._name.equals(name))
                return true

        return false
    }

    /**
     * Return the first portfolio in the list
     */
    fun getFirstPortfolio() : Portfolio?
    {
        if (0 == portfolios.size)
            return null

        return portfolios[0]
    }

    /**
     * Calculate the difference between the current
     * net value and the net value given a specific
     * exchange rate as an argument
     */
    fun getDifferenceBetweenCurrentNetAndNetGivenRate(rate : Rate) : Double
    {
        return this.net() - this.netForRate(rate)
    }

    /**
     * Calculate the percentage difference between
     * the current net value and the net value given
     * a specific rate as an argument
     */
    fun getPercentageDifferenceBetweenCurrentNetAndNetGivenRate(rate : Rate) : Double
    {
        val currentNet : Double = this.net()
        val netForRate : Double = this.netForRate(rate)

        return currentNet / netForRate * 100.0
    }

    /**
     * Calculate the net change in value over
     * all the portfolios given a specific
     * exchange rate as an argument
     */
    private fun netForRate(rate : Rate) : Double
    {
        var ret : Double = 0.0

        for (portfolio in portfolios)
        {
            ret += portfolio.netForRate(rate)
        }

        return ret
    }

    /**
     * Add a new portfolio to the array if one
     * already does not exist with this name
     *
     * @return boolean (true for success, false for failure)
     */
    @Throws(PortfolioExistsException::class)
    fun addPortfolio(portfolio : Portfolio) : Boolean
    {
        if (portfolioWithNameExists(portfolio._name))
        {
            throw PortfolioExistsException("Portfolio with this name already exists")
        }

        portfolios.add(portfolio)

        /*
         * Write the updated data structure to disk
         */
        write()

        return true
    }

    /**
     * Remove portfolio with the given name
     */
    fun removePortfolioByName(name : String)
    {
        var toRemove : Portfolio? = null

        for (portfolio in portfolios)
        {
            if (portfolio._name.equals(name))
            {
                toRemove = portfolio
                break
            }
        }

        if (null != toRemove)
        {
            portfolios.remove(toRemove)
        }
    }

    /**
     * Search for and return a portfolio with
     * the given name if it exists.
     *
     * @return Portfolio or null
     */
    fun getPortfolioWithName(name : String) : Portfolio?
    {
        for (portfolio in portfolios)
            if (portfolio._name.equals(name))
                return portfolio

        return null
    }

    /**
     * Get the net gain or loss over all of
     * the existing portfolios and the
     * investments that make them up
     *
     * @return Double (net value)
     */
    fun net() : Double
    {
        var res : Double = 0.0

        for (portfolio in portfolios)
            res += portfolio.net()

        return res
    }

    fun netFormatted() : String
    {
        val n : Double = this.net()
//        System.out.println("FORMATTED: " + String.format("£%2.f", n))
        return String.format("£%.2f", n)
    }

    /**
     * Return the list of the portfolios
     */
    fun getPortfolios() : MutableList<Portfolio>
    {
        return this.portfolios
    }
}