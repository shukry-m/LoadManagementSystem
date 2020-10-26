package lk.iot.lmsrealtime1.helper;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import lk.iot.lmsrealtime1.R;
import lk.iot.lmsrealtime1.view.MainActivity;

public  class CustomMessage {




    public static void displayStatusMessage(String s, int colorValue, final int id, final Context context) {

        AlertDialog.Builder builder = null;
        View view = null;
        TextView tvOk, tvMessage;
        ImageView imageView;
        int defaultColor = R.color.textGray;
        int successColor = R.color.successColor; // 1
        int errorColor = R.color.errorColor; // 2
        int warningColor = R.color.warningColor; // 3

        int success = R.drawable.ic_success;
        int error_image = R.drawable.ic_error;
        int warning_image = R.drawable.ic_warning;
        //1,2,3

        int color = defaultColor;
        int img = success;
        if (colorValue == 1) {
            color = successColor;
            img = success;

        } else if (colorValue == 2) {
            color = errorColor;
            img = error_image;

        } else if (colorValue == 3) {
            color = warningColor;
            img = warning_image;
        }

        builder = new AlertDialog.Builder(context);
        LayoutInflater mInflater = LayoutInflater.from(context);
        view =  mInflater.inflate(R.layout.layout_for_custom_message, null);

        tvOk = (TextView) view.findViewById(R.id.tvOk);
        tvMessage = (TextView) view.findViewById(R.id.tvMessage);
        imageView = (ImageView)view.findViewById(R.id.iv_status);

        tvMessage.setTextColor(context.getResources().getColor(color));
        tvMessage.setText(s);
        imageView.setImageResource(img);


        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        tvOk.setOnClickListener(    new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id==0){
                    Intent intent = new Intent( context, MainActivity.class );
                    intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                    context.startActivity( intent );
                    //finish();
                    alertDialog.dismiss();
                }else{
                    alertDialog.dismiss();
                }


            }
        });

    }
}
