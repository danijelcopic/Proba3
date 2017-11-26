package rs.aleph.android.example13.activities.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.preference.PreferenceManager;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rs.aleph.android.example13.R;
import rs.aleph.android.example13.activities.db.DatabaseHelper;
import rs.aleph.android.example13.activities.db.model.RealEstate;
import rs.aleph.android.example13.activities.dialogs.AboutDialog;

import static android.R.string.ok;


public class FirstActivity extends AppCompatActivity {


    public static String NOTIF_TOAST = "pref_toast";
    public static String NOTIF_STATUS = "pref_notification";

    private DatabaseHelper databaseHelper;
    private AlertDialog dialogAlert;
    private SharedPreferences preferences;

    // za izbor slike u dijalogu
    private ImageView preview;
    private String imagePath = null;
    private static final int SELECT_PICTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_activity);


        // TOOLBAR
        // aktiviranje toolbara
        Toolbar toolbar = (Toolbar) findViewById(R.id.first_toolbar);
        setSupportActionBar(toolbar);


        // status podesavanja
        preferences = PreferenceManager.getDefaultSharedPreferences(this);


        //  ZA BAZU
        // ucitamo sve podatke iz baze u listu
        List<RealEstate> re = new ArrayList<RealEstate>();
        try {
            re = getDatabaseHelper().getmRealEstateDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        // u String izvucemo iz gornje liste  i sa adapterom posaljemo na View
        List<String> reName = new ArrayList<String>();
        for (RealEstate i : re) {
            reName.add(i.getmName());
        }

        final ListView listView = (ListView) findViewById(R.id.listFirstActivity); // definisemo u koji View saljemo podatke (listFirstActivity)
        ArrayAdapter<String> adapter = new ArrayAdapter<>(FirstActivity.this, R.layout.list_item, reName);  // definisemo kako ce izgledati jedna stavka u View (list_item)
        listView.setAdapter(adapter);


        // sta se desi kada kliknemo na stavku iz liste
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RealEstate realEstate = (RealEstate) listView.getItemAtPosition(position);
                Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
                intent.putExtra("position", realEstate.getmId());  // saljemo intent o poziciji
                startActivity(intent);

            }

        });

    }


    /**
     * MENU
     */

    // prikaz menija
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.first_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // sta se desi kada kliknemo na stavke iz menija
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {

            case R.id.action_add: // otvara se dialog za upis u bazu


                final Dialog dialog = new Dialog(FirstActivity.this); // aktiviramo dijalog
                dialog.setContentView(R.layout.dialog_realestate);


                // pritisnemo btn choose da bi izabrali sliku
                Button choosebtn = (Button) dialog.findViewById(R.id.btn_choose);
                choosebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        preview = (ImageView) dialog.findViewById(R.id.preview_image);
                        selectPicture();
                    }
                });


                final EditText reName = (EditText) dialog.findViewById(R.id.input_realestate_name);
                final EditText reDescription = (EditText) dialog.findViewById(R.id.input_realestate_description);
                final EditText reAddress = (EditText) dialog.findViewById(R.id.input_realestate_address);
                final EditText rePhone = (EditText) dialog.findViewById(R.id.input_realestate_phone);
                final EditText reSquare = (EditText) dialog.findViewById(R.id.input_realestate_square);
                final EditText reRooms = (EditText) dialog.findViewById(R.id.input_realestate_rooms);
                final EditText rePrice = (EditText) dialog.findViewById(R.id.input_realestate_price);


                Button save = (Button) dialog.findViewById(R.id.btn_save);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String name = reName.getText().toString();
                        if (name.isEmpty()) {
                            Toast.makeText(FirstActivity.this, "Must be entered", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String description = reDescription.getText().toString();
                        if (description.isEmpty()) {
                            Toast.makeText(FirstActivity.this, "Must be entered", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String address = reAddress.getText().toString();
                        if (address.isEmpty()) {
                            Toast.makeText(FirstActivity.this, "Must be entered", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        int phone = 0;
                        try {
                            phone = Integer.parseInt(rePhone.getText().toString());
                        } catch (NumberFormatException e) {
                            Toast.makeText(FirstActivity.this, "Must be number.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        double square = 0;
                        try {
                            square = Double.parseDouble(reSquare.getText().toString());
                        } catch (NumberFormatException e) {
                            Toast.makeText(FirstActivity.this, "Must be number.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        double rooms = 0;
                        try {
                            rooms = Double.parseDouble(reRooms.getText().toString());
                        } catch (NumberFormatException e) {
                            Toast.makeText(FirstActivity.this, "Must be number.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        double price = 0;
                        try {
                            price = Double.parseDouble(rePrice.getText().toString());
                        } catch (NumberFormatException e) {
                            Toast.makeText(FirstActivity.this, "Must be number.", Toast.LENGTH_SHORT).show();
                            return;
                        }


                        if (preview == null || imagePath == null){
                            Toast.makeText(FirstActivity.this, "Must be choose", Toast.LENGTH_SHORT).show();
                            return;
                        }



                        RealEstate realEstate = new RealEstate();
                        realEstate.setmName(name);
                        realEstate.setmDescription(description);
                        realEstate.setmPictures(imagePath);
                        realEstate.setmAddress(address);
                        realEstate.setmPhone(phone);
                        realEstate.setmSquare(square);
                        realEstate.setmRooms(rooms);
                        realEstate.setmPrice(price);



                        try {
                            getDatabaseHelper().getmRealEstateDao().create(realEstate);

                            //provera podesavanja
                            boolean toast = preferences.getBoolean(NOTIF_TOAST, false);

                            if (toast) {
                                Toast.makeText(FirstActivity.this, "New note is added", Toast.LENGTH_SHORT).show();
                            }

                            refresh(); // osvezavanje baze

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        dialog.dismiss();

                    }
                });

                Button cancel = (Button) dialog.findViewById(R.id.btn_cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //provera podesavanja
                        boolean toast = preferences.getBoolean(NOTIF_TOAST, false);

                        if (toast) {
                            Toast.makeText(FirstActivity.this, "New note is canceled", Toast.LENGTH_SHORT).show();
                        }

                        refresh(); // osvezavanje baze
                        dialog.dismiss();
                    }
                });

                dialog.show();

                break;


            case R.id.action_about:
                if (dialogAlert == null) {
                    dialogAlert = new AboutDialog(FirstActivity.this).prepareDialog(); // pozivamo prepareDialog() iz klase AboutDialog
                } else {
                    if (dialogAlert.isShowing()) {
                        dialogAlert.dismiss();
                    }

                }
                dialogAlert.show();
                break;

        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * TABELE I BAZA
     */

    //Metoda koja komunicira sa bazom podataka
    public DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }


    // refresh() prikazuje novi sadrzaj.Povucemo nov sadrzaj iz baze i popunimo listu
    private void refresh() {
        ListView listview = (ListView) findViewById(R.id.listFirstActivity);
        if (listview != null) {
            ArrayAdapter<RealEstate> adapter = (ArrayAdapter<RealEstate>) listview.getAdapter();
            if (adapter != null) {
                adapter.clear();
                try {
                    List<RealEstate> list = getDatabaseHelper().getmRealEstateDao().queryForAll();
                    adapter.addAll(list);
                    adapter.notifyDataSetChanged();
                } catch (SQLException e) {
                    e.printStackTrace();

                }
            }
        }
    }


    // metoda za izbor slike
    private void selectPicture(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }


    // kompatibilnost u nazad
    @Override
    public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }


    // ovde refreshujemo bazu kada smo se vratili iz druge aktivnosti
    @Override
    protected void onResume() {
        super.onResume();
        refresh();
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