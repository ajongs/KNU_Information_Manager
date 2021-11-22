package com.example.knu_information_manager;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

public class KeyWordActivity extends Activity {
    private Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.keyword);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }
}
