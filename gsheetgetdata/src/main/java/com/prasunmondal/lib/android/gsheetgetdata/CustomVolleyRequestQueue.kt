package com.prasunmondal.lib.android.gsheetgetdata

import android.content.Context

import com.android.volley.RequestQueue
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack

class CustomVolleyRequestQueue private constructor(context: Context) {
    private var mRequestQueue: RequestQueue? = null

    // Don't forget to start the volley request queue
    val requestQueue: RequestQueue
        get() {
            if (mRequestQueue == null) {
                val cache = DiskBasedCache(mCtx.cacheDir, 10 * 1024 * 1024)
                val network = BasicNetwork(HurlStack())
                mRequestQueue = RequestQueue(cache, network)
                mRequestQueue!!.start()
            }
            return mRequestQueue as RequestQueue
        }

    init {
        mCtx = context
        mRequestQueue = requestQueue
    }

    companion object {
        var mInstance: CustomVolleyRequestQueue? = null
        lateinit var mCtx: Context

        @Synchronized
        fun getInstance(context: Context): CustomVolleyRequestQueue {
            if (mInstance == null) {
                mInstance = CustomVolleyRequestQueue(context)
            }
            return mInstance as CustomVolleyRequestQueue
        }
    }
}
