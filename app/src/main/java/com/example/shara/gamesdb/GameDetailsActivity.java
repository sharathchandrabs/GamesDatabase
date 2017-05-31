package com.example.shara.gamesdb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class GameDetailsActivity extends AppCompatActivity implements SimilarGamesAsyncTask.IData {
    ProgressDialog similarGameDetailsProgress;
    ArrayList<Game> similarItems;
    HashMap<String, String> gameDetailsParams;
    ArrayList<String> similargameURL;
    Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);
        similarGameDetailsProgress = new ProgressDialog(this, R.style.MyTheme);
        similarGameDetailsProgress.setCancelable(false);
        Bundle gameBundle = getIntent().getExtras();
        game = (Game) gameBundle.getSerializable((getResources().getString(R.string.intentToGameDetails)));

        TextView title = (TextView) findViewById(R.id.gameTitle);
        TextView description = new TextView(this);
        ImageView gameImage = (ImageView) findViewById(R.id.gameImage);
        LinearLayout gameDescription = (LinearLayout) findViewById(R.id.gameDesciptionLinearLayout);
        TextView genre = (TextView) findViewById(R.id.gameGenre);
        TextView publisher = (TextView) findViewById(R.id.gamePublisher);

        if (game.getImageUrl() == null || game.getImageUrl().equals("")) {
            Picasso.with(this)
                    .load(R.drawable.image)
                    .placeholder(R.drawable.image)
                    .error(R.drawable.image)
                    .into(gameImage);
        } else {
            Picasso.with(this)
                    .load(game.getImageUrl())
                    .placeholder(R.drawable.image)
                    .error(R.drawable.image)
                    .into(gameImage);
        }

        title.setText(game.getTitle());
        description.setText(game.getOverview());
        gameDescription.addView(description);
        genre.setText(getResources().getString(R.string.gameGenre) + game.getGenre());
        publisher.setText(getResources().getString(R.string.gamePublisher) + game.getPublisher());

        findViewById(R.id.playTrailer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (game.getYouTube() == null || game.getYouTube().equals("")) {
                    Toast.makeText(GameDetailsActivity.this, getResources().getString(R.string.noWebviewDetails), Toast.LENGTH_LONG).show();
                } else {
                    Intent webViewActivity = new Intent(GameDetailsActivity.this, WebviewActivity.class);
                    webViewActivity.putExtra(getResources().getString(R.string.webViewUrl), game.getYouTube());
                    startActivity(webViewActivity);
                }
            }
        });

        findViewById(R.id.similarGames).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                similarGameDetailsProgress.show();
                similargameURL = new ArrayList<String>();
                for (String similar : game.getSimilarIds()) {
                    gameDetailsParams = new HashMap<String, String>();
                    gameDetailsParams.put((getResources().getString(R.string.id)), similar);
                    RequestParamsWithEncoding request = new RequestParamsWithEncoding((getResources().getString(R.string.baseUrlGameDetails)), (getResources().getString(R.string.method)), gameDetailsParams);
                    similargameURL.add(String.valueOf(request.getEncodedUrl()));
                }
                new SimilarGamesAsyncTask(GameDetailsActivity.this).execute(similargameURL);
            }
        });

        findViewById(R.id.finishButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void setupGameDetailsData(ArrayList<Game> gameDetails) {
        //Log.d("similargames", gameDetails.toString());
        similarGameDetailsProgress.dismiss();
        if (gameDetails == null) {
            Toast.makeText(this, getResources().getString(R.string.parsingerror), Toast.LENGTH_LONG).show();
        } else if (gameDetails.size() == 0) {
            Toast.makeText(this, getResources().getString(R.string.noSimilarGamesAvailable), Toast.LENGTH_LONG).show();
        } else {
            Intent similarGamesActivity = new Intent(GameDetailsActivity.this, SimilarGamesActivity.class);
            similarGamesActivity.putExtra(getResources().getString(R.string.intentToSimilarGames), gameDetails);
            similarGamesActivity.putExtra(getResources().getString(R.string.similarGamesTo), game.getTitle());
            startActivity(similarGamesActivity);
        }
    }
}
