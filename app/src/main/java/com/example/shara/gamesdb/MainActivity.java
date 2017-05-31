package com.example.shara.gamesdb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements GameAsyncTask.IData, GameDetailsAsyncTask.IData {
    EditText gameToSearch;
    SimpleDateFormat yearFormat;
    ProgressDialog mainActivityProgress;
    ProgressDialog goButtonProgress;
    HashMap<String, String> parameters;
    HashMap<String, String> gameDetailsParams;
    ListView gamesListView;
    LinearLayout linearLayout;
    RadioGroup radioGroup;
    RadioButton radioButton;
    ScrollView scrollV;
    int selectedRadioButton;
    Button goButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivityProgress = new ProgressDialog(this, R.style.MyTheme);
        mainActivityProgress.setCancelable(false);
        goButton = (Button) findViewById(R.id.goButton);
        goButton.setEnabled(false);
        findViewById(R.id.searchButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameToSearch = (EditText) findViewById(R.id.gameTitleTextView);
                parameters = new HashMap<String, String>();
                parameters.put((getResources().getString(R.string.nameParam)), (gameToSearch.getText().toString()));
                RequestParamsWithEncoding request = new RequestParamsWithEncoding((getResources().getString(R.string.baseUrl)), (getResources().getString(R.string.method)), parameters);
                String Url = request.getEncodedUrl();
                mainActivityProgress.show();
                new GameAsyncTask(MainActivity.this).execute(Url);
            }
        });
    }

   /* @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_main);
        mainActivityProgress = new ProgressDialog(this, R.style.MyTheme);
        mainActivityProgress.setCancelable(false);
        findViewById(R.id.searchButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameToSearch = (EditText) findViewById(R.id.gameTitleTextView);
                parameters = new HashMap<String, String>();
                parameters.put((getResources().getString(R.string.nameParam)), (gameToSearch.getText().toString()));
                RequestParamsWithEncoding request = new RequestParamsWithEncoding((getResources().getString(R.string.baseUrl)), (getResources().getString(R.string.method)), parameters);
                String Url = request.getEncodedUrl();
                mainActivityProgress.show();
                new GameAsyncTask(MainActivity.this).execute(Url);
            }
        });
    }*/

    public void setupData(final ArrayList<Game> gameList) {
        mainActivityProgress.dismiss();
        goButtonProgress = new ProgressDialog(this, R.style.MyTheme);
        yearFormat = new SimpleDateFormat("yyyy");
        goButtonProgress.setCancelable(false);
        if (gameList == null) {
            Toast.makeText(this, getResources().getString(R.string.parsingerror), Toast.LENGTH_LONG).show();
        } else if (gameList.size() == 0) {
            Toast.makeText(this, getResources().getString(R.string.noGamesAvailable), Toast.LENGTH_LONG).show();
        } else {
            goButton.setEnabled(true);
            goButton.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));
            scrollV = (ScrollView) findViewById(R.id.scrollView);
            radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
            for (Game g : gameList) {
                radioButton = new RadioButton(this);
                String year;
                if(g.getReleaseDate() == null){
                    year = "";
                }
                else{
                    year = yearFormat.format(g.getReleaseDate());
                }
                radioButton.setText(g.getTitle() + getResources().getString(R.string.releasedIn) + year + getResources().getString(R.string.platform) + g.getPlatform());
                radioGroup.addView(radioButton);
            }
            //selectedRadioButton = 0;
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    selectedRadioButton = radioGroup.indexOfChild(radioGroup.findViewById(checkedId));
                }
            });
            goButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //call asynctask to parse game details
                    goButtonProgress.show();
                    Game game = gameList.get(selectedRadioButton);
                    gameDetailsParams = new HashMap<String, String>();
                    gameDetailsParams.put((getResources().getString(R.string.id)), String.valueOf(game.getId()));
                    RequestParamsWithEncoding request = new RequestParamsWithEncoding((getResources().getString(R.string.baseUrlGameDetails)), getResources().getString(R.string.method), gameDetailsParams);
                    String gameDetailsUrl = request.getEncodedUrl();
                    new GameDetailsAsyncTask(MainActivity.this).execute(gameDetailsUrl);
                }
            });
        }
    }

    @Override
    public void setupGameDetailsData(Game gameDetails) {
        if (gameDetails != null) {
            goButtonProgress.dismiss();
            Intent activityGameDetails = new Intent(MainActivity.this, GameDetailsActivity.class);
            activityGameDetails.putExtra((getResources().getString(R.string.intentToGameDetails)), gameDetails);
            startActivity(activityGameDetails);
            radioGroup.removeAllViews();
            gameToSearch.setText("");
            goButton.setEnabled(false);
            goButton.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        } else {
            goButtonProgress.dismiss();
            Toast.makeText(this, getResources().getString(R.string.parsingerror), Toast.LENGTH_LONG).show();
        }
    }
}
