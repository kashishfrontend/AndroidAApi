package com.example.api

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var myAdapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)

        // Initialize Retrofit
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://dummyjson.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getProductData()

        // Enqueue the API call
        retrofitData.enqueue(object : Callback<MyData?> {
            override fun onResponse(call: Call<MyData?>, response: Response<MyData?>) {
                // Check if the response is successful
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    val productList = responseBody?.products

                    // Make sure productList is not null
                    if (productList != null) {
                        myAdapter = MyAdapter(this@MainActivity, productList)
                        recyclerView.adapter = myAdapter
                        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                    } else {
                        Log.e("MainActivity", "Product list is null")
                    }
                } else {
                    Log.e("MainActivity", "API call unsuccessful: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<MyData?>, t: Throwable) {
                // Log the failure message
                Log.e("MainActivity", "API call failed: ${t.message}")
            }
        })


    }
}
