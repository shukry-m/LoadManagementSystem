package lk.iot.lmsrealtime1.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import lk.iot.lmsrealtime1.R;
import lk.iot.lmsrealtime1.model.MyMenu;
import lk.iot.lmsrealtime1.view.CustomerInfo;
import lk.iot.lmsrealtime1.view.CostAndPower;
import lk.iot.lmsrealtime1.view.HomeApplianceActivity;
import lk.iot.lmsrealtime1.view.ManualControlActivity;
import lk.iot.lmsrealtime1.view.ScheduleActivity;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuAdapterViewHolder> {

    Context context;
    ArrayList<MyMenu> arrayList;

    public MenuAdapter(Context context, ArrayList<MyMenu> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public MenuAdapter.MenuAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.layout_for_menu, parent, false );
        return new MenuAdapterViewHolder( view );
    }

    @Override
    public void onBindViewHolder(MenuAdapter.MenuAdapterViewHolder holder, final int position) {
        {

            final String menuLabel = arrayList.get( position ).getMenuName();
            int img = arrayList.get( position ).getMenuImage();

            holder.tvMenuLabel.setText( menuLabel );

            //holder.tvMenuLabel.animate().setDuration(700).translationX(20);
           // Picasso.get().load(img).into(profileImage);
            Picasso.get()
                    .load( img )
                    .error( android.R.drawable.stat_notify_error )
                    .into( holder.ivMenuImage );

            holder.ivMenuImage.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    switch (menuLabel) {

                        case "Appliance":
                            Intent oIntent = new Intent( context, HomeApplianceActivity.class );
                            oIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
                            context.startActivity( oIntent );
                            break;
                        case "Manual Control":
                            Intent aIntent = new Intent( context, ManualControlActivity.class );
                            aIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
                            context.startActivity( aIntent );
                            break;
                        case "ScheduleActivity":
                            Intent hIntent = new Intent( context, ScheduleActivity.class );
                            hIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
                            context.startActivity( hIntent );
                            break;
                        case "Cost & Power":
                            Intent cIntent = new Intent( context, CostAndPower.class );
                            cIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
                            context.startActivity( cIntent );
                            break;

                            case "Customer Info":
                            Intent adIntent = new Intent( context, CustomerInfo.class );
                            adIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
                            context.startActivity( adIntent );
                            break;
                        default:
                            if (!checkNetworkConnection()) {
                                Toast.makeText( context, "Please Check your Network Connection...", Toast.LENGTH_LONG ).show();
                                return;
                            }

                    }
                }
            } );
        }
    }

    @Override
    public int getItemCount() {
        if (arrayList != null) {
            return arrayList.size();
        }
        return 0;
    }

    public static class MenuAdapterViewHolder extends RecyclerView.ViewHolder {

        ImageView ivMenuImage;
        TextView tvMenuLabel;

        public MenuAdapterViewHolder(View itemView) {
            super( itemView );

            ivMenuImage = (ImageView) itemView.findViewById( R.id.ivMenuImage );
            tvMenuLabel = (TextView) itemView.findViewById( R.id.tvMenulabel );


        }
    }

    public boolean checkNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}

