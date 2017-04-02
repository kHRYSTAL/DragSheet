package me.khrystal.dragsheetdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Space;

import me.khrystal.widget.dragsheet.InsideHeaderLayout;
import me.khrystal.widget.dragsheet.OutSideScrollView;
import me.khrystal.widget.dragsheet.OutsideDownFrameLayout;

public class MainActivity extends AppCompatActivity {

    OutsideDownFrameLayout frameLayout;
    OutSideScrollView scrollView;
    InsideHeaderLayout headerLayout;
    Space space;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frameLayout = (OutsideDownFrameLayout) findViewById(R.id.downFrameLayout);
        scrollView = (OutSideScrollView) findViewById(R.id.scrollView);
        headerLayout = (InsideHeaderLayout) findViewById(R.id.insideLayout);
        space = (Space) findViewById(R.id.space);
        headerLayout.setOutsideLayout(frameLayout);
        headerLayout.setScrollView(scrollView);
        frameLayout.setInsideLayout(headerLayout);
        frameLayout.setSpace(space);
        scrollView.setOutsideLayout(frameLayout);
    }
}
