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
import com.ghannah.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setMockData()
    {
        val data : MutableMap<String,MutableList<Portfolio>> = mutableMapOf<String,MutableList<Portfolio>>()
        val list : MutableList<Portfolio> = mutableListOf<Portfolio>()
        val portfolio = Portfolio("MyPortfolio")

        PortfolioManager.DATA_DIRECTORY = applicationContext.filesDir.toString()
        PortfolioManager.addPortfolio(portfolio)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

                startActivity(Intent(this, ViewPortfolioActivity::class.java))
            }

        /*
         * Display list of portfolios within the LinearLayout,
         * which is embedded within the ScrollView.
         */
        val ll : LinearLayout = findViewById(R.id.linearLayoutPortfolios)
        val portfolios : MutableList<Portfolio> = PortfolioManager.getPortfolios()

        for (portfolio in portfolios)
        {
            var tv = TextView(this)

            tv.setText(portfolio.toString())
            ll.addView(tv)
        }

//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        appBarConfiguration = AppBarConfiguration(navController.graph)
//        setupActionBarWithNavController(navController, appBarConfiguration)
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