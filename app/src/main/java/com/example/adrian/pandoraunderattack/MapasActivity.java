package com.example.adrian.pandoraunderattack;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MapasActivity extends AppCompatActivity {

    //Declaración de variables
    GoogleMap mapGoogle; //Objeto de tipo google map
    TextView Coordenadas;
    Location miLo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapas);

        //Creacionn del view del mapa
        mapGoogle = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapa)).getMap();
        mapGoogle.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mapGoogle.setMyLocationEnabled(true);

    }

    public void miUbicacion() {
        Location ubicacion = mapGoogle.getMyLocation();
        double lat;
        double log;
        LatLng coordenadas;
        LocationManager admiUbi = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String operadora;
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(30);


        if (ubicacion!=null) {
            lat = ubicacion.getLatitude();
            log = ubicacion.getLongitude();
            coordenadas = new LatLng(lat, log);
            CameraUpdate ubicar= CameraUpdateFactory.newLatLng(coordenadas);
            mapGoogle.moveCamera(ubicar);
            mapGoogle.animateCamera(zoom);
        }
        else{
            //operadora = admiUbi.getBestProvider(criteria, false);
            ubicacion = admiUbi.getLastKnownLocation(admiUbi.GPS_PROVIDER);
            lat = ubicacion.getLatitude();
            log = ubicacion.getLongitude();
            coordenadas = new LatLng(lat, log);
            CameraUpdate ubicar= CameraUpdateFactory.newLatLng(coordenadas);
            mapGoogle.moveCamera(ubicar);
            mapGoogle.animateCamera(zoom);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mapas, menu);
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
}
