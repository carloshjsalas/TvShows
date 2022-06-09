package com.cs.tvshows.protocol

interface CommunicationCallback {
    fun onFragmentEvent(action: ProtocolAction)
}