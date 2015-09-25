package com.example.adrian.pandoraunderattack;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Chat extends AppCompatActivity {

    private Button enviar;
    public static TextView caja;
    public static EditText escribe;
    private Conexion conectar = new Conexion();
    Gson gson=new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        enviar = (Button) findViewById(R.id.sendbut);
        caja = (TextView) findViewById(R.id.cajachat);
        escribe = (EditText) findViewById(R.id.texto);

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conectar.Leer();
                if (escribe.getText().toString().equals("")){
                    Toast.makeText(Chat.this, "Ingrese un texto", Toast.LENGTH_SHORT).show();
                }
                else{
                    JsonParser parser = new JsonParser(); //Enviar mensajes
                    JsonObject o = new JsonObject();
                    o.addProperty("tipo", "mensaje1");
                    o.addProperty("message", String.valueOf(escribe.getText()));
                    o.addProperty("usuario", String.valueOf(MainActivity.usuario));
                    String enviar_msj = gson.toJson(o);
                    conectar.Escribir(enviar_msj);
                    Chat.escribe.setText("");
                }


            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
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
