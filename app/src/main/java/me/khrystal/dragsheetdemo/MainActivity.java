package me.khrystal.dragsheetdemo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.Toast;

import me.khrystal.widget.dragsheet.DisplayUtil;
import me.khrystal.widget.dragsheet.InsideHeaderLayout;
import me.khrystal.widget.dragsheet.OutSideScrollView;
import me.khrystal.widget.dragsheet.OutsideDownFrameLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    OutsideDownFrameLayout downFrameLayout;
    OutSideScrollView scrollView;
    InsideHeaderLayout insideLayout;
    Toolbar toolbar;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        downFrameLayout = (OutsideDownFrameLayout) findViewById(R.id.downFrameLayout);
        scrollView = (OutSideScrollView) findViewById(R.id.scrollView);
        insideLayout = (InsideHeaderLayout) findViewById(R.id.insideLayout);
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        configToolbar();
        configCouplingEffects();
        configImageView();
    }

    private void configToolbar() {
        toolbar.setTitle("Title");
    }

    //设置内外布局联动效果
    private void configCouplingEffects() {
        insideLayout.setOutsideLayout(downFrameLayout);
        insideLayout.setScrollView(scrollView);
        downFrameLayout.setInsideLayout(insideLayout);
        downFrameLayout.setAnimViews(toolbar);
        scrollView.setOutsideLayout(downFrameLayout);

    }

    private void configImageView() {
        imageView = (ImageView) findViewById(R.id.ivTriangel);
        imageView.setImageBitmap(createTriangel((int) (DisplayUtil.getScreenSize(this).x), DisplayUtil.dp2px(this, 50)));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.space:
                Toast.makeText(MainActivity.this, "Space is click", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private Bitmap createTriangel(int width, int height) {
        if (width <= 0 || height <= 0) {
            return null;
        }
        return getBitmap(width, height, getResources().getColor(R.color.colorPrimary));
    }

    private Bitmap getBitmap(int width, int height, int backgroundColor) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(backgroundColor);
        Path path = new Path();
        path.moveTo(width / 2, 0);
        path.lineTo(width, height);
        path.lineTo(0, height);
        path.close();

        canvas.drawPath(path, paint);
        return bitmap;

    }
}
