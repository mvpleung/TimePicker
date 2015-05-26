/*
 * Copyright (C) 2007 The Android Open Source Project
 * Copyright (C) 2013 Ivan Kovac  navratnanos@gmail.com
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lzc.timepick.widget;

import java.util.Calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.ViewGroup;
import android.view.Window;

import com.lzc.timepick.widget.TimePicker.OnTimeChangedListener;

public class TimePickerDialog extends AlertDialog implements OnTimeChangedListener {

	private static final String HOUR = "hour";
	private static final String MINUTE = "minute";
	private static final String SECONDS = "seconds";
	private static final String IS_24_HOUR = "is24hour";

	private final TimePicker mTimePicker;
	private final Calendar mCalendar;
	private final java.text.DateFormat mDateFormat;
	private OnTimeChangedListener onTimeChangedListener;

	int mInitialHourOfDay;
	int mInitialMinute;
	int mInitialSeconds;
	boolean mIs24HourView;

	/**
	 * @param context
	 *            Parent.
	 * @param hourOfDay
	 *            The initial hour.
	 * @param minute
	 *            The initial minute.
	 * @param is24HourView
	 *            Whether this is a 24 hour view, or AM/PM.
	 */
	public TimePickerDialog(Context context, int hourOfDay, int minute, int seconds, boolean is24HourView) {

		this(context, 0, hourOfDay, minute, seconds, is24HourView);
	}

	/**
	 * @param context
	 *            Parent.
	 * @param theme
	 *            the theme to apply to this dialog
	 * @param hourOfDay
	 *            The initial hour.
	 * @param minute
	 *            The initial minute.
	 * @param is24HourView
	 *            Whether this is a 24 hour view, or AM/PM.
	 */
	public TimePickerDialog(Context context, int theme, int hourOfDay, int minute, int seconds, boolean is24HourView) {
		super(context, theme);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mInitialHourOfDay = hourOfDay;
		mInitialMinute = minute;
		mInitialSeconds = seconds;
		mIs24HourView = is24HourView;

		mDateFormat = DateFormat.getTimeFormat(context);
		mCalendar = Calendar.getInstance();
		updateTitle(mInitialHourOfDay, mInitialMinute, mInitialSeconds);

		mTimePicker = new TimePicker(getContext());
		mTimePicker.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		setView(mTimePicker);

		mTimePicker.setCurrentHour(mInitialHourOfDay);
		mTimePicker.setCurrentMinute(mInitialMinute);
		mTimePicker.setCurrentSecond(mInitialSeconds);
		mTimePicker.setIs24HourView(mIs24HourView);
		mTimePicker.setOnTimeChangedListener(this);
	}

	public void setOnTimeChangedListener(OnTimeChangedListener onTimeChangedListener) {
		this.onTimeChangedListener = onTimeChangedListener;
	}

	public void onTimeChanged(TimePicker view, int hourOfDay, int minute, int seconds) {
		onTimeChangedListener.onTimeChanged(view, hourOfDay, minute, seconds);
		updateTitle(hourOfDay, minute, seconds);
	}

	public void updateTime(int hourOfDay, int minutOfHour, int seconds) {
		mTimePicker.setCurrentHour(hourOfDay);
		mTimePicker.setCurrentMinute(minutOfHour);
		mTimePicker.setCurrentSecond(seconds);
	}

	private void updateTitle(int hour, int minute, int seconds) {
		mCalendar.set(Calendar.HOUR_OF_DAY, hour);
		mCalendar.set(Calendar.MINUTE, minute);
		mCalendar.set(Calendar.SECOND, seconds);
		setTitle(mDateFormat.format(mCalendar.getTime()) + ":" + String.format("%02d", seconds));
	}

	@Override
	public Bundle onSaveInstanceState() {
		Bundle state = super.onSaveInstanceState();
		state.putInt(HOUR, mTimePicker.getCurrentHour());
		state.putInt(MINUTE, mTimePicker.getCurrentMinute());
		state.putInt(SECONDS, mTimePicker.getCurrentSeconds());
		state.putBoolean(IS_24_HOUR, mTimePicker.is24HourView());
		return state;
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		int hour = savedInstanceState.getInt(HOUR);
		int minute = savedInstanceState.getInt(MINUTE);
		int seconds = savedInstanceState.getInt(SECONDS);
		mTimePicker.setCurrentHour(hour);
		mTimePicker.setCurrentMinute(minute);
		mTimePicker.setCurrentSecond(seconds);
		mTimePicker.setIs24HourView(savedInstanceState.getBoolean(IS_24_HOUR));
		mTimePicker.setOnTimeChangedListener(this);
		updateTitle(hour, minute, seconds);
	}

}
