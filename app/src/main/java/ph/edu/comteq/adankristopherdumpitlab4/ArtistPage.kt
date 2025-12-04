package ph.edu.comteq.adankristopherdumpitlab4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ph.edu.comteq.adankristopherdumpitlab4.R
import ph.edu.comteq.adankristopherdumpitlab4.artistmodel.Artist
import ph.edu.comteq.adankristopherdumpitlab4.artistmodel.populateArtists
import android.content.Intent

val playfairdisplayregular3 = FontFamily(
    Font(R.font.playfairdisplayregular, FontWeight.Normal)
)

class ArtistPage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ArtistViewing()
        }
    }
}

@Composable
fun ArtistViewing() {
    var searchQuery by remember { mutableStateOf("") }
    var selectedTabIndex by remember { mutableStateOf(0) }
    val artists = remember { populateArtists() }
    val tabs = listOf("Artists", "Artworks")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5DC))
    ) {
        // Title Text
        Text(
            modifier = Modifier.padding(start = 16.dp, top = 16.dp),
            text = AnnotatedString("Explore the art of ", spanStyle = SpanStyle(Color.Black)) +
                    AnnotatedString("Renaissance", spanStyle = SpanStyle(Color(0xFFD4AF37))),
        fontFamily = playfairdisplayregular3,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold
        )

        // Search bar with icons
        OutlinedTextField(
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
            value = searchQuery,
            onValueChange = { newValue -> searchQuery = newValue },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            placeholder = { Text("Type to Search...") },
            singleLine = true,
            //trailingIcon = { Icon(Icons.Filled.CameraAlt, contentDescription = "Scan Icon") }
        )

        // Tabs for Artists / Artworks
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index }
                )
            }
        }

        when (selectedTabIndex) {
            0 -> ArtistList(artists.filter { it.name.contains(searchQuery, ignoreCase = true) })
            1 -> ArtworksGrid(artists)
        }
    }
}

//Artist Tab
@Composable
fun ArtistList(artists: List<Artist>) {
    LazyColumn(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(artists) { artist ->
            ArtistCard(artist)
        }
    }
}

@Composable
fun ArtistCard(artist: Artist) {
    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Artist Info Row
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = artist.avatar),
                    contentDescription = "Portrait of ${artist.name}",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            val intent = Intent(context, ExhibitPage::class.java).apply {
                                putExtra("ARTIST_ID", artist.id)
                                putExtra("ARTIST_NAME", artist.name)
                            }
                            context.startActivity(intent)
                        },
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = artist.name,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = playfairdisplayregular3
                    )
                    Text(
                        text = artist.birthDeath,
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Artworks:", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))

            // Artworks Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                artist.artworks.forEach { art ->
                    Image(
                        painter = painterResource(id = art),
                        contentDescription = "Artwork by ${artist.name}",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}
//Artworks
@Composable
fun ArtworksGrid(artists: List<Artist>) {
    val allArtworks = artists.flatMap { it.artworks }
    LazyColumn(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                "All Artworks",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        items(allArtworks) { artwork ->
            Image(
                painter = painterResource(id = artwork),
                contentDescription = "Artwork",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}
