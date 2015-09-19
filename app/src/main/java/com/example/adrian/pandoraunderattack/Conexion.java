package com.example.adrian.pandoraunderattack;

import com.google.gson.Gson;

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
    public static String mensaje;
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
                        sockete = new Socket("172.26.35.35", 8080);
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
                        if (lector == null) {

                        } else {
                            String valor = lector.readLine();
                            Conexion.mensaje = valor.toString();
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
    public String Entrada(){
//        System.out.println(Conexion.mensaje);
        return Conexion.mensaje;
    }


}
