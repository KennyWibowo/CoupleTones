package com.helloworld.kenny.coupletones.SwipeAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.ArraySwipeAdapter;
import com.daimajia.swipe.implments.SwipeItemAdapterMangerImpl;
import com.daimajia.swipe.util.Attributes;
import com.helloworld.kenny.coupletones.Favorites.FavoriteEntry;
import com.helloworld.kenny.coupletones.Favorites.Favorites;
import com.helloworld.kenny.coupletones.R;

import java.util.List;

/**
 * Created by Kenny on 5/5/2016.
 * Citations:
 *      https://github.com/paraches/ListViewCellDeleteAnimation
 *      https://github.com/daimajia/AndroidSwipeLayout
 */
public class FavoriteSwipeAdapter<T> extends ArraySwipeAdapter<T> {


    final int ANIMATION_DURATION = 200;
    private Favorites favorites;
    private FavoriteSwipeAdapter<T> me = this;
    private LayoutInflater inflater;
    private FavoriteSwipeItemManager itemManager = new FavoriteSwipeItemManager(this);
    {}

    public FavoriteSwipeAdapter(Context context, int resource, int textViewResourceId, Favorites favorites) {
        super(context, resource, textViewResourceId, (List<T>) favorites.getAllEntries());
        this.favorites = favorites;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        boolean convertViewIsNull = convertView == null;
        System.out.println("Position: " + position);
        View v = super.getView(position, convertView, parent);

        v.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Deleting at " + position);
                deleteCell(v, position);
            }
        });


        return v;
    }

    private void deleteCell(final View v, final int index) {
        Animation.AnimationListener al = new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                FavoriteEntry f = favorites.getEntry(index);

                FavoriteMemory m = (FavoriteMemory) v.getTag(FavoriteSwipeItemManager.MEM_KEY);
                if(m != null)
                    m.needInflate = true;

                f.getMarker().remove();

                me.notifyDataSetChanged();

                Toast.makeText(v.getContext(), "Favorite deleted successfully", Toast.LENGTH_SHORT).show();
            }
            @Override public void onAnimationRepeat(Animation animation) {}
            @Override public void onAnimationStart(Animation animation) {}
        };

        collapse(v, al);
    }

    private void collapse(final View v, Animation.AnimationListener al) {
        final SwipeLayout curr = (SwipeLayout) v.getParent().getParent();
        final int initialHeight = curr.getMeasuredHeight();

        Animation anim = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    curr.setVisibility(View.GONE);
                }
                else {
                    curr.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    curr.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        if (al!=null) {
            anim.setAnimationListener(al);
        }
        anim.setDuration(ANIMATION_DURATION);
        v.startAnimation(anim);
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.right_swipe_layout;
    }


    class FavoriteSwipeItemManager extends SwipeItemAdapterMangerImpl {

        final static int MEM_KEY = 9001;

        public FavoriteSwipeItemManager(BaseAdapter context) {
            super(context);
        }

        @Override
        public void initialize(View v, int position) {
            super.initialize(v, position);
            v.setTag(MEM_KEY, new FavoriteMemory(false));
        }

        @Override
        public void updateConvertView(View v, int position) {
            FavoriteMemory memory = (FavoriteMemory) v.getTag(MEM_KEY);
            if(memory.needInflate) {
                initialize(v, position);
            } else {
                super.updateConvertView(v, position);
            }

        }

    }
    class FavoriteMemory {
        boolean needInflate;

        FavoriteMemory(boolean needInflate){
            this.needInflate = needInflate;
        }
    }

}
