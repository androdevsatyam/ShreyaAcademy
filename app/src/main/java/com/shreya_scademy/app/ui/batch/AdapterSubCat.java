package com.shreya_scademy.app.ui.batch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shreya_scademy.app.R;
import com.shreya_scademy.app.utils.widgets.CustomSmallText;

import java.util.ArrayList;

public class AdapterSubCat extends RecyclerView.Adapter<AdapterSubCat.MyViewHolder> {
    View view;
    ArrayList<ModelCatSubCat.batchData.SubCategory> list;
    Context context;
    String stuId;


    public AdapterSubCat(ArrayList<ModelCatSubCat.batchData.SubCategory> list, Context context,String stuId) {
        this.list = list;
        this.context=context;
        this.stuId=stuId;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.adapter_subcat, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position1) {
        try {

            ArrayList<ModelCatSubCat.batchData.SubCategory.BatchData> listBatch = list.get(holder.getAdapterPosition()).BatchData;

            if(listBatch != null && !listBatch.isEmpty()){
                holder.itemView.setVisibility(View.VISIBLE);
                holder.subCatList.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));

                holder.catHead.setText(list.get(holder.getAdapterPosition()).SubcategoryName);
                holder.catHead.setTextSize(12f);
                AdapterList adapterCatSubCat = new AdapterList(listBatch, context, stuId);
                holder.subCatList.setAdapter(adapterCatSubCat);

                if (listBatch.size() < 2) {
                    holder.catSeeAll.setVisibility(View.GONE);
                }
                holder.catSeeAll.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {

                                                            context.startActivity(new Intent(context.getApplicationContext(), ActivityAllBatch.class)
                                                                    .putExtra("subcatname", "" + list.get(holder.getAdapterPosition()).SubcategoryName)
                                                                    .putExtra("subcatId", "" + list.get(holder.getAdapterPosition()).SubcategoryId)
                                                                    .putExtra("stuId", stuId).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                                        }

                                                    }
                );
            }else{
                holder.itemView.setVisibility(View.GONE);
            }

        }catch (Exception e){

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        CustomSmallText catHead,catSeeAll;
        RecyclerView subCatList;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            catHead=itemView.findViewById(R.id.catHead);
            catSeeAll=itemView.findViewById(R.id.catSeeAll);
            subCatList=itemView.findViewById(R.id.subCatList);
        }
    }
}