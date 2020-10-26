package lk.iot.lmsrealtime1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import lk.iot.lmsrealtime1.R;
import lk.iot.lmsrealtime1.helper.ClickListener;
import lk.iot.lmsrealtime1.model.AutomaticSchedule;

public class ScheduleAutomaticAdapter extends RecyclerView.Adapter<ScheduleAutomaticAdapter.ScheduleAutomaticAdapterViewHolder> {

        Context context;
        ArrayList<AutomaticSchedule> list;
        ClickListener listener;
        String userId;

        public ScheduleAutomaticAdapter(Context context, String userId, ArrayList<AutomaticSchedule>list, ClickListener listener){
            this.context = context;
            this.list = list;
            this.listener=listener;
            this.userId = userId;
        }


    @NonNull
        @Override
        public ScheduleAutomaticAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.layout_for_schedule_automatic_item, parent, false );
            return new ScheduleAutomaticAdapterViewHolder( view,listener );
        }

        @Override
        public void onBindViewHolder(ScheduleAutomaticAdapterViewHolder holder, int position) {
            AutomaticSchedule as = list.get(position);
            holder.item.setText(as.getA_LABEL());
            boolean res =  as.getA_STATUS()!= null && as.getA_STATUS().equals("1");
            holder.item.setChecked(res);

//            if(automaticStatus.equals("1")){
//                holder.item.setClickable(true);
//            }else{
//                holder.item.setClickable(false);
//               // holder.item.setChecked(false);
//            }
            //holder.item.setClickable(false);
        }

        @Override
        public int getItemCount() {
            if(list != null){
                return list.size();
            }
            return 0;
        }

        public static class ScheduleAutomaticAdapterViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {

            CheckBox item;

            private WeakReference<ClickListener> listenerRef;
            public ScheduleAutomaticAdapterViewHolder(@NonNull View itemView, ClickListener listener) {
                super(itemView);

                item = itemView.findViewById(R.id.item);

                item.setOnCheckedChangeListener(this);
                listenerRef = new WeakReference<>(listener);
            }

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                listenerRef.get().onCheckedChanged(getAdapterPosition(),buttonView,isChecked);
            }
        }
    }
