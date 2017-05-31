package com.example.shara.gamesdb;

import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by shara on 2/18/2017.
 */

public class SimilarGamesAsyncTask extends AsyncTask<ArrayList<String>, Void, ArrayList<Game>> {
    Game gamesToAdd;
    ArrayList<Game> similarGamesArray;
    SimilarGamesAsyncTask.IData GameDetailsActivity;

    public SimilarGamesAsyncTask(SimilarGamesAsyncTask.IData GameDetailsActivity) {
        this.GameDetailsActivity = GameDetailsActivity;
    }

    public static interface IData {
        public void setupGameDetailsData(ArrayList<Game> gameDetails);
    }

    @Override
    protected ArrayList<Game> doInBackground(ArrayList<String>... params) {

        BufferedReader reader = null;
        similarGamesArray = new ArrayList<>();
        for (String s : params[0]) {
            try {
                URL url = new URL(s);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                int statusCode = connection.getResponseCode();
                if (statusCode == HttpURLConnection.HTTP_OK) {
                    InputStream in = connection.getInputStream();
                    gamesToAdd = SimilarGamesXmlParser.GamePullParser.parseGamePullParser(in);
                } else {
                    return null;
                }

                similarGamesArray.add(gamesToAdd);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
        }

        return similarGamesArray;
    }

    @Override
    protected void onPostExecute(ArrayList<Game> gameDetails) {
        GameDetailsActivity.setupGameDetailsData(gameDetails);
    }
}
