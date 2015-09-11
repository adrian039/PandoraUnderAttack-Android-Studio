package com.example.adrian.pandoraunderattack;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.app.AlertDialog;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
        textusuario = (EditText) findViewById(R.id.txtusuario);
        textclavereg = (EditText) findViewById(R.id.txtclavereg);
        textconfirmclave = (EditText) findViewById(R.id.txtconfirmclave);
        botonregistrar = (Button) findViewById(R.id.btnregistrar);
        conectar12.Conectar();
        conectar12.Leer();
        botonregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textusuario.getText().toString().equals("") || textclavereg.getText().toString().equals("")
                        || textconfirmclave.getText().toString().equals("")){
                    Toast.makeText(Registrar.this, "Debe ingresar todos los datos", Toast.LENGTH_LONG).show();
                }
                else if ((textclavereg.getText().toString().equals(textconfirmclave.getText().toString()))==false){
                    Toast.makeText(Registrar.this, "Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
                }
                else {
                    JsonParser parser = new JsonParser();
                    JsonObject o = new JsonObject();
                    o.addProperty("tipo", "registrar");
                    o.addProperty("nombre", String.valueOf(textusuario.getText()));
                    o.addProperty("clave", String.valueOf(textclavereg.getText()));
                    o.addProperty("clan", "");
                    String enviarUsuario = gson.toJson(o);
                    conectar12.Escribir(enviarUsuario);
                    String respuesta=String.valueOf(conectar12.Entrada());
                    JsonElement elemento = parser.parse(respuesta);
                    String respuestaIn=elemento.getAsJsonObject().get("estado").getAsString();
                    if(respuestaIn.equals("existe")){
                        Toast.makeText(Registrar.this, "Ya existe un usuario registrado con ese nombre",
                                Toast.LENGTH_LONG).show();
                        textusuario.setText("");
                        textclavereg.setText("");
                        textconfirmclave.setText("");
                    }
                    else{
                        Toast.makeText(Registrar.this, "Registro Completo",
                                Toast.LENGTH_LONG).show();
                        textusuario.setText("");
                        textclavereg.setText("");
                        textconfirmclave.setText("");
                    }

                }
                //else{
                  //  JsonObject o = new JsonObject();
                    //o.addProperty("tipo", "registrar");
                  //  o.addProperty("nombre", String.valueOf(textusuario.getText()));
                  //  o.addProperty("clave", String.valueOf(textclavereg.getText()));
                  //  o.addProperty("clan","");
                  //  String enviar_mensaje = gson.toJson(o);
                  //  conectar12.Escribir(enviar_mensaje);
                  //  textusuario.setText("");
                  //  textclavereg.setText("");
                 //   textconfirmclave.setText("");
               // }
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
