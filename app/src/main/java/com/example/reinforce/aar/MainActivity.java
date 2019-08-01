package com.example.reinforce.aar;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.reinforce.demo_aar.SayHello;

/**
 * @anthor kb_jay
 * create at 2019-08-01 13:12
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView mTv = this.findViewById(R.id.tv);
        String text = SayHello.sayHello();
        mTv.setText(text);
    }
}
