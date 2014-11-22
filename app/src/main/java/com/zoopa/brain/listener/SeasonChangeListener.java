package com.zoopa.brain.listener;

import android.view.View;

import com.zoopa.brain.model.Series;

import com.zoopa.brain.interfaces.OnSeriesChangeListener;

public class SeasonChangeListener implements View.OnClickListener {
    private Series series;
    private int changeAmount;
    private OnSeriesChangeListener listener;

    public SeasonChangeListener(OnSeriesChangeListener listener, Series series, int changeAmount) {
        this.listener = listener;
        this.series = series;
        this.changeAmount = changeAmount;
    }

    @Override
    public void onClick(View v) {
        int newSeason = series.getSeason() + changeAmount;
        series.setSeason(newSeason);

        if(listener != null) {
            listener.onSeriesChange(series);
        }
    }
}
