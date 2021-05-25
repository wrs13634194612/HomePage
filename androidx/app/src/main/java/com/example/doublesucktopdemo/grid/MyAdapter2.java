package com.example.doublesucktopdemo.grid;



        import android.annotation.SuppressLint;
        import android.content.Context;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;

        import com.example.doublesucktopdemo.R;

        import java.util.ArrayList;
        import java.util.List;

        import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter2 extends RecyclerView.Adapter<BaseRecyclerViewHolder> {
    private List<String> mDataList = new ArrayList<>();
    private Context context;

    public MyAdapter2(Context mContext, List<String> list) {
        this.context=mContext;
        this.mDataList = list;
    }


    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item2, parent, false);
        return new BaseRecyclerViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, final int position) {
        TextView name = holder.findBindItemView(R.id.tv_title);
        name.setText("id:"+position+mDataList.get(position));
        //抽象方法
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG", "holder.itemView:" + position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

/*    @Override
    protected int setupItemLayoutId() {
        return ;
    }

    @Override
    protected void findBindView(int position, BaseRecyclerViewHolder holder) {
        String data = mDataList.get(position);
        TextView name = holder.findBindItemView(R.id.tv_title);
        name.setText(data);

    }*/
}
