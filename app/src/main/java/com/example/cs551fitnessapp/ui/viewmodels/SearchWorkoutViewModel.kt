package com.example.cs551fitnessapp.ui.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.cs551fitnessapp.ui.viewmodels.states.ExerciseUiState
import com.example.cs551fitnessapp.database.Exercise
import com.example.cs551fitnessapp.database.RetrofitInstance
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import android.net.ConnectivityManager
import android.net.NetworkCapabilities


@OptIn(FlowPreview::class)
class SearchWorkoutViewModel(application: Application) : AndroidViewModel(application) {

    private val _searchQuery  = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _uiState = MutableStateFlow<ExerciseUiState>(ExerciseUiState.Idle)
    val uiState: StateFlow<ExerciseUiState> = _uiState.asStateFlow()

    private var nextCursor   : String?  = null
    private var canLoadMore  : Boolean  = true
    private var isLoadingMore: Boolean  = false
    private val pageSize = 20

    init {
        viewModelScope.launch {
            _searchQuery
                .debounce(300)
                .distinctUntilChanged()
                .collectLatest { query ->
                    resetCursor()
                    fetchExercises(query, reset = true)
                }
        }
    }

    fun onSearchQueryChange(query: String) { _searchQuery.value = query }

    fun loadNextPage() {
        if (!canLoadMore || isLoadingMore) return
        viewModelScope.launch {
            fetchExercises(_searchQuery.value, reset = false)
        }
    }

    fun reloadIfIdle() { // Called from the screen after rotation
        if (_uiState.value is ExerciseUiState.Idle) {
            viewModelScope.launch {
                fetchExercises(_searchQuery.value, reset = true)
            }
        }
    }

    private fun resetCursor() {
        nextCursor    = null
        canLoadMore   = true
        isLoadingMore = false
    }

    private suspend fun fetchExercises(query: String, reset: Boolean) {

        if (!isInternet(getApplication())) {
            _uiState.value = ExerciseUiState.NoInternet
            return
        }

        if (reset) _uiState.value = ExerciseUiState.Loading
        isLoadingMore = !reset

        try {
            val response = RetrofitInstance.api.getExercises(
                query = query,
                limit = pageSize,
                after = if (reset) null else nextCursor
            )
            val newItems = response.data
            nextCursor  = response.meta?.nextCursor
            canLoadMore = nextCursor != null

            val existing = if (reset) emptyList()
            else (_uiState.value as? ExerciseUiState.Success)?.exercises ?: emptyList()

            _uiState.value = ExerciseUiState.Success(existing + newItems)

        } catch (e: Exception) {
            if (reset) _uiState.value = ExerciseUiState.Error(e.message ?: "Something went wrong")
        } finally {
            isLoadingMore = false
        }
    }

    fun isInternet(context: Context): Boolean { //check internet connection
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = cm.getNetworkCapabilities(cm.activeNetwork) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
