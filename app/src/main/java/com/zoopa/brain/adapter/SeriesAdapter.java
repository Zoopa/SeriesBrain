package com.zoopa.brain.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.zoopa.brain.R;
import com.zoopa.brain.listener.EpisodeChangeListener;
import com.zoopa.brain.listener.SeasonChangeListener;
import com.zoopa.brain.model.Series;

import java.util.List;

import com.zoopa.brain.interfaces.OnSeriesChangeListener;

public class SeriesAdapter extends BaseExpandableListAdapter implements OnSeriesChangeListener {
    private List<Series> seriesList;
    private Context context;
    private OnSeriesChangeListener seriesChangeListener;

    public SeriesAdapter(Context context, List<Series> seriesList) {
        this.context = context;
        this.seriesList = seriesList;
    }

    public void setOnSeriesChangeListener(OnSeriesChangeListener listener) {
        this.seriesChangeListener = listener;
    }

    @Override
    public void onSeriesChange(Series series) {
        if(seriesChangeListener != null) {
            seriesChangeListener.onSeriesChange(series);
        }
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Series s = seriesList.get(groupPosition);
        GroupViewHolder holder;

        if(convertView == null) {
            holder = new GroupViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.series_group_item, parent, false);

            holder.seriesName = (TextView) convertView.findViewById(R.id.seriesName);
            holder.seriesSeason = (TextView) convertView.findViewById(R.id.seriesSeason);
            holder.seriesEpisode = (TextView) convertView.findViewById(R.id.seriesEpisode);
            holder.letterImage = (ImageView) convertView.findViewById(R.id.letterImage);

            holder.season = context.getString(R.string.season);
            holder.episode = context.getString(R.string.episode);

            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }

        String firstLetter = s.getName().substring(0, 1);
        TextDrawable letter = TextDrawable.builder().buildRound(firstLetter, ColorGenerator.DEFAULT.getColor(firstLetter));
        holder.letterImage.setImageDrawable(letter);

        holder.seriesName.setText(s.getName());
        holder.seriesSeason.setText(holder.season + " " + s.getSeason());
        holder.seriesEpisode.setText(holder.episode + " " + s.getEpisode());

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder;
        Series series = getGroup(groupPosition);

        if(convertView == null) {
            holder = new ChildViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.series_child_item, parent, false);

            holder.seasonMinus = (ImageButton) convertView.findViewById(R.id.seasonMinus);
            holder.seasonPlus = (ImageButton) convertView.findViewById(R.id.seasonPlus);
            holder.episodeMinus = (ImageButton) convertView.findViewById(R.id.episodeMinus);
            holder.episodePlus = (ImageButton) convertView.findViewById(R.id.episodePlus);
            holder.seasonTitle = (ImageView) convertView.findViewById(R.id.seasonTitle);
            holder.episodeTitle = (ImageView) convertView.findViewById(R.id.episodeTitle);

            Resources res = context.getResources();
            holder.plusDrawable = TextDrawable.builder().buildRoundRect(context.getString(R.string.plus_one), res.getColor(R.color.drawable_plus_one), 10);
            holder.minusDrawable = TextDrawable.builder().buildRoundRect(context.getString(R.string.minus_one), res.getColor(R.color.drawable_minus_one), 10);
            holder.episodeTitleDrawable = TextDrawable.builder().buildRoundRect(context.getString(R.string.ep_title), res.getColor(R.color.drawable_title), 10);
            holder.seasonTitleDrawable = TextDrawable.builder().buildRoundRect(context.getString(R.string.se_title), res.getColor(R.color.drawable_title), 10);

            holder.seasonMinus.setImageDrawable(holder.minusDrawable);
            holder.seasonPlus.setImageDrawable(holder.plusDrawable);
            holder.episodeMinus.setImageDrawable(holder.minusDrawable);
            holder.episodePlus.setImageDrawable(holder.plusDrawable);
            holder.seasonTitle.setImageDrawable(holder.seasonTitleDrawable);
            holder.episodeTitle.setImageDrawable(holder.episodeTitleDrawable);

            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }

        holder.seasonPlus.setOnClickListener(new SeasonChangeListener(this, series, +1));
        holder.seasonMinus.setOnClickListener(new SeasonChangeListener(this, series, -1));
        holder.episodePlus.setOnClickListener(new EpisodeChangeListener(this, series, +1));
        holder.episodeMinus.setOnClickListener(new EpisodeChangeListener(this, series, -1));

        return convertView;
    }

    @Override
    public int getGroupCount() {
        return seriesList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Series getGroup(int groupPosition) {
        return seriesList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class GroupViewHolder {
        TextView seriesName;
        TextView seriesSeason;
        TextView seriesEpisode;
        ImageView letterImage;
        String season;
        String episode;
    }

    static class ChildViewHolder {
        ImageButton seasonPlus;
        ImageButton seasonMinus;
        ImageButton episodePlus;
        ImageButton episodeMinus;
        ImageView seasonTitle;
        ImageView episodeTitle;
        TextDrawable plusDrawable;
        TextDrawable minusDrawable;
        TextDrawable episodeTitleDrawable;
        TextDrawable seasonTitleDrawable;
    }
}
