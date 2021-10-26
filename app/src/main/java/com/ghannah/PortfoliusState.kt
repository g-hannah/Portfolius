package com.ghannah

object PortfoliusState
{
    private var selectedPortfolio : Portfolio? = null
    private var selectedInvestment : Investment? = null
    private var selectedInvestmentCurrency : String? = null

    fun setCurrentlySelectedPortfolio(portfolio : Portfolio)
    {
        this.selectedPortfolio = portfolio
    }

    fun setCurrentlySelectedInvestment(investment : Investment)
    {
        this.selectedInvestment = investment
    }

    fun setCurrentlySelectedInvestmentCurrency(value : String)
    {
        this.selectedInvestmentCurrency = value
    }

    fun getCurrentlySelectedPortfolio() : Portfolio?
    {
        return this.selectedPortfolio
    }

    fun getCurrentlySelectedInvestment() : Investment?
    {
        return this.selectedInvestment
    }

    fun getCurrentlySelectedInvestmentCurrency() : String?
    {
        return this.selectedInvestmentCurrency
    }

    fun unsetSelectedPortfolio()
    {
        this.selectedPortfolio = null
    }
}