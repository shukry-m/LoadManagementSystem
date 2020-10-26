package lk.iot.lmsrealtime1.helper;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

import lk.iot.lmsrealtime1.R;

public class TimePickerFragment extends DialogFragment {



    public TimePickerFragment(TimePickerDialog.OnTimeSetListener listener) {
        super();
        this.listener = listener;
    }

    private TimePickerDialog.OnTimeSetListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int cHour = c.get(Calendar.HOUR_OF_DAY);
        int cMinute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), listener, cHour, cMinute,
                android.text.format.DateFormat.is24HourFormat(getActivity()));
    }

    public void setListener(TimePickerDialog.OnTimeSetListener listener) {
        this.listener = listener;
    }
}