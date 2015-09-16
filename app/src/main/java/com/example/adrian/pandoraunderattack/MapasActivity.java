package com.example.adrian.pandoraunderattack;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapasActivity extends AppCompatActivity {

    //Declaración de variables
    private GoogleMap mapGoogle; //Objeto de tipo google map
    //private TextView Coordenadas;
    private Location miLo;
    int Recurso1; //Cantidades de recursos
    int Recurso2;
    int Recurso3;
    private CameraUpdate zoom = CameraUpdateFactory.zoomTo(30); //valor del zoom
    private LatLng coordenadas; //Objeto que almacenará los valores de la ubicacion
    private boolean buscarme=true;
    private Handler hiloBusqueda;
    final double Rango=0.1;//Revisar este valor


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapas);

        //Creacion del view del mapa
        mapGoogle = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapa)).getMap();
        mapGoogle.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mapGoogle.setMyLocationEnabled(true);

        //Hilo para que se actualize "coordenadas"
        hiloBusqueda = new Handler();
        hiloBusqueda.post(new Runnable(){
            @Override
            public void run(){
                while(buscarme){
                    setCoordenadas();
                }
            }
        });

    }

    private void setCoordenadas() {
        Location ubicacion = mapGoogle.getMyLocation(); //Objeto location
        double Lat;
        double Log;
        LocationManager admiUbi = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            if (ubicacion != null) {
                Lat = ubicacion.getLatitude();
                Log = ubicacion.getLongitude();
                coordenadas = new LatLng(Lat, Log);
            } else {
                //operadora = admiUbi.getBestProvider(criteria, false);
                ubicacion = admiUbi.getLastKnownLocation(admiUbi.GPS_PROVIDER);
                Lat = ubicacion.getLatitude();
                Log = ubicacion.getLongitude();
                coordenadas = new LatLng(Lat, Log);
            }
        }catch(Exception e){
            setCoordenadas();
        }
    }

    public void zoomUbicacion(LatLng location) {
        try {
            CameraUpdate ubicar = CameraUpdateFactory.newLatLng(location);
            mapGoogle.moveCamera(ubicar);
            mapGoogle.animateCamera(zoom);

        }catch(Exception e){
            //Agregar algo xD
        }
    }

    public void addRecurso(final double Latitud, final double Longitud, int Recurso){

        LatLng Posicion = new LatLng(Latitud,Longitud);
        CameraUpdate recurso= CameraUpdateFactory.newLatLng(Posicion);
        mapGoogle.moveCamera(recurso);
        mapGoogle.animateCamera(zoom);

        try{

           if (Recurso==1){ //Recurso 1= ?
               Marker RecursoA = mapGoogle.addMarker(new MarkerOptions()
                       .position(Posicion)
                       .title("NOMBREDELRECURSO") //Cmabiar por el nombre del recurso
                       .snippet("NOTA ADICIONAL") //Agregar nota adicional
                       .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))); //Color del marcador

           }

           else if (Recurso==2){ //Recurso 2=?
               Marker RecursoB = mapGoogle.addMarker(new MarkerOptions()
                       .position(Posicion)
                       .title("NOMBREDELRECURSO") //Cmabiar por el nombre del recurso
                       .snippet("NOTA ADICIONAL") //Agregar nota adicional
                       .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))); //Color del marcador

           }

           else if (Recurso==3) { //Recurso 3=?
               Marker RecursoC = mapGoogle.addMarker(new MarkerOptions()
                       .position(Posicion)
                       .title("NOMBREDELRECURSO") //Cmabiar por el nombre del recurso
                       .snippet("NOTA ADICIONAL") //Agregar nota adicional
                       .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))); //Color del marcador

           }
           else{
                //Agregar un else
           }
           mapGoogle.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(final Marker marker) {
                    if( ((Latitud-Rango<coordenadas.latitude) && (coordenadas.latitude<Latitud+Rango))
                            &&
                            ((Longitud+Rango<coordenadas.latitude) && (coordenadas.latitude<Longitud-Rango)) ){
                        //Agregar que hace cuando toca un recurso
                        return true;
                    }
                    else {
                        return false;
                    }
                }
            });
       }catch(Exception e){//Agregar exception
          }

    }

    private void addReliquia(){ //Revisar nombre
        final double LatiReliquia=coordenadas.latitude;
        final double LongiReliquia=coordenadas.longitude;

        Marker ReliquiaClan = mapGoogle.addMarker(new MarkerOptions()
                .position(coordenadas)
                .title("NOMBREDELRECURSO") //Cmabiar por el nombre del recurso
                .snippet("NOTA ADICIONAL") //Agregar nota adicional
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))); //Color del marcador
        mapGoogle.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                if( ((LatiReliquia-Rango<coordenadas.latitude) && (coordenadas.latitude<LatiReliquia+Rango))
                        &&
                    ((LongiReliquia+Rango<coordenadas.latitude) && (coordenadas.latitude<LongiReliquia-Rango)) ){
                    //Agregar que hace cuando toca un recurso
                    return true;
                }
                else {
                    return false;
                }

            }
        });
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
