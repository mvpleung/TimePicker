package com.lzc.timepick.widget;

import java.util.Calendar;

import net.simonvt.numberpicker.NumberPicker;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.lzc.mytimepick.R;

public class TimePicker extends FrameLayout {

	private static final OnTimeChangedListener NO_OP_CHANGE_LISTENER = new OnTimeChangedListener() {
		public void onTimeChanged(TimePicker view, int hourOfDay, int minute, int seconds) {
		}
	};

	private int mCurrentHour = 0; // 0-23
	private int mCurrentMinute = 0; // 0-59
	private int mCurrentSeconds = 0; // 0-59
	private Boolean mIs24HourView = false;
	private boolean mIsAm;

	private final NumberPicker mHourPicker;
	private final NumberPicker mMinutePicker;
	private final NumberPicker mSecondPicker;

	private OnTimeChangedListener mOnTimeChangedListener;

	/**
	 * The callback interface used to indicate the time has been adjusted.
	 */
	public interface OnTimeChangedListener {

		/**
		 * @param view
		 *            The view associated with this listener.
		 * @param hourOfDay
		 *            The current hour.
		 * @param minute
		 *            The current minute.
		 * @param seconds
		 *            The current second.
		 */
		void onTimeChanged(TimePicker view, int hourOfDay, int minute, int seconds);
	}

	public TimePicker(Context context) {
		this(context, null);
	}

	public TimePicker(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TimePicker(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.time_picker, this, // we are the parent
				true);

		// hour
		mHourPicker = (NumberPicker) findViewById(R.id.hour);
		mHourPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				// TODO Auto-generated method stub
				mCurrentHour = newVal;
				if (!mIs24HourView) {
					if (mCurrentHour == 12) {
						mCurrentHour = 0;
					}
					if (!mIsAm) {
						mCurrentHour += 12;
					}
				}
				onTimeChanged();
			}
		});

		mMinutePicker = (NumberPicker) findViewById(R.id.minute);
		mMinutePicker.setMinValue(0);
		mMinutePicker.setMaxValue(59);
		mMinutePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
			@Override
			public void onValueChange(NumberPicker spinner, int oldVal, int newVal) {
				mCurrentMinute = newVal;
				onTimeChanged();
			}
		});

		mSecondPicker = (NumberPicker) findViewById(R.id.seconds);
		mSecondPicker.setMinValue(0);
		mSecondPicker.setMaxValue(59);
		mSecondPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				mCurrentSeconds = newVal;
				onTimeChanged();

			}
		});

		configurePickerRanges();
		Calendar cal = Calendar.getInstance();
		setOnTimeChangedListener(NO_OP_CHANGE_LISTENER);
		setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
		setCurrentMinute(cal.get(Calendar.MINUTE));
		setCurrentSecond(cal.get(Calendar.SECOND));

		mIsAm = (mCurrentHour < 12);

		if (!isEnabled()) {
			setEnabled(false);
		}
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		mMinutePicker.setEnabled(enabled);
		mHourPicker.setEnabled(enabled);
	}

	private static class SavedState extends BaseSavedState {

		private final int mHour;
		private final int mMinute;

		private SavedState(Parcelable superState, int hour, int minute) {
			super(superState);
			mHour = hour;
			mMinute = minute;
		}

		private SavedState(Parcel in) {
			super(in);
			mHour = in.readInt();
			mMinute = in.readInt();
		}

		public int getHour() {
			return mHour;
		}

		public int getMinute() {
			return mMinute;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			super.writeToParcel(dest, flags);
			dest.writeInt(mHour);
			dest.writeInt(mMinute);
		}
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		return new SavedState(superState, mCurrentHour, mCurrentMinute);
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		SavedState ss = (SavedState) state;
		super.onRestoreInstanceState(ss.getSuperState());
		setCurrentHour(ss.getHour());
		setCurrentMinute(ss.getMinute());
	}

	public void setOnTimeChangedListener(OnTimeChangedListener onTimeChangedListener) {
		mOnTimeChangedListener = onTimeChangedListener;
	}

	public Integer getCurrentHour() {
		return mCurrentHour;
	}

	public void setCurrentHour(Integer currentHour) {
		this.mCurrentHour = currentHour;
		updateHourDisplay();
	}

	public void setIs24HourView(Boolean is24HourView) {
		if (mIs24HourView != is24HourView) {
			mIs24HourView = is24HourView;
			configurePickerRanges();
			updateHourDisplay();
		}
	}

	public boolean is24HourView() {
		return mIs24HourView;
	}

	public Integer getCurrentMinute() {
		return mCurrentMinute;
	}

	public void setCurrentMinute(Integer currentMinute) {
		this.mCurrentMinute = currentMinute;
		updateMinuteDisplay();
	}

	public Integer getCurrentSeconds() {
		return mCurrentSeconds;
	}

	public void setCurrentSecond(Integer currentSecond) {
		this.mCurrentSeconds = currentSecond;
		updateSecondsDisplay();
	}

	@Override
	public int getBaseline() {
		return mHourPicker.getBaseline();
	}

	private void updateHourDisplay() {
		int currentHour = mCurrentHour;
		if (!mIs24HourView) {
			if (currentHour > 12)
				currentHour -= 12;
			else if (currentHour == 0)
				currentHour = 12;
		}
		mHourPicker.setValue(currentHour);
		mIsAm = mCurrentHour < 12;
		onTimeChanged();
	}

	private void configurePickerRanges() {
		if (mIs24HourView) {
			mHourPicker.setMinValue(0);
			mHourPicker.setMaxValue(23);
		} else {
			mHourPicker.setMinValue(1);
			mHourPicker.setMaxValue(12);
			mHourPicker.setFormatter(null);
		}
	}

	private void onTimeChanged() {
		mOnTimeChangedListener.onTimeChanged(this, getCurrentHour(), getCurrentMinute(), getCurrentSeconds());
	}

	private void updateMinuteDisplay() {
		mMinutePicker.setValue(mCurrentMinute);
		mOnTimeChangedListener.onTimeChanged(this, getCurrentHour(), getCurrentMinute(), getCurrentSeconds());
	}

	private void updateSecondsDisplay() {
		mSecondPicker.setValue(mCurrentSeconds);
		mOnTimeChangedListener.onTimeChanged(this, getCurrentHour(), getCurrentMinute(), getCurrentSeconds());
	}
}
