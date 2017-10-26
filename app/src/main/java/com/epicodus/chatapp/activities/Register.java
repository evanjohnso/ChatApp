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
import com.epicodus.chatapp.Constants;
import com.epicodus.chatapp.R;
import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Register extends AppCompatActivity {
    @Bind(R.id.username) EditText username;
    @Bind(R.id.password) EditText password;
    @Bind(R.id.registerButton) Button registerButton;
    String user, pass;
    DatabaseReference mDataBase;

    @OnClick(R.id.login)
    public void loginButtonWasClicked(View v) {
        startActivity(new Intent(Register.this, Login.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
//
//        Map<String, String> newUserAndPassword = new HashMap<>();
//
//        newUser.put("russia", "russia");
//        newUserAndPassword.put("password", "russia");
//
        mDataBase = FirebaseDatabase
                .getInstance()
                .getReference();

//        FirebaseDatabase
//                .getInstance()
//                .getReference("users/russia")
//                .setValue(newUserAndPassword);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = username.getText().toString();
                pass = password.getText().toString();

                if (user.equals("")) {
                    username.setError("can't be blank");
                } else if (pass.equals("")) {
                    password.setError("can't be blank");
                } else if (!user.matches("[A-Za-z0-9]+")) {
                    username.setError("only alphabet or number allowed");
                } else if (user.length() < 5) {
                    username.setError("at least 5 characters long");
                } else if (pass.length() < 5) {
                    password.setError("at least 5 characters long");
                } else {
                    final ProgressDialog pd = new ProgressDialog(Register.this);
                    pd.setMessage("Loading...");
                    pd.show();

                    mDataBase
                            .child("userNames")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (!dataSnapshot.hasChild(user)) {
                                        Map<String, String> newUser = new HashMap<>();
                                        newUser.put("password", pass);
                                        Map<String, Object> newName = new HashMap<>();
                                        newName.put(user, user);
                                        mDataBase.child("userNames").updateChildren(newName);
                                        mDataBase.child("users").child(user).setValue(newUser);
                                        Toast.makeText(Register.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Register.this, Chat.class));
                                        pd.cancel();
                                    } else {
                                        pd.cancel();
                                        Toast.makeText(Register.this, "Username already exits", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Toast.makeText(Register.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }
}