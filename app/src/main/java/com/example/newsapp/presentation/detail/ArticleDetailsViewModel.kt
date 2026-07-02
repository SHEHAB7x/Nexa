package com.example.newsapp.presentation.detail

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.domain.model.Article
import com.example.newsapp.domain.model.TextSize
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.utils.UserPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ArticleDetailsViewModel @Inject constructor(
    val repository: NewsRepository,
    private val preferencesManager: UserPreferencesManager,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _isSaved = MutableStateFlow(false)
    val isSaved = _isSaved.asStateFlow()

    private val _ttsState = MutableStateFlow<TtsState>(TtsState.Ready)
    val ttsState = _ttsState.asStateFlow()

    private var currentArticle : Article? = null

    val textSize: StateFlow<TextSize> = preferencesManager.selectedTextSize
        .stateIn(
            scope         = viewModelScope,
            started       = SharingStarted.WhileSubscribed(5000),
            initialValue  = TextSize.MEDIUM
        )

    private var tts: TextToSpeech? = null

    fun setArticle(article: Article){
        currentArticle = article
        viewModelScope.launch {
            repository.isArticleSaved(article.url).collect { saved ->
                _isSaved.value = saved
            }
        }
    }

    fun toggleSave(){
        viewModelScope.launch {
            currentArticle?.let { article ->
                if(_isSaved.value) repository.deleteArticle(article)
                else repository.saveArticle(article)
            }
        }
    }

    fun toggleTts() {
        when (_ttsState.value) {
            is TtsState.Playing -> stopTts()
            else -> startTts()
        }
    }

    private fun startTts(){
        val article = currentArticle ?: return
        val textToSpeak = buildString {
            append(article.title)
            append(". ")
            article.description?.let {
                append(it)
                append(". ")
            }
            article.content
                ?.replace(Regex("\\[\\+\\d+ chars\\]"), "")
                ?.let { append(it) }
        }

        if (textToSpeak.isBlank()) {
            _ttsState.value = TtsState.Error("No content to read")
            return
        }

        _ttsState.value = TtsState.Initializing


        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {


                val result = tts?.setLanguage(Locale.getDefault())

                if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    tts?.setLanguage(Locale.ENGLISH)
                }

                tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {
                        _ttsState.value = TtsState.Playing()
                    }

                    override fun onDone(utteranceId: String?) {
                        _ttsState.value = TtsState.Ready
                    }

                    @Deprecated("Deprecated in Java")
                    override fun onError(utteranceId: String?) {
                        _ttsState.value = TtsState.Error("Speech failed")
                    }
                })

                tts?.speak(
                    textToSpeak,
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    "news_article"
                )

            } else {
                _ttsState.value = TtsState.Error("TTS initialization failed")
            }
        }
    }

    private fun stopTts() {
        tts?.stop()
        _ttsState.value = TtsState.Ready
    }


    override fun onCleared() {
        super.onCleared()
        tts?.shutdown()
        tts = null
    }
}