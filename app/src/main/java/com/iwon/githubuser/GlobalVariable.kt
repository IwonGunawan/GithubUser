package com.iwon.githubuser

class GlobalVariable {

    companion object{
        const val endPoint : String = "https://api.github.com/"
        const val headerAccept : String = "application/vnd.github.v3+json"
        const val headerAuth : String = "token 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" // expired until 23 july 2022

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