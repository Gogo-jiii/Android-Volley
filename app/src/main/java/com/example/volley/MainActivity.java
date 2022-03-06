package com.example.volley;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button btnGetRequest, btnPostRequest;
    TextView txtResultValue;
    ProgressBar progressBar;
    private StringRequest getRequest, postRequest;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGetRequest = findViewById(R.id.btnGetRequest);
        btnPostRequest = findViewById(R.id.btnPostRequest);
        txtResultValue = findViewById(R.id.txtResultValue);
        progressBar = findViewById(R.id.progressBar);

        requestQueue = Volley.newRequestQueue(MainActivity.this);

        btnGetRequest.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                getRequest();
            }
        });

        btnPostRequest.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                postRequest();
            }
        });
    }

    private void postRequest() {
        progressBar.setVisibility(View.VISIBLE);

        String url = "https://reqres.in/api/users";
        String requestBody;
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("name", "IT wala");
            requestBody = jsonBody.toString();

            postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override public void onResponse(String response) {
                            Log.d("TAG_Response", response);
                            txtResultValue.setText(response);
                            progressBar.setVisibility(View.GONE);
                        }
                    }, new Response.ErrorListener() {
                @Override public void onErrorResponse(VolleyError error) {
                    Log.d("TAG_Error", error.toString());
                    progressBar.setVisibility(View.GONE);
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

            };

            getRequest.setTag("postRequest");
            requestQueue.add(postRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getRequest() {
        progressBar.setVisibility(View.VISIBLE);

        String url = "https://simplifiedcoding.net/demos/marvel";

        getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override public void onResponse(String response) {
                        txtResultValue.setText(response);

                        progressBar.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                txtResultValue.setText("Something went wrong, please try again later.");
                progressBar.setVisibility(View.GONE);
            }
        });

        getRequest.setTag("getRequest");
        requestQueue.add(getRequest);
    }

    @Override protected void onStop() {
        if (requestQueue != null) {
            requestQueue.cancelAll("getRequest");
            requestQueue.cancelAll("postRequest");
            progressBar.setVisibility(View.GONE);
        }
        super.onStop();
    }
}