package com.lwm.practive2048;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author：lwm on 2016/7/20 17:12
 * @updateDate：2016/7/20
 * @version：1.0
 * @email：846988094@qq.com
 */
public class GameView  extends GridLayout {
    //创建出保存整个方块的二维数组
    private Cart[][] cardsMap=new Cart[4][4];
    private List<Point> emptyPoints=new ArrayList<Point>();
    //构造函数，传入参数，初始化类
    public GameView(Context context) {
        super(context);
        initGame();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGame();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initGame();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initGame();
    }

    //初始化方法
    private void initGame(){
        //设置4列,背景颜色
        setColumnCount(4);
        setBackgroundColor(Color.parseColor("#bbada0"));
        //监听用户的触摸方向
        setOnTouchListener(new OnTouchListener() {
            //不让外类访问
            private float startX, startY, offsetX, offsetY;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //实例有方法，而常量用类名调用
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = motionEvent.getX();
                        startY = motionEvent.getY();
                    case MotionEvent.ACTION_UP:
                        offsetX = motionEvent.getX() - startX;
                        offsetY = motionEvent.getY() - startY;
                        //得到偏移量后处理，判断用户向那个方向移动
                        if (Math.abs(offsetX) > Math.abs(offsetY)) {
                            if (offsetX < -5) {
                                swipeLeft();
                            } else if (offsetX > 5) {
                                swipeRight();
                            }
                        } else {
                            if (offsetY < -5) {
                                swipeUp();
                            } else if (offsetY > 5) {
                                swipeDown();
                            }
                        }
                        break;
                }
                return true;
            }
        });
    }

    @Override//适配不同手机屏幕，求得每个方格的边长
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int cardWidth=(Math.min(w,h)-10)/4;
        addCards(cardWidth, cardWidth);
        startGame();
    }
    //开启游戏
    private void startGame(){
        //开始之前清零操作
        MainActivity.getMainActivity().clearScore();
        for(int j=0;j<4;j++){
            for(int i=0;i<4;i++){
                cardsMap[i][j].setNum(0);
            }
        }
        //注意：写在循环外边
        addRandomNum();
        addRandomNum();
    }
    //添加卡片到Gridlayout
   private void addCards(int cardWidth,int cardHeight){
       Cart c;
       for(int i=0;i<4;i++) {
           for (int j = 0; j < 4; j++) {
           c=new Cart(getContext());
               c.setNum(0);
               addView(c, cardWidth, cardHeight);
               //保存到二维数组中
               cardsMap[i][j]=c;
           }
       }

   }
    //生成一个随机数
    private void addRandomNum(){
        emptyPoints.clear();
        for(int j=0;j<4;j++){
            for(int i=0;i<4;i++){
                if(cardsMap[i][j].getNum()<= 0) {
                    emptyPoints.add(new Point(i,j));
                }
            }
        }
            //随机移除一个点
            Point p=emptyPoints.remove((int)(Math.random()*emptyPoints.size()));
            //生成随机数，按照9比1的比例
            cardsMap[p.x][p.y].setNum(Math.random()>0.1?2:4);
    }
    //检查游戏是否完成
    private void checkComplete(){
        //完成标志
        Boolean isComplete=true;
        ALL://跳出循环
        for(int y=0;y<4;y++){
            for(int x=0;x<4;x++){
                if(cardsMap[x][y].getNum()<=0||
                        (x>0&&cardsMap[x][y].equals(cardsMap[x-1][y]))||
                        (x<3&&cardsMap[x][y].equals(cardsMap[x+1][y]))||
                        (y>0&&cardsMap[x][y].equals(cardsMap[x][y-1]))||
                        (y<3&&cardsMap[x][y].equals(cardsMap[x][y+1]))){
                    isComplete=false;
                    break ALL;
                }
            }
        }
        //如果游戏结束时。待测试----
        if(isComplete){
            //弹出对话框
            new AlertDialog.Builder(getContext()).setTitle("你好！").setMessage("游戏结束")
                    .setPositiveButton("重来",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startGame();
                }
            }).show();
        }
    }
    //用户向左滑动
    private void swipeLeft() {
        //定义标志
        Boolean isAdd=false;
        //按行遍历
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                for (int x1 = x + 1; x1 < 4; x1++) {
                    if (cardsMap[x1][y].getNum() > 0) {
                        if (cardsMap[x][y].getNum() <= 0) {
                            cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
                            cardsMap[x1][y].setNum(0);
                            x--;
                            isAdd=true;
                        } else if (cardsMap[x1][y].equals(cardsMap[x][y])) {
                            cardsMap[x][y].setNum(cardsMap[x1][y].getNum() * 2);
                            cardsMap[x1][y].setNum(0);
                            //有合并。即添加分数
                            MainActivity.getMainActivity().addSorce(cardsMap[x][y].getNum());
                            isAdd=true;
                        }
                    }
                    break;
                }
            }
        }
        //用户移动生效，即产生一个随机数
        if(isAdd){
            addRandomNum();
            //添加随机数时候进行判断
            checkComplete();
        }
    }
    //用户向右滑动
    private void swipeRight(){
        //定义标志
        Boolean isAdd=false;
        //按行遍历
        for(int y=0;y<4;y++){
            for(int x=3;x<=0;x--){
                for(int x1=x-1;x1>=0;x1--){
                    if(cardsMap[x1][y].getNum()>0){
                        if(cardsMap[x][y].getNum()<=0){
                            cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
                            cardsMap[x1][y].setNum(0);
                            x++;
                            isAdd=true;
                        }else if(cardsMap[x1][y].equals(cardsMap[x][y])){
                            cardsMap[x][y].setNum(cardsMap[x1][y].getNum()*2);
                            cardsMap[x1][y].setNum(0);
                            //有合并。即添加分数
                            MainActivity.getMainActivity().addSorce(cardsMap[x][y].getNum());
                            isAdd=true;
                        }
                        break;
                    }
                }
            }
        }
        //用户移动生效，即产生一个随机数
        if(isAdd){
            addRandomNum();
            checkComplete();
        }
    }
    //用户向上滑动
    private void swipeUp(){
        //定义标志
        Boolean isAdd=false;
        //按列遍历
        for(int x=0;x<4;x++){
            for(int y=0;y<4;y++){
                for(int y1=y+1;y1<4;y1++){
                    if(cardsMap[x][y1].getNum()>0){
                        if(cardsMap[x][y].getNum()<=0){
                            cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
                            cardsMap[x][y1].setNum(0);
                            y--;
                            isAdd=true;
                        }else if(cardsMap[x][y1].equals(cardsMap[x][y])){
                            cardsMap[x][y].setNum(cardsMap[x][y1].getNum()*2);
                            cardsMap[x][y1].setNum(0);
                            //有合并。即添加分数
                            MainActivity.getMainActivity().addSorce(cardsMap[x][y].getNum());
                            isAdd=true;
                        }
                        break;
                    }
                }
            }
        }
        //用户移动生效，即产生一个随机数
        if(isAdd){
            addRandomNum();
            checkComplete();
        }
    }
    //用户向下滑动
    private void swipeDown(){
        //定义标志
        Boolean isAdd=false;
        //按列遍历
        for(int x=0;x<4;x++){
            for(int y=3;y<=0;y--){
                for(int y1=x-1;y1>=0;y1--){
                    if(cardsMap[x][y1].getNum()>0){
                        if(cardsMap[x][y].getNum()<=0){
                            cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
                            cardsMap[x][y1].setNum(0);
                            y++;
                            isAdd=true;
                        }else if(cardsMap[x][y1].equals(cardsMap[x][y])){
                            cardsMap[x][y].setNum(cardsMap[x][y1].getNum()*2);
                            cardsMap[x][y1].setNum(0);
                            //有合并。即添加分数
                            MainActivity.getMainActivity().addSorce(cardsMap[x][y].getNum());
                            isAdd=true;
                        }
                        break;
                    }
                }
            }
        }
        //用户移动生效，即产生一个随机数
        if(isAdd){
            addRandomNum();
            checkComplete();
        }
    }
}
