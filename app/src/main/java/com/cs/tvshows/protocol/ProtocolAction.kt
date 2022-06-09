package com.cs.tvshows.protocol

sealed class ProtocolAction {
    class OnOpenTvShowDetails(val tvShowId: Int) : ProtocolAction()
}