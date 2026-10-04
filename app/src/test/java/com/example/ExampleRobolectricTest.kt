package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.ai.AiServiceImpl
import com.example.domain.model.AfricanCountries
import com.example.domain.model.Platform
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("AfricaCreator", appName)
    }

    @Test
    fun `ai service generates valid offline creator pack`() = runBlocking {
        val aiService = AiServiceImpl()
        assertTrue(aiService.isDemoMode)

        val pack = aiService.generateQuickPack(
            idea = "Business de livraison écolo",
            country = "Côte d'Ivoire",
            platform = Platform.TIKTOK
        )

        assertNotNull(pack)
        assertTrue(pack.hooks.isNotEmpty())
        assertTrue(pack.script.intro.isNotBlank())
        assertTrue(pack.hashtags.isNotEmpty())
        assertTrue(pack.videoPrompt.contains("9:16"))
    }

    @Test
    fun `african countries list contains key nations`() {
        val list = AfricanCountries.list
        assertTrue(list.any { it.name == "Togo" })
        assertTrue(list.any { it.name == "Bénin" })
        assertTrue(list.any { it.name == "Côte d'Ivoire" })
        assertTrue(list.any { it.name == "Sénégal" })
        assertTrue(list.any { it.name == "Cameroun" })
    }
}
