package alec.is.awesome.sevenwondersscorecalculator;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;


public class Input extends AppCompatActivity {
    int stage = 0;
    int score = 0;
    int gear = 0;
    int compass = 0;
    int clay = 0;
    String name = "";

    EditText input;
    TextView text;
    ImageView img;
    ScoreCalculator.Expansions type = new ScoreCalculator.Expansions();
    HashMap<Integer, Integer> scoreValues = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        input = (EditText) findViewById(R.id.input);
        text = (TextView) findViewById(R.id.text);
        img = (ImageView) findViewById(R.id.imageView);
        Intent i = getIntent();
        type = i.getParcelableExtra("type");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_input, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    @Override
    public void onBackPressed(){
        if (stage == 0){
            super.onBackPressed();
        }
        stage--;
        selectImages();
    }

    private void selectImages(){
        Bitmap bit = null;
        switch (stage){
            case 0:
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                text.setText("Name");
                break;
            case 1:
                input.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
                text.setText("VP Military");
                bit = BitmapFactory.decodeResource(getResources(), R.drawable.card1);
                break;
            case 2:
                text.setText("# of Coins");
                bit = BitmapFactory.decodeResource(getResources(), R.drawable.card2);
                break;
            case 3:
                text.setText("VP Stages of Wonder");
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case 4:
                text.setText("VP Blue Cards");
                bit = BitmapFactory.decodeResource(getResources(), R.drawable.card4);
                break;
            case 5:
                text.setText("VP Yellow Cards");
                bit = BitmapFactory.decodeResource(getResources(), R.drawable.card5);
                break;
            case 6:
                text.setText("Science Gears");
                bit = BitmapFactory.decodeResource(getResources(), R.drawable.card6);
                break;
            case 7:
                text.setText("Science Compass");
                bit = BitmapFactory.decodeResource(getResources(), R.drawable.card7);
                break;
            case 8:
                text.setText("Science Clay Tablet");
                bit = BitmapFactory.decodeResource(getResources(), R.drawable.card8);
                break;
            case 9:
                text.setText("VP Purple Cards");
                bit = BitmapFactory.decodeResource(getResources(), R.drawable.card9);
                break;
            case 10:
                if(type.cities){
                    text.setText("VP Black Cards");
                    bit = BitmapFactory.decodeResource(getResources(), R.drawable.card10);
                    break;
                }
            case 11:
                if(type.leaders){
                    text.setText("VP Leaders");
                    bit = BitmapFactory.decodeResource(getResources(), R.drawable.card11);
                    break;
                }
            case 12:
                if(type.babel){
                    text.setText("Babel Tiles");
                    break;
                }
            case 13:
                if(type.projects) {
                    text.setText("VP from projects");
                    break;
                }
            case 14:
                if(type.projects) {
                    text.setText("Project Science Token");
                    bit = BitmapFactory.decodeResource(getResources(), R.drawable.card12);
                    break;
                }
        }
        input.setText("");
        img.setImageBitmap(bit);
    }

    public void submit(View view){
        switch (stage){
            case 0: name = input.getText().toString();
                break;
            case 1:
                scoreValues.put(stage, getNum(input));
                break;
            case 2:
                scoreValues.put(stage, ((getNum(input) < 0)? getNum(input) : getNum(input) / 3));
                break;
            case 3:
                scoreValues.put(stage, getNum(input));
                break;
            case 4:
                scoreValues.put(stage, getNum(input));
                break;
            case 5:
                scoreValues.put(stage, getNum(input));
                break;
            case 6: gear = getNum(input);
                break;
            case 7: compass = getNum(input);
                break;
            case 8: clay = getNum(input);
                break;
            case 9:
                scoreValues.put(stage, getNum(input));
                if(!type.projects && !type.babel && !type.leaders && !type.cities) {
                    scoreValues.put(-1, doScience(7));
                    end();
                }
                if(type.cities){
                    break;
                }
            case 10:
                if(type.leaders){
                    scoreValues.put(stage, getNum(input));
                    stage = 10;
                    break;
                }
            case 11:
                if(type.babel){
                    scoreValues.put(-1, doScience(7));

                    scoreValues.put(stage, getNum(input));
                    stage = 11;
                    break;
                }
            case 12:
                if(type.projects) {
                    scoreValues.put(stage, (type.babel)? getNum(input) * 2 : getNum(input));
                    stage = 12;
                    break;
                }
            case 13:
                scoreValues.put(stage, (type.babel)? getNum(input) * 2 : getNum(input));
                if(type.projects) {
                    stage = 13;
                    break;
                } else {
                    scoreValues.put(-1, doScience(7));
                    end();
                }
            case 14:
                if(type.projects){
                    int i = getNum(input);
                    scoreValues.put(-1, doScience(i * 3 + 7));
                } else {
                    scoreValues.put(stage, (type.babel)? getNum(input) * 2 : getNum(input));
                }
                end();
        }
        stage++;
        selectImages();
    }

    public int doScience(int mult){
        int iscore = clay * clay;
        iscore += compass * compass;
        iscore += gear * gear;
        if(gear <= compass && gear <= clay){
            iscore += mult * gear;
        } else if(compass <= gear && compass <= clay){
            iscore += mult * compass;
        } else {
            iscore += mult * clay;
        }
        return iscore;
    }

    public void end(){
        Intent i = new Intent();
        for (Integer integer : scoreValues.values()){
            score += integer;
        }
        i.putExtra("score", score);
        i.putExtra("name", name);
        setResult(0, i);
        finish();
    }

    public int getNum(EditText eddy){
        int i = 0;
        try{
            i = Integer.parseInt(eddy.getText().toString());
        } catch (Exception e){
            return 0;
        }
        return i;
    }
}
