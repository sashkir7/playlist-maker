package ui.player

sealed class PlayerState {

    object Default : PlayerState()

    object Prepared : PlayerState()

    data class Playing(
        val currentPosition: String
    ) : PlayerState()

    object Paused : PlayerState()

    data class Favorite(
        val isFavorite: Boolean
    ) : PlayerState()
}
