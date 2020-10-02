package com.example.acupuncture;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class Func {

    public static void set_user_image_no_cache(Context cxt , String url , ImageView img) {
        Picasso.with(cxt).invalidate("");
        Picasso.with(cxt).load(url)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(img);
    }

    public static void set_user_image(Context cxt , String url , ImageView img) {
        Picasso.with(cxt).load(url).into(img);
    }
}
