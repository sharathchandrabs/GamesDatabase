package com.example.shara.gamesdb;

import android.util.Xml;

import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by shara on 2/18/2017.
 */

public class GameDetailsXMLParser {
    static public class GamePullParser extends DefaultHandler {
        static Game game = null;
        static SimpleDateFormat gameDate = new SimpleDateFormat("MM/dd/yyyy");
        StringBuilder xmlInnerText;

        static public Game parseGamePullParser(InputStream in) throws XmlPullParserException, IOException {
            final XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(in, Xml.Encoding.UTF_8.toString());
            ArrayList<Game> gameArrayList = new ArrayList<>();
            ArrayList<String> similarIdsArray = new ArrayList<String>();
            int event = parser.getEventType();
            int enterFirstGameTag = 0;
            String imageUrl = "";
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("Data")) {
                            game = new Game();
                        } else if (parser.getName().equals("id")) {
                            if (enterFirstGameTag == 0) {
                                game.setId(Integer.parseInt(parser.nextText().trim()));
                            } else {
                                similarIdsArray.add(parser.nextText().trim());
                            }
                        } else if (parser.getName().equals("Similar")) {
                            enterFirstGameTag++;
                        } else if (parser.getName().equals("GameTitle")) {
                            game.setTitle(parser.nextText().trim());
                        } else if (parser.getName().equals("Overview")) {
                            game.setOverview(parser.nextText().trim());
                            parser.nextTag();
                        } else if (parser.getName().equals("ReleaseDate")) {
                            try {
                                game.setReleaseDate(gameDate.parse(parser.nextText().trim()));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else if (parser.getName().equals("Platform")) {
                            game.setPlatform(parser.nextText().trim());
                        } else if (parser.getName().equals("genre")) {
                            game.setGenre(parser.nextText().trim());
                        } else if (parser.getName().equals("Youtube")) {
                            game.setYouTube(parser.nextText().trim());
                        } else if (parser.getName().equals("Publisher")) {
                            game.setPublisher(parser.nextText().trim());
                        } else if (parser.getName().equals("baseImgUrl")) {
                            imageUrl = parser.nextText().trim();
                        } else if (parser.getName().equals("thumb")) {
                            game.setImageUrl(imageUrl + parser.nextText().trim());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("Data")) {
                            game.setSimilarIds(similarIdsArray);
                            break;
                        }
                }
                event = parser.next();
            }
            return game;
        }
    }
}
