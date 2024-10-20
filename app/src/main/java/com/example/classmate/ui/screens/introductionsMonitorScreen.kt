package com.example.classmate.ui.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.classmate.R
import com.example.classmate.ui.viewModel.IntroductionStudentViewModel


@Composable
fun IntroductionsMonitorScreen (navController: NavController, authViewModel: IntroductionStudentViewModel = viewModel()){

    val scrollState = rememberScrollState()

    val image by authViewModel.currentImage.observeAsState(R.drawable.local_library)
    val message by authViewModel.currentMessage.observeAsState("¡Ayuda a los estudiantes con sus materias!")
    val navigateToNextScreen by authViewModel.navigateToNextScreen.observeAsState(false)


    if (navigateToNextScreen) {
        authViewModel.navigateToNextScreen.value = false
        navController.navigate("HomeStudentScreen")
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerpadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerpadding)
            .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally) {

            Image(
                painter = painterResource(id = R.drawable.encabezadointroduccion),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(modifier = Modifier
                .weight(0.1f)) // Espacio en blanco

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(220.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE0E0E0))
            ) {
                Image(
                    painter = painterResource(id = image),
                    contentDescription = "Ícono personalizado",
                    modifier = Modifier.size(150.dp),
                    colorFilter = ColorFilter.tint(Color.Black)
                )
            }

            Box(modifier = Modifier
                .weight(0.1f)) // Espacio en blanco

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                val circleSizes = listOf(15.dp, 10.dp, 10.dp, 10.dp)

                for (i in circleSizes.indices) {
                    Box(
                        modifier = Modifier
                            .size(if (i == authViewModel.currentIndex) circleSizes[0] else circleSizes[1])
                            .clip(CircleShape)
                            .background(Color(0xFF000000))
                    )
                }

            }
            Box(modifier = Modifier
                .weight(0.1f)) // Espacio en blanco

            Box(modifier = Modifier.padding(20.dp),
                contentAlignment = Alignment.Center){
                Text(
                    text = message,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    style = TextStyle(
                        lineHeight = 38.sp
                    )
                )
            }



            Box(modifier = Modifier
                .weight(0.1f)) // Espacio en blanco

            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center

            ){
                Image(
                    painter = painterResource(id = R.drawable.encabezadointroduccion2),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                IconButton(
                    onClick = { authViewModel.changeContent() },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)

                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Navegar",
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }


        }
    }

}