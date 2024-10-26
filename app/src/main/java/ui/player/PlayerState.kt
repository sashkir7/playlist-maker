package ui.player

sealed interface PlayerState {

    object Default : PlayerState

    object Prepared : PlayerState

    data class Playing(
        val currentPosition: String
    ) : PlayerState

    object Paused : PlayerState

}
