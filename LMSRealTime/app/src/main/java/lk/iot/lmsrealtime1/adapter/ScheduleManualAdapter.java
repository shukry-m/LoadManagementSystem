package lk.iot.lmsrealtime1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import lk.iot.lmsrealtime1.R;
import lk.iot.lmsrealtime1.helper.ClickListener;
import lk.iot.lmsrealtime1.model.ScheduleManual;

public class ScheduleManualAdapter extends RecyclerView.Adapter<ScheduleManualAdapter.MyViewHolder> {

    static Context context;
    ArrayList<ScheduleManual> list;
    ClickListener listener;


    public ScheduleManualAdapter(Context context, ArrayList<ScheduleManual>list,ClickListener listener){
        this.context = context;
        this.list = list;
        this.listener=listener;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.layout_for_schedule_manual, parent, false );
        return new MyViewHolder( view,listener );
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
       ScheduleManual scheduleCooking = list.get(position);
        holder.text.setText(scheduleCooking.getS_LABEL());
        holder.starttime.setText(scheduleCooking.getS_Start_Time());
        holder.endTime.setText(scheduleCooking.getS_End_Time());
    }

    @Override
    public int getItemCount() {
        if(list != null){
            return list.size();
        }
        return 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        TextView starttime,endTime,text;
        private WeakReference<ClickListener> listenerRef;
        public MyViewHolder(@NonNull View itemView, ClickListener listener) {
            super(itemView);

            text = itemView.findViewById(R.id.text);
            starttime = itemView.findViewById(R.id.starttime);
            endTime = itemView.findViewById(R.id.endTime);
            starttime.setOnClickListener(this);
            endTime.setOnClickListener(this);
            listenerRef = new WeakReference<>(listener);
        }


        @Override
        public void onClick(View v) {
            listenerRef.get()
                    .onPositionClicked(getAdapterPosition(),v);
        }
    }
}
