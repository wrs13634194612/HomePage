package com.example.mepositry.recycle;


        import android.content.Context;
        import android.support.annotation.NonNull;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;

        import com.bumptech.glide.Glide;
        import com.example.mepositry.ProductBean;
        import com.example.mepositry.R;


        import java.util.ArrayList;
        import java.util.List;

public class LanguageRecycleAdapter extends RecyclerView.Adapter<LanguageViewHolder> {

    private static ArrayList<ProductBean> languageList;
    private Context context;

    private ILanguageRecycleListener listener;

    public LanguageRecycleAdapter(Context mContext,ArrayList<ProductBean>  mList, ILanguageRecycleListener listener) {
        this.languageList = mList;
        this.listener = listener;
        context = mContext;
    }


    @NonNull
    @Override
    public LanguageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LanguageViewHolder holder;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_study, parent, false);
        holder = new LanguageViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull LanguageViewHolder holder, final int position) {
        holder.item_title.setText(languageList.get(position).getName());

        /**
         *    holder.layout[i].setOnClickListener(new View.OnClickListener() {
         *                 @Override
         *                 public void onClick(View view) {
         *                     // Log.e("TAG", "onBindViewHolder:" + languageList.get(position).getCitylist().get(finalI).getArea_name());
         *                     listener.itemOnClick(languageList.get(position).getCitylist().get(finalI).getArea_name());
         *                 }
         *             });
         */
    }

    @Override
    public int getItemCount() {
        return languageList.size();
    }


}
