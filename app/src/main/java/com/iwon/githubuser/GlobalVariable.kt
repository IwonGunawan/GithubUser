package com.iwon.githubuser

import android.widget.ImageView
import com.bumptech.glide.Glide

class GlobalVariable {

    companion object{
        const val endPoint : String = "https://api.github.com/"
        const val headerAccept : String = "application/vnd.github.v3+json"
        const val headerAuth : String = BuildConfig.KEY_TOKEN // Expires on Wed, Aug 24 2022

        const val TAG : String = "githubUserApp"
        const val sFOLLOWERS : String = "followers"
        const val sFOLLOWING : String = "following"
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

        fun ImageView.loadImage(url : String?){
            Glide.with(this.context)
                .load(url)
                .centerCrop()
                .into(this)
        }
    } // END
}