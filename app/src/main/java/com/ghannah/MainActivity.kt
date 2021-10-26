package com.ghannah

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.isEmpty
import com.ghannah.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setMockData()
    {
//        val portfolio = Portfolio("MyPortfolio")
//
//        PortfolioManager.addPortfolio(portfolio)


        val ratesData : MutableMap<String,MutableList<Rate>> = mutableMapOf<String,MutableList<Rate>>()
        val listBTC : MutableList<Rate> = mutableListOf<Rate>()
        val listETH : MutableList<Rate> = mutableListOf<Rate>()

        listBTC.add(Rate(45833.01, System.currentTimeMillis()))
        listETH.add(Rate(3072.13, System.currentTimeMillis()))
        ratesData.put("BTC", listBTC)
        ratesData.put("ETH", listETH)
        ExchangeRatesManager.setRatesData(ratesData)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun showPortfolios()
    {
        /*
         * Display list of portfolios within the LinearLayout,
         * which is embedded within the ScrollView.
         */
        val ll : LinearLayout? = findViewById(R.id.linearLayoutPortfolios)
        val portfolios : MutableList<Portfolio> = PortfolioManager.getPortfolios()

        if (!ll?.isEmpty()!!)
        {
            ll.removeAllViews()
        }

        if (portfolios.isEmpty())
        {
            val tv = TextView(this)
            tv.text = applicationContext.resources.getString(R.string.no_portfolios_string)
            ll?.addView(tv)
        }
        else
        {
            for (portfolio in portfolios)
            {
                var tv = TextView(this)

                tv.text = portfolio.toString()
                ll?.addView(tv)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun showStats()
    {
        /*
         * Retrieve current net value of all portfolios,
         * and calculate difference in value and percentage
         * difference between that current value and a given
         * value in the past (1 hour perhaps, or 24 hours ago)
         */
        val totalNetChange : Double = PortfolioManager.net()
        val r : Rate = ExchangeRatesManager.getRateForCurrencyAtTimepoint("BTC", 24)
        val netChangeValue : Double = PortfolioManager.getDifferenceBetweenCurrentNetAndNetGivenRate(r)
        val netChangePercentage : Double = PortfolioManager.getPercentageDifferenceBetweenCurrentNetAndNetGivenRate(r)

        /*
         * Display those values in their TextView objects in the GUI
         */
        findViewById<TextView>(R.id.totalNetChange).text = "£%.2f".format(totalNetChange)
        findViewById<TextView>(R.id.textViewNetChange).text = "£%+.2f".format(netChangeValue)
        findViewById<TextView>(R.id.textViewNetPercentageChange).text = "%+.2f%%".format(netChangePercentage)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PortfolioManager.DATA_DIRECTORY = applicationContext.filesDir.toString()
        PortfolioManager.read()
        setMockData()

        /*
            Create separate thread that will handle
            communication with the remote server.

            Get the rates history for cryptocurrencies

            Read local user data into memory (portfolios)
         */

        /*
         * Read the portfolios into memory.
         *
         * Pass the path to our application's
         * private data directory.
         */

        /*
         * Save the currently selected portfolio in the state
         */
        val selectedPortfolio : Portfolio? = PortfolioManager.getFirstPortfolio()

        if (null != selectedPortfolio)
            PortfoliusState.setCurrentlySelectedPortfolio(selectedPortfolio)

        /*
         * Set the content view for this activity
         */
        setContentView(R.layout.activity_main)


        showStats()
        showPortfolios()

        /*
         * Set click event handler for button
         * to create a new portfolio
         */
        findViewById<Button>(R.id.buttonCreatePortfolio)
            .setOnClickListener {

                startActivity(Intent(this, CreatePortfolioActivity::class.java))
            }

        /*
         * Set click event handler for button
         * for viewing the currently selected portfolio
         */
        findViewById<Button>(R.id.buttonViewPortfolio)
            .setOnClickListener {

                if (PortfolioManager.isEmpty())
                {
                    Notification.error(this, "No portfolios to view!")
                    return@setOnClickListener
                }

                startActivity(Intent(this, ViewPortfolioActivity::class.java))
            }



//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        appBarConfiguration = AppBarConfiguration(navController.graph)
//        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onRestart()
    {
        super.onRestart()

        showStats()
        showPortfolios()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}