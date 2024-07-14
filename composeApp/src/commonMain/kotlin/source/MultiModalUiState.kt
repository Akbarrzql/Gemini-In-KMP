package source

sealed interface MultiModalUiState {

    /**
     * Empty state when the screen is first shown
     */
    data object Initial: MultiModalUiState

    /**
     * Still loading
     */
    data object Loading: MultiModalUiState

    /**
     * Text has been generated
     */
    data class Success(
        val outputText: String
    ): MultiModalUiState

    /**
     * There was an error generating text
     */
    data class Error(
        val errorMessage: String
    ): MultiModalUiState
}