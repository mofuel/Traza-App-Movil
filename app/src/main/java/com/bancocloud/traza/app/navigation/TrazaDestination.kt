package com.bancocloud.traza.app.navigation

import androidx.annotation.StringRes
import com.bancocloud.traza.R

enum class TrazaDestination(
    val route: String,
    @param:StringRes val labelRes: Int,
) {
    Home(route = "home", labelRes = R.string.destination_home),
    Onboarding(route = "onboarding", labelRes = R.string.destination_onboarding),
    Operations(route = "operations", labelRes = R.string.destination_operations),
    DataOps(route = "dataops", labelRes = R.string.destination_data_ops),
    Copilot(route = "copilot", labelRes = R.string.destination_copilot),
    ;

    companion object {
        val topLevelEntries: List<TrazaDestination> = entries

        fun fromRoute(route: String?): TrazaDestination =
            entries.firstOrNull { it.route == route } ?: Home
    }
}
