package com.example.newsapp.domain.model

enum class Country(val code: String, val displayName: String, val flag: String) {
    UNITED_STATES("us", "United States", "🇺🇸"),
    EGYPT("eg",         "Egypt",         "🇪🇬"),
    UNITED_KINGDOM("gb","United Kingdom","🇬🇧"),
    GERMANY("de",       "Germany",       "🇩🇪"),
    FRANCE("fr",        "France",        "🇫🇷"),
    ITALY("it",         "Italy",         "🇮🇹"),
    SPAIN("es",         "Spain",         "🇪🇸"),
    BRAZIL("br",        "Brazil",        "🇧🇷"),
    RUSSIA("ru",        "Russia",        "🇷🇺"),
    CHINA("cn",         "China",         "🇨🇳"),
    JAPAN("jp",         "Japan",         "🇯🇵"),
    INDIA("in",         "India",         "🇮🇳"),
    CANADA("ca",        "Canada",        "🇨🇦"),
    AUSTRALIA("au",     "Australia",     "🇦🇺"),
    SAUDI_ARABIA("sa",  "Saudi Arabia",  "🇸🇦");

    companion object {
        fun fromCode(code: String): Country =
            values().find { it.code == code } ?: UNITED_STATES
    }
}