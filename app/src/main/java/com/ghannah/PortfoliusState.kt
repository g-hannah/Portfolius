package com.ghannah

object PortfoliusState
{
    private lateinit var selectedPortfolio : Portfolio
    private lateinit var selectedInvestment : Investment

    fun setCurrentlySelectedPortfolio(portfolio : Portfolio)
    {
        this.selectedPortfolio = portfolio
    }

    fun setCurrentlySelectedInvestment(investment : Investment)
    {
        this.selectedInvestment = investment
    }

    fun getCurrentlySelectedPortfolio() : Portfolio
    {
        return this.selectedPortfolio
    }

    fun getCurrentlySelectedInvestment() : Investment
    {
        return this.selectedInvestment
    }
}