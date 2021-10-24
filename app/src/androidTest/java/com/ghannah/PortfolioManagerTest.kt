package com.ghannah

import android.os.Handler
import android.os.Looper
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PortfolioManagerTest
{
    /**
     * Test that we are getting correct values when
     * calculating the percentage difference between
     * a current value and a previous value.
     */
    @Test
    fun test_correctPercentageDifferenceIsCalculated()
    {
        Handler(Looper.getMainLooper()).post {
            Assert.assertEquals(
                100.0,
                PortfolioManager.getPercentageDifferenceBetweenValues(200.0, 100.0),
                0.01
            )
            Assert.assertEquals(
                -300.0,
                PortfolioManager.getPercentageDifferenceBetweenValues(-200.0, 100.0),
                0.01
            )
            Assert.assertEquals(
                300.0,
                PortfolioManager.getPercentageDifferenceBetweenValues(200.0, -100.0),
                0.01
            )
            Assert.assertEquals(
                -100.0,
                PortfolioManager.getPercentageDifferenceBetweenValues(-200.0, -100.0),
                0.01
            )
            Assert.assertEquals(
                -50.0,
                PortfolioManager.getPercentageDifferenceBetweenValues(100.0, 200.0),
                0.01
            )
            Assert.assertEquals(
                -150.0,
                PortfolioManager.getPercentageDifferenceBetweenValues(-100.0, 200.0),
                0.01
            )
            Assert.assertEquals(
                150.0,
                PortfolioManager.getPercentageDifferenceBetweenValues(100.0, -200.0),
                0.01
            )
            Assert.assertEquals(
                50.0,
                PortfolioManager.getPercentageDifferenceBetweenValues(-100.0, -200.0),
                0.01
            )
            Assert.assertEquals(
                196.29629629,
                PortfolioManager.getPercentageDifferenceBetweenValues(80.0, 27.0),
                0.01
            )
            Assert.assertEquals(
                288.135593,
                PortfolioManager.getPercentageDifferenceBetweenValues(111.0, -59.0),
                0.01
            )
        }
    }
}