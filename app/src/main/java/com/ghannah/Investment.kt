package com.ghannah

import kotlin.properties.Delegates

/**
 * A class to store information about
 * an individual investment which
 * belongs to some portfolio
 */
class Investment
{

//    private val amount : Double = _amount
//    private val rate : Double = _rate
//    private val fee : Double = _fee
    private var amount by Delegates.notNull<Double>()
    private var rate by Delegates.notNull<Double>()
    private var fee by Delegates.notNull<Double>()
    private lateinit var id : String

    constructor(_amount : Double, _rate : Double, _fee : Double)
    {
        this.amount = _amount
        this.rate = _rate
        this.fee = _fee
        this.id = _generateId()
    }

    private fun _generateId() : String
    {
        return System.currentTimeMillis().toString()
    }

    public fun getId() : String
    {
        return this.id
    }

    public fun getEffectiveRate() : Double
    {
        return ((this.amount * this.rate) + this.fee) / this.amount
    }

    public fun net() : Double
    {
        return 0.0
    }
}