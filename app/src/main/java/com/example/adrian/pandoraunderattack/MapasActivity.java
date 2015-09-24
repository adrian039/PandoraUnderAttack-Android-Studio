package com.example.adrian.pandoraunderattack;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
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
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Pantalla del mapa (pantalla principal del juego)
 * @author Esteban Agüero Pérez
 */
public class MapasActivity extends MainActivity {
    public static Button bnotificacion;
    //Declaración de variables
    private GoogleMap mapGoogle; //Objeto de tipo google map
    Gson gson=new Gson();
    private static int Recurso1; //Cantidades de recursos
    private static int Recurso2;
    private static int Recurso3;
    Notificaciones verificar=new Notificaciones();
    private static String Usuario=String.valueOf(MainActivity.usuario);
    private CameraUpdate zoom; //valor del zoom
    private LatLng coordenadas; //Objeto que almacenará los valores de la ubicacion
    private boolean buscarme=true;
    private Handler hiloBusqueda; //Hilo que actualiza las coordenadas #REVISAR NO FUNKA
    private Button marcador;
    private static int puntaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapas);

        bnotificacion=(Button)findViewById(R.id.bnotificacion);
        zoom = CameraUpdateFactory.zoomTo(30);
        verificar.Verificar(Usuario);
        //Creacion del view del mapa
        mapGoogle = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapa)).getMap();
        mapGoogle.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mapGoogle.setMyLocationEnabled(true);
        setCoordenadas();
        zoomUbicacion(coordenadas);


        try {
            setAtributos();
            addReliquia(getReliquia());
        }catch (Exception e){}

        //Listener para los markers
        mapGoogle.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            final private int Rango = 10;//Ajustar rango entre el recurso y nuestra unicacion actual

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
                        puntaje += 8;
                        Toast.makeText(getBaseContext(), "Añadiendo recursos...", Toast.LENGTH_LONG).show();
                        //circleRecurso.remove();
                        marker.remove();
                    } else if (marker.getTitle().equals("Oro")) {
                        Recurso2 += 100;
                        puntaje += 4;
                        Toast.makeText(getBaseContext(), "Añadiendo recursos...", Toast.LENGTH_LONG).show();
                        //circleRecurso.remove();
                        marker.remove();
                    } else if (marker.getTitle().equals("Hierro")) {
                        Recurso3 += 1000;
                        puntaje += 2;
                        Toast.makeText(getBaseContext(), "Añadiendo recursos...", Toast.LENGTH_LONG).show();
                        //circleRecurso.remove();
                        marker.remove();
                    } else if (marker.getTitle().equals("RELIQUIA")) {
                        Toast.makeText(getBaseContext(), "Esta es la reliquia del clan", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getBaseContext(), "Atrapaste la reliquia de otro clan", Toast.LENGTH_LONG).show();
                        puntaje += 1000;
                        marker.remove();
                    }

                }
                updateAtributos();
            }
        });

        //listener del boton recursos
        findViewById(R.id.bRecursos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapasActivity.this, RecursosActivity.class));
            }
        });

        //Boton de agregar recursos de prueba
        marcador = (Button) findViewById(R.id.addRecurso);
        marcador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCoordenadas();
                addRecurso(coordenadas.latitude, coordenadas.longitude, 3);
            }
        });
        findViewById(R.id.bChat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapasActivity.this, Chat.class));
            }
        });
        bnotificacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonObject o =new JsonObject();
                o.addProperty("tipo","SolNotificaion");
                o.addProperty("usuario",Usuario);
                String enviar_mensaje =gson.toJson(o);
                conectar.Escribir(enviar_mensaje);
            }
        });
    }

    public static int getRecurso1() {
        try {
            return Recurso1;
        }catch (Exception E){
            return 0;
        }
    }

    public static int getRecurso2() {
        try {
            return Recurso2;
        }catch (Exception E){
            return 0;
        }
    }

    public static int getRecurso3() {
        try {
            return Recurso3;
        }catch (Exception E){
            return 0;
        }
    }

    public static int getPuntaje(){
        return puntaje;
    }

    public static String getUsuario() {
        return Usuario;
    }

    public void setAtributos(){
        conectar.Leer();
        JsonParser parser = new JsonParser();
        JsonObject o = new JsonObject();
        o.addProperty("tipo", "recurso");
        o.addProperty("nombre", Usuario);
        String enviarClan = gson.toJson(o);
        conectar.Escribir(enviarClan);
        while(conectar.Entrada()==null){
            String respuesta = conectar.Entrada();
        }
        String respuesta = conectar.Entrada().toString();
        JsonElement elemento = parser.parse(respuesta);
        Recurso1 = elemento.getAsJsonObject().get("gemas").getAsInt();
        Recurso2 = elemento.getAsJsonObject().get("oro").getAsInt();
        Recurso3 = elemento.getAsJsonObject().get("hierro").getAsInt();
        puntaje = elemento.getAsJsonObject().get("puntaje").getAsInt();
        Conexion.mensaje=null;

    }

    public void updateAtributos(){
        conectar.Leer();
        JsonParser parser = new JsonParser();
        JsonObject o = new JsonObject();
        o.addProperty("tipo", "recursoUpdate");
        o.addProperty("nombre", getUsuario());
        o.addProperty("gemas", getRecurso1());
        o.addProperty("oro", getRecurso2());
        o.addProperty("hierro", getRecurso3());
        o.addProperty("puntaje",getPuntaje());
        String enviarClan = gson.toJson(o);
        conectar.Escribir(enviarClan);
        Conexion.mensaje=null;

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
                ubicacion = admiUbi.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
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

        final LatLng Posicion = new LatLng(Latitud, Longitud); //Crea objeto tipo LatLng para manejo de coordenadas
        zoomUbicacion(Posicion); //Zoom a la ubicacion del recurso
        final Circle circleRecurso; //Crea un circulo alrededor de el recurso

        /**circleRecurso=mapGoogle.addCircle(new CircleOptions() //Da caracteristicas al circulo
                .center(Posicion)
                .radius(1)
                .strokeColor(Color.RED)
                .fillColor(Color.BLUE));
        **/
        try{
            Bitmap bMapGemas = BitmapFactory.decodeResource(getResources(), R.drawable.gemas);
            Bitmap bMapOro = BitmapFactory.decodeResource(getResources(), R.drawable.oro);
            Bitmap bMapHierro = BitmapFactory.decodeResource(getResources(), R.drawable.hierro);

           if (Recurso==1){ //Recurso 1= ?
               Marker RecursoA = mapGoogle.addMarker(new MarkerOptions()
                       .position(Posicion)
                       .title("Gemas") //Cmabiar por el nombre del recurso
                       .snippet("Piedras de gran valor y difíciles de encontrar") //Agregar nota adicional
                       .icon(BitmapDescriptorFactory.fromBitmap(bMapGemas))); //Color del marcador

           }

           else if (Recurso==2){ //Recurso 2=?
               Marker RecursoB = mapGoogle.addMarker(new MarkerOptions()
                       .position(Posicion)
                       .title("Oro") //Cmabiar por el nombre del recurso
                       .snippet("NOTA ADICIONAL") //Agregar nota adicional
                     //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))); //Color del marcador
                       .icon(BitmapDescriptorFactory.fromBitmap(bMapOro))); //Color del marcador
           }

           else if (Recurso==3) { //Recurso 3=?
               Marker RecursoC = mapGoogle.addMarker(new MarkerOptions()
                       .position(Posicion)
                       .title("Hierro") //Cmabiar por el nombre del recurso
                       .snippet("NOTA ADICIONAL") //Agregar nota adiciona
                     //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))); //Color del marcador
                       .icon(BitmapDescriptorFactory.fromBitmap(bMapHierro))); //Color del marcador
           }
           else{
                //Agregar un else
           }
       }catch(Exception e){//Agregar exception
        }
    }

    public LatLng getReliquia(){
        conectar.Leer();
        JsonParser parser = new JsonParser();
        JsonObject o = new JsonObject();
        o.addProperty("tipo", "miClanReliquia");
        o.addProperty("nombre", Usuario);
        String enviarClan = gson.toJson(o);
        conectar.Escribir(enviarClan);
        while(conectar.Entrada()==null){
            String respuesta = conectar.Entrada();
        }
        String respuesta = conectar.Entrada().toString();
        JsonElement elemento = parser.parse(respuesta);
        double lat= elemento.getAsJsonObject().get("reliquiaLat").getAsDouble();
        double lng= elemento.getAsJsonObject().get("reliquiaLng").getAsDouble();
        Conexion.mensaje=null;
        LatLng reliquia=new LatLng(lat,lng);
        return reliquia;
    }
    /**
     * Añade la reliquia del clan que esta unido el cliente
     */
    private void addReliquia(LatLng reli){ //Revisar nombre
        //final double LatiReliquia=coordenadas.latitude;
        //final double LongiReliquia=coordenadas.longitude;
        Bitmap bMapReliquia = BitmapFactory.decodeResource(getResources(), R.drawable.reliquia);
        Marker ReliquiaClan = mapGoogle.addMarker(new MarkerOptions()
                .position(reli)
                .title("RELIQUIA") //Cmabiar por el nombre del recurso
                .snippet("NOTA ADICIONAL") //Agregar nota adicional
              //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))); //Color del marcador
                .icon(BitmapDescriptorFactory.fromBitmap(bMapReliquia))); //Color del marcador
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

    public void ChangeColor(String boton){
        if(boton.equals("notificaiones")) {
            bnotificacion.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        }
        else if(boton.equals("chat")){
            findViewById(R.id.bChat).setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        }
    }

    public void MostrarAvisos(String tipo, String Usuario){
        if(tipo.equals("solicitud")){
            Toast.makeText(MapasActivity.this,"Nueva solicitud de ingreso al clan de "+Usuario,Toast.LENGTH_LONG).show();
        }
        else if(tipo.equals("votacion")){
            Toast.makeText(MapasActivity.this, "Nueva decision para votar", Toast.LENGTH_LONG).show();
        }
    }

    public void ResponderNotificacion(JsonElement elemento){
        JsonObject entrada=elemento.getAsJsonObject();
        String tipo=entrada.getAsJsonObject().get("tipoNotificacion").toString();
        final String solicitante =entrada.getAsJsonObject().get("solicitante").toString();
        if(tipo.equals("solicitud")) {
            final String clan=entrada.getAsJsonObject().get("clan").toString();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder
                    .setTitle("Nueva Solicitud")
                    .setMessage(solicitante+" quiere unirse a tu clan")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            JsonObject o = new JsonObject();
                            o.addProperty("tipo", "respSolicitud");
                            o.addProperty("solicitante",solicitante);
                            o.addProperty("clan",clan);
                            o.addProperty("estado","aceptada");
                            String enviar_mensaje=gson.toJson(o);
                            conectar.Escribir(enviar_mensaje);
                            Toast.makeText(MapasActivity.this, "Solicitud Aceptada.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Denegar", null)
                    .show();
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
