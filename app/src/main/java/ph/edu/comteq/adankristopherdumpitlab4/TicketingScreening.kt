package ph.edu.comteq.adankristopherdumpitlab4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ph.edu.comteq.adankristopherdumpitlab4.ui.theme.AdanKristopherDumpitLab4Theme
import kotlin.time.Duration
import java.time.Instant

class TicketingScreening : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimpleTicketingScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleTicketingScreen() {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Instant.now()
            .plus(java.time.Duration.ofDays(2)).toEpochMilli(),
        selectableDates = object: SelectableDates{
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= Instant.now()
                    .plus(java.time.Duration.ofDays(1)).toEpochMilli()
            }
        }
    )
    Column(
        modifier = Modifier.background((Color.Black))
    ) {
        Column(
            modifier = Modifier.weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().height(230.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.museum),
                    contentDescription = "Museum",
                    modifier = Modifier.fillMaxWidth().height(230.dp),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier.fillMaxWidth().height(230.dp)
                        .background(Color.Black.copy(alpha = 0.7f))
                )
                Text(
                    "Official\nTicketing Service",
                    fontSize = 32.sp,
                    fontFamily = playfairdisplayregular,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    lineHeight = 36.sp
                )
            }
            // inner container for date and ticket types
            Column (
                modifier = Modifier.fillMaxWidth().padding(20.dp),
            ){
                DatePicker(
                    modifier = Modifier.padding(0.dp).fillMaxWidth(),
                    state = datePickerState,
                    title = null,
                    showModeToggle = false,
                    headline = {
                        Text(
                            "1. Date to Visit",
                            fontSize = 26.sp,
                            fontFamily = playfairdisplayregular,
                        )
                    },
                    colors = DatePickerDefaults.colors(
                        titleContentColor = Color(0xFFd29f1b),
                        headlineContentColor = Color(0xFFd29f1b),
                        weekdayContentColor = Color(0xFFd29f1b),
                        containerColor = Color.Transparent,
                        dayContentColor = Color.White,
                        todayContentColor = Color(0xFFd29f1b),
                        todayDateBorderColor = Color(0xFFd29f1b),
                        selectedDayContainerColor = Color(0xFFd29f1b),
                        selectedDayContentColor = Color.Black,
                        disabledDayContentColor = Color.Gray // Corrected parameter name
                    )
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth().height(80.dp)
                .background(Color(0xFFd29f1b))
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Total: P500",
                fontSize = 26.sp,
                fontFamily = playfairdisplayregular,
                color = Color.Black
            )
            Button(
                modifier = Modifier.padding(5.dp),
                onClick = {/*TODO*/ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                )
            ) {
                Text(
                    "Checkout",
                    fontSize = 20.sp,
                    fontFamily = playfairdisplayregular,
                    color = Color(0xFFd29f1b)
                )
            }
        }
    }
}