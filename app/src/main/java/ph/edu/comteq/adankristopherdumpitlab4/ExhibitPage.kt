package ph.edu.comteq.adankristopherdumpitlab4

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import java.io.InputStreamReader
import ph.edu.comteq.adankristopherdumpitlab4.artistmodel.Artist
import ph.edu.comteq.adankristopherdumpitlab4.artistmodel.populateArtists
import ph.edu.comteq.adankristopherdumpitlab4.R
import androidx.compose.foundation.ExperimentalFoundationApi

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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExhibitScreen(artistId: Int, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var artist by remember { mutableStateOf<Artist?>(null) }
    var artworks by remember { mutableStateOf<List<Artwork>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val pagerState = rememberPagerState(pageCount = { artworks.size })
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

    LaunchedEffect(artworks.size) {
        if (artworks.isNotEmpty()) {
            val middleIndex = artworks.size / 2
            coroutineScope.launch {
                pagerState.scrollToPage(middleIndex)
            }
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
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures { change, dragAmount ->
                            coroutineScope.launch {
                                val targetPage = if (dragAmount > 0) {
                                    pagerState.currentPage - 1
                                } else {
                                    pagerState.currentPage + 1
                                }.coerceIn(0, artworks.size - 1)

                                pagerState.animateScrollToPage(targetPage)
                            }
                            change.consume()
                        }
                    }
            ) { page ->
                val artwork = artworks[page]
                val imageId = artist!!.artworks[page]

                val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction

                ArtworkCard(
                    artwork = artwork,
                    imageId = imageId,
                    index = page,
                    totalItems = artworks.size,
                    pageOffset = pageOffset,
                    pagerState = pagerState
                )
            }

            PageIndicator(
                pageCount = artworks.size,
                currentPage = pagerState.currentPage,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )
        } else {
            Text("Artist or artworks not found.", color = Color.White)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ArtworkCard(
    artwork: Artwork,
    imageId: Int,
    index: Int,
    totalItems: Int,
    pageOffset: Float,
    pagerState: PagerState
) {
    val scaleFactor = 0.8f
    val alphaFactor = 0.5f

    val scale = remember(pageOffset) {
        if (kotlin.math.abs(pageOffset) < 1) {
            lerp(1f, scaleFactor, kotlin.math.abs(pageOffset))
        } else {
            scaleFactor
        }
    }

    val alpha = remember(pageOffset) {
        if (kotlin.math.abs(pageOffset) < 1) {
            lerp(1f, alphaFactor, kotlin.math.abs(pageOffset))
        } else {
            alphaFactor
        }
    }

    val rotationY = remember(pageOffset) {
        pageOffset * 15f
    }

    val translationX = remember(pageOffset) {
        pageOffset * 100f
    }

    val isCurrentPage = pagerState.currentPage == index
    val isMiddle = index == totalItems / 2

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                this.alpha = alpha
                this.rotationY = rotationY
                this.translationX = translationX
            }
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        if (!isMiddle) {
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
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
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

        val cardShape = if (isMiddle) {
            RoundedCornerShape(
                topStart = 160.dp,
                topEnd = 160.dp,
                bottomStart = 16.dp,
                bottomEnd = 16.dp
            )
        } else {
            RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = 160.dp,
                bottomEnd = 160.dp
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
                .padding(vertical = 8.dp),
            shape = cardShape,
            elevation = CardDefaults.cardElevation(
                defaultElevation = if (isCurrentPage) 16.dp else 4.dp
            )
        ) {
            Image(
                painter = painterResource(id = imageId),
                contentDescription = artwork.title,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(cardShape),
                contentScale = ContentScale.Fit
            )
        }

        if (isMiddle) {
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
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
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

@Composable
fun PageIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pageCount) { index ->
            val isSelected = index == currentPage
            val size = if (isSelected) 12.dp else 8.dp
            val color = if (isSelected) Color(0xFFFFD700) else Color.Gray

            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(size)
                    .background(
                        color = color,
                        shape = RoundedCornerShape(50)
                    )
            )
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