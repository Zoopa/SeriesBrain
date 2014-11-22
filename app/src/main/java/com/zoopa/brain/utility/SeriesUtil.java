package com.zoopa.brain.utility;

import android.content.Context;

import com.zoopa.brain.model.Series;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SeriesUtil {
    private Context context;

    public SeriesUtil(Context context) {
        this.context = context;
    }

    public List<Series> importSeries() throws IOException {
        File file = new File(context.getExternalFilesDir(null), "exported_series.csv");
        List<Series> importedSeries = new ArrayList<Series>();
        FileReader fileReader = new FileReader(file);
        BufferedReader bufReader = new BufferedReader(fileReader);
        String line;

        while((line = bufReader.readLine()) != null) {
            importedSeries.add(createSeriesFromCsvLine(line));
        }

        bufReader.close();

        return importedSeries;
    }

    public void exportSeries(List<Series> seriesList) throws IOException {
        File file = new File(context.getExternalFilesDir(null), "exported_series.csv");
        FileWriter writer = new FileWriter(file);

        for(Series series : seriesList) {
            writer.write(series.toString() + '\n');
        }

        writer.close();
    }

    private Series createSeriesFromCsvLine(String line) {
        //TODO: error handling
        String[] tokens = line.split(";");

        String name = tokens[0];
        int season = Integer.parseInt(tokens[1]);
        int episode = Integer.parseInt(tokens[2]);

        return new Series(name, season, episode);
    }
}
