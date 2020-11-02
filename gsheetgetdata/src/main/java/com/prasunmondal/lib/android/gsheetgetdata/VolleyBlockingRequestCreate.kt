package com.truiton.volleyblockingrequest.com.prasunmondal.lib.GSheetGetRequest

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.RequestFuture
import com.truiton.volleyblockingrequest.Res
import org.json.JSONObject
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*

class VolleyBlockingRequestCreate {

    private var mQueue: RequestQueue? = null
    private var resultJSON: JSONObject? = null

    private inner class ThreadB(private val context: Context, val url: String) : AsyncTask<Void, Void, JSONObject>() {

        override fun doInBackground(vararg params: Void): JSONObject? {
            val futureRequest = RequestFuture.newFuture<JSONObject>()
            mQueue = CustomVolleyRequestQueue.getInstance(context.applicationContext).requestQueue

            val jsonRequest = JsonObjectRequest(Request.Method.GET, url, JSONObject(), futureRequest, futureRequest)
            jsonRequest.tag = REQUEST_TAG
            mQueue!!.add(jsonRequest)
            try {
                return futureRequest.get(10, TimeUnit.SECONDS)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } catch (e: ExecutionException) {
                e.printStackTrace()
            } catch (e: TimeoutException) {
                e.printStackTrace()
            }

            return null
        }
    }

    companion object {
        const val REQUEST_TAG = "VolleyBlockingRequestActivity"
    }

    fun startParsingTask(context: Context, url: String, onComplete: (JSONObject) -> Unit) {
        val threadA = object : Thread() {
            @SuppressLint("SetTextI18n")
            override fun run() {
                val threadB = ThreadB(context, url)
                var jsonObject: JSONObject? = null
                try {
                    jsonObject = threadB.execute().get(10, TimeUnit.SECONDS)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                } catch (e: ExecutionException) {
                    e.printStackTrace()
                } catch (e: TimeoutException) {
                    e.printStackTrace()
                }

                resultJSON = jsonObject
                onComplete.invoke(resultJSON!!)
            }
        }
        threadA.start()
    }

    fun <T> parseJSONObject(type: Type, arrayLabel: String): List<T>? {
        if(resultJSON == null) {
            return null
        }
        val jsonarray = resultJSON!!.getJSONArray(arrayLabel)
        return GsonBuilder().create().fromJson(jsonarray.toString(), type) as List<T>
    }

    fun onStop() {
        if (mQueue != null) {
            mQueue!!.cancelAll(REQUEST_TAG)
        }
    }
}