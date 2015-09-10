package com.example.adrian.pandoraunderattack;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.app.AlertDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Registrar extends Activity {
    private EditText textusuario;
    private  EditText textclavereg;
    private EditText textconfirmclave;
    private Button botonregistrar;
    Socket sockete = null;
    BufferedReader lector = null;
    PrintWriter escritor = null;
    Conexion conectar12=new Conexion();
    Gson gson=new Gson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        textusuario = (EditText) findViewById(R.id.txtnombre);
        textclavereg = (EditText) findViewById(R.id.txtclavereg);
        textconfirmclave = (EditText) findViewById(R.id.txtconfirmclave);
        botonregistrar = (Button) findViewById(R.id.btnregistrar);
        conectar12.Conectar();
        botonregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonObject o = new JsonObject();
                o.addProperty("tipo", "registrar");
                o.addProperty("nombre", "adrian");
                o.addProperty("clave","adrian12");
                String enviar_mensaje = gson.toJson(o);
                conectar12.Escribir(enviar_mensaje);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registrar, menu);
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
    public void mensaje(String mensaje_1){
        new AlertDialog.Builder(this)
                .setMessage(mensaje_1)
                .setPositiveButton("OK", null)
                .show();
    }

}
