package com.example.adrian.pandoraunderattack;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Pantalla del mapa (pantalla principal del juego)
 * @author Esteban Agüero Pérez
 */
public class MapasActivity extends MainActivity {

    //Declaración de variables
    private GoogleMap mapGoogle; //Objeto de tipo google map

    static int Recurso1; //Cantidades de recursos
    static int Recurso2;
    static int Recurso3;
    private CameraUpdate zoom; //valor del zoom
    private LatLng coordenadas; //Objeto que almacenará los valores de la ubicacion
    private boolean buscarme=true;
    private Handler hiloBusqueda; //Hilo que actualiza las coordenadas #REVISAR NO FUNKA
    private Button marcador;
    private int puntaje;

    /**
     *
     * @return Recurso1
     */
    public static int getRecurso1() {
        try {
            return Recurso1;
        }catch (Exception E){
            return 0;
        }
    }

    /**
     *
     * @return Recurso2
     *
     */
    public static int getRecurso2() {
        try {
            return Recurso2;
        }catch (Exception E){
            return 0;
        }
    }

    /**
     *
     * @return Recurso3
     */
    public static int getRecurso3() {
        try {
            return Recurso3;
        }catch (Exception E){
            return 0;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapas);

        //Creacion del view del mapa
        mapGoogle = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapa)).getMap();
        mapGoogle.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mapGoogle.setMyLocationEnabled(true);
        zoom = CameraUpdateFactory.zoomTo(30);

        //Creacion coordenadas iniciales

        //Hacer esto thread
        //Hilo para que se actualize "coordenadas" #REVISAR
        //hiloBusqueda = new Handler();
        //hiloBusqueda.post(new Runnable() {
        //    @Override
        //    public void run() {
        //        while (buscarme) {
        //           setCoordenadas();
        //        }
        //    }
        //});
        findViewById(R.id.bRecursos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapasActivity.this, RecursosActivity.class));
            }
        });

        //Boton de agregar recursos de prueba
        marcador=(Button) findViewById(R.id.addRecurso);
        marcador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCoordenadas();
                addRecurso(coordenadas.latitude,coordenadas.longitude,1);
            }
        });

    }

    /**
     * Establece las coordenadaes de la ubicacion actual
     */
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

    /**
     * Realiza zoom a una ubicacion especifica
     * @param location
     */
    public void zoomUbicacion(LatLng location) {
        try {
            CameraUpdate ubicar = CameraUpdateFactory.newLatLng(location);
            mapGoogle.moveCamera(ubicar);
            mapGoogle.animateCamera(zoom);

        }catch(Exception e){
            //Agregar algo xD
        }
    }

    /**
     * Añade un recurso al mapa
     * @param Latitud
     * @param Longitud
     * @param Recurso
     */
    public void addRecurso(final double Latitud, final double Longitud, int Recurso){

        final LatLng Posicion = new LatLng(Latitud,Longitud); //Crea objeto tipo LatLng para manejo de coordenadas
        zoomUbicacion(Posicion); //Zoom a la ubicacion del recurso
        final Circle circleRecurso; //Crea un circulo alrededor de el recurso

        circleRecurso=mapGoogle.addCircle(new CircleOptions() //Da caracteristicas al circulo
                .center(Posicion)
                .radius(1)
                .strokeColor(Color.RED)
                .fillColor(Color.BLUE));

        try{

           if (Recurso==1){ //Recurso 1= ?
               Marker RecursoA = mapGoogle.addMarker(new MarkerOptions()
                       .position(Posicion)
                       .title("Gemas") //Cmabiar por el nombre del recurso
                       .snippet("Piedras de gran valor y difíciles de encontrar") //Agregar nota adicional
                       .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))); //Color del marcador

           }

           else if (Recurso==2){ //Recurso 2=?
               Marker RecursoB = mapGoogle.addMarker(new MarkerOptions()
                       .position(Posicion)
                       .title("Oro") //Cmabiar por el nombre del recurso
                       .snippet("NOTA ADICIONAL") //Agregar nota adicional
                       .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))); //Color del marcador

           }

           else if (Recurso==3) { //Recurso 3=?
               Marker RecursoC = mapGoogle.addMarker(new MarkerOptions()
                       .position(Posicion)
                       .title("Elixir") //Cmabiar por el nombre del recurso
                       .snippet("NOTA ADICIONAL") //Agregar nota adicional
                       .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))); //Color del marcador

           }
           else{
                //Agregar un else
           }
            mapGoogle.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                final private int Rango = 1;//Ajustar rango entre el recurso y nuestra unicacion actual

                @Override
                public void onInfoWindowClick(Marker marker) { //Tomamos el control del metodo onInfo...
                    setCoordenadas();
                    float[] distancia = new float[2]; //Objeto requerido para poder usar el metodo distance... en Location
                    Location.distanceBetween(coordenadas.latitude, coordenadas.longitude,
                            marker.getPosition().latitude, marker.getPosition().longitude, distancia);//Calcula la distancia entre puntos

                    if (distancia[0] > Rango) {  //Verifica que la distancia no sea mayor al rango
                        Toast.makeText(getBaseContext(), "Fuera", Toast.LENGTH_LONG).show();

                    } else {
                        //Agregar que cuando se toca un recurso se sume el recurso X a la cantidad total
                        if (marker.getTitle().equals("Gemas")) {
                            Recurso1 += 10;
                            puntaje+=8;
                            Toast.makeText(getBaseContext(), "Añadiendo recursos...", Toast.LENGTH_LONG).show();
                            circleRecurso.remove();
                            marker.remove();
                        } else if (marker.getTitle().equals("Oro")) {
                            Recurso2 += 100;
                            puntaje+=4;
                            Toast.makeText(getBaseContext(), "Añadiendo recursos...", Toast.LENGTH_LONG).show();
                            circleRecurso.remove();
                            marker.remove();
                        } else if (marker.getTitle().equals( "Elixir")) {
                            Recurso3 += 1000;
                            puntaje+=2;
                            Toast.makeText(getBaseContext(), "Añadiendo recursos...", Toast.LENGTH_LONG).show();
                            circleRecurso.remove();
                            marker.remove();
                        } else if (marker.getTitle().equals("RELIQUIA")) {
                            Toast.makeText(getBaseContext(), "Esta es la reliquia del clan", Toast.LENGTH_LONG).show();
                        } else{
                            Toast.makeText(getBaseContext(), "Atrapaste la reliquia de otro clan", Toast.LENGTH_LONG).show();
                            puntaje+=1000;
                            marker.remove();
                        }

                    }
                }
            });

       }catch(Exception e){//Agregar exception
          }

    }

    /**
     * Añade la reliquia del clan que esta unido el cliente
     */
    private void addReliquia(){ //Revisar nombre
        final double LatiReliquia=coordenadas.latitude;
        final double LongiReliquia=coordenadas.longitude;

        Marker ReliquiaClan = mapGoogle.addMarker(new MarkerOptions()
                .position(coordenadas)
                .title("RELIQUIA") //Cmabiar por el nombre del recurso
                .snippet("NOTA ADICIONAL") //Agregar nota adicional
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))); //Color del marcador

    }

    /**
     * Añade las reliquias de otros clanes
     */
    private void addReliquiaEnemiga(){ //Revisar nombre
        final double LatiReliquia=coordenadas.latitude;
        final double LongiReliquia=coordenadas.longitude;

        Marker ReliquiaEnemiga = mapGoogle.addMarker(new MarkerOptions()
                .position(coordenadas)
                .title("Reliquia Enemiga") //Cmabiar por el nombre del recurso
                .snippet("NOTA ADICIONAL") //Agregar nota adicional
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))); //Color del marcador

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
