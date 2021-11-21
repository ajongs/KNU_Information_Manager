package com.example.knu_information_manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity {
    TextView textView;

    private RecyclerView recyclerView;
    private ArrayList<ViewData> result = new ArrayList<>();
    private ParseAdapter parseAdapter;
    private int count=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //textView = (TextView) findViewById(R.id.textView1);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);


        //final Bundle bundle = new Bundle();

        //StringBuilder sb = new StringBuilder();
        Parsing parsing = new Parsing();
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
    private class Parsing extends AsyncTask<Void, Void, Void>{

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
            String URL ="https://www.kongju.ac.kr/kor/article/student_news/?mno=&pageIndex=1&categoryCnt=1&searchCategory=&searchCategory0=&searchCondition=1&searchKeyword=#article";

            try {
                Document doc = Jsoup.connect(URL).get();
                Elements temele = doc.select(".subject");
                Boolean isEmpty = temele.isEmpty();


                if(isEmpty==false){
                    for(Element e:temele){
                        if(e.text().contains("공통")){
                            count++;
                            ViewData data = new ViewData(Integer.toString(count), e.text());
                            result.add(data);
                        }
                    }
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