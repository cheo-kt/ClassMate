package com.example.classmate.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.example.classmate.R

@Composable
fun RatingBar(
    rating: Int,
    stars: Int = 5,
    onRatingChanged: (Int) -> Unit,
) {
    Row(horizontalArrangement = Arrangement.Center) {
        for (index in 1..stars) {
            Icon(
                painter = painterResource(if (index <= rating) R.drawable.baseline_star_24 else R.drawable.baseline_star_outline_24),
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .clickable { onRatingChanged(index) }
            )
        }
    }
}