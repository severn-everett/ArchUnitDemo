package com.severett.archunitdemo.hexagonal.domain

enum class CreditCardIssuer(val brand: String, val initialNums: Int, val limit: Int) {
    AMERICAN_EXPRESS("American Express", 34, 13),
    DINERS_CLUB_CARTE_BLANCHE("Diners Club Carte Blanche", 300, 11),
    DINERS_CLUB_INTERNATIONAL("Diners Club International", 36, 12),
    DINERS_CLUB_US_AND_CANADA("Diners Club US and Canada", 54, 14),
    DISCOVER_CARD("Discover Card", 6011, 12),
    INSTAPAYMENT("InstaPayment", 637, 13),
    JCB("JCB", 3528, 12),
    LASER("Laser", 6304, 12),
    MAESTRO("Maestro", 5018, 8),
    MASTERCARD("Mastercard", 51, 14),
    VISA("Visa", 43, 11),
    VISA_ELECTRON("Visa Electron", 4026, 12),
}
