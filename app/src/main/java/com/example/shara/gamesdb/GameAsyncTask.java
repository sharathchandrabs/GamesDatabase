package com.example.shara.gamesdb;

import android.os.AsyncTask;

import org.json.JSONException;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by shara on 2/16/2017.
 */

public class GameAsyncTask extends AsyncTask<String, Void, ArrayList<Game>> {

    IData mainActivity;

    public GameAsyncTask(IData mainActivity) {
        this.mainActivity = mainActivity;
    }

    public static interface IData {
        public void setupData(ArrayList<Game> gameList);
    }

    @Override
    protected ArrayList<Game> doInBackground(String... params) {

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
                return GameXMLParser.GamePullParser.parseGamePullParser(in);

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Game> gameList) {
        mainActivity.setupData(gameList);
    }
}
