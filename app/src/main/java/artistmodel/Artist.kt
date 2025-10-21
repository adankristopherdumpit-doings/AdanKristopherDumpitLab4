package ph.edu.comteq.adankristopherdumpitlab4.artistmodel

import ph.edu.comteq.adankristopherdumpitlab4.R
data class Artist(
    val id: Int,
    val name: String,
    val birthDeath: String,
    val avatar: Int,
    val artworks: List<Int>
)

fun populateArtists() = listOf(
    Artist(
        1,
        "Leonardo da Vinci",
        "1452 - 1519",
        R.drawable.leonardo_da_vinci_leonardo_da_vinci,
        listOf(R.drawable.leonardo_da_vinci_mona_lisa, R.drawable.leonardo_da_vinci_lady_ermine, R.drawable.leonardo_da_vinci_litta_madonna)
    ),
    Artist(
        2,
        "Michelangelo",
        "1475 – 1564",
        R.drawable.michelangelo_michelangelo,
        listOf(R.drawable.michelangelo_david, R.drawable.michelangelo_delphic_sibyl, R.drawable.michelangelo_torment_of_saint_anthony)
    ),
    Artist(
        3,
        "Gustav Klimt",
        "1862 – 1918",
        R.drawable.gustav_klimt_gustav_klimt,
        listOf(R.drawable.gustav_klimt_adele_bloch_bauer, R.drawable.gustav_klimt_the_kiss, R.drawable.gustav_klimt_lady_with_fan)
    )
)