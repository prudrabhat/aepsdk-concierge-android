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

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adobe.marketing.mobile.concierge.R
import com.adobe.marketing.mobile.concierge.network.MultimodalElement
import com.adobe.marketing.mobile.concierge.ui.components.image.AsyncImage
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
            "productPrice" to "\$63.97"
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
            "productWasPrice" to "\$179.95"
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
    // Title + price + was price + badge (no subtitle)
    MultimodalElement(
        id = "6",
        url = "https://picsum.photos/id/60/190/190",
        title = "Product Name Goes Here",
        content = mapOf(
            "productName" to "Product Name Goes Here",
            "productPrice" to "\$159.99",
            "productWasPrice" to "\$199.99",
            "productBadge" to "Extended Sizes"
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
 * One "Buy now" style variant (fill treatment + shape) applied to a real [ExtendedProductCard]
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
 * Which mock "Buy now" sheet is currently shown on top of the demo grid, and for which product.
 */
private sealed interface BuyNowFlowStep {
    data class SelectingOptions(val product: MultimodalElement) : BuyNowFlowStep
    data class ReviewingOrder(val product: MultimodalElement) : BuyNowFlowStep
}

/**
 * Demo screen that renders sample extended product cards with varied content, each with a
 * "Buy now" CTA. Tapping "Buy now" opens [ProductOptionsSheet] for that card's data; its
 * "Review Order" button then swaps in [CheckoutReviewSheet]. Intended for use in the test app to
 * validate card layout, spacing, and the CTA.
 */
@Composable
internal fun ExtendedProductCardDemoScreen() {
    CompositionLocalProvider(LocalImageProvider provides DefaultImageProvider()) {
        ConciergeTheme(theme = figmaProductCardTheme) {
            var buyNowStep by remember { mutableStateOf<BuyNowFlowStep?>(null) }
            val cardMaxHeight = ConciergeStyles.extendedProductCardStyle.cardMaxHeight
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF5F5F5))
                        .statusBarsPadding()
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
                                            // No subtitle: the CTA only renders when one is
                                            // absent (see ExtendedProductCard), so sampleCards[0]
                                            // (which has a subtitle) would hide it here.
                                            element = sampleCards[5],
                                            modifier = Modifier.height(cardMaxHeight),
                                            onBuyNowClick = { buyNowStep = BuyNowFlowStep.SelectingOptions(it) }
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
                                    onBuyNowClick = { buyNowStep = BuyNowFlowStep.SelectingOptions(it) }
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
                                    onBuyNowClick = { buyNowStep = BuyNowFlowStep.SelectingOptions(it) }
                                )
                            }
                        }
                    }
                }

            when (val step = buyNowStep) {
                is BuyNowFlowStep.SelectingOptions -> ProductOptionsSheet(
                    product = step.product,
                    onBack = { buyNowStep = null },
                    onReviewOrder = { buyNowStep = BuyNowFlowStep.ReviewingOrder(step.product) }
                )
                is BuyNowFlowStep.ReviewingOrder -> CheckoutReviewSheet(
                    product = step.product,
                    onBack = { buyNowStep = null }
                )
                null -> Unit
            }
        }
    }
}

private data class ColorSwatchOption(val color: Color, val enabled: Boolean = true)

private val colorSwatchOptions = listOf(
    ColorSwatchOption(Color(0xFF2E2E2E)),
    ColorSwatchOption(Color(0xFF9F9996)),
    ColorSwatchOption(Color(0xFFFFFFFF)),
    ColorSwatchOption(Color(0xFFC7C0BC), enabled = false)
)

private data class SizeOption(val label: String, val enabled: Boolean = true)

private val sizeOptions = listOf(
    SizeOption("XS"),
    SizeOption("S"),
    SizeOption("M"),
    SizeOption("L", enabled = false),
    SizeOption("XL")
)

/**
 * Mock "select options" bottom sheet shown when a sample card's "Buy now" CTA is tapped — lets
 * the shopper pick color/size/quantity/fulfillment before moving to order review. Matches the
 * design spec's presentation and row structure using placeholder swatches and copy, since
 * there's no real variant/inventory data to back it. Demo-only: none of the selectors are wired
 * to real state; "Review Order" always advances to [CheckoutReviewSheet].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductOptionsSheet(
    product: MultimodalElement,
    onBack: () -> Unit,
    onReviewOrder: () -> Unit
) {
    val productName = product.content["productName"] as? String ?: product.title

    ModalBottomSheet(
        onDismissRequest = onBack,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
        containerColor = Color.White,
        dragHandle = null
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Select Options",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF282323)
                )
                if (!productName.isNullOrBlank()) {
                    Text(
                        text = productName,
                        fontSize = 12.sp,
                        color = Color(0xFF716E6C),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
            IconButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterEnd)) {
                Icon(
                    painter = painterResource(id = R.drawable.close),
                    contentDescription = "Close",
                    tint = Color(0xFF716E6C)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            AttributeSelectorRow(label = "Color", value = "Black")
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(colorSwatchOptions.size) { index ->
                    val swatch = colorSwatchOptions[index]
                    SelectableSwatch(color = swatch.color, selected = index == 0, enabled = swatch.enabled)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            AttributeSelectorRow(label = "Size", value = "M")
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(sizeOptions.size) { index ->
                    val size = sizeOptions[index]
                    SelectablePill(label = size.label, selected = size.label == "M", enabled = size.enabled)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Quantity",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF797979),
                    modifier = Modifier.weight(1f)
                )
                QuantityStepper()
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FulfillmentTile(
                    icon = Icons.Filled.LocalShipping,
                    title = "Ship",
                    detail = "Get it by Fri, Aug 28",
                    selected = true,
                    modifier = Modifier.weight(1f)
                )
                FulfillmentTile(
                    icon = Icons.Filled.Storefront,
                    title = "Store Pickup",
                    detail = "Ready today at your store",
                    selected = false,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onReviewOrder,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBB5811))
            ) {
                Text(text = "Review Order", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.White)
            }
            Text(
                text = "Standard shipping and return policy apply.",
                fontSize = 10.sp,
                color = Color(0xFF716E6C),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun AttributeSelectorRow(label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF797979))
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF2E2E2E),
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
private fun SelectableSwatch(color: Color, selected: Boolean, enabled: Boolean) {
    Box(
        modifier = Modifier
            .size(56.dp)
            .background(Color(0xFFF6F5F3))
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = if (selected) Color(0xFF2E2E2E) else Color(0xFFD9D9D9)
            )
            .alpha(if (enabled) 1f else 0.4f)
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color)
        )
    }
}

@Composable
private fun SelectablePill(label: String, selected: Boolean, enabled: Boolean) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = if (selected) Color(0xFF2E2E2E) else Color(0xFFD9D9D9)
            )
            .alpha(if (enabled) 1f else 0.4f),
        contentAlignment = Alignment.Center
    ) {
        Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF2E2E2E))
    }
}

@Composable
private fun QuantityStepper() {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF6F5F3))
    ) {
        QuantityStepperButton(symbol = "–")
        Box(modifier = Modifier.size(width = 38.dp, height = 40.dp), contentAlignment = Alignment.Center) {
            Text(text = "1", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF292525))
        }
        QuantityStepperButton(symbol = "+")
    }
}

@Composable
private fun QuantityStepperButton(symbol: String) {
    Box(modifier = Modifier.size(width = 38.dp, height = 40.dp), contentAlignment = Alignment.Center) {
        Text(text = symbol, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF161313))
    }
}

@Composable
private fun FulfillmentTile(
    icon: ImageVector,
    title: String,
    detail: String,
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .border(
                width = if (selected) 2.5.dp else 1.dp,
                color = if (selected) Color(0xFF161313) else Color(0xFF413D3B),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF141414), modifier = Modifier.size(24.dp))
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF292525),
            modifier = Modifier.padding(top = 4.dp)
        )
        Text(
            text = detail,
            fontSize = 12.sp,
            color = Color(0xFF292525),
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

/**
 * Mock order-review bottom sheet shown after "Review Order" is tapped in [ProductOptionsSheet].
 * Matches the "BuyNow - Prefab Sheets" design spec's presentation and row structure (product
 * summary, standalone shipping-address card, grouped delivery/payment/promo/rewards card, sticky
 * checkout button shown in its disabled state) using placeholder copy, since there's no real
 * address/payment/promo/rewards data to back it. Demo-only: not backed by any real checkout flow.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CheckoutReviewSheet(product: MultimodalElement, onBack: () -> Unit) {
    val productName = product.content["productName"] as? String ?: product.title
    val productPrice = product.content["productPrice"] as? String
    val imageUrl = product.url ?: product.thumbnailUrl

    ModalBottomSheet(
        onDismissRequest = onBack,
        // Opens straight to the fully-expanded state instead of a partial "peek" height, so
        // every row (through the checkout button) is visible without an extra drag gesture.
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
        containerColor = Color.White,
        dragHandle = null
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = "Checkout",
                modifier = Modifier.align(Alignment.Center),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF282323)
            )
            IconButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterEnd)) {
                Icon(
                    painter = painterResource(id = R.drawable.close),
                    contentDescription = "Close",
                    tint = Color(0xFF716E6C)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            // Product summary
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (imageUrl != null) {
                    AsyncImage(
                        url = imageUrl,
                        contentDescription = productName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(72.dp)
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp)
                ) {
                    if (!productName.isNullOrBlank()) {
                        Text(
                            text = productName,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF292525),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    if (!productPrice.isNullOrBlank()) {
                        Text(
                            text = productPrice,
                            fontSize = 12.sp,
                            color = Color(0xFF3D3A36),
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
                Text(
                    text = "Edit",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF292525)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Shipping address — its own standalone card, above the delivery/payment/promo/rewards group
            CheckoutRow(
                title = "Shipping Address",
                detail = "Add a shipping address",
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF6F5F3))
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Delivery / payment / promo / rewards — grouped card, no real data behind any of it
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF6F5F3))
            ) {
                CheckoutRow(title = "Delivery Method", detail = "Standard shipping (5–7 business days)")
                CheckoutDivider()
                CheckoutRow(title = "Payment Method", detail = "Add a payment method", trailingLabel = "Add")
                CheckoutDivider()
                CheckoutRow(title = "Promo Code", detail = "Enter a promo code")
                CheckoutDivider()
                CheckoutRow(title = "Rewards", detail = "Sign in to earn rewards", leadingIconRes = R.drawable.sparkle)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Order total — expandable per the spec's chevron-down affordance
            CheckoutRow(
                title = "Order Total",
                detail = productPrice ?: "",
                chevronIconRes = R.drawable.chevron_down,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF6F5F3))
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Sticky bottom action bar, shown disabled per the design's state — a guest
        // checkout can't proceed until shipping/payment are filled in, which this mockup can't
        // actually collect.
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {},
                enabled = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = Color(0xFFC7C0BC),
                    disabledContentColor = Color(0xFF413D3B)
                )
            ) {
                Text(
                    text = "Checkout",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
            Text(
                text = "Standard shipping and return policy apply.",
                fontSize = 10.sp,
                color = Color(0xFF716E6C),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

/**
 * One row in the mock checkout's grouped card (title + detail + trailing chevron, optional
 * leading icon or trailing text label). Purely presentational — no click handling, since none of
 * these rows are backed by real shipping/payment/promo/rewards data.
 */
@Composable
private fun CheckoutRow(
    title: String,
    detail: String,
    modifier: Modifier = Modifier,
    trailingLabel: String? = null,
    leadingIconRes: Int? = null,
    chevronIconRes: Int = R.drawable.chevron_right
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leadingIconRes != null) {
            Icon(
                painter = painterResource(id = leadingIconRes),
                contentDescription = null,
                tint = Color(0xFF595451),
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 8.dp)
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF292525))
            Text(
                text = detail,
                fontSize = 12.sp,
                color = Color(0xFF3D3A36),
                modifier = Modifier.padding(top = 2.dp)
            )
        }
        if (trailingLabel != null) {
            Text(
                text = trailingLabel,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF292525),
                modifier = Modifier.padding(end = 8.dp)
            )
        }
        Icon(
            painter = painterResource(id = chevronIconRes),
            contentDescription = null,
            tint = Color(0xFF595451),
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
private fun CheckoutDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp)
            .height(0.5.dp)
            .background(Color(0xFFC7C0BC))
    )
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