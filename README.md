# NewsApp

A modern Android news application built with **Clean Architecture** and **Jetpack Compose**.

## Screenshots
_Coming soon_

## Tech Stack

| Layer        | Technology                                        |
|--------------|---------------------------------------------------|
| UI           | Jetpack Compose, Material 3                       |
| Architecture | Clean Architecture (Domain / Data / Presentation) |
| DI           | Hilt                                              |
| Networking   | Retrofit, OkHttp, Gson                            |
| Local DB     | Room                                              |
| Images       | Coil                                              |
| Async        | Kotlin Coroutines, Flow                           |
| Navigation   | Jetpack Navigation Compose                        |

## Architecture

app/
├── data/          # Retrofit, Room, DTOs, Mappers, RepositoryImpl
├── domain/        # Models, Repository interfaces, UseCases
├── presentation/  # Compose screens, ViewModels, UiState
└── di/            # Hilt modules

## Features

- Top headlines horizontal scroll
- 5 news categories (Apple, Tesla, Business, WSJ, TechCrunch)
- Article detail screen
- Save articles to favorites
- Search and filter

## Setup

1. Clone the repository
2. Get a free API key from [newsapi.org](https://newsapi.org)
3. Add to your `local.properties`: NEWS_API_KEY=your_api_key_here

## API

This app uses [NewsAPI.org](https://newsapi.org).