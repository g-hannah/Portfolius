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

        PortfolioManager.read(filesDir.toString())


        setContentView(R.layout.activity_main)

        /*
            This value will be calculated over
            all user's portfolios
         */
        // findViewById<TextView>(R.id.totalNetChange).text = PortfolioContainer.net().toString()
        findViewById<TextView>(R.id.totalNetChange).text = PortfolioManager.netFormatted()
        findViewById<TextView>(R.id.textViewNetChange).text = "+£0.00"
        findViewById<TextView>(R.id.textViewNetPercentageChange).text = "+0.00%"

        /*
            Set up event handlers for various
            GUI components (such as button to
            create new portfolio)
         */
        findViewById<Button>(R.id.buttonCreatePortfolio)
            .setOnClickListener {

                startActivity(Intent(this, CreatePortfolioActivity::class.java))
            }

        val ll : LinearLayout = findViewById(R.id.linearLayoutPortfolios)
        val portfolios : MutableList<Portfolio> = PortfolioManager.getPortfolios()
        for (portfolio in portfolios)
        {
            var tv = TextView(this)
            tv.setText(portfolio.toString())
            ll.addView(tv)
        }

//        button.setOnClickListener {
//            startActivity(Intent(this, CreatePortfolio::class.java))
//        }

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