package com.example.galleryme.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.galleryme.core.PageQuery
import com.example.galleryme.core.helper.Resource
import com.example.galleryme.core.helper.SharedPreferenceHelper
import com.example.galleryme.domain.model.Image
import com.example.galleryme.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageViewModel @Inject constructor(
    private val getAllImageUseCase: GetAllImageUseCase,
    private val getImagesByAuthorUseCase: GetImagesByAuthorUseCase,
    private val getAllAuthorsUseCase: GetAllAuthorsUseCase,
    private val sharedPreferenceHelper: SharedPreferenceHelper
) : ViewModel() {

    private val _viewState = MutableStateFlow<UiState>(UiState.Loading())
    val viewState = _viewState.asStateFlow()

    private val _currentAuthor: MutableLiveData<String> = MutableLiveData("")
    val currentAuthor: LiveData<String> = _currentAuthor

    private val _authors = MutableLiveData(emptyList<String>())
    val authors: LiveData<List<String>> = _authors


    fun setAuthor(author: String) {
        sharedPreferenceHelper.setAuthor(author)
        _currentAuthor.value = author
        refresh()
    }

    init {
        _currentAuthor.value = sharedPreferenceHelper.getAuthor()
        refresh()
        getAuthors()
    }

    private fun refresh() {
        if (_currentAuthor.value == "") {
            getImages()
        } else {
            getImagesByAuthor()
        }
    }

    fun getImages() {
        viewModelScope.launch {
            getAllImageUseCase.execute(GetAllImagesUseCaseParams(PageQuery(1, 30)))
                .map { result ->
                    if (result.data != null) {
                        getAuthors()
                    }
                    result
                }
                .collect { result ->
                    _viewState.update {
                        when (result.status) {
                            Resource.Status.LOADING -> {
                                UiState.Loading(result.data)
                            }
                            Resource.Status.SUCCESS -> {
                                UiState.Success(result.data!!)
                            }
                            Resource.Status.ERROR -> {
                                UiState.Error(result.message, result.data)
                            }
                        }
                    }
                }
        }
    }

    private fun getImagesByAuthor() {
        val author = _currentAuthor.value ?: ""
        viewModelScope.launch {
            _viewState.update {
                UiState.Loading()
            }
            getImagesByAuthorUseCase.execute(GetImagesByAuthorUseCaseParams(author))
                .collect { result ->
                    _viewState.update {
                        UiState.Success(result)
                    }
                }
        }
    }

    private fun getAuthors() {
        viewModelScope.launch {
            getAllAuthorsUseCase.execute()
                .collect { result ->
                    _authors.value = result
                }
        }
    }
}

sealed interface UiState {
    data class Loading(
        val data: List<Image>? = null
    ) : UiState

    data class Success(
        val data: List<Image>
    ) : UiState

    data class Error(
        val message: String? = null, val data: List<Image>? = null
    ) : UiState
}