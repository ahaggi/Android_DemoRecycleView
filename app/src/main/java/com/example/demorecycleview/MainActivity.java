package com.example.demorecycleview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.example.mvvm_architecture.R;
import com.google.android.material.snackbar.Snackbar;

import android.content.Context;
import android.widget.LinearLayout;

import java.util.ArrayList;


/*
-----------------
| RecyclerView  |
| LayoutManager |  ->  Adapter  ->  Dataset
-----------------

            https://guides.codepath.com/android/using-the-recyclerview#binding-the-adapter-to-the-recyclerview
*/

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private ArrayList<String> imgsName = new ArrayList<>();
    private ArrayList<String> imgsUri = new ArrayList<>();

    private RecycleViewAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initImageBitMap();
        setupFab();


    }


    private void initImageBitMap() {
        Log.d(TAG, "initImageBitMap: preparing bitmap");

        for (int i = 0; i < 20; i++) {
            imgsName.add("cat nr:" + i);
            imgsUri.add("https://cdn2.thecatapi.com/images/163.jpg");
        }
        initRecycleView();
    }


    private void initRecycleView() {
        Log.d(TAG, "initRecycleView: ");
         recyclerView = findViewById(R.id.recycleView);
         adapter = new RecycleViewAdapter(this, imgsUri, imgsName);

        recyclerView.setAdapter(adapter);


//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//          Alt: iff we want to use "alternative 3 in the method scrollToPosition" we could use our implementation of the CostumeLinearLayoutManager
        CostumeLinearLayoutManager linearLayoutManager = new CostumeLinearLayoutManager(this);

//      setLayoutManager is to set the layout of the contents, i.e. list of repeating views in the recycler view
        recyclerView.setLayoutManager(linearLayoutManager);
    }


    private void setupFab() {
        FloatingActionButton fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newItemPos = imgsName.size();
                imgsName.add("cat nr:" + newItemPos);
                imgsUri.add("https://cdn2.thecatapi.com/images/163.jpg");

                adapter.notifyItemInserted(newItemPos);
                scrollToPosition(newItemPos);

                Snackbar.make(view, "bla bla bla!", Snackbar.LENGTH_SHORT)
                        .show();
            }
        });
    }


    private void scrollToPosition(int position){
//       1- abrupt scrolling
//        ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(position, 0);


//        2- smooth scrolling
//        RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(this) {
//            @Override protected int getVerticalSnapPreference() {
//                return LinearSmoothScroller.SNAP_TO_START;
//            }
//        };
//        smoothScroller.setTargetPosition(position);
//        ((LinearLayoutManager) recyclerView.getLayoutManager()).startSmoothScroll(smoothScroller);



//        3- using our implementation of the CostumeLinearLayoutManager
//           Remember to use
//           CostumeLinearLayoutManager linearLayoutManager = new CostumeLinearLayoutManager(this);
//           in the above method initRecycleView()
        recyclerView.smoothScrollToPosition(position);



    }




}



// The flwg implementation will be used ONLY in  "alternative 3 to scroll in the above methode scrollToPosition"
class CostumeLinearLayoutManager extends LinearLayoutManager {

    public CostumeLinearLayoutManager(Context context) {
        this(context, LinearLayout.VERTICAL, false);
    }
    public CostumeLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state,
                                       int position) {
        RecyclerView.SmoothScroller smoothScroller = new TopSnappedSmoothScroller(recyclerView.getContext());
        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);
    }

    private class TopSnappedSmoothScroller extends LinearSmoothScroller {
        public TopSnappedSmoothScroller(Context context) {
            super(context);

        }

        @Override
        public PointF computeScrollVectorForPosition(int targetPosition) {
            return CostumeLinearLayoutManager.this
                    .computeScrollVectorForPosition(targetPosition);
        }

        @Override
        protected int getVerticalSnapPreference() {
            return SNAP_TO_START;
        }
    }
}