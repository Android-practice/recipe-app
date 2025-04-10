package yj.practice.myrecipeapp

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    //private
    private val _categorieState = mutableStateOf(RecipeState())

    //public ( expose to the other class )
    val categorieState: State<RecipeState> =
        _categorieState //read-only (State<>) & when value "RecipeState" changes, UI will be automatically update

    init {
        fetchCategories()
    }

    private fun fetchCategories() {
        // viewModelScope : provide launch for suspend(run on the background-use coroutine) functions to be processed
        viewModelScope.launch { //coroutine scope!
            try {
                val response =
                    recipeService.getCategories() //getCategories : suspend function! -> which require viewModelScope.launch
                _categorieState.value = _categorieState.value.copy(
                    list = response.categories,
                    loading = false, //complete (success) no longer loading
                    error = null
                )

            } catch (e: Exception) {
                _categorieState.value = _categorieState.value.copy(
                    loading = false, //complete (fail) no longer loading
                    error = "Error fetching Categories ${e.message}"
                )
            }

        }
    }

    data class RecipeState(
        val loading: Boolean = true,
        val list: List<Category> = emptyList(),
        val error: String? = null,
    )
}