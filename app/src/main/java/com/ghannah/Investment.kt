package com.ghannah

/**
 * A class to store information about
 * an individual investment which
 * belongs to some portfolio
 */
class Investment(
    _amount : Double,
    _rate : Double,
    _fee : Double
    ) {

    private val amount : Double = _amount
    private val rate : Double = _rate
    private val fee : Double = _fee

    public fun getEffectiveRate() : Double
    {
        return ((this.amount * this.rate) + this.fee) / this.amount
    }

    public fun net() : Double
    {
        return 0.0
    }
}