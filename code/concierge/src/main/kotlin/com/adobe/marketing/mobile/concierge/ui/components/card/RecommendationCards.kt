/*
 * Copyright 2025 Adobe. All rights reserved.
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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.adobe.marketing.mobile.concierge.ConciergeConstants
import com.adobe.marketing.mobile.concierge.network.MultimodalElement
import com.adobe.marketing.mobile.concierge.ui.theme.CardsAlignment
import com.adobe.marketing.mobile.concierge.ui.theme.ConciergeTheme
import com.adobe.marketing.mobile.concierge.ui.theme.ProductCardStyle
import com.adobe.marketing.mobile.services.Log

private const val TAG = "RecommendationCards"

/**
 * Composable that displays product recommendation cards containing one or more [MultimodalElement]s
 * in a single card or carousel style layout.
 */
@Composable
internal fun RecommendationCards(
    elements: List<MultimodalElement>,
    modifier: Modifier = Modifier,
    onImageClick: (MultimodalElement) -> Unit = {},
    onActionClick: (ProductActionButton) -> Unit = {},
    leadingInset: Dp = 0.dp
) {
    if (elements.isEmpty()) {
        Log.debug(ConciergeConstants.EXTENSION_NAME, TAG, "No elements to display, returning early")
        return
    }

    Log.debug(
        ConciergeConstants.EXTENSION_NAME,
        TAG,
        "Rendering ImageCarousel with ${elements.size} elements"
    )

    AnimatedVisibility(
        visible = elements.isNotEmpty(),
        enter = fadeIn(animationSpec = tween(durationMillis = 220)),
        exit = fadeOut(animationSpec = tween(durationMillis = 180))
    ) {
        val horizontalAlignment = when (ConciergeTheme.behavior?.productCard?.cardsAlignment) {
            CardsAlignment.CENTER -> Alignment.CenterHorizontally
            CardsAlignment.END -> Alignment.End
            else -> Alignment.Start
        }
        val cardStyle = ConciergeTheme.behavior?.productCard?.cardStyle ?: ProductCardStyle.ACTION_BUTTON
        val useExtendedProductCards = cardStyle == ProductCardStyle.PRODUCT_DETAIL
        Column(
            modifier = modifier
                .fillMaxWidth()
                .then(if (elements.size == 1) Modifier.padding(start = leadingInset) else Modifier),
            horizontalAlignment = horizontalAlignment
        ) {
            if (elements.size == 1) {
                if (useExtendedProductCards) {
                    ExtendedProductCard(
                        element = elements[0],
                        onCardClick = onImageClick,
                        onActionClick = onActionClick
                    )
                } else {
                    ProductCard(
                        element = elements[0],
                        onImageClick = onImageClick,
                        onActionClick = onActionClick
                    )
                }
            } else {
                ProductCarousel(
                    elements = elements,
                    onImageClick = onImageClick,
                    onActionClick = onActionClick,
                    useExtendedProductCards = useExtendedProductCards,
                    leadingInset = leadingInset
                )
            }
        }
    }
}


/**
 * Data class representing an action button for product recommendations with multimodal elements
 */
internal data class ProductActionButton(
    val id: String,
    val text: String,
    val url: String? = null,
    /** The card's actual product name, so click analytics report the product rather than the button label. */
    val productName: String? = null
)

/**
 * Builds a [ProductActionButton] from an element's already-parsed `primaryText`/`primaryUrl`
 * (entity_info.primary), or null when the backend sent no primary action for this element.
 * `url` is optional -- a text-only action is valid; callers that require a destination (e.g.
 * [ExtendedProductCard]'s single CTA slot) check `.url` themselves. The single source of truth
 * for the primary/secondary action-button contract shared by [ProductCard] and
 * [ExtendedProductCard].
 */
internal fun primaryActionButton(element: MultimodalElement): ProductActionButton? =
    actionButton(element, textKey = "primaryText", urlKey = "primaryUrl", idSuffix = "primary")

/**
 * Builds a [ProductActionButton] from an element's already-parsed `secondaryText`/`secondaryUrl`
 * (entity_info.secondary), or null when the backend sent no secondary action for this element.
 */
internal fun secondaryActionButton(element: MultimodalElement): ProductActionButton? =
    actionButton(element, textKey = "secondaryText", urlKey = "secondaryUrl", idSuffix = "secondary")

private fun actionButton(element: MultimodalElement, textKey: String, urlKey: String, idSuffix: String): ProductActionButton? {
    val text = element.content[textKey] as? String
    if (text.isNullOrBlank()) return null
    return ProductActionButton(
        id = "${element.id}_$idSuffix",
        text = text,
        url = element.content[urlKey] as? String,
        productName = element.content["productName"] as? String
    )
}
