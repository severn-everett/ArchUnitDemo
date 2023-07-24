package com.severett.archunitdemo.hexagonal.domain

enum class CreditCardIssuer(val brand: String, val initialNums: List<UInt>, val limit: Int) {
    AMERICAN_EXPRESS("American Express", listOf(3u, 4u), 13),
    DINERS_CLUB_CARTE_BLANCHE("Diners Club Carte Blanche", listOf(3u, 0u, 0u), 11),
    DINERS_CLUB_INTERNATIONAL("Diners Club International", listOf(3u, 6u), 12),
    DINERS_CLUB_US_AND_CANADA("Diners Club US and Canada", listOf(5u, 4u), 14),
    DISCOVER_CARD("Discover Card", listOf(6u, 0u, 1u, 1u), 12),
    INSTAPAYMENT("InstaPayment", listOf(6u, 3u, 7u), 13),
    JCB("JCB", listOf(3u, 5u, 2u, 8u), 12),
    LASER("Laser", listOf(6u, 3u, 0u, 4u), 12),
    MAESTRO("Maestro", listOf(5u, 0u, 1u, 8u), 8),
    MASTERCARD("Mastercard", listOf(5u, 1u), 14),
    VISA("Visa", listOf(4u, 3u), 11),
    VISA_ELECTRON("Visa Electron", listOf(4u, 0u, 2u, 6u), 12),
}
