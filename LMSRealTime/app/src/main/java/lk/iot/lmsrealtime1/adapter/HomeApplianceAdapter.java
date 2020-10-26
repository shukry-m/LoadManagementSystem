package lk.iot.lmsrealtime1.adapter;
//lk.iot.lmsrealtime
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import lk.iot.lmsrealtime1.R;
import lk.iot.lmsrealtime1.helper.ClickListener;
import lk.iot.lmsrealtime1.model.ManualControl;

public class HomeApplianceAdapter  extends RecyclerView.Adapter<HomeApplianceAdapter.HomeApplianceViewHolder> {

    Context context;
    ArrayList<ManualControl> list;
    ClickListener listener;

    public HomeApplianceAdapter(Context context, ArrayList<ManualControl>list, ClickListener listener){
        this.context = context;
        this.list = list;
        this.listener=listener;
    }



    @NonNull
    @Override
    public HomeApplianceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.layout_for_home_appl, parent, false );
        return new HomeApplianceViewHolder( view,listener );
    }

    @Override
    public void onBindViewHolder(@NonNull HomeApplianceViewHolder holder, int position) {
       ManualControl manualControl = list.get(position);
        holder.txtItem.setText(manualControl.getM_LABEL());

    }

    @Override
    public int getItemCount() {
        if(list != null){
            return list.size();
        }
        return 0;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class HomeApplianceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView txtItem;
        ImageView ic_cross;
        private WeakReference<ClickListener> listenerRef;
        public HomeApplianceViewHolder(@NonNull View itemView,ClickListener listener) {
            super(itemView);

            txtItem = itemView.findViewById(R.id.txtItem);
            ic_cross = itemView.findViewById(R.id.ic_cross);

            ic_cross.setOnClickListener(this);
            listenerRef = new WeakReference<>(listener);
        }

        @Override
        public void onClick(View v) {
            listenerRef.get()
                    .onPositionClicked(getAdapterPosition(),v);
        }
    }
}
