package com.example.newsapp.presentation.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
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
import com.example.newsapp.domain.model.TextSize
import com.example.newsapp.presentation.theme.NewsAppTheme
import com.example.newsapp.utils.ReadTimeCalculator

@Composable
fun ArticleDetailScreen(
    article: Article?,
    onBackClick : () -> Unit,
    viewModel: ArticleDetailsViewModel = hiltViewModel()
){
    val isSaved by viewModel.isSaved.collectAsState()
    val context = LocalContext.current
    val textSize by viewModel.textSize.collectAsState()
    val ttsState by viewModel.ttsState.collectAsState()

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
        context = context,
        textSize = textSize,
        ttsState = ttsState,
        viewModel = viewModel
    )
}

@Composable
fun ArticleDetailContent(
    article: Article,
    isSaved: Boolean,
    onBackClick: () -> Unit,
    onToggleSave: () -> Unit,
    context: Context,
    textSize: TextSize,
    ttsState: TtsState,
    viewModel: ArticleDetailsViewModel
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

                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.4f))
                        .clickable{
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, article.title)
                                putExtra(Intent.EXTRA_TEXT, "${article.title}\n\n${article.url}")
                            }
                            context.startActivity(
                                Intent.createChooser(shareIntent, "Share article via")
                            )
                        }
                        .align(Alignment.TopEnd),
                    contentAlignment = Alignment.Center
                ){
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

            }
            Column(
                modifier = Modifier
                    .fillMaxHeight(.6f)
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
                        fontSize = textSize.descriptionSize,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 22.sp
                    )
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)

                TtsControls(
                    ttsState = ttsState,
                    onToggle = viewModel::toggleTts
                )

                article.content?.let {
                    Text(
                        text       = it,
                        color      = MaterialTheme.colorScheme.onBackground,
                        fontSize   = textSize.bodySize,
                        lineHeight = 22.sp
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Text(
                        text     = article.publishedAt.take(10),
                        color    = MaterialTheme.colorScheme.outline,
                        fontSize = 12.sp
                    )
                    Text(
                        text  = "·",
                        color = MaterialTheme.colorScheme.outline,
                        fontSize = 12.sp
                    )
                    Text(
                        text       = ReadTimeCalculator.calculate(article.content, article.description),
                        color      = MaterialTheme.colorScheme.primary,
                        fontSize   = 12.sp,
                        fontWeight = FontWeight.SemiBold
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

@Composable
fun TtsControls(
    ttsState: TtsState,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        IconButton(
            onClick  = onToggle,
            enabled  = ttsState !is TtsState.Initializing
        ) {
            when (ttsState) {
                is TtsState.Initializing -> {
                    CircularProgressIndicator(
                        modifier  = Modifier.size(24.dp),
                        color     = MaterialTheme.colorScheme.primary,
                        strokeWidth = 2.dp
                    )
                }
                is TtsState.Playing -> {
                    Icon(
                        imageVector        = Icons.Default.ExitToApp,  // stopcircle
                        contentDescription = "Stop reading",
                        tint               = MaterialTheme.colorScheme.primary,
                        modifier           = Modifier.size(32.dp)
                    )
                }
                else -> {
                    Icon(
                        imageVector        = Icons.Default.CheckCircle,  // PlayCircle
                        contentDescription = "Read article",
                        tint               = MaterialTheme.colorScheme.primary,
                        modifier           = Modifier.size(32.dp)
                    )
                }
            }
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text       = when (ttsState) {
                    is TtsState.Initializing -> "Preparing..."
                    is TtsState.Playing      -> "Reading article..."
                    is TtsState.Error        -> "Could not read article"
                    else                     -> "Listen to article"
                },
                fontSize   = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color      = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text     = "Text to speech",
                fontSize = 11.sp,
                color    = MaterialTheme.colorScheme.outline
            )
        }

        val infiniteTransition = rememberInfiniteTransition(label = "speaker")
        val scale by infiniteTransition.animateFloat(
            initialValue  = 1f,
            targetValue   = if (ttsState is TtsState.Playing) 1.2f else 1f,
            animationSpec = infiniteRepeatable(
                animation  = tween(600),
                repeatMode = RepeatMode.Reverse
            ),
            label = "speaker scale"
        )

        Icon(
            imageVector        = Icons.Default.KeyboardArrowUp, // Volume Up
            contentDescription = null,
            tint               = if (ttsState is TtsState.Playing)
                MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.outline,
            modifier           = Modifier
                .size(22.dp)
                .scale(scale)
        )
    }
}


/*@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ArticleDetailPreview() {
    NewsAppTheme {
        ArticleDetailContent(
            article = fakeArticle,
            isSaved = false,
            onBackClick = {},
            onToggleSave = {},
            context = LocalContext.current,

        )
    }
}*/

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