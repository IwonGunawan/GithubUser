package com.iwon.githubuser

class GlobalVariable {

    companion object{
        const val endPoint : String = "https://api.github.com/"
        const val headerAccept : String = "application/vnd.github.v3+json"
        const val headerAuth : String = "token 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" // expired until 17 August 2022

        const val TAG : String = "githubUserApp"
        const val GRAPH_USERNAME : String = "username"
        const val iRESPONSE_OK: Int = 200
    }
}