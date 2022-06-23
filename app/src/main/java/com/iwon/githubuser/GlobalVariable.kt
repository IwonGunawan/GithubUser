package com.iwon.githubuser

import android.app.AlertDialog
import android.content.Context
import android.widget.Button
import android.widget.TextView

class GlobalVariable {

    companion object{
        const val endPoint : String = "https://api.github.com/"
        const val headerAccept : String = "application/vnd.github.v3+json"
        const val headerAuth : String = "token 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" // expired until 17 August 2022

        const val TAG : String = "githubUserApp"
        const val GRAPH_USERNAME : String = "username"
        const val iRESPONSE_OK: Int = 200


        fun isIOException(t: Throwable): Boolean {
            return when (t.javaClass.canonicalName) {
                "java.net.SocketException" -> {
                    true
                }
                "java.net.ConnectException" -> {
                    true
                }
                "java.net.UnknownHostException" -> {
                    true
                }
                "javax.net.ssl.SSLException" -> {
                    true
                }
                else -> {
                    false
                }
            }
        }


    }
}