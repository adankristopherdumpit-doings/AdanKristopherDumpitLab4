package ph.edu.comteq.adankristopherdumpitlab4

import androidx.compose.ui.input.key.type

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

data class Artwork(
    val title: String,
    val years: String,
    val born_at: String,
    val comment: String
)

class ExhibitPage : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        val artworkImageId = intent.getIntExtra("ARTWORK_IMAGE_ID", 0)

        setContent {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Artwork Exhibit") },
                        navigationIcon = {
                            IconButton(onClick = { finish() }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                            }
                        }
                    )
                }
            ) { paddingValues ->
                val artworkTitle = resources.getResourceEntryName(artworkImageId)
                    .replace("leonardo_da_vinci_", "") // Clean up the name
                    .replace("_", " ")
                    .replaceFirstChar { it.uppercase() }

                ExhibitScreen(
                    artworkTitle = artworkTitle,
                    artworkImageId = artworkImageId,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

@Composable
fun ExhibitScreen(artworkTitle: String, artworkImageId: Int, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var artwork by remember { mutableStateOf<Artwork?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = artworkTitle) {
        artwork = getArtworkDetails(context, artworkTitle)
        isLoading = false
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = artworkImageId),
            contentDescription = artwork?.title ?: "Artwork",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else if (artwork != null) {
            Text(
                text = artwork!!.title,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = playfairdisplayregular3
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "c. ${artwork!!.years}",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Location: ${artwork!!.born_at}",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(24.dp))
            Text(
                text = artwork!!.comment,
                fontSize = 18.sp,
                lineHeight = 26.sp
            )
        } else {
            Text("Artwork details not found.")
        }
    }
}


private fun getArtworkDetails(context: Context, title: String): Artwork? {
    val gson = Gson()
    val assetManager = context.assets
    val inputStream = assetManager.open("artworks.json")
    val reader = InputStreamReader(inputStream)
    val artworkListType = object : TypeToken<List<Artwork>>() {}.type
    val artworks: List<Artwork> = gson.fromJson(reader, artworkListType)

    return artworks.find { it.title.equals(title, ignoreCase = true) }
}
