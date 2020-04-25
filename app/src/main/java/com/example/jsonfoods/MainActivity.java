package com.example.jsonfoods;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

public class MainActivity extends AppCompatActivity {

    private TextView tv;
    private Button btnDownload;
    private Button btnClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.tv);
        btnDownload = findViewById(R.id.button);
        btnClear = findViewById(R.id.btnClear);

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadFoodList();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setText("Press Download button to view data");
            }
        });
    }

    private void downloadFoodList() {
        //Create a RequestQueue using Volley.newRequestQueue
        RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "https://api.myjson.com/bins/70imm";

        Toast.makeText(this, "Making request", Toast.LENGTH_SHORT).show();

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("RESPONSE", "GOT respomse");

                Toast.makeText(MainActivity.this, "Length = "+response.length(),
                        Toast.LENGTH_LONG).show();
                //Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                tv.setText(response.toString());
                Gson gson = new Gson();
                StringBuilder sb = new StringBuilder();
                FoodItem foodItem = null;
                try {
                    for (int i = 0; i < response.length(); i++) {
                         foodItem = gson.fromJson(response.getJSONObject(i).toString(), FoodItem.class);
                        sb.append((i+1) +"."+foodItem.toString() + "\n");
                    }
                } catch (JSONException jsonException) {

                }
                //TempData tdata = gson.fromJson(response.toString(), TempData.class);
                tv.setText(sb.toString());
            }
        },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        tv.setText(error.toString());
                    }
                });

        queue.add(jsonObjectRequest);
    }
}
