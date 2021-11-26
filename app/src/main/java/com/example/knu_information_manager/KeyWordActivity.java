package com.example.knu_information_manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class KeyWordActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private RecyclerView recyclerView;
    private ArrayList<String> list;
    private Button registerBtn, deleteBtn;
    private EditText inputText;
    private SQLiteDatabase sqlDB;
    private myDBHelper dbHelper;
    private TextView keywordView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.keyword);
        registerBtn = (Button) findViewById(R.id.registerBtn);

        deleteBtn = (Button) findViewById(R.id.deleteBtn_list);
        keywordView = (TextView) findViewById(R.id.keyword_list);

        inputText = (EditText)findViewById(R.id.inputText);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("KNU Info Manager");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);

        navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()){
                    case R.id.home:
                        finish();
                        return true;
                    case R.id.Keyword:
                        intent = getIntent();
                        finish();
                        startActivity(intent);
                        return true;
                }
                return false;
            }
        });

        //어댑터 호출
        list = new ArrayList<>();
        dbHelper = new myDBHelper(this);
        viewKeywords();
        KeyListAdapter keyListAdapter = new KeyListAdapter(list);
        //리사이클러 뷰 삭제 버튼 이벤트
        keyListAdapter.setOnItemClickListener(new KeyListAdapter.OnItemClickEventListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getApplicationContext(), list.get(position),Toast.LENGTH_SHORT).show();
                String keyword = list.get(position);
                sqlDB = dbHelper.getWritableDatabase();
                sqlDB.execSQL("DELETE FROM KeywordTBL WHERE keywords = '"+keyword+"';");
                list.remove(position);
                keyListAdapter.notifyDataSetChanged();
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(keyListAdapter);


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.clear();
                String keyword = inputText.getText().toString();
                sqlDB = dbHelper.getWritableDatabase();
                sqlDB.execSQL("INSERT INTO KeywordTBL VALUES ('"+keyword+"')");
                //Toast.makeText(getApplicationContext(), keyword+" 가 추가되었습니다.", Toast.LENGTH_SHORT).show();

                Cursor cursor = sqlDB.rawQuery("SELECT * FROM KeywordTBL;", null);
                while(cursor.moveToNext()){
                    list.add(cursor.getString(0));
                    Toast.makeText(getApplicationContext(), list.get(0), Toast.LENGTH_SHORT).show();
                }
                keyListAdapter.notifyDataSetChanged();
                cursor.close();
                sqlDB.close();
            }
        });

    }
    public void viewKeywords(){

        sqlDB = dbHelper.getReadableDatabase();

        Cursor cursor = sqlDB.rawQuery("SELECT * FROM KeywordTBL;", null);
        while(cursor.moveToNext()){
            list.add(cursor.getString(0));
        }
        cursor.close();
        sqlDB.close();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                drawerLayout.openDrawer(Gravity.LEFT);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public class myDBHelper extends SQLiteOpenHelper{

        public myDBHelper(@Nullable Context context) {
            super(context, "KeyWordDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE KeywordTBL(keywords CHAR(50));");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //
        }
    }
}
