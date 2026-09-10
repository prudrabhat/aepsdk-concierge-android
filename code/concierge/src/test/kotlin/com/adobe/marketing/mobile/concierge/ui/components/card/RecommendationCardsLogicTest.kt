/*
 * Copyright 2026 Adobe. All rights reserved.
 * This file is licensed to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. You may obtain a copy
 * of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR REPRESENTATIONS
 * OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.adobe.marketing.mobile.concierge.ui.components.card

import com.adobe.marketing.mobile.concierge.network.MultimodalElement
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class RecommendationCardsLogicTest {

    @Test
    fun `primaryActionButton builds a button from primaryText and primaryUrl`() {
        val element = MultimodalElement(
            id = "prod-1",
            content = mapOf(
                "primaryText" to "Buy now",
                "primaryUrl" to "https://example.com/checkout"
            )
        )

        val button = primaryActionButton(element)

        assertEquals(ProductActionButton(id = "prod-1_primary", text = "Buy now", url = "https://example.com/checkout"), button)
    }

    @Test
    fun `primaryActionButton allows a null url`() {
        val element = MultimodalElement(
            id = "prod-2",
            content = mapOf("primaryText" to "Buy now")
        )

        val button = primaryActionButton(element)

        assertEquals(ProductActionButton(id = "prod-2_primary", text = "Buy now", url = null), button)
    }

    @Test
    fun `primaryActionButton returns null when primaryText is missing`() {
        val element = MultimodalElement(id = "prod-3", content = emptyMap())

        assertNull(primaryActionButton(element))
    }

    @Test
    fun `primaryActionButton returns null when primaryText is blank`() {
        val element = MultimodalElement(
            id = "prod-4",
            content = mapOf("primaryText" to "   ")
        )

        assertNull(primaryActionButton(element))
    }

    @Test
    fun `secondaryActionButton builds a button from secondaryText and secondaryUrl`() {
        val element = MultimodalElement(
            id = "prod-5",
            content = mapOf(
                "secondaryText" to "Learn more",
                "secondaryUrl" to "https://example.com/details"
            )
        )

        val button = secondaryActionButton(element)

        assertEquals(ProductActionButton(id = "prod-5_secondary", text = "Learn more", url = "https://example.com/details"), button)
    }

    @Test
    fun `secondaryActionButton returns null when secondaryText is blank`() {
        val element = MultimodalElement(
            id = "prod-6",
            content = mapOf("secondaryText" to "   ")
        )

        assertNull(secondaryActionButton(element))
    }
}
