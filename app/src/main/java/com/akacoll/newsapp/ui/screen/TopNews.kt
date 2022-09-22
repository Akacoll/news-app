package com.akacoll.newsapp.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.akacoll.newsapp.MockData
import com.akacoll.newsapp.MockData.getTimeAgo
import com.akacoll.newsapp.network.models.TopNewsArticle
import com.skydoves.landscapist.coil.CoilImage
import com.akacoll.newsapp.R
import com.akacoll.newsapp.components.SearchBar
import com.akacoll.newsapp.network.NewsManager

@Composable
fun TopNews(
    navController: NavController,
    articles: List<TopNewsArticle>,
    query: MutableState<String>,
    newsManager: NewsManager
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        Text(text = "Top News", fontWeight = FontWeight.SemiBold)
        SearchBar(query = query, newsManager = newsManager)
        val searchedText = query.value
        val resultList = mutableListOf<TopNewsArticle>()
        if (searchedText != "") {
            resultList.addAll(newsManager.searchedNewsResponse.value.articles ?: articles)
        } else {
            resultList.addAll(articles)
        }
        LazyColumn {
            items(resultList.size) { i ->
                TopNewsItem(article = resultList[i],
                    onNewsClick = {
                        navController.navigate("Detail/$i")
                    })
            }
        }
    }
}

@Composable
fun TopNewsItem(article: TopNewsArticle, onNewsClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .height(200.dp)
            .padding(8.dp)
            .clickable {
                onNewsClick()
            }
    ) {
        CoilImage(
            imageModel = article.urlToImage,
            contentDescription = "",
            contentScale = ContentScale.Crop,
            error = ImageBitmap.imageResource(R.drawable.breaking_news),
            placeHolder = ImageBitmap.imageResource(R.drawable.breaking_news)
        )
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .padding(top = 16.dp, start = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            article.publishedAt?.let {
                Text(
                    text = MockData.stringToDate(it).getTimeAgo(),
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(80.dp))
            Text(
                text = article.title ?: "Not available",
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview
@Composable
fun TopNewsPreview() {
    TopNewsItem(
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