package rs.aleph.android.example13.activities.activity;

import android.Manifest;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.sql.SQLException;

import rs.aleph.android.example13.R;
import rs.aleph.android.example13.activities.db.DatabaseHelper;
import rs.aleph.android.example13.activities.db.model.RealEstate;


import static rs.aleph.android.example13.activities.activity.FirstActivity.NOTIF_TOAST;
import static rs.aleph.android.example13.activities.activity.FirstActivity.NOTIF_STATUS;


public class SecondActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int SELECT_PICTURE = 1;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 0;
    private static int NOTIFICATION_ID = 1;
    private int position = 0;
    private DatabaseHelper databaseHelper;
    private RealEstate realEstate;
    private SharedPreferences preferences;
    private AlertDialog dialogAlert;
    private Context context;
    // za izbor slike u dijalogu
    private ImageView preview;
    private String imagePath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


        // TOOLBAR
        // aktiviranje toolbara 2 koji je drugaciji od onog iz prve aktivnosti
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_second);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.BLACK);


        // Navigation Drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // prikazivanje strelice u nazad u toolbaru ... mora se u manifestu definisati zavisnost parentActivityName
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
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

            // String picture = realEstate.getmPictures();

            String address = realEstate.getmAddress();

            int phone = realEstate.getmPhone();
            String stringPhone = Integer.toString(phone);

            double square = realEstate.getmSquare();
            String stringSquare = Double.toString(square);

            double rooms = realEstate.getmSquare();
            String stringRooms = Double.toString(rooms);

            double price = realEstate.getmPrice();
            String stringPrice = Double.toString(price);


            // name
            TextView reName = (TextView) findViewById(R.id.input_realestate_name);
            reName.setText(name);

            // description
            TextView reDescription = (TextView) findViewById(R.id.input_realestate_description);
            reDescription.setText(description);

            // picture
            ImageView imageView = (ImageView) findViewById(R.id.picture);
            Uri mUri = Uri.parse(realEstate.getmPictures());
            imageView.setImageURI(mUri);

            // address
            TextView reAddress = (TextView) findViewById(R.id.input_realestate_address);
            reAddress.setText(address);

            // phone
            TextView rePhone = (TextView) findViewById(R.id.input_realestate_phone);
            rePhone.setText(stringPhone);

            // square
            TextView reSquare = (TextView) findViewById(R.id.input_realestate_square);
            reSquare.setText(stringSquare);

            // rooms
            TextView reRooms = (TextView) findViewById(R.id.input_realestate_rooms);
            reRooms.setText(stringRooms);

            // price
            TextView rePrice = (TextView) findViewById(R.id.input_realestate_price);
            rePrice.setText(stringPrice);


        } catch (SQLException e) {
            e.printStackTrace();
        }


        // fab
        FloatingActionButton button = (FloatingActionButton) findViewById(R.id.fab);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // kreiramo notifikaciju u builderu
                NotificationCompat.Builder builder = new NotificationCompat.Builder(SecondActivity.this);
                Bitmap bitmap = BitmapFactory.decodeResource(SecondActivity.this.getResources(), R.drawable.ic_stat_tour);
                builder.setSmallIcon(R.drawable.ic_stat_tour);
                builder.setContentTitle(SecondActivity.this.getString(R.string.notification_title));
                builder.setContentText(SecondActivity.this.getString(R.string.notification_text));
                builder.setLargeIcon(bitmap);

                // provera podesavanja
                boolean status = preferences.getBoolean(NOTIF_STATUS, false);

                if (status) {

                    // prikaz u status baru (notification bar)
                    NotificationManager manager = (NotificationManager) SecondActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(NOTIFICATION_ID, builder.build());

                }

            }
        });


    }


    // MENU
    // prikaz menija
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_second, menu);
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

            // kada pritisnemo ikonicu za promenu
            case R.id.action_edit:

                edit();

                break;

        }
        return super.onOptionsItemSelected(item);
    }


    // Navigation Drawer ... u kom je stanju ... prikazan ili ne, pa da se vrati ili otvori
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    // sta se desi kada kliknemo na stavke iz Notification Drawera
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_list) {

            // saljemo intent prvoj aktivnosti da bi videli listu
            Intent intent = new Intent(SecondActivity.this, FirstActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_settings) {

            Intent intent = new Intent(SecondActivity.this, SettingsActivity.class);  // saljemo intent Settings.class
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     * EDIT podataka
     */

    // pozivamo pri izmeni podataka ....
    private void edit() {

        final Dialog dialog = new Dialog(SecondActivity.this); // aktiviramo dijalog
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


        // update podataka u dialog pre edita
        reName.setText(realEstate.getmName());
        reDescription.setText(realEstate.getmName());

        // TODO: ovde moram videti za ponovni izbor slike


        reAddress.setText(realEstate.getmAddress());

        int phone = realEstate.getmPhone();
        String stringPhone = Integer.toString(phone);
        rePhone.setText(stringPhone);

        double square = realEstate.getmPhone();
        String stringSquare = Double.toString(square);
        reSquare.setText(stringSquare);

        double roms = realEstate.getmPhone();
        String stringRoms = Double.toString(roms);
        reRooms.setText(stringRoms);

        double price = realEstate.getmPhone();
        String stringPrice = Double.toString(price);
        rePrice.setText(stringPrice);


        // save
        Button save = (Button) dialog.findViewById(R.id.btn_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = reName.getText().toString();
                if (name.isEmpty()) {
                    Toast.makeText(SecondActivity.this, "Must be entered", Toast.LENGTH_SHORT).show();
                    return;
                }

                String description = reDescription.getText().toString();
                if (description.isEmpty()) {
                    Toast.makeText(SecondActivity.this, "Must be entered", Toast.LENGTH_SHORT).show();
                    return;
                }

                String address = reAddress.getText().toString();
                if (address.isEmpty()) {
                    Toast.makeText(SecondActivity.this, "Must be entered", Toast.LENGTH_SHORT).show();
                    return;
                }

                int phone = 0;
                try {
                    phone = Integer.parseInt(rePhone.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(SecondActivity.this, "Must be number.", Toast.LENGTH_SHORT).show();
                    return;
                }

                double square = 0;
                try {
                    square = Double.parseDouble(reSquare.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(SecondActivity.this, "Must be number.", Toast.LENGTH_SHORT).show();
                    return;
                }

                double rooms = 0;
                try {
                    rooms = Double.parseDouble(reRooms.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(SecondActivity.this, "Must be number.", Toast.LENGTH_SHORT).show();
                    return;
                }

                double price = 0;
                try {
                    price = Double.parseDouble(rePrice.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(SecondActivity.this, "Must be number.", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (preview == null || imagePath == null) {
                    Toast.makeText(SecondActivity.this, "Picture must be choose", Toast.LENGTH_SHORT).show();
                    return;
                }


                realEstate.setmName(name);
                realEstate.setmDescription(description);
                realEstate.setmPictures(imagePath);
                realEstate.setmAddress(address);
                realEstate.setmPhone(phone);
                realEstate.setmSquare(square);
                realEstate.setmRooms(rooms);
                realEstate.setmPrice(price);


                try {

                    getDatabaseHelper().getmRealEstateDao().update(realEstate);

                    //provera podesavanja (toast ili notification bar)
                    boolean toast = preferences.getBoolean(NOTIF_TOAST, false);
                    boolean status = preferences.getBoolean(NOTIF_STATUS, false);

                    if (toast) {
                        Toast.makeText(SecondActivity.this, "Real Estate is updated", Toast.LENGTH_SHORT).show();
                    }

                    if (status) {
                        showStatusMesage("Real Estate is updated");
                    }


                    finish();  // ovo sam morao da bi se vratio na prvu aktivnost i osvezio bazu novim podacima

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                dialog.dismiss();

            }
        });

        // cancel
        Button cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }


    // //provera podesavanja (toast ili notification bar) .... ovo pozivamo kada kliknemo na ikonicu u Tolbaru
    private void showMessage(String message) {

        boolean toast = preferences.getBoolean(NOTIF_TOAST, false);
        boolean status = preferences.getBoolean(NOTIF_STATUS, false);

        if (toast) {  // ako je aktivan toast prikazi ovo
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }

        if (status) {  // ako je aktivan statusbar pozovi metodu ... i prosledi joj poruku (tekst) koji ce ispisati
            showStatusMesage(message);
        }
    }


    // prikazivanje poruka u notification baru (status bar)
    private void showStatusMesage(String message) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_stat_tour);
        builder.setContentTitle("Ispit");
        builder.setContentText(message);

        // slicica u notification drawer-u
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_tour);
        builder.setLargeIcon(bm);

        notificationManager.notify(1, builder.build());
    }


    // metoda za izbor slike
    private void selectPicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }


    // sistemska metoda koja se automatski poziva kada se aktivnost startuje u startActivityForResult rezimu   (slika)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    if (selectedImageUri != null) {
                        imagePath = selectedImageUri.toString();
                    }

                    if (preview != null) {
                        preview.setImageBitmap(bitmap);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    //Metoda koja komunicira sa bazom podataka
    public DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }


    // pokretanje phone aplikacije
    public void call(View v) {

        int phone = realEstate.getmPhone();
        String stringPhone = Integer.toString(phone);
        Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", stringPhone, null));


        //provera permissiona
        if (ActivityCompat.checkSelfPermission(SecondActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(SecondActivity.this,
                    android.Manifest.permission.CALL_PHONE)) {
            } else {
                ActivityCompat.requestPermissions(SecondActivity.this,
                        new String[]{android.Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_CALL_PHONE);
            }
        }

        startActivity(callIntent);
    }


    // picasso .... full image
    public void fullImage(View view) {
        final Dialog dialog = new Dialog(SecondActivity.this);
        dialog.setContentView(R.layout.fullimage);

        ImageView image = (ImageView) dialog.findViewById(R.id.full_image);
        Uri mUri = Uri.parse(realEstate.getmPictures());

        Picasso.with(SecondActivity.this).load(mUri).into(image);

        Button close = (Button) dialog.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void reset() {
        imagePath = "";
        preview = null;
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
