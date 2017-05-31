package com.example.shara.gamesdb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class SimilarGamesActivity extends AppCompatActivity {
    SimpleDateFormat yearFormat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_similar_games);
        yearFormat = new SimpleDateFormat("yyyy");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            TextView textView = (TextView) findViewById(R.id.textViewSimilarTo);
            textView.append(extras.getString(getResources().getString(R.string.similarGamesTo)));
            LinearLayout similarGamesLayout = (LinearLayout) findViewById(R.id.similarGamesScrollViewLinear);
            ArrayList<Game> similarGames = (ArrayList<Game>) extras.getSerializable(getResources().getString(R.string.intentToSimilarGames));
            TextView similarGamesTextView;
            String similarGameDetails;
            for (Game game : similarGames) {
                similarGamesTextView = new TextView(this);
//                similarGamesTextView.setBackground(R.drawable.scrollviewborder);
                similarGameDetails = game.getTitle() == "" || game.getTitle() == null ? getResources().getString(R.string.gameTitleUnavailable) : game.getTitle();
                similarGameDetails += game.getReleaseDate() == null || game.getReleaseDate() == null ? getResources().getString(R.string.gameReleaseNotAvailable) : getResources().getString(R.string.releasedIn) + yearFormat.format(game.getReleaseDate());
                similarGameDetails += game.getPlatform() == "" || game.getPlatform() == null ? getResources().getString(R.string.gamePlatformUnavailable) : getResources().getString(R.string.platform) + game.getPlatform();

                similarGamesTextView.setText(similarGameDetails);
                similarGamesLayout.addView(similarGamesTextView);
            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.noSimilarGameDetails), Toast.LENGTH_LONG).show();
        }
        findViewById(R.id.similarGamesFinishButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
