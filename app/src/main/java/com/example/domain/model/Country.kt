package com.example.domain.model

data class AfricanCountry(
    val code: String,
    val name: String,
    val flag: String,
    val defaultCity: String,
    val currency: String,
    val typicalAudiences: List<String> = listOf("Jeunes entrepreneurs", "Étudiants", "Grand public", "Communauté tech")
)

object AfricanCountries {
    val list = listOf(
        AfricanCountry("TG", "Togo", "🇹🇬", "Lomé", "FCFA"),
        AfricanCountry("BJ", "Bénin", "🇧🇯", "Cotonou", "FCFA"),
        AfricanCountry("GH", "Ghana", "🇬🇭", "Accra", "GHS"),
        AfricanCountry("CI", "Côte d'Ivoire", "🇨🇮", "Abidjan", "FCFA"),
        AfricanCountry("SN", "Sénégal", "🇸🇳", "Dakar", "FCFA"),
        AfricanCountry("CM", "Cameroun", "🇨🇲", "Douala", "FCFA"),
        AfricanCountry("NG", "Nigeria", "🇳🇬", "Lagos", "NGN"),
        AfricanCountry("CD", "RDC", "🇨🇩", "Kinshasa", "USD / CDF"),
        AfricanCountry("GN", "Guinée", "🇬🇳", "Conakry", "GNF"),
        AfricanCountry("ML", "Mali", "🇲🇱", "Bamako", "FCFA"),
        AfricanCountry("BF", "Burkina Faso", "🇧🇫", "Ouagadougou", "FCFA"),
        AfricanCountry("NE", "Niger", "🇳🇪", "Niamey", "FCFA"),
        AfricanCountry("GA", "Gabon", "🇬🇦", "Libreville", "FCFA"),
        AfricanCountry("CG", "Congo", "🇨🇬", "Brazzaville", "FCFA"),
        AfricanCountry("OTHER", "Autre pays d'Afrique", "🌍", "Capitale", "Devise locale")
    )

    fun findByCode(code: String): AfricanCountry {
        return list.find { it.code.equals(code, ignoreCase = true) } ?: list[0]
    }

    fun findByName(name: String): AfricanCountry {
        return list.find { it.name.equals(name, ignoreCase = true) } ?: list[0]
    }
}
