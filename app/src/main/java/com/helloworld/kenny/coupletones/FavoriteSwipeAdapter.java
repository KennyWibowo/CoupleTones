package com.helloworld.kenny.coupletones;

import android.content.Context;
import android.view.View;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.ArraySwipeAdapter;
import com.daimajia.swipe.implments.SwipeItemMangerImpl;

import java.util.List;

/**
 * Created by Kenny on 5/5/2016.
 */
public class FavoriteSwipeAdapter<T> extends ArraySwipeAdapter<T> {


    public FavoriteSwipeAdapter(Context context, int resource, int textViewResourceId, List<T> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.right_swipe_layout;
    }

}
