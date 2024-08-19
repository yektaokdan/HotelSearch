package com.yektaokdan.hotelsearch

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var searchInput: EditText
    private lateinit var searchButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ResultsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        searchInput = findViewById(R.id.search_input)
        searchButton = findViewById(R.id.search_button)
        recyclerView = findViewById(R.id.results_recycler_view)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ResultsAdapter()
        recyclerView.adapter = adapter

        searchButton.setOnClickListener {
            val query = searchInput.text.toString()
            if (query.isNotEmpty()) {
                fetchHotels(query)
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun fetchHotels(query: String) {
        val client = OkHttpClient()
        val url = "https://api.makcorps.com/mapping?api_key=66c31c850285a172f2d1a3f1&name=$query"
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                response.body?.let { responseBody ->
                    val jsonResponse = responseBody.string()
                    val jsonArray = JSONArray(jsonResponse)
                    val results = mutableListOf<Hotel>()

                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val name = jsonObject.optString("name")
                        val address = jsonObject.optJSONObject("details")?.optString("address") ?: "Adres Bilgisi Yok"
                        val type = jsonObject.optString("type", "Belirtilmemiş")
                        val coords = jsonObject.optString("coords", "Koordinat Bilgisi Yok")
                        results.add(Hotel(name, address, type, coords))
                    }

                    runOnUiThread {
                        adapter.setHotels(results)
                    }
                }
            }
        })
    }


}
