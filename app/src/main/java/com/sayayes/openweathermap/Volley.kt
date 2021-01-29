package com.sayayes.openweathermap

import android.content.Context
import android.support.v7.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import org.json.JSONObject


class VolleyActivity : AppCompatActivity() {


    interface VolleyStringResponse {
        fun onSuccess(response: String?)
        fun onError(error: VolleyError?)
    }

    fun getCurrentWeather(url: String?, volleyResponse: VolleyStringResponse) {
        val queue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(Request.Method.GET, url, object: Response.Listener<String?> {
            override fun onResponse(response: String?) {
                volleyResponse.onSuccess(response)
            }
        }, object: Response.ErrorListener {
            override fun onErrorResponse(error: VolleyError?) {
                volleyResponse.onError(error)
            }
        })
//        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest)
        queue.add(stringRequest)
    }



    fun getForecastWeather(url: String?, volleyResponse: VolleyStringResponse) {
        val queue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(Request.Method.GET, url, object: Response.Listener<String?> {
            override fun onResponse(response: String?) {
                volleyResponse.onSuccess(response)
            }
        }, object: Response.ErrorListener {
            override fun onErrorResponse(error: VolleyError?) {
                volleyResponse.onError(error)
            }
        })
//        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest)
        queue.add(stringRequest)
    }

}

////Interface
//interface VolleyStringResponse {
//    fun onSuccess(response: String?)
//    fun onError(error: VolleyError?)
//}