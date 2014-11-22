package com.zoopa.brain.listener;

import android.widget.ExpandableListView;

public class GroupExpandedListener implements ExpandableListView.OnGroupExpandListener {
    private int lastExpandedGroup = -1;
    private ExpandableListView seriesListView;

    public GroupExpandedListener(ExpandableListView seriesListView) {
        this.seriesListView = seriesListView;
    }

    @Override
    public void onGroupExpand(int groupPosition) {
        if(groupPosition != lastExpandedGroup) {
            seriesListView.collapseGroup(lastExpandedGroup);
        }

        lastExpandedGroup = groupPosition;
    }
}
