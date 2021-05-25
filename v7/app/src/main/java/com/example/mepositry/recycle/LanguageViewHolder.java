package com.example.mepositry.recycle;


        import android.support.annotation.NonNull;
        import android.support.v7.widget.RecyclerView;
        import android.view.View;
        import android.widget.Button;
        import android.widget.GridView;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.ListView;
        import android.widget.TextView;

        import com.example.mepositry.R;


public class LanguageViewHolder extends RecyclerView.ViewHolder {
    public TextView item_title;
    public int MAX = 4;
    public LinearLayout[] layout = new LinearLayout[MAX];
    public ImageView[] iv = new ImageView[MAX];
    public TextView[] tv = new TextView[MAX];


    public LanguageViewHolder(@NonNull View itemView) {
        super(itemView);
        item_title = itemView.findViewById(R.id.item_title);
        //layout
        layout[0] =  itemView.findViewById(R.id.ll_0);
        layout[1] = itemView.findViewById(R.id.ll_1);
        layout[2] = itemView.findViewById(R.id.ll_2);
        layout[3] = itemView.findViewById(R.id.ll_3);
        //image
        iv[0] =  itemView.findViewById(R.id.iv_0);
        iv[1] = itemView.findViewById(R.id.iv_1);
        iv[2] = itemView.findViewById(R.id.iv_2);
        iv[3] = itemView.findViewById(R.id.iv_3);
        //textview
        tv[0] =  itemView.findViewById(R.id.tv_0);
        tv[1] = itemView.findViewById(R.id.tv_1);
        tv[2] = itemView.findViewById(R.id.tv_2);
        tv[3] = itemView.findViewById(R.id.tv_3);
    }
}
