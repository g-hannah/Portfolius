package com.ghannah

import android.app.ActionBar
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.isEmpty
import com.ghannah.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default

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
            Rate(44601.54, ts - (hour * 21)),
            Rate(43789.32, ts - (hour * 22)),
            Rate(42493.29, ts - (hour * 23)),
            Rate(41098.29, ts - (hour * 24))
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
            Rate(2999.54, ts - (hour * 21)),
            Rate(2856.32, ts - (hour * 22)),
            Rate(2786.29, ts - (hour * 23)),
            Rate(2677.29, ts - (hour * 24))
        )

        ratesData["BTC"] = listBTC
        ratesData["ETH"] = listETH

        ExchangeRatesManager.setRatesData(ratesData)
        PortfoliusState.resetTotalGainOrLoss()
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
            tv.textAlignment = View.TEXT_ALIGNMENT_CENTER
            ll?.addView(tv)
        }
        else
        {
            for (portfolio in portfolios)
            {
                var _ll : LinearLayout = LinearLayout(this)
                var btn = Button(this)
                btn.setBackgroundColor(0xffffff)
                btn.height = ActionBar.LayoutParams.WRAP_CONTENT
                btn.textAlignment = View.TEXT_ALIGNMENT_CENTER
               // var btn = Button(this)

//                val mapButtonToPortfolio : MutableMap<Button,Portfolio> = mutableMapOf<Button,Portfolio>()
//                mapButtonToPortfolio.put(btn, portfolio)

                val mapTvToPortfolio : MutableMap<Button,Portfolio> = mutableMapOf<Button,Portfolio>()
                mapTvToPortfolio.put(btn, portfolio)

                btn.setOnClickListener {

                    val p : Portfolio? = mapTvToPortfolio.get(it)
                    if (null != p)
                    {
                        PortfoliusState.setCurrentlySelectedPortfolio(p)
                        startActivity(Intent(this, ViewPortfolioActivity::class.java))
                    }
                }

//                btn.setOnClickListener {
//
//                    val p : Portfolio? = mapButtonToPortfolio.get(it)
//                    PortfoliusState.setCurrentlySelectedPortfolio(p)
//                }
//
//                _ll.addView(tv)
//                _ll.addView(btn)

                btn.text = portfolio.toString()
                ll?.addView(btn)
            }
        }
    }

    private fun setTextViewValue(tv : TextView?, value : Double, fmt : String)
    {
        if (null != tv)
        {
            tv.text = fmt.format(value)

            if (0.0 > value)
            {
                tv.setTextColor(resources.getColor(R.color.colorNegative))
            }
            else
            {
                tv.setTextColor(resources.getColor(R.color.colorPositive))
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
        //setMockData()
//        val rate1 : Rate? = ExchangeRatesManager.getRateForCurrency("ETH")
//        val rate2 : Rate? = ExchangeRatesManager.getRateForCurrencyAtTimepoint("ETH", 24)
//        val ncv1 : Double = PortfolioManager.getDifferenceBetweenCurrentNetAndNetGivenRate(rate1!!)
//        val ncv2 : Double = PortfolioManager.getDifferenceBetweenCurrentNetAndNetGivenRate(rate2!!)
//        val ncp1 : Double = PortfolioManager.getPercentageDifferenceBetweenCurrentNetAndNetGivenRate(rate1!!)
//        val ncp2 : Double = PortfolioManager.getPercentageDifferenceBetweenCurrentNetAndNetGivenRate(rate2!!)
//        Notification.send(this, "" + rate1?.getValue())
//        Notification.send(this, "" + rate2?.getValue())
//        Notification.send(this, "" + ncv1)
//        Notification.send(this, "" + ncv2)
//        Notification.send(this, "" + ncp1)
//        Notification.send(this, "" + ncp2)

        GlobalScope.launch {

            val totalNetChange : Double = PortfolioManager.net()
            val netChangeValue : Double = PortfolioManager.getDifferenceBetweenCurrentNetAndNetForTimepoint(24)
            val netChangePercentage : Double = PortfolioManager.getPercentageDifferenceBetweenCurrentNetAndNetForTimepoint(24)
            val allTimeGainLoss : Double = PortfoliusState.getTotalGainOrLoss()

            /*
             * Display those values in their TextView objects in the GUI
             */
            findViewById<TextView>(R.id.totalNetChange).text = "£%.2f".format(totalNetChange)
            setTextViewValue(findViewById<TextView>(R.id.textViewNetChange), netChangeValue, "£%+.2f")
            setTextViewValue(findViewById<TextView>(R.id.textViewNetPercentageChange), netChangePercentage, "%+.2f%%")
            setTextViewValue(findViewById<TextView>(R.id.textViewAllTimeGainLossValue), allTimeGainLoss,"£%+.2f")
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*
            Start the exchange rates manager in separate thread of execution
         */
//        CoroutineScope(Default).launch {
//
//            ExchangeRatesManager.start()
//        }

        PortfoliusState.setDataDirectory(applicationContext.filesDir.toString())
       // PortfolioManager.DATA_DIRECTORY = applicationContext.filesDir.toString()
        PortfolioManager.read()
        setMockData()

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