package com.example.nikhil.eatnjoy.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nikhil.eatnjoy.R;

/**
 * Created by HP 840 G1 ULTRABOOK on 7/8/2018.
 */


    public class Converter
    {

        public static Drawable convertLayoutImage(Context context, int count, int drawbleId)
        {
            LayoutInflater inflater=LayoutInflater.from(context);

            View view=inflater.inflate(R.layout.badge,null);
            ((ImageView)view.findViewById(R.id.icon)).setImageResource(drawbleId);

            if (count==0)
            {
                View v1= view.findViewById(R.id.counter);
                v1.setVisibility(View.GONE);
            }
            else
            {
                TextView tv=view.findViewById(R.id.count);
                tv.setText(""+count);
            }

            view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

            view.layout(0,0,view.getMeasuredWidth(),view.getMeasuredHeight());

            view.setDrawingCacheEnabled(true);
            view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            Bitmap bitmap=Bitmap.createBitmap(view.getDrawingCache());
            view.setDrawingCacheEnabled(false);
            return new BitmapDrawable(context.getResources(),bitmap);

        }

    }


