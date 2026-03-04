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

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import com.adobe.marketing.mobile.concierge.network.MultimodalElement
import com.adobe.marketing.mobile.concierge.ui.theme.ConciergeStyles
import kotlin.math.roundToInt

/**
 * Composable that displays a carousel of product images with navigation controls.
 */
@Composable
internal fun ProductCarousel(
    elements: List<MultimodalElement>,
    onImageClick: (MultimodalElement) -> Unit
) {
    val style = ConciergeStyles.productCarouselStyle
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val itemStridePx = with(LocalDensity.current) {
        (style.imageWidth.toPx() + style.itemSpacing.toPx()).coerceAtLeast(1f)
    }
    val currentPage by remember(listState, elements.size, itemStridePx) {
        derivedStateOf {
            if (elements.isEmpty()) {
                0
            } else {
                val index = listState.firstVisibleItemIndex
                val offset = listState.firstVisibleItemScrollOffset
                (index + (offset / itemStridePx).roundToInt()).coerceIn(0, elements.lastIndex)
            }
        }
    }
    LaunchedEffect(listState.isScrollInProgress, currentPage, elements.size) {
        if (!listState.isScrollInProgress && elements.isNotEmpty()) {
            val needsSnap = listState.firstVisibleItemIndex != currentPage ||
                listState.firstVisibleItemScrollOffset != 0
            if (needsSnap) {
                listState.animateScrollToItem(currentPage)
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Stable LazyRow carousel implementation to avoid experimental pager APIs.
        LazyRow(
            state = listState,
            contentPadding = PaddingValues(
                start = style.horizontalPadding,
                end = style.imageWidth,
                top = style.verticalPadding,
                bottom = style.verticalPadding
            ),
            horizontalArrangement = Arrangement.spacedBy(style.itemSpacing),
            modifier = Modifier.fillMaxWidth()
        ) {
            itemsIndexed(elements) { _, element ->
                ProductImage(
                    element = element,
                    modifier = Modifier
                        .width(style.imageWidth)
                        .height(style.imageHeight),
                    onImageClick = onImageClick,
                    isMultiElement = true
                )
            }
        }

        // Carousel switcher controls
        CarouselSwitcher(
            currentPage = currentPage,
            totalPages = elements.size,
            onPreviousClick = {
                if (currentPage > 0) {
                    val targetPage = currentPage - 1
                    coroutineScope.launch {
                        listState.animateScrollToItem(targetPage)
                    }
                }
            },
            onNextClick = {
                if (currentPage < elements.size - 1) {
                    val targetPage = currentPage + 1
                    coroutineScope.launch {
                        listState.animateScrollToItem(targetPage)
                    }
                }
            },
            onPageClick = { page ->
                coroutineScope.launch {
                    listState.animateScrollToItem(page)
                }
            }
        )
    }
}

/**
 * Composable that displays carousel navigation controls with arrows and page indicators.
 */
@Composable
internal fun CarouselSwitcher(
    currentPage: Int,
    totalPages: Int,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onPageClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val style = ConciergeStyles.productCarouselStyle

    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Previous button
        IconButton(
            onClick = onPreviousClick,
            enabled = currentPage > 0
        ) {
            Icon(
                imageVector = Icons.Default.ChevronLeft,
                contentDescription = "Previous page",
                tint = if (currentPage > 0) {
                    style.navigationIconActiveColor
                } else {
                    style.navigationIconInactiveColor
                }
            )
        }

        Box(modifier = Modifier.width(style.navigationSpacing))

        // Page indicators
        Row(
            horizontalArrangement = Arrangement.spacedBy(style.indicatorSpacing),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(totalPages) { page ->
                Box(
                    modifier = Modifier
                        .size(style.indicatorSize)
                        .clip(CircleShape)
                        .background(
                            if (page == currentPage) {
                                style.indicatorActiveColor
                            } else {
                                style.indicatorInactiveColor
                            }
                        )
                        .clickable { onPageClick(page) }
                )
            }
        }

        Box(modifier = Modifier.width(style.navigationSpacing))

        // Next button
        IconButton(
            onClick = onNextClick,
            enabled = currentPage < totalPages - 1
        ) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Next page",
                tint = if (currentPage < totalPages - 1) {
                    style.navigationIconActiveColor
                } else {
                    style.navigationIconInactiveColor
                }
            )
        }
    }
}
