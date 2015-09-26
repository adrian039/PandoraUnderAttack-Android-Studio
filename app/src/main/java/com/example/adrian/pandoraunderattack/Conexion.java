package com.example.adrian.pandoraunderattack;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
/**
 * Esta clase realiza la conexion por medio de un socket al servidor
 * @since 9/09/15.
 * @author Adrian Sanchez
 */
public class Conexion {
    public static JsonElement mensaje;
    public static Socket sockete = null;
    BufferedReader lector = null;
    PrintWriter escritor = null;
    Thread principal=null;
    String mensaje_in=null;
    Gson gson=new Gson();
    public Conexion(){
        Conectar();
    }

    /**
     * Se encarga de conectar el cliente al servidor
     */
    public void Conectar(){
        principal = new Thread(new Runnable() {
            public void run() {
                try {
                        if(sockete==null){
                        sockete = new Socket("192.168.100.4", 8080);
                        Leer();}

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        principal.start();
    }

    /**
     * Lee los datos que el servidor le envia
     */
    public void Leer(){
        Thread leer_hilo=new Thread(new Runnable(){
            public void run(){
                try{
                    lector=new BufferedReader(new InputStreamReader(sockete.getInputStream()));
                    System.out.println(lector.toString());
                    while(true){
                        JsonParser parser = new JsonParser();
                        String mensaje  = lector.readLine();
                        JsonElement elemento = parser.parse(mensaje);
                        String entrada = elemento.getAsJsonObject().get("tipo").getAsString();
                        if (lector == null) {
                        }
                        else if(entrada.equals("chat")){
                            String chat = elemento.getAsJsonObject().get("message").getAsString();
                            System.out.println(chat);
                            Chat.caja.setText(Chat.caja.getText().toString()+ "\n"+ chat + "\n");
                        }
                        else {

                            Conexion.mensaje = elemento;
                            System.out.print(Conexion.mensaje);

                        }
                    }

                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }

        });
        leer_hilo.start();

    }

    /**
     * Envia datos al servidor
     * @param dato
     */
    public void Escribir(String dato){

        try{
            escritor= new PrintWriter(sockete.getOutputStream(), true);
            escritor.println(dato);
            System.out.println(dato);
        }catch(Exception ex){
            ex.printStackTrace();
        }

    }
    public JsonElement Entrada(){
//        System.out.println(Conexion.mensaje);
        return Conexion.mensaje;
    }


}
