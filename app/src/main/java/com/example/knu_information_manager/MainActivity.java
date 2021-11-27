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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

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
    private TextView keywordTV;
    final static String CSE_URL = "https://cse.kongju.ac.kr/bbs/ZD1110/1405/artclList.do";
    final static String KONGJU_URL = "https://www.kongju.ac.kr/kor/article/student_news/?mno=&pageIndex=";

    private ArrayList<String> knu_link;
    private ArrayList<String> cse_link;
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
        StringBuilder str = new StringBuilder("키워드 ");
        sqlDB = dbHelper.getReadableDatabase();

        Cursor cursor = sqlDB.rawQuery("SELECT * FROM KeywordTBL;", null);
        while(cursor.moveToNext()){
            str.append("#"+cursor.getString(0)+" ");
        }
        cursor.close();
        sqlDB.close();

        str.append("로 검색된 결과입니다.");
        keywordTV.setText(str);

        kongjuBtn.callOnClick();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = new ArrayList<>();
        knu_link = new ArrayList<>();
        cse_link = new ArrayList<>();
        keywordTV = (TextView) findViewById(R.id.textView_keyword);
        dbHelper = new KeyWordActivity.myDBHelper(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        kongjuBtn = (Button) findViewById(R.id.kongjuBtn);
        computerBtn = (Button)findViewById(R.id.computerBtn);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("공주대학교 정보 알림이");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);

        //네이게이션 화면 설정
        navigationView = (NavigationView) findViewById(R.id.navigationView);

        kongjuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
    }

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
                query = ".subject a";
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
            knu_link.clear();
            cse_link.clear();
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
            super.onPostExecute(unused);
            progressDialog.dismiss();
            parseAdapter = new ParseAdapter(result);
            parseAdapter.setOnItemClickListener(new ParseAdapter.OnItemClickEventListener() {
                @Override
                public void onItemClick(View view, int position) {
                    //새로운 창 열기
                    if(flag){
                        String str = "https://www.kongju.ac.kr/kor/article/student_news/"+knu_link.get(position);
                        Intent linkIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(str));
                        startActivity(linkIntent);
                    }
                    else{

                        String str = "https://cse.kongju.ac.kr"+cse_link.get(position);
                        Intent linkIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(str));
                        startActivity(linkIntent);
                    }
                }
            });
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
                                    if(flag){
                                        knu_link.add(e.attr("onclick").replaceAll("[^\\d]", ""));
                                    }
                                    else{
                                        cse_link.add(e.attr("href"));
                                    }
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
}