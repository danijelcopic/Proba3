package rs.aleph.android.example13.activities.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;

import java.text.SimpleDateFormat;

import rs.aleph.android.example13.R;
import rs.aleph.android.example13.activities.db.DatabaseHelper;
import rs.aleph.android.example13.activities.db.model.RealEstate;


import static rs.aleph.android.example13.R.id.notes_name;
import static rs.aleph.android.example13.activities.activity.FirstActivity.NOTIF_TOAST;


public class SecondActivity extends AppCompatActivity {

    private int position = 0;

    private DatabaseHelper databaseHelper;
    private RealEstate realEstate;
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

        // TOOLBAR
        // aktiviranje toolbara 2 koji je drugaciji od onog iz prve aktivnosti
        Toolbar toolbar = (Toolbar) findViewById(R.id.second_toolbar);
        setSupportActionBar(toolbar);


        // prikazivanje strelice u nazad u toolbaru ... mora se u manifestu definisati zavisnost parentActivityName
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.show();
        }


        // status podesavanja
        preferences = PreferenceManager.getDefaultSharedPreferences(this);


        // hvatamo intent iz prve aktivnosti
        Intent intent = getIntent();
        position = intent.getExtras().getInt("position");

        // na osnovu dobijene pozicije od intenta, pupunjavamo polja u drugoj aktivnosti
        try {

            realEstate = getDatabaseHelper().getmRealEstateDao().queryForId((int) position);
            String name = realEstate.getmName();
            String description = realEstate.getmDescription();

            // name
            TextView notesName = (TextView) findViewById(notes_name);
            notesName.setText(name);

            //description
            TextView notesDescription = (TextView) findViewById(R.id.notes_description);
            notesDescription.setText(description);



        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // MENU
    // prikaz menija
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.second_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // sta se desi kada kliknemo na stavke iz menija
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {


            // kada pritisnemo ikonicu za brisanje
            case R.id.action_delete:
                try {
                    getDatabaseHelper().getmRealEstateDao().delete(realEstate);

                    //provera podesavanja
                    boolean toast = preferences.getBoolean(NOTIF_TOAST, false);

                    if (toast) {
                        Toast.makeText(SecondActivity.this, "Real Estate is deleted", Toast.LENGTH_SHORT).show();
                    }


                    finish();


                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    //Metoda koja komunicira sa bazom podataka
    public DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // nakon rada sa bazom podataka potrebno je obavezno osloboditi resurse!!!
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }


}
