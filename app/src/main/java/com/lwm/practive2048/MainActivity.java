package com.lwm.practive2048;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    //定义
    private TextView tv_Sorce;
    private static MainActivity mainActivity=null;
    private int score=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_Sorce=(TextView)findViewById(R.id.score);
    }

    public void clearScore(){
        score=0;
        showSorce();
    }
    public void showSorce(){

         tv_Sorce.setText(score+"");
    }
    public void addSorce(int s){
        score+=s;
        showSorce();
    }
    //构造函数，传递自己
    public MainActivity(){
        mainActivity=this;
    }
    //得到本类的实例
    public static MainActivity getMainActivity(){
        return mainActivity;
    }

}
