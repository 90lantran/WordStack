package com.google.engedu.wordstack;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int WORD_LENGTH = 5;
    public static final int LIGHT_BLUE = Color.rgb(176, 200, 255);
    public static final int LIGHT_GREEN = Color.rgb(200, 255, 200);
    private ArrayList<String> words = new ArrayList<>();
    private Random random = new Random();
    private StackedLayout stackedLayout;
    private String word1, word2;
    private Stack<LetterTile> placedTiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while((line = in.readLine()) != null) {
                String word = line.trim();
                /**
                 **
                 **  YOUR CODE GOES HERE
                 **
                 **/
                if (word.length() == WORD_LENGTH){
                    words.add(word);
                }
            }
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }
        LinearLayout verticalLayout = (LinearLayout) findViewById(R.id.vertical_layout);
        stackedLayout = new StackedLayout(this);
        verticalLayout.addView(stackedLayout, 3);

        View word1LinearLayout = findViewById(R.id.word1);
        //word1LinearLayout.setOnTouchListener(new TouchListener());
        word1LinearLayout.setOnDragListener(new DragListener());
        View word2LinearLayout = findViewById(R.id.word2);
        //word2LinearLayout.setOnTouchListener(new TouchListener());
        word2LinearLayout.setOnDragListener(new DragListener());
    }

    private class TouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN && !stackedLayout.empty()) {
                LetterTile tile = (LetterTile) stackedLayout.peek();
                tile.moveToViewGroup((ViewGroup) v);
                if (stackedLayout.empty()) {
                    TextView messageBox = (TextView) findViewById(R.id.message_box);
                    messageBox.setText(word1 + " " + word2);
                }
                /**
                 **setOnTouchListener
                 **  YOUR CODE GOES HERE
                 **
                 **/
                placedTiles.push(tile);

                return true;
            }
            return false;
        }
    }

    private class DragListener implements View.OnDragListener {

        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    v.setBackgroundColor(LIGHT_BLUE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackgroundColor(LIGHT_GREEN);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackgroundColor(LIGHT_BLUE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    v.setBackgroundColor(Color.WHITE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign Tile to the target Layout
                    LetterTile tile = (LetterTile) event.getLocalState();
                    tile.moveToViewGroup((ViewGroup) v);
                    if (stackedLayout.empty()) {
                        TextView messageBox = (TextView) findViewById(R.id.message_box);
                        messageBox.setText(word1 + " " + word2);
                    }
                    /**
                     **
                     **  YOUR CODE GOES HERE
                     **
                     **/
                    placedTiles.push(tile);

                    return true;
            }
            return false;
        }
    }

    public boolean onStartGame(View view) {

        // initialize placedTiles
        placedTiles = new Stack<>();

        // Clear up 2 white lines for 2 words
        LinearLayout word1LinearLayout = (LinearLayout)findViewById(R.id.word1);
        word1LinearLayout.removeAllViews();

        LinearLayout word2LinearLayout = (LinearLayout)findViewById(R.id.word2);
        word2LinearLayout.removeAllViews();

        stackedLayout.clear();


        TextView messageBox = (TextView) findViewById(R.id.message_box);
        messageBox.setText("Game started");
        /**
         **  YOUR CODE GOES HERE
         **/
        // pick 2 words
        Random r = new Random();
        int index1 = r.nextInt(words.size());
        int index2 = r.nextInt(words.size());
        word1 = words.get(index1);
        word2 = words.get(index2);
        Log.e(TAG, word1);
        Log.e(TAG, word2);

        // scramble 2 words
        String scramble ="";
        int counter1 = 0;
        int counter2 = 0;

        while(counter1 < WORD_LENGTH && counter2 < WORD_LENGTH){
            int whichWord = r.nextInt(2);
            if (whichWord == 0){
                scramble += word1.charAt(counter1);
                counter1++;
            } else {
                scramble+=word2.charAt(counter2);
                counter2++;
            }
        }

        if (counter1 == WORD_LENGTH && counter2 < WORD_LENGTH){
            scramble +=word2.substring(counter2);
        }

        if (counter2 == WORD_LENGTH && counter1 < WORD_LENGTH){
            scramble +=word1.substring(counter1);
        }

        messageBox.setText(scramble);
        for (int i = scramble.length() - 1; i >= 0 ; i--){
            stackedLayout.push(new LetterTile(this, scramble.charAt(i)));
        }

        return true;
    }

    public boolean onUndo(View view) {
        /**
         **
         **  YOUR CODE GOES HERE
         **
         **/
        if(!placedTiles.isEmpty()){
            LetterTile tile = placedTiles.pop();
            tile.moveToViewGroup(stackedLayout);
        }


        return true;
    }
}
