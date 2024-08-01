package domain.search

import domain.player.Track

interface TrackInteractor {

    fun searchTrack(expression: String, consumer: Consumer)

    interface Consumer {
        fun consume(foundTracks: List<Track>?, errorMessage: String?)
    }
}