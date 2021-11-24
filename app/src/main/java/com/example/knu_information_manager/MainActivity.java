package com.example.knu_information_manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity{
    TextView textView;

    private RecyclerView recyclerView;
    private ArrayList<ViewData> result = new ArrayList<>();
    private ParseAdapter parseAdapter;
    private int count=-1;
    private Button kongjuBtn, computerBtn;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    final static String CSE_URL = "https://cse.kongju.ac.kr/ZD1110/11560/subview.do?enc=Zm5jdDF8QEB8JTJGYmJzJTJGWkQxMTEwJTJGMTQwNSUyRmFydGNsTGlzdC5kbyUzRnBhZ2UlM0QxJTI2c3JjaENvbHVtbiUzRCUyNnNyY2hXcmQlM0QlMjZiYnNDbFNlcSUzRCUyNmJic09wZW5XcmRTZXElM0QlMjZyZ3NCZ25kZVN0ciUzRCUyNnJnc0VuZGRlU3RyJTNEJTI2aXNWaWV3TWluZSUzRGZhbHNlJTI2";
    final static String KONGJU_URL = "https://www.kongju.ac.kr/kor/article/student_news/?mno=&pageIndex=1&categoryCnt=1&searchCategory=&searchCategory0=&searchCondition=1&searchKeyword=#article";
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //textView = (TextView) findViewById(R.id.textView1);
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
                result.clear();
                Parsing parsing = new Parsing(KONGJU_URL);
                parsing.execute();
            }
        });
        computerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* TODO parsing 클래스 하나 더만들어야함
                result.clear();
                Parsing parsing = new Parsing(CSE_URL);
                parsing.execute();

                 */
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()){
                    case R.id.home:
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
        Parsing parsing = new Parsing(KONGJU_URL);
        parsing.execute();
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

        public Parsing(String URL) {
            this.URL = URL;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void unused) {
            //super.onPostExecute(unused);
            //textView.setText(result.toString());
            parseAdapter = new ParseAdapter(result);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(parseAdapter);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //String URL ="https://www.kongju.ac.kr/kor/article/student_news/?mno=&pageIndex=1&categoryCnt=1&searchCategory=&searchCategory0=&searchCondition=1&searchKeyword=#article";

            try {
                Document doc = Jsoup.connect(URL).get();
                Elements temele = doc.select(".subject");
                Boolean isEmpty = temele.isEmpty();


                if(isEmpty==false){
                    for(Element e:temele){
                        if(e.text().contains("2021")){
                            count++;
                            ViewData data = new ViewData(Integer.toString(count), e.text());
                            result.add(data);
                        }
                    }
                    System.out.println("document 불러오기 성공");
                }
                //parseAdapter.notifyDataSetChanged();

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