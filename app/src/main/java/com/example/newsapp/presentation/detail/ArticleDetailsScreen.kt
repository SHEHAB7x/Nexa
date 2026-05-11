package com.example.newsapp.presentation.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.newsapp.domain.model.Article
import com.example.newsapp.domain.model.Source
import com.example.newsapp.presentation.theme.DarkGray
import com.example.newsapp.presentation.theme.LightGray
import com.example.newsapp.presentation.theme.NewsAppTheme
import com.example.newsapp.presentation.theme.Secondary
import com.example.newsapp.presentation.theme.White

@Composable
fun ArticleDetailScreen(
    article: Article?,
    onBackClick : () -> Unit,
    viewModel: ArticleDetailsViewModel = hiltViewModel()
){
    val isSaved by viewModel.isSaved.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(article) {
        article?.let {
            viewModel.setArticle(it)
        }
    }

    if(article == null){
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
        return
    }

    ArticleDetailContent(
        article = article,
        isSaved = isSaved,
        onBackClick = onBackClick,
        onToggleSave = { viewModel.toggleSave() },
        context = context
    )
}

@Composable
fun ArticleDetailContent(
    article: Article,
    isSaved: Boolean,
    onBackClick: () -> Unit,
    onToggleSave: () -> Unit,
    context: Context
){
    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)){
        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.fillMaxWidth().fillMaxHeight(.4f))
            {
                AsyncImage(
                    model = article.imageUrl,
                    contentDescription = article.description,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier.fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.3f),
                                    Color.Black.copy(alpha = 0.7f)
                                )
                            )
                        )
                )

                Box(modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black.copy(alpha = 0.45f))
                    .padding(12.dp)
                ){
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = article.publishedAt,
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 11.sp
                        )
                        Text(
                            text = article.title,
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )
                        article.author?.let {
                            Text(
                                text = it,
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 11.sp
                            )
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .size(36.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .clickable{ onBackClick() }
                        .align(Alignment.TopStart),
                    contentAlignment = Alignment.Center
                ){
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

            }
            Column(
                modifier = Modifier
                    .fillMaxHeight(.7f)
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                Text(
                    text = article.source.name,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                article.description?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 22.sp
                    )
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)

                article.content?.let {
                    Text(
                        text       = it,
                        color      = MaterialTheme.colorScheme.onBackground,
                        fontSize   = 14.sp,
                        lineHeight = 22.sp
                    )
                }

                Text(
                    text = "Read full article →",
                    color      = MaterialTheme.colorScheme.secondary,
                    fontSize   = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier   = Modifier.clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
                        context.startActivity(intent)
                    }
                )

                Spacer(modifier = Modifier.height(80.dp))


            }
            Row(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxHeight()
                    .padding(end = 40.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable{ onToggleSave() },
                    contentAlignment = Alignment.Center,
                ){
                    Icon(
                        imageVector = if (isSaved) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = if (isSaved) "Remove from favorites" else "Add to favorites",
                        tint = MaterialTheme.colorScheme.background,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ArticleDetailPreview() {
    NewsAppTheme {
        ArticleDetailContent(
            article = fakeArticle,
            isSaved = false,
            onBackClick = {},
            onToggleSave = {},
            context = LocalContext.current
        )
    }
}

private val fakeArticle = Article(
    source      = Source(id = null, name = "BBC News"),
    author      = "John Doe",
    title       = "Crypto investors should be prepared to lose all their money",
    description = "The Bank of England governor says investors should be ready.",
    url         = "https://example.com",
    imageUrl    = null,
    publishedAt = "2026-04-07",
    content     = "null",
    isSaved     = false
)