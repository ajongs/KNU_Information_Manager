package com.example.knu_information_manager;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{
    TextView textView;

    private RecyclerView recyclerView;
    private ArrayList<ViewData> result = new ArrayList<>();
    private ParseAdapter parseAdapter;
    private int count=-1;
    private Button kongjuBtn, computerBtn;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private KeyWordActivity.myDBHelper dbHelper;
    private SQLiteDatabase sqlDB;
    private ArrayList<String> list;
    //final static String CSE_URL = "https://cse.kongju.ac.kr/ZD1110/11560/subview.do?enc=Zm5jdDF8QEB8JTJGYmJzJTJGWkQxMTEwJTJGMTQwNSUyRmFydGNsTGlzdC5kbyUzRnBhZ2UlM0QxJTI2c3JjaENvbHVtbiUzRCUyNnNyY2hXcmQlM0QlMjZiYnNDbFNlcSUzRCUyNmJic09wZW5XcmRTZXElM0QlMjZyZ3NCZ25kZVN0ciUzRCUyNnJnc0VuZGRlU3RyJTNEJTI2aXNWaWV3TWluZSUzRGZhbHNlJTI2";
    final static String CSE_URL = "https://cse.kongju.ac.kr/bbs/ZD1110/1405/artclList.do";
    final static String KONGJU_URL = "https://www.kongju.ac.kr/kor/article/student_news/?mno=&pageIndex=";

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

    @Override
    protected void onResume() {
        super.onResume();
        kongjuBtn.callOnClick();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //textView = (TextView) findViewById(R.id.textView1);
        list = new ArrayList<>();
        dbHelper = new KeyWordActivity.myDBHelper(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        kongjuBtn = (Button) findViewById(R.id.kongjuBtn);
        computerBtn = (Button)findViewById(R.id.computerBtn);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("KNU Info Manager");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);

        //네이게이션 화면 설정
        navigationView = (NavigationView) findViewById(R.id.navigationView);

        kongjuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Intent intent = new Intent(MainActivity.this, KeyWordActivity.class);
                startActivity(intent);*/
                computerBtn.setBackgroundColor(getResources().getColor(R.color.violet));
                computerBtn.setTextColor(Color.WHITE);
                kongjuBtn.setBackgroundColor(Color.WHITE);
                kongjuBtn.setTextColor(getResources().getColor(R.color.violet));
                Parsing parsing = new Parsing(KONGJU_URL, true);
                parsing.execute();
            }
        });
        computerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                list.clear();
                result.clear();
                kongjuBtn.setBackgroundColor(getResources().getColor(R.color.violet));
                kongjuBtn.setTextColor(Color.WHITE);
                computerBtn.setBackgroundColor(Color.WHITE);
                computerBtn.setTextColor(getResources().getColor(R.color.violet));
                Parsing parsing = new Parsing(CSE_URL, false);
                parsing.execute();


            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()){
                    case R.id.home:
                        result.clear();
                        intent = getIntent();
                        finish();
                        startActivity(intent);
                        break;
                    case R.id.Keyword:
                        intent = new Intent(MainActivity.this, KeyWordActivity.class);
                        startActivity(intent);
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });

        //final Bundle bundle = new Bundle();

        //StringBuilder sb = new StringBuilder();
        //Parsing parsing = new Parsing(KONGJU_URL,true );
        //Parsing parsing = new Parsing(CSE_URL, false);
        //parsing.execute();
        /*
        new Thread(){
            @Override
            public void run() {
                String URL ="https://www.kongju.ac.kr/kor/article/student_news/?mno=&pageIndex=1&categoryCnt=1&searchCategory=&searchCategory0=&searchCondition=1&searchKeyword=#article";

                try {
                    Document doc = Jsoup.connect(URL).get();
                    Elements temele = doc.select(".subject");
                    Boolean isEmpty = temele.isEmpty();


                    if(isEmpty==false){
                        for(Element e:temele){
                            if(e.text().contains("예산")){
                                result.add(e.text());
                                sb.append(e.text());
                            }
                        }
                        bundle.putString("article", sb.toString());
                        System.out.println(sb.toString());
                        Message msg = handler.obtainMessage();
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("document 불러오기 실패");

                }
            }
        }.start();*/
    }
    /*
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.home){
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
        else if(id == R.id.Keyword){
            Intent intent = new Intent(MainActivity.this, KeyWordActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }*/

    private class Parsing extends AsyncTask<Void, Void, Void>{
        String URL;
        String query;
        Boolean flag;
        ProgressDialog progressDialog = null;

        public Parsing(String URL, Boolean flag) {
            this.URL = URL;
            //true 공주대사이트
            this.flag = flag;
            if(flag==true){
                query = ".subject";
            }
            //false 컴공사이트
            else{
                query = ".td-subject a";
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            count=0;
            result.clear();
            list.clear();
            progressDialog = new ProgressDialog(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            if(flag){
                progressDialog.setMessage("공주대학교 공지사항 불러오는 중...");
            }
            else{
                progressDialog.setMessage("컴퓨터공학부 공지사항 불러오는 중...");
            }
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void unused) {
            //super.onPostExecute(unused);
            //textView.setText(result.toString());
            progressDialog.dismiss();
            parseAdapter = new ParseAdapter(result);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(parseAdapter);
            parseAdapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                sqlDB = dbHelper.getReadableDatabase();

                Cursor cursor = sqlDB.rawQuery("SELECT * FROM KeywordTBL;", null);
                while(cursor.moveToNext()){
                    list.add(cursor.getString(0));
                }
                cursor.close();
                sqlDB.close();

                for(int i=0; i<10; i++){
                    Document doc;
                    if(flag){
                        doc = Jsoup.connect(URL+i).get();
                    }else {
                        doc = Jsoup.connect(URL).data("page", Integer.toString(i)).post();
                    }

                    Elements temele = doc.select(query);
                    Boolean isEmpty = temele.isEmpty();


                    if(isEmpty==false){
                        for(Element e:temele){
                            for(String keyword : list){
                                if(e.text().contains(keyword)){
                                    count++;
                                    ViewData data = new ViewData(Integer.toString(count), e.text());
                                    result.add(data);
                                }
                            }

                        }
                        System.out.println("document 불러오기 성공");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("document 불러오기 실패");

            }
            return null;
        }
    }
    /* 스레드와 핸들러 사용부분
    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            Bundle bundle = msg.getData();
            textView.setText(bundle.getString("article"));
        }
    };*/
}