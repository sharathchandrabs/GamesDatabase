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

public class GameDetailsAsyncTask extends AsyncTask<String, Void, Game> {

    IData mainActivity;

    public GameDetailsAsyncTask(IData mainActivity) {
        this.mainActivity = mainActivity;
    }

    public static interface IData {
        public void setupGameDetailsData(Game gameDetails);
    }

    @Override
    protected Game doInBackground(String... params) {

        BufferedReader reader = null;

        try {
            URL url = new URL(params[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int statusCode = connection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                InputStream in = connection.getInputStream();
                //return here
                return GameDetailsXMLParser.GamePullParser.parseGamePullParser(in);

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Game gameDetails) {
        mainActivity.setupGameDetailsData(gameDetails);
    }
}

