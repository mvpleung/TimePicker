package com.lzc.timepick;

import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.lzc.timepick.widget.TimePicker;
import com.lzc.timepick.widget.TimePicker.OnTimeChangedListener;
import com.lzc.timepick.widget.TimePickerDialog;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Calendar now = Calendar.getInstance();
		TimePickerDialog mTimePicker = new TimePickerDialog(this, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), now.get(Calendar.SECOND), true);
		mTimePicker.setOnTimeChangedListener(new OnTimeChangedListener() {

			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute, int seconds) {
				// TODO Auto-generated method stub
				showToast(hourOfDay + ":" + minute + ":" + seconds);
			}
		});
		mTimePicker.show();
	}

	private static Toast mToast;

	/**
	 * 
	 * @param ctx
	 * @param msg
	 * @param duration
	 */
	public void showToast(CharSequence msg) {
		if (mToast == null)
			mToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
		else {
			mToast.setText(msg);
			mToast.setDuration(Toast.LENGTH_SHORT);
		}
		mToast.show();
	}
}
