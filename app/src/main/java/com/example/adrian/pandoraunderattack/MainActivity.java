package com.example.adrian.pandoraunderattack;

import android.content.Intent;
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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Pantalla de inicio de sesion
 * @author Adrian Sanchez
 *
 */
public class MainActivity extends AppCompatActivity {
    private TextView nombre;
    private EditText cuadronombre;
    private Button boton;
    private TextView resp;
    private TextView clave;
    private EditText textclave;
    private Button registrar;
    public static String usuario;
    Conexion conectar=new Conexion();
    Gson gson=new Gson();

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nombre = (TextView) findViewById(R.id.lblnombre);
        cuadronombre = (EditText) findViewById(R.id.txtnombre);
        boton = (Button) findViewById(R.id.btnenviar);
        clave = (TextView) findViewById(R.id.lblclave);
        textclave = (EditText) findViewById(R.id.txtclave);
        registrar = (Button) findViewById(R.id.btnregistrarse);

        findViewById(R.id.btnregistrarse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Registrar.class));

            }
        });
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conectar.Leer();
                if (cuadronombre.getText().toString().equals("") || textclave.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this,"Debe ingresar todos los datos",Toast.LENGTH_LONG).show();
                }
                else {
                    JsonParser parser = new JsonParser();
                    JsonObject o = new JsonObject();
                    o.addProperty("tipo", "ingresar");
                    o.addProperty("nombre", String.valueOf(cuadronombre.getText()));
                    o.addProperty("clave", String.valueOf(textclave.getText()));
                    String enviar_mensaje = gson.toJson(o);
                    conectar.Escribir(enviar_mensaje);
                    while(conectar.Entrada()==null){
                        String respuesta = conectar.Entrada();
                    }
                    String respuesta = conectar.Entrada().toString();
                    JsonElement elemento = parser.parse(respuesta);
                    String respuestaIn = elemento.getAsJsonObject().get("estado").getAsString();
                    String estadoClan=elemento.getAsJsonObject().get("clan").getAsString();
                    Conexion.mensaje=null;
                    if (respuestaIn.equals("error")) {
                        Toast.makeText(MainActivity.this, "Usuario o contraseña incorrectos",
                                Toast.LENGTH_LONG).show();
                        cuadronombre.setText("");
                        textclave.setText("");
                    } else if(estadoClan.equals("si")){
                        Toast.makeText(MainActivity.this, "Este usuario si tiene clan",
                                Toast.LENGTH_LONG).show();
                        startActivity(new Intent(MainActivity.this, MapasActivity.class));
                        MainActivity.usuario= String.valueOf(cuadronombre.getText());
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Login Exitoso",
                                Toast.LENGTH_LONG).show();
                        MainActivity.usuario= String.valueOf(cuadronombre.getText());
                        cuadronombre.setText("");
                        textclave.setText("");
                        startActivity(new Intent(MainActivity.this, BuscarClan.class));
                    }
                    System.out.println(enviar_mensaje);

                }

            }

        });


    }

    /**
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

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