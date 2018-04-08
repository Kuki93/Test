package com.test.helpme.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemDragListener {

    private ItemTouchHelper itemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final PhotoGridLayoutManger layoutManger = new PhotoGridLayoutManger();
        recyclerView.setLayoutManager(layoutManger);
        final List<Integer> datas = new ArrayList<>();
        datas.add(R.drawable.da_bao_yu);
        datas.add(R.drawable.da_feng);
        datas.add(R.drawable.da_xue);
        datas.add(R.drawable.duo_yun);
        datas.add(R.drawable.fu_chen);
        datas.add(R.drawable.wu_mai);
        for (int i = 0; i < 3; i++) {
            datas.add(0);
        }
        final MyAdapter mAdapter = new MyAdapter(datas, this);
        recyclerView.setAdapter(mAdapter);

        final DragCallBack mCallback = new DragCallBack(mAdapter);
        itemTouchHelper = new ItemTouchHelper(mCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                mCallback.setScale(layoutManger.getScale());
            }
        });
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                    mCallback.setEvent(event);
                return false;
            }

        });
    }


    @Override
    public void onStartDrags(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }

    public static class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> implements ItemMoveListener {

        private List<Integer> mData;
        private ItemDragListener mItemDragListener;

        public MyAdapter(List<Integer> mData, ItemDragListener mItemDragListener) {
            this.mData = mData;
            this.mItemDragListener = mItemDragListener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // 实例化展示的view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo_view, parent, false);
            // 实例化viewholder
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            // 绑定数据
            if (mData.get(position) != 0) {
                holder.photo.setImageResource(mData.get(position));
                holder.photo.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        mItemDragListener.onStartDrags(holder);
                        return false;
                    }
                });
            } else {
                holder.photo.setImageResource(R.drawable.add_figure);
                holder.photo.setOnTouchListener(null);
            }
        }

        @Override
        public int getItemCount() {
            return mData == null ? 0 : mData.size();
        }

        @Override
        public boolean onItemMove(int fromPosition, int toPosition) {
            if (mData.get(toPosition) == 0)
                return false;
            Collections.swap(mData, fromPosition, toPosition);
            notifyItemMoved(fromPosition, toPosition);
            return true;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            ImageView photo;

            public ViewHolder(View itemView) {
                super(itemView);
                photo = itemView.findViewById(R.id.photo);
            }
        }
    }


}
