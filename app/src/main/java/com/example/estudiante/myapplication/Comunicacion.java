package com.example.estudiante.myapplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Observable;

/**
 * Created by estudiante on 29/03/17.
 */

public class Comunicacion extends Observable implements Runnable {
    private final int puerto = 5000;
    private MulticastSocket socket;
    private final String GROUP_ADDRESS = "224.2.2.2";
    private InetAddress ip;
    private int identificador;
    private boolean identificado;
    private static Comunicacion ref;

    public Comunicacion() {

        try {
            System.out.println("iniciando socket en el puerto:" + puerto);
            // INICIALIZA EL MULTICAST SE HABRE LA PUERTA
            socket = new MulticastSocket(puerto);
            System.out.println("uniendo al grupo" + GROUP_ADDRESS);
            // RETORNA EL IP DEL PC EN STRING
            ip = InetAddress.getByName(GROUP_ADDRESS);
            socket.joinGroup(ip);
            // ipGroup = InetAddress.getLocalHost().getHostAddress();

        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // CALCULO AUTOID
        // 1.SALUDAR AL GRUPO
        saludarGrupo();
        // ESPERA RESPUESTA DE LOS MIEMBROS

        try {
            // DEFINE CUANTO TIEMPO ESPERA
            socket.setSoTimeout(3000);// 3 SEGUNDOS
            // SI 'IDENTIFICADO' ES FALSO ENTONCES RECIBE RESPUESTA : Es decir aun no esta identificado indentifiquese
            while (!identificado) {
                recibeRespuesta();
            }
            // SI YA SE IDENTIFICO ENVIA UN MENSAJE CON SU ID AL GRUPO
            if(identificado){
                enviarMensaje(new AutoId("Mi ID es:"+identificador));
            }
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void saludarGrupo() {
        // TODO Auto-generated method stub
        System.out.println("hola soy un nuevo miembro");
        AutoId mensaje = new AutoId("hola soy un nuevo miembro");
        byte[] bytes = serializa(mensaje);
        try {
            enviarMensaje(bytes, ip, puerto);
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: handle exception
        }
    }

    private void recibeRespuesta() throws Exception {
        // TODO Auto-generated method stub
        try {
            // RECIVE
            // DatagramPacket recibePaquete = recibeMensaje();
            // DESERIALIZA
            Object recibeObjeto = recibeMensaje();
            // DETECTA SI EL QUE RECIBIO EL MENSAJE ES UN AUTOIDMENSAJE OBJETO
            if (recibeObjeto instanceof AutoId) {
                AutoId mensaje = (AutoId) recibeObjeto;
                String mensajeContenido = mensaje.getContenido();
                // SI ESTO ES UN MENSAJE RESPONDIDO
                if (mensajeContenido.contains("yo soy:")) {
                    // HOLA SOY:0//HOLA SOY:1
                    String[] partes = mensajeContenido.split(":");
                    int externoID = Integer.parseInt(partes[1]);
                    if (externoID >= identificador) {
                        identificador = externoID + 1;
                    }
                }

                if (mensajeContenido.contains("x:")) {
                    System.out.println(mensajeContenido);
                }

            }

        } catch (SocketTimeoutException e) {
            System.out.println("AutoId tiempo finalizado");
            if (identificador == 0) {
                identificador = 1;
            }
            identificado = true;
            // SE TERMINA EL TIEMPO DE ESPERA
            socket.setSoTimeout(0);
            System.out.println("Mi AutoId es:" + identificador);

            // TODO: handle exception
        }

    }

    private void respuestaSaludo() {
        AutoId mensaje = new AutoId("Hola, yo soy:" + identificador);
        byte[] bytes = serializa(mensaje);

        try {
            enviarMensaje(bytes, ip, puerto);
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: handle exception
        }
    }

    private byte[] serializa(Object data) {
        // TODO Auto-generated method stub
        byte[] bytes = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(data);
            bytes = baos.toByteArray();
            // CIERRA ARROYOS
            oos.close();

        } catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
            ;
        }
        return bytes;
    }

    private Object deserializa(byte[] bytes) {
        // TODO Auto-generated method stub
        Object data = null;

        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            data = ois.readObject();

            // CIERRA ARROYOS
            ois.close();

        } catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return data;
    }

    public void enviarMensaje(byte[] data, InetAddress ip2, int puerto2) throws IOException {
        DatagramPacket paquete = new DatagramPacket(data, data.length, ip2, puerto2);
        System.out.println("Enviando dato a:" + ip2.getHostAddress() + ":" + puerto2);
        socket.send(paquete);
        System.out.println("Los datos fueron enviados");

        // TODO Auto-generated method stub

    }

    public Object recibeMensaje() throws Exception {
        byte[] buffer = new byte[1024];
        DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);
        socket.receive(paquete);
        System.out.println("Data recibio de:" + paquete.getAddress() + ":" + paquete.getPort());

        ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
        ObjectInputStream ois = new ObjectInputStream(bais);
        Object data = ois.readObject();

        // CIERRA ARROYOS
        ois.close();
        return data;
    }

    public void run() {

        while (true) {

            try {
                // RECIBE
                // DatagramPacket paqueteRecibido = recibeMensaje();
                // DESERIALIZA
                Object objetoRecibido = recibeMensaje();
                // Object objetoRecibido =
                // deserializa(paqueteRecibido.getData());
                if (socket != null) {
                    // VALIDAR QUE NO HAYA ERRORES CON LOS DATOS POR QUE EL
                    // PROCESO DE DESEREALIZACION PUEDE DEVOLVER NULL
                    if (objetoRecibido != null) {

                        // SI OBJETO RECIBIDO ES UN AUTOID
                        if (objetoRecibido instanceof AutoId) {
                            AutoId mensaje = (AutoId) objetoRecibido;
                            String mensajeContenido = mensaje.getContenido();

                            // ESTAMOS INTERESADOS UNICAMENTE EN LOS NUEVOS
                            // MIEMBROS QUE NO SE HAN IDENTIFICADO
                            if (mensajeContenido.contains("nuevo miembro")) {
                                respuestaSaludo();
                            }
                            // SI NECESITAMOS VALIDAR OTRO TIPO DE OBJETOS ESTE
                            // ES EL MOMENTO
                            // NOTIFICAR A LOS AOBSERVADORES QUE HAN LLEGADO
                            // NUEVOS DATOS Y TRANSMITIRLE LOS DATOS

                            // If we need to validate other kind of objects this
                            // is
                            // the moment

                            // Notify the observers that new data has arrived
                            // and
                            // pass
                            // the data to them
                            setChanged();
                            notifyObservers(objetoRecibido);
                            clearChanged();
                        }

                    }
                }

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();

            }

        }
    }

    public int getIdentificador() {
        // TODO Auto-generated method stub
        return this.identificador;
    }

    public static Comunicacion getInstance(){
        if(ref==null){
            ref= new Comunicacion();
            new Thread(ref).start();
        }
        return ref;
    }

    public void enviarMensaje(Object mensaje) {
        try {
            System.out.println("Enviar mensaje a Jugadores");
            byte[] bytes = serializa(mensaje);
            enviarMensaje(bytes, ip, puerto);
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: handle exception
        }

    }
}
