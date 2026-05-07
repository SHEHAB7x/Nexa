# NewsApp

A modern Android news application built with **Clean Architecture** and **Jetpack Compose**.

## 📸 Screenshots
<p align="center"> 
  <img src="screenshots/splash.png" width="1080"  alt=""/>
  <img src="screenshots/home.png" width="1080"  alt=""/>
  <img src="screenshots/search.png" width="1080"  alt=""/>
  <img src="screenshots/details.png" width="1080"  alt=""/>
  <img src="screenshots/favorites.png" width="1080"  alt=""/>
</p>

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
The project follows **Clean Architecture** with 3 layers:
---

### 📦 Layers Overview

#### 🔹 Data Layer
Handles data sources and implementation details.
- Remote APIs (Retrofit)
- Local Database (Room)
- Mappers (DTO ↔ Domain)
- Repository Implementations

---

#### 🔹 Domain Layer
Contains the core business logic.
- Models (pure Kotlin)
- Repository Interfaces
- Use Cases (single responsibility per action)

---

#### 🔹 Presentation Layer
Responsible for UI and user interaction.
- Jetpack Compose UI
- ViewModels
- State Management
- Reusable Components

---

## Features

- 📰 Top headlines horizontal scroll
- 🗂️ 5 news categories — Apple, Tesla, Business, WSJ, TechCrunch
- 🔍 Search and filter articles
- 💾 Save articles to favorites
- 📄 Full article detail screen
- 🔔 Notifications screen

## Setup

1. Clone the repository

```bash
git clone https://github.com/yourname/NewsApp.git
```

2. Get a free API key from [newsapi.org](https://newsapi.org)

3. Create a `local.properties` file in the project root and add:

```properties
NEWS_API_KEY=your_api_key_here
```

4. Build and run the project in Android Studio

---
## API

This app uses [NewsAPI.org](https://newsapi.org) with the following endpoints:

| Category   | Endpoint                                   |
|------------|--------------------------------------------|
| Apple      | `GET /v2/everything?q=apple`               |
| Tesla      | `GET /v2/everything?q=tesla`               |
| Business   | `GET /v2/top-headlines?category=business`  |
| WSJ        | `GET /v2/everything?domains=wsj.com`       |
| TechCrunch | `GET /v2/top-headlines?sources=techcrunch` |

---

## Author

Built by **Shehab Abdelhares**  
[GitHub](https://github.com/SHEHAB7x) · [LinkedIn](https://www.linkedin.com/in/shehab0x/)
