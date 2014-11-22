package com.zoopa.brain;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SeriesCreateActivity extends Activity {
    private Button saveBtn;
    private Button cancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_series);

        saveBtn = (Button) findViewById(R.id.saveButton);
        cancelBtn = (Button) findViewById(R.id.cancelButton);

        saveBtn.setOnClickListener(new SaveClicked());
        cancelBtn.setOnClickListener(new CancelClicked());
    }


    class SaveClicked implements OnClickListener {

        @Override
        public void onClick(View v) {

            Intent intent = new Intent();

            EditText nameTxt = (EditText) findViewById(R.id.newSeriesName);
            EditText seasonTxt = (EditText) findViewById(R.id.newSeriesSeason);
            EditText episodeTxt = (EditText) findViewById(R.id.newSeriesEpisode);

            String seriesName = nameTxt.getText().toString();
            int seriesSeason = 1;
            int seriesEpisode = 1;

            try {
                seriesSeason = Integer.parseInt(seasonTxt.getText().toString());
                seriesEpisode = Integer.parseInt(episodeTxt.getText().toString());
            }
            catch(NumberFormatException ex){}

            if(seriesName.length() > 0) {
                intent.putExtra("name", seriesName);
                intent.putExtra("season", seriesSeason);
                intent.putExtra("episode", seriesEpisode);

                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    class CancelClicked implements OnClickListener {

        @Override
        public void onClick(View v) {
            setResult(RESULT_CANCELED);
            finish();
        }
    }
}
