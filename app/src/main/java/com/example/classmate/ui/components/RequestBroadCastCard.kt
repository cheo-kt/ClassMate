package com.example.classmate.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.classmate.R
import com.example.classmate.domain.model.Monitor
import com.example.classmate.domain.model.RequestBroadcast
import com.google.gson.Gson

@Composable
fun RequestBroadcastCard(monitor: Monitor?, requests:List<RequestBroadcast?>, filter:String, navController: NavController){
    val rb:List<RequestBroadcast?> = if(filter.isNotEmpty()) {
        requests.filter {
            it?.studentName!!.startsWith(
                filter,
                ignoreCase = true
            )
        }
    } else {
        requests
    }
    rb.forEach { request ->
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp,
            ), modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = R.drawable.botonestudiante,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .size(50.dp)
                        .clip(CircleShape)
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(20.dp)
                ) {
                    androidx.compose.material3.Text(
                        text = request!!.studentName,
                        color = Color(0xFF209619),
                        fontSize = 16.sp,
                    )
                    androidx.compose.material3.Text(
                        text = ("Materia:" + request.subjectname),
                        fontSize = 12.sp,
                    )
                }
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(horizontal = 5.dp)
                    ) {
                        IconButton(onClick = {
                            navController.navigate(
                                "DecisionMonitor?request=${
                                    Gson().toJson(
                                        request
                                    ) ?: "No"
                                }&monitor=${
                                    Gson().toJson(
                                        monitor
                                    ) ?: "No"
                                }"
                            )
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.PlayArrow,
                                contentDescription = "Arrow",
                                modifier = Modifier.size(50.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}