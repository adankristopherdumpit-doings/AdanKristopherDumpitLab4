package ph.edu.comteq.adankristopherdumpitlab4

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.foundation.layout.offset
import ph.edu.comteq.adankristopherdumpitlab4.ui.theme.AdanKristopherDumpitLab4Theme

val playfairdisplayregular = FontFamily(
    Font(R.font.playfairdisplayregular, FontWeight.Normal)
)
val optima = FontFamily(
    Font(R.font.optima, FontWeight.Normal)
)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Homepage()
        }
    }
}

@Composable
fun ReverseTypingText(
    fullText: String,
    modifier: Modifier = Modifier,
    fontFamily: FontFamily? = null,
    fontSize: androidx.compose.ui.unit.TextUnit = 28.sp,
    color: Color = Color(0xFFD4AF37),
    durationMillis: Int = 4000
) {
    var targetLength by remember { mutableStateOf(0) }
    val animatedLength by animateIntAsState(
        targetValue = targetLength,
        animationSpec = tween(durationMillis = durationMillis)
    )

    LaunchedEffect(Unit) {
        targetLength = fullText.length
    }

    Text(
        text = fullText.take(animatedLength),
        fontFamily = fontFamily,
        fontSize = fontSize,
        color = color,
        modifier = modifier
    )
}

@Composable
fun ReverseTypingInText(
    fullText: String,
    modifier: Modifier = Modifier,
    fontFamily: FontFamily? = null,
    fontSize: androidx.compose.ui.unit.TextUnit = 16.sp,
    color: Color = Color.White,
    textAlign: TextAlign? = null,
    lineHeight: androidx.compose.ui.unit.TextUnit = androidx.compose.ui.unit.TextUnit.Unspecified,
    durationMillis: Int = 12000,
    trigger: Any = Unit
) {
    var targetLength by remember(trigger) { mutableStateOf(0) }
    val animatedLength by animateIntAsState(
        targetValue = targetLength,
        animationSpec = tween(durationMillis = durationMillis)
    )

    LaunchedEffect(trigger) {
        targetLength = fullText.length
    }

    Text(
        text = fullText.takeLast(animatedLength),
        fontFamily = fontFamily,
        fontSize = fontSize,
        color = color,
        textAlign = textAlign,
        lineHeight = lineHeight,
        modifier = modifier
    )
}

@Composable
fun Homepage() {
    val context = LocalContext.current

    var startAnim by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        startAnim = true
    }
    val imageAlpha by animateFloatAsState(
        targetValue = if (startAnim) 1f else 0f,
        animationSpec = tween(durationMillis = 4000)
    )
    val imageOffset by animateDpAsState(
        targetValue = if (startAnim) 0.dp else (-300).dp,
        animationSpec = tween(durationMillis = 4000)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo Image
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .size(100.dp)
                .padding(bottom = 8.dp)
        )

        // "Gallery" Text
        Text(
            text = "Gallery",
            fontFamily = optima,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFD4AF37)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Louvre Image with gradient and overlay text
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Image(
                painter = painterResource(id = R.drawable.louvre),
                contentDescription = "Louvre",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(alpha = imageAlpha)
                    .offset(y = imageOffset)
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                        )
                    )
            )

            ReverseTypingText(
                fullText = "Experience Art",
                fontFamily = playfairdisplayregular,
                fontSize = 28.sp,
                color = Color(0xFFD4AF37),
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Description
        ReverseTypingInText(
            fullText = "We are thrilled to invite you to join us for an extraordinary event that will immerse you in the world of art.",
            fontFamily = optima,
            fontSize = 16.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Explore Button
        Button(
            onClick = {
                val intent = Intent(context, ExploreActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFD4AF37),
                contentColor = Color.Black
            )
        ) {
            Text("Explore Now", fontWeight = FontWeight.Bold)
        }
    }
}
