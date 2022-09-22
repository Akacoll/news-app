package com.akacoll.newsapp.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.akacoll.newsapp.MockData
import com.akacoll.newsapp.MockData.getTimeAgo
import com.akacoll.newsapp.network.models.NewsData
import com.akacoll.newsapp.R
import com.akacoll.newsapp.network.models.TopNewsArticle
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun DetailScreen(navController: NavController,scrollState: ScrollState, article: TopNewsArticle) {
    Scaffold(
        topBar = {
            DetailTopAppBar(onBackPressed = {
                navController.navigate("TopNews")
            })
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Detail Screen", fontWeight = FontWeight.SemiBold)
            CoilImage(
                imageModel = article.urlToImage,
                contentDescription = "",
                contentScale = ContentScale.Crop,
                error = ImageBitmap.imageResource(R.drawable.breaking_news),
                placeHolder = ImageBitmap.imageResource(R.drawable.breaking_news)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoWithIcon(Icons.Default.Edit, info = article.author ?: "Not available")
                article.publishedAt?.let {
                    InfoWithIcon(Icons.Default.DateRange,
                        info = MockData.stringToDate(it).getTimeAgo())
                }
            }
            Text(text = article.title ?: "Not available", fontWeight = FontWeight.Bold)
            Text(text = article.description ?: "Not available", modifier = Modifier.padding(top = 16.dp))
        }
    }

}

@Composable
fun DetailTopAppBar(onBackPressed: () -> Unit = {}) {
    TopAppBar(
        title = {
            Text(text = "Detail Screen", fontWeight = FontWeight.SemiBold)
        },
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
            }
        }
    )
}

@Composable
fun InfoWithIcon(icon: ImageVector, info: String) {
    Row {
        Icon(
            imageVector = icon,
            contentDescription = "Author",
            modifier = Modifier.padding(end = 8.dp),
            colorResource(id = R.color.purple_500)
        )
        Text(text = info, overflow = TextOverflow.Ellipsis)
    }
}

@Preview
@Composable
fun DetailScreenPreview() {
    DetailScreen(
        rememberNavController(),
        rememberScrollState(),
        MockData.topNewsList[0].let {
            TopNewsArticle(
                author = it.author,
                title = it.title,
                description = it.description,
                publishedAt = it.publishedAt
            )
        }
    )
}