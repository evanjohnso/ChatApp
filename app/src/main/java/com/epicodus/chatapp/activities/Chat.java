package com.epicodus.chatapp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

import com.epicodus.chatapp.R;

import butterknife.ButterKnife;

public class Chat extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
    }
}
