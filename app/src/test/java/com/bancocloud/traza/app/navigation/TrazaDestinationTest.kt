package com.bancocloud.traza.app.navigation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test

class TrazaDestinationTest {
    @Test
    fun topLevelEntries_matchDocumentedAdminMobileNavigation() {
        assertEquals(
            listOf("home", "onboarding", "operations", "dataops", "copilot"),
            TrazaDestination.topLevelEntries.map(TrazaDestination::route),
        )
    }

    @Test
    fun unknownRoute_fallsBackToHome() {
        assertSame(TrazaDestination.Home, TrazaDestination.fromRoute("unknown"))
    }
}
