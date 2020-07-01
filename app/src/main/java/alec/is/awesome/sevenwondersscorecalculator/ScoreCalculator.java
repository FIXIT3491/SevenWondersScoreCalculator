package alec.is.awesome.sevenwondersscorecalculator;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;


public class ScoreCalculator extends AppCompatActivity {

    TextView main;
    Expansions type = new Expansions();
    CheckBox cities;
    CheckBox leaders;
    CheckBox projects;
    CheckBox babel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_calculator);
        main  = (TextView) findViewById(R.id.textMain);
        cities = (CheckBox) findViewById(R.id.cities);
        leaders = (CheckBox) findViewById(R.id.leaders);
        projects = (CheckBox) findViewById(R.id.projects);
        babel = (CheckBox) findViewById(R.id.babel);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_score_calculator, menu);
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

    public void startInput(View view){
        Intent i = new Intent(this, Input.class);
        i.putExtra("type", type);
        startActivityForResult(i, 0);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        String original = main.getText().toString();
        try {
            int i = data.getIntExtra("score", -1);
            String s = data.getStringExtra("name");
            main.setText(original + System.getProperty("line.separator") + s + ": " + i);
        } catch(Exception e){
            main.setText(original);
        }
    }

    public static class Expansions implements Parcelable{
        public boolean cities = false;
        public boolean leaders = false;
        public boolean babel = false;
        public boolean projects = false;

        public Expansions(){}

        public int describeContents() {
            return 0;
        }

        // write your object's data to the passed-in Parcel
        public void writeToParcel(Parcel out, int flags) {
            boolean [] bools = {cities, leaders, babel, projects};
            out.writeBooleanArray(bools);
        }

        // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
        public static final Parcelable.Creator<Expansions> CREATOR = new Parcelable.Creator<Expansions>() {
            public Expansions createFromParcel(Parcel in) {
                return new Expansions(in);
            }

            public Expansions[] newArray(int size) {
                return new Expansions[size];
            }
        };

        // example constructor that takes a Parcel and gives you an object populated with it's values
        private Expansions(Parcel in) {
            boolean[] bools = new boolean[4];
            in.readBooleanArray(bools);
            this.cities = bools[0];
            this.leaders = bools[1];
            this.babel = bools[2];
            this.projects = bools[3];
        }
    }

    public void babel(View view) {
        type.babel = (type.babel)? false : true;
    }

    public void leaders(View view){
        type.leaders = (type.leaders)? false :true;
    }

    public void cities(View view){
       type.cities = (type.cities)? false :true;
    }

    public void projects(View view){
        type.projects = (type.projects)? false :true;
    }

    public void reset(View view){
        main.setText("Scores");
        type = new Expansions();
        babel.setChecked(false);
        projects.setChecked(false);
        leaders.setChecked(false);
        cities.setChecked(false);
    }
}