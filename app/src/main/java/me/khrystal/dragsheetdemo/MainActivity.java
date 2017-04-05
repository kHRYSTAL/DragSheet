package me.khrystal.dragsheetdemo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.Toast;

import me.khrystal.widget.dragsheet.InsideHeaderLayout;
import me.khrystal.widget.dragsheet.OutSideScrollView;
import me.khrystal.widget.dragsheet.OutsideDownFrameLayout;
import me.khrystal.widget.dragsheet.Util;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    OutsideDownFrameLayout frameLayout;
    OutSideScrollView scrollView;
    InsideHeaderLayout headerLayout;
    Space space;
    ImageView imageView;
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
        space.setOnClickListener(this);

        configImageView();
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
