package com.ghannah

import android.content.Intent
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
import com.ghannah.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        PortfolioManager.read(filesDir.toString())

        /*
         * Save the currently selected portfolio in the state
         */
        val selectedPortfolio : Portfolio? = PortfolioManager.getFirstPortfolio()

        if (null != selectedPortfolio)
            PortfoliusState.setCurrentlySelectedPortfolio(selectedPortfolio)

        setContentView(R.layout.activity_main)

        /*
         * Get the total net change in value over all portfolios,
         */
        val totalNetChange : Double = PortfolioManager.net()
        val r : Rate = ExchangeRatesManager.getRateForCurrencyAtTimepoint("BTC", 24)
        val netChangeValue : Double = PortfolioManager.getNetChangeGivenRate(r)
        val netChangePercentage : Double = PortfolioManager.getNetPercentageChangeGivenRate(r)

        findViewById<TextView>(R.id.totalNetChange).text = "£%.2f".format(totalNetChange)
        findViewById<TextView>(R.id.textViewNetChange).text = "£%+.2f".format(netChangeValue)
        findViewById<TextView>(R.id.textViewNetPercentageChange).text = "%+.2f%%".format(netChangePercentage)

        /*
            Set up event handlers for various
            GUI components (such as button to
            create new portfolio)
         */
        findViewById<Button>(R.id.buttonCreatePortfolio)
            .setOnClickListener {

                startActivity(Intent(this, CreatePortfolioActivity::class.java))
            }

//        findViewById<Button>(R.id.buttonViewPortfolio)
//            .setOnClickListener {
//
//                startActivity(Intent(this, SelectPortfolioActivity::class.java))
//            }

        val ll : LinearLayout = findViewById(R.id.linearLayoutPortfolios)
        val portfolios : MutableList<Portfolio> = PortfolioManager.getPortfolios()
//        val mapButtons : MutableMap<String,Portfolio> = mutableMapOf<String,Portfolio>()

        for (portfolio in portfolios)
        {
//            var btn = Button(this)
            var tv = TextView(this)
//            var lil = LinearLayout(this)
//
//            lil.addView(tv)
//            lil.addView(btn)
//
//            mapButtons.put(btn.toString(), portfolio)

//            btn.setOnClickListener {
//                val p : Portfolio? = mapButtons.get(it.toString())
//                if (null != p)
//                {
//                    PortfoliusState.setCurrentlySelectedPortfolio(p)
//                    System.out.println("DEBUG: set selected portfolio: " + p.toString())
//                }
//            }

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