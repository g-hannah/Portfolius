package com.ghannah

class Rate(_value : Double, _timestamp : Long)
{
    private val value : Double = _value
    private val timestamp : Long = _timestamp

    fun getValue() : Double
    {
        return this.value
    }
}