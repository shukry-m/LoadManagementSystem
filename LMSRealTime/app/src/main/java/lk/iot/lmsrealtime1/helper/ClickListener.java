package lk.iot.lmsrealtime1.helper;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;

public interface ClickListener extends CompoundButton.OnCheckedChangeListener {
    void onPositionClicked(int position, View view);
     void onCheckedChanged(CompoundButton cb, boolean on);
     void onCheckedChanged(int position,CompoundButton cb, boolean on);

}

