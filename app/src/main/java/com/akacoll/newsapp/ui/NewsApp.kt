package com.akacoll.newsapp.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.akacoll.newsapp.MockData
import com.akacoll.newsapp.components.BottomMenu
import com.akacoll.newsapp.network.NewsManager
import com.akacoll.newsapp.network.models.ArticleCategory
import com.akacoll.newsapp.network.models.TopNewsArticle
import com.akacoll.newsapp.ui.screen.*

@Composable
fun NewsApp() {
    val navController = rememberNavController()
    val scrollState = rememberScrollState()
    MainScreen(navController, scrollState)
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(navController: NavHostController, scrollState: ScrollState) {
    Scaffold(bottomBar = { BottomMenu(navController = navController) }) { paddingValues ->
        Navigation(navController, scrollState, paddingValues = paddingValues)
    }
}

@Composable
fun Navigation(
    navController: NavHostController,
    scrollState: ScrollState,
    newsManager: NewsManager = NewsManager(),
    paddingValues: PaddingValues
) {
    val articlesList = mutableListOf(TopNewsArticle())
    articlesList.addAll(newsManager.newsResponse.value.articles ?: listOf(TopNewsArticle()))
    articlesList.let { articles ->
        Log.d("news", "$articles")
        NavHost(
            navController = navController,
            startDestination = BottomMenuScreen.TopNews.route,
            modifier = Modifier.padding(paddingValues = paddingValues)
        ) {
            bottomNavigation(navController = navController, articles, newsManager)
            composable("TopNews") {
                TopNews(navController = navController, articles, newsManager.query, newsManager)
            }
            composable(
                route = "Detail/{index}",
                arguments = listOf(
                    navArgument("index") {
                        type = NavType.IntType
                    }
                )
            ) { navBackStackEntry ->
                val index = navBackStackEntry.arguments?.getInt("index")
                index?.let {
                    articles.clear()
                    if (newsManager.query.value.isNotEmpty())
                        articles.addAll(newsManager.searchedNewsResponse.value.articles ?: listOf())
                    else
                        articles.addAll(newsManager.newsResponse.value.articles ?: listOf())

                    val article = articles[index]
                    DetailScreen(navController, scrollState, article)
                }
            }
        }
    }
}

fun NavGraphBuilder.bottomNavigation(
    navController: NavHostController,
    articles: List<TopNewsArticle>,
    newsManager: NewsManager
) {
    composable(BottomMenuScreen.TopNews.route) {
        TopNews(
            navController = navController,
            articles = articles,
            query = newsManager.query,
            newsManager = newsManager
        )
    }
    composable(BottomMenuScreen.Categories.route) {
        newsManager.onSelectedCategoryChanged(ArticleCategory.BUSINESS.categoryName)
        newsManager.getArticlesByCategory(ArticleCategory.BUSINESS.categoryName)

        Categories({
            newsManager.onSelectedCategoryChanged(it)
            newsManager.getArticlesByCategory(it)
        }, newsManager = newsManager)
    }
    composable(BottomMenuScreen.Sources.route) {
        Sources(newsManager = newsManager)
    }
}