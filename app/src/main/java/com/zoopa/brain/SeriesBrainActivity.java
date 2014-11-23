package com.zoopa.brain;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.zoopa.brain.adapter.SeriesAdapter;
import com.zoopa.brain.listener.GroupExpandedListener;
import com.zoopa.brain.model.Series;
import com.zoopa.brain.utility.DatabaseHelper;
import com.zoopa.brain.utility.SeriesUtil;

import java.io.IOException;
import java.util.List;

import com.zoopa.brain.interfaces.OnSeriesChangeListener;

public class SeriesBrainActivity extends Activity {
    private final static int MENU_DELETE = Menu.FIRST;
    private final static int MENU_CREATE = Menu.FIRST + 1;
    private final static int MENU_EXPORT = Menu.FIRST + 2;
    private final static int MENU_IMPORT = Menu.FIRST + 3;
    private final static int ACTIVITY_NEW = 1;
    private ExpandableListView seriesListView;
    private DatabaseHelper dbHelper;
    private SeriesAdapter sAdapter;
    private List<Series> seriesList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        seriesListView = (ExpandableListView) findViewById(R.id.seriesList);
        seriesListView.setOnGroupExpandListener(new GroupExpandedListener(seriesListView));

        dbHelper = new DatabaseHelper(this);
        dbHelper.openDatabase();

        seriesList = dbHelper.getAllSeries();
        sAdapter = new SeriesAdapter(this, seriesList);
        seriesListView.setAdapter(sAdapter);

        sAdapter.setOnSeriesChangeListener(new OnSeriesChangeListener() {
            @Override
            public void onSeriesChange(Series series) {
                sAdapter.notifyDataSetChanged();
                dbHelper.updateSeries(series);
            }
        });

        registerForContextMenu(seriesListView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        Series series = getContextMenuSelectedSeries(menuInfo);

        if(series != null) {
            menu.setHeaderTitle(series.getName());
            menu.add(Menu.NONE, MENU_DELETE, 0, R.string.delete_series);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getItemId() == MENU_DELETE) {
            Series series = getContextMenuSelectedSeries(item.getMenuInfo());

            if(series != null) {
                dbHelper.deleteSeries(series);
                seriesList.remove(series);
                sAdapter.notifyDataSetChanged();
            }
        }

        return true;
    }

    private Series getContextMenuSelectedSeries(ContextMenuInfo contextMenuInfo) {
        ExpandableListView.ExpandableListContextMenuInfo cmi =
                (ExpandableListView.ExpandableListContextMenuInfo) contextMenuInfo;

        long packedPosition = cmi.packedPosition;
        int positionType = ExpandableListView.getPackedPositionType(packedPosition);

        if(positionType == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
            int groupId = ExpandableListView.getPackedPositionGroup(packedPosition);
            return seriesList.get(groupId);
        }

        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_CREATE, 0, R.string.create_series);
        menu.add(1, MENU_IMPORT, 0, R.string.import_series);
        menu.add(2, MENU_EXPORT, 0, R.string.export_series);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {

        if(menu.getItemId() == MENU_CREATE) {

            Intent intent = new Intent(this, SeriesCreateActivity.class);
            startActivityForResult(intent, ACTIVITY_NEW);
        } else if(menu.getItemId() == MENU_IMPORT) {
            showImportDialog();
        } else if(menu.getItemId() == MENU_EXPORT) {
            try {
                SeriesUtil seriesUtil = new SeriesUtil(this);
                seriesUtil.exportSeries(seriesList);
                Toast.makeText(getApplicationContext(), R.string.toast_export_done, Toast.LENGTH_SHORT).show();
            } catch(IOException e) {
                Toast.makeText(getApplicationContext(), R.string.toast_export_failed, Toast.LENGTH_SHORT).show();
            }
        }

        return true;
    }

    public void showImportDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final Context ctx = getApplicationContext();
        final SeriesUtil seriesUtil = new SeriesUtil(this);

        builder.setMessage(R.string.import_dialog_message)
                .setTitle(R.string.import_dialog_title);

        builder.setPositiveButton(R.string.import_dialog_positive,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            seriesList = seriesUtil.importSeries();
                            sAdapter.notifyDataSetChanged();
                            Toast.makeText(ctx, R.string.toast_import_ok, Toast.LENGTH_SHORT).show();
                        } catch(IOException e) {
                            Toast.makeText(ctx, R.string.toast_import_failed, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        builder.setNegativeButton(R.string.import_dialog_negative,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(ctx, R.string.toast_import_cancelled, Toast.LENGTH_SHORT).show();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ACTIVITY_NEW && resultCode == RESULT_OK) {

            String seriesName = data.getStringExtra("name");
            int seriesEpisode = data.getIntExtra("episode", 1);
            int seriesSeason = data.getIntExtra("season", 1);

            Series series = new Series(seriesName, seriesSeason, seriesEpisode);
            dbHelper.insertSeries(series);
            seriesList.add(series);
            sAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(dbHelper != null) {
            dbHelper.closeDatabase();
        }
    }
}