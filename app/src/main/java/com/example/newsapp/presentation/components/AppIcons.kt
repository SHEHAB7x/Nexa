package com.example.newsapp.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.newsapp.R

object AppIcons {
    val GitHub
        @Composable
        get() = painterResource(R.drawable.github)

    val LinkedIn
    @Composable
    get() = painterResource(R.drawable.ic_linkedin)

    val Email
    @Composable
    get() = painterResource(R.drawable.ic_email)

    val OpenInNew
    @Composable
    get() = painterResource(R.drawable.ic_open_in_new)
}