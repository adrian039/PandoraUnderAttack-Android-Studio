package com.example.adrian.pandoraunderattack;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Esta es la pantalla que busca un clan para unirse
 * @author Adrian Sanchez
 */
public class BuscarClan extends MainActivity {
    private EditText clan;
    private Button unirse;
    private Button crear;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_clan);
        clan = (EditText) findViewById(R.id.txtNomClan);
        unirse = (Button) findViewById(R.id.btnUnirse);
        crear = (Button) findViewById(R.id.btnCrear);
        findViewById(R.id.btnCrear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BuscarClan.this, RegistrarClan.class));
            }
        });
        unirse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clan.getText().toString().equals("")){
                    Toast.makeText(BuscarClan.this, "Debe ingresar un nombre de clan.", Toast.LENGTH_LONG).show();
                }
                else{
                    JsonParser parser = new JsonParser();
                    JsonObject o = new JsonObject();
                    o.addProperty("tipo", "buscarClan");
                    o.addProperty("clan", String.valueOf(clan.getText()));
                    o.addProperty("usuario",String.valueOf(MainActivity.usuario));
                    String enviarSolicitud = gson.toJson(o);
                    conectar.Escribir(enviarSolicitud);
                    while(conectar.Entrada()==null){
                        String respuesta = conectar.Entrada();
                    }
                    String respuesta = conectar.Entrada().toString();
                    JsonElement elemento = parser.parse(respuesta);
                    String respuestaIn = elemento.getAsJsonObject().get("estado").getAsString();
                    Conexion.mensaje=null;
                    if (respuestaIn.equals("error")) {
                        Toast.makeText(BuscarClan.this, "No existe un clan registrado con ese nombre",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(BuscarClan.this, "Solicitud enviada correctamente",
                                Toast.LENGTH_LONG).show();
                        clan.setText("");

                    }
                }
            }
        });

    }


    /**
     *
     * @param item
     * @return
     */
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
