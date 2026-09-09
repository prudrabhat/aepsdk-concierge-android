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

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adobe.marketing.mobile.concierge.network.MultimodalElement
import com.adobe.marketing.mobile.concierge.ui.theme.ConciergeLayout
import com.adobe.marketing.mobile.concierge.ui.theme.ConciergeProductCardCtaButtonColors
import com.adobe.marketing.mobile.concierge.ui.theme.ConciergeStyles
import com.adobe.marketing.mobile.concierge.ui.theme.ConciergeTheme
import com.adobe.marketing.mobile.concierge.ui.theme.ConciergeThemeColors
import com.adobe.marketing.mobile.concierge.ui.theme.ConciergeThemeConfig
import com.adobe.marketing.mobile.concierge.ui.theme.ConciergeThemeData
import com.adobe.marketing.mobile.concierge.ui.theme.ConciergeThemeTokens
import com.adobe.marketing.mobile.concierge.utils.image.DefaultImageProvider
import com.adobe.marketing.mobile.concierge.utils.image.LocalImageProvider

// Variants by title/description line count, all with price + was price
private val lineCountVariants = listOf(
    // 2 lines of title + 2 lines of description
    MultimodalElement(
        id = "v1",
        url = "https://picsum.photos/id/10/190/190",
        content = mapOf(
            "productName" to "Product Name Goes Here Long Title Two Lines",
            "productDescription" to "Subtitle text goes here to describe the product or campaign",
            "productPrice" to "\$399.99",
            "productWasPrice" to "\$599.99"
        )
    ),
    // 2 lines of title + 1 line of description
    MultimodalElement(
        id = "v2",
        url = "https://picsum.photos/id/20/190/190",
        content = mapOf(
            "productName" to "Product Name Goes Here Long Title Two Lines",
            "productDescription" to "Short subtitle text",
            "productPrice" to "\$399.99",
            "productWasPrice" to "\$599.99"
        )
    ),
    // 1 line of title + 2 lines of description
    MultimodalElement(
        id = "v3",
        url = "https://picsum.photos/id/30/190/190",
        content = mapOf(
            "productName" to "Product Name",
            "productDescription" to "Subtitle text goes here to describe the product or campaign",
            "productPrice" to "\$399.99",
            "productWasPrice" to "\$599.99"
        )
    ),
    // 1 line of title + 1 line of description
    MultimodalElement(
        id = "v4",
        url = "https://picsum.photos/id/40/190/190",
        content = mapOf(
            "productName" to "Product Name",
            "productDescription" to "Short subtitle text",
            "productPrice" to "\$399.99",
            "productWasPrice" to "\$599.99"
        )
    )
)

// Sample data covering the required content variations
private val sampleCards = listOf(
    // Title + subtitle + price + was price + badge
    MultimodalElement(
        id = "1",
        url = "https://picsum.photos/id/10/190/190",
        title = "Product Name Goes Here Long Title Two Lines",
        content = mapOf(
            "productName" to "Product Name Goes Here Long Title Two Lines",
            "productDescription" to "Subtitle text goes here to describe the product or campaign",
            "productPrice" to "\$399.99",
            "productWasPrice" to "\$599.99",
            "productBadge" to "Sale"
        )
    ),
    // Title + price only (no subtitle, no was price, no badge)
    MultimodalElement(
        id = "2",
        url = "https://picsum.photos/id/20/190/190",
        title = "Product Name Goes Here",
        content = mapOf(
            "productName" to "Product Name Goes Here",
            "productPrice" to "\$63.97",
            "primaryText" to "Buy now",
            "primaryUrl" to "#"
        )
    ),
    // Title + subtitle + price + badge (no was price)
    MultimodalElement(
        id = "3",
        url = "https://picsum.photos/id/30/190/190",
        title = "Product Name Goes Here Long Title Two Lines",
        content = mapOf(
            "productName" to "Product Name Goes Here Long Title Two Lines",
            "productDescription" to "Subtitle text goes here to describe the product or campaign",
            "productPrice" to "\$54.99",
            "productBadge" to "New Arrival"
        )
    ),
    // Title + price + was price (no subtitle, no badge)
    MultimodalElement(
        id = "4",
        url = "https://picsum.photos/id/40/190/190",
        title = "Product Name Goes Here",
        content = mapOf(
            "productName" to "Product Name Goes Here",
            "productPrice" to "\$139.95",
            "productWasPrice" to "\$179.95",
            "primaryText" to "Buy now",
            "primaryUrl" to "#"
        )
    ),
    // Title + subtitle + price (no was price, no badge)
    MultimodalElement(
        id = "5",
        url = "https://picsum.photos/id/50/190/190",
        title = "Product Name Goes Here Long Title Two Lines",
        content = mapOf(
            "productName" to "Product Name Goes Here Long Title Two Lines",
            "productDescription" to "Subtitle text goes here to describe the product or campaign",
            "productPrice" to "\$190.00"
        )
    ),
    // Title + price + was price + badge (no subtitle) -- also the one used by the CTA
    // style-variant picker below, so it carries a primaryText/primaryUrl for the button to show.
    MultimodalElement(
        id = "6",
        url = "https://picsum.photos/id/60/190/190",
        title = "Product Name Goes Here",
        content = mapOf(
            "productName" to "Product Name Goes Here",
            "productPrice" to "\$159.99",
            "productWasPrice" to "\$199.99",
            "productBadge" to "Extended Sizes",
            "primaryText" to "Buy now",
            "primaryUrl" to "#"
        )
    ),
    // Ranged price (e.g. multiple sizes/colors)
    MultimodalElement(
        id = "7",
        url = "https://picsum.photos/id/70/190/190",
        title = "Product Name Goes Here",
        content = mapOf(
            "productName" to "Product Name Goes Here",
            "productDescription" to "Available in multiple options",
            "productPrice" to "\$19.99 – \$49.99"
        )
    ),
    // "See price in cart" variant
    MultimodalElement(
        id = "8",
        url = "https://picsum.photos/id/80/190/190",
        title = "Product Name Goes Here",
        content = mapOf(
            "productName" to "Product Name Goes Here",
            "productDescription" to "Subtitle text goes here",
            "productPrice" to "See price in cart",
            "productWasPrice" to "\$199.99",
        )
    )
)

/**
 * Matches the "Vertical Card - With description" design spec (222x367, 190x190 image, 8dp
 * radius, #E3E3E3 outline, subtle drop shadow), so the demo renders at the intended card size.
 * ConciergeThemeLoader.default() has null tokens, which would otherwise fall back to the SDK's
 * generic 250dp-wide card default instead of this card's actual spec.
 *
 * The button only renders when there's no subtitle (see [ExtendedProductCard]), which is what
 * keeps worst-case content within this height without clipping.
 */
private val figmaCardLayout = ConciergeLayout(
    productCardWidth = 222.0,
    productCardMinHeight = 240.0,
    productCardMaxHeight = 367.0,
    productImageWidth = 190.0,
    productImageHeight = 190.0,
    productCardBorderRadius = 8.0,
    productCardOutlineColor = "#E3E3E3",
    productCardBackgroundColor = "#FFFFFF",
    multimodalCardBoxShadow = mapOf(
        "offsetX" to 0.0,
        "offsetY" to 1.0,
        "blurRadius" to 4.0,
        "spreadRadius" to 0.0,
        "color" to Color(0x1A191F1C)
    )
)

private val figmaProductCardTheme = ConciergeThemeData(
    config = ConciergeThemeConfig(),
    tokens = ConciergeThemeTokens(cssLayout = figmaCardLayout)
)

/**
 * One CTA button style variant (fill treatment + shape) applied to a real [ExtendedProductCard]
 * via a nested [ConciergeTheme] override, for design review. "Outlined" isn't included here since
 * the real CTA button has no border-drawing support today — see the standalone comparison
 * elsewhere for that treatment.
 */
private data class CtaCardStyleVariant(val label: String, val theme: ConciergeThemeData)

/** DSG's primary brand color (matches --color-primary in themeDefault.json). */
private const val DSG_BRAND_COLOR = "#006554"

private val ctaCardStyleVariants = listOf(
    CtaCardStyleVariant(
        "Neutral • Pill",
        ConciergeThemeData(
            config = ConciergeThemeConfig(),
            tokens = ConciergeThemeTokens(
                cssLayout = figmaCardLayout.copy(productCardCtaButtonBorderRadius = 99.0),
                colors = ConciergeThemeColors(
                    productCardCtaButton = ConciergeProductCardCtaButtonColors(
                        backgroundColor = "#EDEDED",
                        textColor = "#191F1C"
                    )
                )
            )
        )
    ),
    CtaCardStyleVariant(
        "Brand • Pill",
        ConciergeThemeData(
            config = ConciergeThemeConfig(),
            tokens = ConciergeThemeTokens(
                cssLayout = figmaCardLayout.copy(productCardCtaButtonBorderRadius = 99.0),
                colors = ConciergeThemeColors(
                    productCardCtaButton = ConciergeProductCardCtaButtonColors(
                        backgroundColor = DSG_BRAND_COLOR,
                        textColor = "#FFFFFF"
                    )
                )
            )
        )
    ),
    CtaCardStyleVariant(
        "Neutral • Rounded",
        ConciergeThemeData(
            config = ConciergeThemeConfig(),
            tokens = ConciergeThemeTokens(
                cssLayout = figmaCardLayout.copy(productCardCtaButtonBorderRadius = 8.0),
                colors = ConciergeThemeColors(
                    productCardCtaButton = ConciergeProductCardCtaButtonColors(
                        backgroundColor = "#EDEDED",
                        textColor = "#191F1C"
                    )
                )
            )
        )
    ),
    CtaCardStyleVariant(
        "Brand • Rounded",
        ConciergeThemeData(
            config = ConciergeThemeConfig(),
            tokens = ConciergeThemeTokens(
                cssLayout = figmaCardLayout.copy(productCardCtaButtonBorderRadius = 8.0),
                colors = ConciergeThemeColors(
                    productCardCtaButton = ConciergeProductCardCtaButtonColors(
                        backgroundColor = DSG_BRAND_COLOR,
                        textColor = "#FFFFFF"
                    )
                )
            )
        )
    )
)

/**
 * Demo screen that renders sample extended product cards with varied content, for visual QA of
 * card layout, spacing, and the CTA button's style/theming. Intended for use in the test app.
 */
@Composable
internal fun ExtendedProductCardDemoScreen() {
    CompositionLocalProvider(LocalImageProvider provides DefaultImageProvider()) {
        ConciergeTheme(theme = figmaProductCardTheme) {
            val cardMaxHeight = ConciergeStyles.extendedProductCardStyle.cardMaxHeight
            // Lightweight tap feedback for manual QA -- not a checkout flow, just confirms the
            // CTA is wired and shows which button/URL was tapped.
            val context = LocalContext.current
            val onActionClick: (ProductActionButton) -> Unit = { button ->
                Toast.makeText(context, "${button.text} -> ${button.url}", Toast.LENGTH_SHORT).show()
            }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF5F5F5))
                        .statusBarsPadding()
                        .testTag("ExtendedProductCardDemoScreenList")
                ) {
                    item {
                        Text(
                            text = "CTA Button Style Options",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF333333),
                            modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 4.dp)
                        )
                        Text(
                            text = "Neutral vs brand fill × pill/rounded shape, applied to the real card via a nested theme override",
                            fontSize = 13.sp,
                            color = Color(0xFF666666),
                            modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
                        )
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(ctaCardStyleVariants.size) { index ->
                                val variant = ctaCardStyleVariants[index]
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    ConciergeTheme(theme = variant.theme) {
                                        ExtendedProductCard(
                                            // No subtitle and has a primaryText -- both required
                                            // for the CTA to render (see ExtendedProductCard).
                                            element = sampleCards[5],
                                            modifier = Modifier.height(cardMaxHeight),
                                            onActionClick = onActionClick
                                        )
                                    }
                                    Text(
                                        text = variant.label,
                                        fontSize = 12.sp,
                                        color = Color(0xFF666666),
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                }
                            }
                        }
                    }
                    item {
                        Text(
                            text = "Title & Description Variants",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF333333),
                            modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 4.dp)
                        )
                        Text(
                            text = "2L title + 2L desc  •  2L title + 1L desc  •  1L title + 2L desc  •  1L title + 1L desc",
                            fontSize = 13.sp,
                            color = Color(0xFF666666),
                            modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
                        )
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(lineCountVariants.size) { index ->
                                ExtendedProductCard(
                                    element = lineCountVariants[index],
                                    modifier = Modifier.height(cardMaxHeight),
                                    onActionClick = onActionClick
                                )
                            }
                        }
                    }
                    item {
                        Text(
                            text = "Content Variations",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF333333),
                            modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 4.dp)
                        )
                        Text(
                            text = "Badge, subtitle, price, was price, ranged price, see price in cart",
                            fontSize = 13.sp,
                            color = Color(0xFF666666),
                            modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
                        )
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(sampleCards.size) { index ->
                                ExtendedProductCard(
                                    element = sampleCards[index],
                                    modifier = Modifier.height(cardMaxHeight),
                                    onActionClick = onActionClick
                                )
                            }
                        }
                    }
                }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F5F5, widthDp = 480, heightDp = 900)
@Composable
internal fun ExtendedProductCardPreview() {
    ExtendedProductCardDemoScreen()
}

/**
 * Public entry point for the test app to launch the product card demo. The only public symbol
 * exposed for this purpose — everything it delegates to stays internal.
 */
@Composable
fun ProductCardDemoScreen() {
    ExtendedProductCardDemoScreen()
}