package com.example.demorecycleview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mvvm_architecture.R;

import java.util.ArrayList;

// 1- create the adapterClass "with out extending anything"
// 2- create the viewHolderClass "MyViewHolder" and extend RecyclerView.ViewHolder:
//     This class will hold the  viewItem.xml in memory for each individual entry
//     and declare all of the widgets in the viewItem.xml
// 3- extend the adapterClass with RecyclerView.Adapter<T> where T is the newly created viewHolderClass "MyViewHolder"
// 4- Add the req members and init them in the default constructor
// 5- implement onCreateViewHolder and create & return an instance of "MyViewHolder"
// 6- implement onBindViewHolder
// TLDR; bunch of viewholder will be inflated in "onCreateViewHolder"; and then the adapter will recycle and fill them with data in "onBindViewHolder"

// IMPORTANT take a look at https://guides.codepath.com/android/using-the-recyclerview#binding-the-adapter-to-the-recyclerview
//                          https://www.youtube.com/watch?v=Vyqz_-sJGFk


public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> {
    private static final String TAG = "RecycleViewAdapter";

    private ArrayList<String> imgsName;
    private ArrayList<String> imgsUri;
    private Context context;

    private int cntOnCreateViewHolder = 0;
    private int cntOnBindViewHolder = 0;

    public RecycleViewAdapter(Context context, ArrayList<String> imgsUri, ArrayList<String> imgsName) {
        this.imgsName = imgsName;
        this.imgsUri = imgsUri;
        this.context = context;
    }

    @NonNull
    @Override

    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // This is the place where the RecyclerView comes when it needs a new ViewHolder for a particular type of view.
        // Initialisation specific things like setting onClickListeners should be done here.
        // IMPORTANT TO NOTICE THAT THE VIEW "HOLDER" WILL BE INFLATED BUT WITHOUT ANY DATA HERE
        Log.d(TAG, "onCreateViewHolder: ##########################" + ++cntOnCreateViewHolder);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        // NOW HERE WILL THE ADAPTER FILL THE VIEW "HOLDER" WITH GIVEN DATA
        // this method will be called every time an item is add to the list
        // Data is attached to the View in this function. Keep in mind that ViewHolders are recycled so the same ViewHolder will be used with some other Data as well,
        // so better update data each time this function is called.
        Log.d(TAG, "onBindViewHolder: **************************" + ++cntOnBindViewHolder);



        Glide.with(context).asBitmap().load(imgsUri.get(position))    .apply(new RequestOptions().override(64, 64)).into(holder.itemImageView);
        holder.itemTextView.setText(imgsName.get(position));



        // if we want some action to happen when the user clicks an an item
        holder.itemLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on " + imgsName.get(position));
                Toast.makeText(context, imgsUri.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onViewAttachedToWindow(@NonNull MyViewHolder holder) {
        //Whenever a ViewHolder occurs on the Screen, this callback is fired, User oriented events like Playing Videos or Audios when Views come onto screen should be done inside this.
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewRecycled(@NonNull MyViewHolder holder) {
        //Once a ViewHolder is successfully recycled, onViewRecycled gets called. This is when we should release resources held by the ViewHolder
        super.onViewRecycled(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull MyViewHolder holder) {
        //When the ViewHolder goes off the screen, this gets called. Perfect place to pause videos and audios, or other Memory intensive events.
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    // return the size of the data
    public int getItemCount() {

        // we could also use imgsUri.size()
        return imgsName.size();
    }


    // This class will hold the widget/viewItem.xml in memory for each individual entry/data
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "MyViewHolder";
        ImageView itemImageView;
        TextView itemTextView;
        LinearLayout itemLinearLayout;


        public MyViewHolder(@NonNull View v) {
            super(v);
            itemImageView = v.findViewById(R.id.itemImageView);
            itemTextView = v.findViewById(R.id.itemTextView);
            itemLinearLayout = v.findViewById(R.id.itemLinearLayout);

        }
    }
}
