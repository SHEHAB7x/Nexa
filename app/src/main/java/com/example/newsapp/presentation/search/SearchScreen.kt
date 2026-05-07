package com.example.newsapp.presentation.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.newsapp.domain.model.Article
import com.example.newsapp.presentation.home.ArticleListItem
import com.example.newsapp.presentation.home.categories
import com.example.newsapp.presentation.theme.DarkGray
import com.example.newsapp.presentation.theme.LightGray
import com.example.newsapp.presentation.theme.MediumGray
import com.example.newsapp.presentation.theme.Primary
import com.example.newsapp.presentation.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun  SearchScreen(
    onArticleClick: (Article) -> Unit,
    onBackClick: () -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = DarkGray,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onBackClick() }
            )

            OutlinedTextField(
                value = uiState.query,
                onValueChange = viewModel::onQueryChanged,
                placeholder = { Text("Search news...", color = MediumGray) },
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Primary,
                    unfocusedBorderColor = LightGray,
                    cursorColor = Primary
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = MediumGray
                    )
                },
                trailingIcon = {
                    if (uiState.query.isNotBlank()) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear",
                            tint = MediumGray,
                            modifier = Modifier.clickable {
                                viewModel.onQueryChanged("")
                            }
                        )
                    }
                }
            )
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Primary)
                        .clickable { viewModel.toggleFilterSheet() }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "⚙ Filter",
                        color = White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            items(categories) { category ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            if (uiState.selectedCategory == category) Primary
                            else LightGray
                        )
                        .clickable { viewModel.onCategorySelected(category) }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = category.label,
                        color = if (uiState.selectedCategory == category) White
                        else DarkGray,
                        fontSize = 13.sp
                    )
                }
            }
        }

        HorizontalDivider(color = LightGray)

        if (uiState.articles.isNotEmpty()) {
            Text(
                text = "About ${uiState.articles.size} results for \"${uiState.query}\"",
                color = MediumGray,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Primary)
                }
            }

            uiState.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = uiState.error!!, color = Color.Red)
                }
            }

            uiState.query.isBlank() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Type to search news...",
                        color = MediumGray,
                        fontSize = 14.sp
                    )
                }
            }

            uiState.articles.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No results for \"${uiState.query}\"",
                        color = MediumGray,
                        fontSize = 14.sp
                    )
                }
            }

            else -> {
                LazyColumn {
                    items(uiState.articles) { article ->
                        ArticleListItem(
                            article = article,
                            onClick = { onArticleClick(article) }
                        )
                        HorizontalDivider(
                            color    = LightGray,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }
        }
    }

    if (uiState.showFilterSheet) {
        FilterBottomSheet(
            onDismiss = viewModel::dismissFilterSheet,
            onSave    = viewModel::dismissFilterSheet
        )
    }
}

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun FilterBottomSheet(
        onDismiss: () -> Unit,
        onSave: () -> Unit
    ) {
        var selectedSort by remember { mutableStateOf("Recommended") }
        val sortOptions = listOf("Recommended", "Latest", "Most Viewed")

        ModalBottomSheet(
            onDismissRequest = onDismiss,
            containerColor = White,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Filter",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkGray
                    )
                    TextButton(onClick = { selectedSort = "Recommended" }) {
                        Text(text = "⟳ Reset", color = MediumGray)
                    }
                }

                Text(
                    text = "Sort By",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DarkGray
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    sortOptions.forEach { option ->
                        FilterChip(
                            selected = selectedSort == option,
                            onClick = { selectedSort = option },
                            label = { Text(option) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Primary,
                                selectedLabelColor = White,
                                containerColor = LightGray,
                                labelColor = DarkGray
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onSave,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary)
                ) {
                    Text(
                        text = "SAVE",
                        color = White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
