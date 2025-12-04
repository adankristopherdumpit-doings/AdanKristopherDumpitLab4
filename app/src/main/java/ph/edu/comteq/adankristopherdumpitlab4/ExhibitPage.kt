package ph.edu.comteq.adankristopherdumpitlab4

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight as ComposeFontWeight
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader
import ph.edu.comteq.adankristopherdumpitlab4.artistmodel.Artist
import ph.edu.comteq.adankristopherdumpitlab4.artistmodel.populateArtists
import ph.edu.comteq.adankristopherdumpitlab4.R

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

        val artistId = intent.getIntExtra("ARTIST_ID", 0)
        val artistName = intent.getStringExtra("ARTIST_NAME") ?: ""

        setContent {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("$artistName's Artwork Exhibit") },
                        navigationIcon = {
                            IconButton(onClick = { finish() }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                            }
                        }
                    )
                }
            ) { paddingValues ->
                ExhibitScreen(
                    artistId = artistId,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

@Composable
fun ExhibitScreen(artistId: Int, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var artist by remember { mutableStateOf<Artist?>(null) }
    var artworks by remember { mutableStateOf<List<Artwork>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = artistId) {
        artist = populateArtists().find { it.id == artistId }
        if (artist != null) {
            artworks = artist!!.artworks.mapNotNull { artworkId ->
                val title = context.resources.getResourceEntryName(artworkId)
                    .replace(artist!!.name.lowercase().replace(" ", "_") + "_", "")
                    .replace("_", " ")
                    .replaceFirstChar { it.uppercase() }
                getArtworkDetails(context, title)
            }
        }
        isLoading = false
    }

    // Auto-scroll to middle item for initial view
    LaunchedEffect(artworks.size) {
        if (artworks.isNotEmpty()) {
            val middleIndex = artworks.size / 2
            pagerState.scrollToPage(middleIndex)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (artist != null && artworks.isNotEmpty()) {
            HorizontalPager(
                count = artworks.size,
                state = pagerState,
                modifier = Modifier.fillMaxWidth()
            ) { page ->
                val artwork = artworks[page]
                ArtworkCard(
                    artwork = artwork,
                    imageId = artist!!.artworks[page],
                    index = page,
                    totalItems = artworks.size
                )
            }
        } else {
            Text("Artist or artworks not found.")
        }
    }
}

@Composable
fun ArtworkCard(artwork: Artwork, imageId: Int, index: Int, totalItems: Int) {
    val isMiddle = index == totalItems / 2

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        if (!isMiddle) {
            // Title and details above image for first and last items
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFD700), RoundedCornerShape(8.dp))
                    .padding(vertical = 12.dp, horizontal = 16.dp)
            ) {
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = artwork.title,
                        fontSize = 24.sp,
                        fontWeight = ComposeFontWeight.Bold,
                        fontFamily = playfairdisplayregular3,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "c. ${artwork.years}, ${artwork.born_at}",
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                }
            }
        }

        // Larger image with shield-like shape (rectangle top, U-shaped bottom)
        Image(
            painter = painterResource(id = imageId),
            contentDescription = artwork.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 160.dp, bottomEnd = 160.dp)),
            contentScale = ContentScale.Fit
        )

        if (isMiddle) {
            // Title and details below image for middle item
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFD700), RoundedCornerShape(8.dp))
                    .padding(vertical = 12.dp, horizontal = 16.dp)
            ) {
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = artwork.title,
                        fontSize = 24.sp,
                        fontWeight = ComposeFontWeight.Bold,
                        fontFamily = playfairdisplayregular3,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "c. ${artwork.years}, ${artwork.born_at}",
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Comment with black background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.Black,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.Top
            ) {
                Image(
                    painter = painterResource(id = R.drawable.quote),
                    contentDescription = "Quote",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = artwork.comment,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )
            }
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
