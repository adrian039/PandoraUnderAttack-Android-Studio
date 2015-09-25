package com.example.adrian.pandoraunderattack;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Pantalla de registro de un nuevo clan
 * @author Adrian Sanchez
 */
public class RegistrarClan extends MainActivity {
    private ImageView escudo;
    private EditText nombre;
    private Button registrar;
    Gson gson=new Gson();
    LocationManager admiUbi;
    Location ubicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_clan);
        escudo=(ImageView)findViewById(R.id.imgClan);
        nombre=(EditText)findViewById(R.id.txtClan);
        registrar=(Button)findViewById(R.id.btnRegClan);
        admiUbi = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            ubicacion = admiUbi.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        }catch (SecurityException e){
            Toast.makeText(RegistrarClan.this, "ERROR.", Toast.LENGTH_LONG).show();
        }

        final double Lat = ubicacion.getLatitude();
        final double Log = ubicacion.getLongitude();
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conectar.Leer();
                if(nombre.getText().toString().equals("")){
                    Toast.makeText(RegistrarClan.this, "Debe ingresar un nombre para el clan.", Toast.LENGTH_LONG).show();
                }
                else{
                    JsonParser parser = new JsonParser();
                    JsonObject o = new JsonObject();
                    o.addProperty("tipo", "registrarClan");
                    o.addProperty("nombre", String.valueOf(nombre.getText()));
                    o.addProperty("creador",String.valueOf(MainActivity.usuario));
                    o.addProperty("imagen","");
                    o.addProperty("reliquiaLat", Lat);
                    o.addProperty("reliquiaLng", Log);
                    o.addProperty("puntaje", 0);
                    String enviarClan = gson.toJson(o);
                    conectar.Escribir(enviarClan);
                    while(conectar.Entrada()==null){
                    }
                    JsonElement elemento = conectar.Entrada();
                    String respuestaIn = elemento.getAsJsonObject().get("estado").getAsString();
                    Conexion.mensaje=null;
                    if (respuestaIn.equals("existe")) {
                        Toast.makeText(RegistrarClan.this, "Ya existe un clan registrado con ese nombre",
                                Toast.LENGTH_LONG).show();
                        nombre.setText("");
                    } else {
                        Toast.makeText(RegistrarClan.this, "Registro Completo",
                                Toast.LENGTH_LONG).show();
                        nombre.setText("");
                        startActivity(new Intent(RegistrarClan.this, MapasActivity.class));

                    }
                }
            }
        });
        escudo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Seleccione su imagen"), 1);
            }
        });
    }

    public void onActivityResult(int reqCode, int resCode, Intent data){
        if (resCode == RESULT_OK){
            if (reqCode==1) {
                escudo.setImageURI(data.getData());
            }
        }
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
