package com.lwm.practive2048;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * @author：lwm on 2016/7/20 19:22
 * @updateDate：2016/7/20
 * @version：1.0
 * @email：846988094@qq.com
 */
public class Cart extends FrameLayout {
    //私有化成员变量
    private TextView label;
    private int num=0;
    //构造方法，初始化参数
    public Cart(Context context) {
        super(context);
        //创建出label，设定出大小,背景颜色，并居中显示
        label=new TextView(getContext());
        label.setTextSize(40);
        label.setBackgroundColor(Color.parseColor("#ccc0b3"));
        label.setGravity(Gravity.CENTER);
        LayoutParams lp=new LayoutParams(-1,-1);
        lp.setMargins(10,10,0,0);
        addView(label,lp);
        setNum(0);
    }
    //get，set
    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
        if(num<=0){
            //设置为空字符串
            label.setText("");
        }else {
            //设置文本为字符串
            label.setText(num + "");
        }
    }

    //继承equals方法，用于比较两个cart中num是否相同
    public boolean equals(Cart o) {
        return getNum()==o.getNum();

    }
}
