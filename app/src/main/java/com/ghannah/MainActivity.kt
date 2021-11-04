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

    /**
     * XXX
     *
     * Used until backend server is set up and the
     * exchange rates manager actually starts
     * retrieving live exchange rates.
     */
    @RequiresApi(Build.VERSION_CODES.N)
    private fun setMockData()
    {
//        val portfolio = Portfolio("MyPortfolio")
//
//        PortfolioManager.addPortfolio(portfolio)


        var ts : Long = System.currentTimeMillis()
        val hour : Long = (1000L * 60L * 60L)
        val ratesData : MutableMap<String,MutableList<Rate>> = mutableMapOf<String,MutableList<Rate>>()

        val listBTC : MutableList<Rate> = mutableListOf<Rate>(
            Rate(45833.01, ts),
            Rate(45412.43, ts - hour),
            Rate(45333.33, ts - (hour * 2)),
            Rate(45350.31, ts - (hour * 3)),
            Rate(44999.87, ts - (hour * 4)),
            Rate(44789.54, ts - (hour * 5)),
            Rate(44780.01, ts - (hour * 6)),
            Rate(44767.43, ts - (hour * 7)),
            Rate(44766.33, ts - (hour * 8)),
            Rate(44751.31, ts - (hour * 9)),
            Rate(44720.87, ts - (hour * 10)),
            Rate(44716.54, ts - (hour * 11)),
            Rate(44700.33, ts - (hour * 12)),
            Rate(44659.31, ts - (hour * 13)),
            Rate(44678.87, ts - (hour * 14)),
            Rate(44644.54, ts - (hour * 15)),
            Rate(44642.01, ts - (hour * 16)),
            Rate(44638.43, ts - (hour * 17)),
            Rate(44627.33, ts - (hour * 18)),
            Rate(44617.31, ts - (hour * 19)),
            Rate(44615.87, ts - (hour * 20)),
            Rate(44601.54, ts - (hour * 21))
        )

        val listETH : MutableList<Rate> = mutableListOf<Rate>(
            Rate(3265.01, ts),
            Rate(3217.43, ts - hour),
            Rate(3201.33, ts - (hour * 2)),
            Rate(3189.31, ts - (hour * 3)),
            Rate(3168.87, ts - (hour * 4)),
            Rate(3155.54, ts - (hour * 5)),
            Rate(3150.01, ts - (hour * 6)),
            Rate(3148.43, ts - (hour * 7)),
            Rate(3143.33, ts - (hour * 8)),
            Rate(3128.31, ts - (hour * 9)),
            Rate(3122.87, ts - (hour * 10)),
            Rate(3119.54, ts - (hour * 11)),
            Rate(3116.33, ts - (hour * 12)),
            Rate(3112.31, ts - (hour * 13)),
            Rate(3102.87, ts - (hour * 14)),
            Rate(3093.54, ts - (hour * 15)),
            Rate(3061.01, ts - (hour * 16)),
            Rate(3036.43, ts - (hour * 17)),
            Rate(3029.33, ts - (hour * 18)),
            Rate(3019.31, ts - (hour * 19)),
            Rate(3007.87, ts - (hour * 20)),
            Rate(2999.54, ts - (hour * 21))
        )

        ratesData["BTC"] = listBTC
        ratesData["ETH"] = listETH

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
        val allTimeGainLoss : Double = PortfoliusState.getTotalGainOrLoss()

        val gainOrLoss : String = if (0.0 > allTimeGainLoss) "Total loss " else "Total gain "

        /*
         * Display those values in their TextView objects in the GUI
         */
        findViewById<TextView>(R.id.totalNetChange).text = "£%.2f".format(totalNetChange)
        findViewById<TextView>(R.id.textViewNetChange).text = "£%+.2f".format(netChangeValue)
        findViewById<TextView>(R.id.textViewNetPercentageChange).text = "%+.2f%%".format(netChangePercentage)
        findViewById<TextView>(R.id.textViewAllTimeGainLossValue).text = gainOrLoss + "£%.2f".format(allTimeGainLoss)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PortfoliusState.setDataDirectory(applicationContext.filesDir.toString())
       // PortfolioManager.DATA_DIRECTORY = applicationContext.filesDir.toString()
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