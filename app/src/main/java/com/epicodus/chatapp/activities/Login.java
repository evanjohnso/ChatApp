package com.epicodus.chatapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.epicodus.chatapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Login extends AppCompatActivity {
    @OnClick(R.id.register)
    public void registerViewWasClicked(View v) {
        startActivity(new Intent(Login.this, Register.class));
    }

    @OnClick(R.id.loginButton)
    public void loginViewWasClicked(View v) {
        user = userName.getText().toString();
        pass = password.getText().toString();

        if(user.trim().isEmpty()) {
            userName.setError("can't be blank");
        } else if(pass.equals("")) {
            password.setError("can't be blank");
        } else {
            String url = "https://chatapp-aa575.firebaseio.com/users.json";
            final ProgressDialog pd = new ProgressDialog(Login.this);
            pd.setMessage("Loading...");
            pd.show();

            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    Log.v("response", s);
                    if(s.equals("null")){
                        Toast.makeText(Login.this, "user not found", Toast.LENGTH_LONG).show();
                    }
                    else {
                        try {
                            JSONObject obj = new JSONObject(s);
                            Log.v("asJSONOBJECT", obj.toString());
                            if(!obj.has(user)){
                                Toast.makeText(Login.this, "user not found", Toast.LENGTH_LONG).show();
                            }
                            else if(obj.getJSONObject(user).getString("password").equals(pass)){
                                UserDetails.username = user;
                                UserDetails.password = pass;
                                startActivity(new Intent(Login.this, Users.class));

                            }
                            else {
                                Toast.makeText(Login.this, "incorrect password", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    pd.dismiss();
                }
            },new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    System.out.println("" + volleyError);
                    pd.dismiss();
                }
            });
            RequestQueue rQueue = Volley.newRequestQueue(Login.this);
            rQueue.add(request);
        }
    }

    private EditText userName, password;
    private String user, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        userName = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
    }
}
