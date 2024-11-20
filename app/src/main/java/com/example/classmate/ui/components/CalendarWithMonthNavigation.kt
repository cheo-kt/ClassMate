package com.example.classmate.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.classmate.domain.model.Appointment
import com.example.classmate.domain.model.Request

import com.example.classmate.domain.model.RequestBroadcast
import java.time.YearMonth
import java.time.ZoneId
@Composable
fun CalendarWithMonthNavigation(listRequestBroadcast: List<RequestBroadcast>, listAppointment: List<Appointment>, listRequest: List<Request>, type: Int, navController: NavController) {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    val daysInMonth = currentMonth.lengthOfMonth()

    // Agrupar las solicitudes por día del mes, manteniendo las instancias completas
    val requestBroadcastDates: Map<Int, List<RequestBroadcast>> = listRequestBroadcast
        .mapNotNull {
            it.dateInitial?.toDate()?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()
        }
        .filter { it.year == currentMonth.year && it.month == currentMonth.month }
        .groupBy { it.dayOfMonth }
        .mapValues { entry ->
            listRequestBroadcast.filter {
                it.dateInitial?.toDate()?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()?.dayOfMonth == entry.key
            }
        }

    // Agrupar las citas por día del mes, manteniendo las instancias completas
    val appointmentDates: Map<Int, List<Appointment>> = listAppointment
        .mapNotNull {
            it.dateInitial?.toDate()?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()
        }
        .filter { it.year == currentMonth.year && it.month == currentMonth.month }
        .groupBy { it.dayOfMonth }
        .mapValues { entry ->
            listAppointment.filter {
                it.dateInitial?.toDate()?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()?.dayOfMonth == entry.key
            }
        }
    // Agrupar las citas por día del mes, manteniendo las instancias completas
    val requestDates: Map<Int, List<Request>> = listRequest
        .mapNotNull {
            it.dateInitial?.toDate()?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()
        }
        .filter { it.year == currentMonth.year && it.month == currentMonth.month }
        .groupBy { it.dayOfMonth }
        .mapValues { entry ->
            listRequest.filter {
                it.dateInitial?.toDate()?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()?.dayOfMonth == entry.key
            }
        }


    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Navegación de meses
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val ButtonSize = LocalConfiguration.current.screenWidthDp.dp / 10
                IconButton(onClick = { currentMonth = currentMonth.minusMonths(1) }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Star", tint = Color.Black, modifier = Modifier.size(ButtonSize))
                }

                Text(
                    text = "${currentMonth.month.name} ${currentMonth.year}",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { currentMonth = currentMonth.plusMonths(1) }) {
                    Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Star", tint = Color.Black, modifier = Modifier.size(ButtonSize))
                }
            }

            // Calendario con los días del mes
            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(4.dp)
            ) {
                items((1..daysInMonth).toList()) { day ->
                    val requestsBroadcastForDay = requestBroadcastDates[day] ?: emptyList()
                    val appointmentsForDay = appointmentDates[day] ?: emptyList()
                    val requestsForDay = requestDates[day] ?: emptyList()

                    val requestIdsForDay = requestsForDay.map { it }
                    val appointmentIdsForDay = appointmentsForDay.map { it }
                    val requestsBroadcastIdsForDay = requestsBroadcastForDay.map { it }

                    // Pasa los IDs al hacer clic en un día
                    DayBox(day, requestsBroadcastForDay, appointmentsForDay,requestsForDay, onClick = {
                        // Serializar solo los IDs
                        val requestBroadcastJson = serializeList(requestsBroadcastIdsForDay)
                        val appointmentJson = serializeList(appointmentIdsForDay)
                        val requestJson = serializeList(requestIdsForDay)
                        if(type==1){
                            navController.navigate("DayOfCalendarStudent?requestsBroadcastForDay=$requestBroadcastJson&requestForDay=$requestJson&appointmentsForDay=$appointmentJson")
                        }else{
                            navController.navigate("DayOfCalendarMonitor?appointmentsForDay=$appointmentJson")
                        }

                    })
                }
            }
        }
    }
}
@Composable
fun DayBox(
    day: Int,
    requestsBroadcast: List<RequestBroadcast>,
    appointments: List<Appointment>,
    request: List<Request>,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(2.dp)
            .aspectRatio(0.5f)
            .fillMaxWidth()
            .border(1.dp, Color.Black)
            .clickable { onClick() }, // Aquí agregamos el borde negro
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = day.toString(),
            modifier = Modifier.padding(4.dp),
            textAlign = TextAlign.Center
        )

        // Indicadores de objetos con puntos de color
        Row() {
            // Si hay requests, mostrar punto azul
            requestsBroadcast.forEach { _ ->
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(Color.Green)
                        .padding(4.dp)
                        .clip(shape = CircleShape)
                        .align(Alignment.CenterVertically)
                )
            }

            // Si hay appointments, mostrar punto verde
            appointments.forEach { _ ->
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(Color.Blue)
                        .padding(4.dp)
                        .clip(shape = CircleShape)
                        .align(Alignment.CenterVertically)
                )
            }

            request.forEach { _ ->
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(Color.Cyan)
                        .padding(4.dp)
                        .clip(shape = CircleShape)
                        .align(Alignment.CenterVertically)
                )
            }
        }
    }
}
