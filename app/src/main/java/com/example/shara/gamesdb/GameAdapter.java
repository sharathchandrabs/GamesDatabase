package com.example.shara.gamesdb;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.shara.gamesdb.Game;
import com.example.shara.gamesdb.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shara on 2/17/2017.
 */

public class GameAdapter extends ArrayAdapter {

    Context gContext;
    int gResource;
    List<Game> gObjects;
    ArrayList<RadioButton> radio;

    public GameAdapter(Context context, int resource, List<Game> objects) {
        super(context, resource, objects);
        this.gContext = context;
        this.gResource = resource;
        this.gObjects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) gContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(gResource, parent, false);
        }
        Game game = (Game) gObjects.get(position);

        /*RadioGroup radioGroup = (RadioGroup) convertView.findViewById(R.id.radioButtonGroup);*/
        /*radio = new ArrayList<RadioButton>();*/
        RadioButton radioButton = (RadioButton) convertView.findViewById(R.id.radioButton);

        radioButton.setText(game.getTitle() + ". Released in: " + game.getReleaseDate() + ". Platform:" + game.getPlatform());
            /*radio.add(radioButton);*/


        /*radioGroup.addView(radioButton);*/
        return convertView;
    }
}
