package servidorchat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
 
public class ServidorChat {
    public static void main(String[] args) {        
        int maximoConexiones = 10;
        ServerSocket servidor = null; 
        Socket socket = null;
        MensajesChat mensajes = new MensajesChat();
        
        try {
            // Se crea el serverSocket
        	ArchivoDePropiedades arch = new ArchivoDePropiedades("config.properties");
        	arch.lectura();
        	int puerto = arch.getPuerto();
            servidor = new ServerSocket(puerto, maximoConexiones);
            System.out.println("Servidor funcionando en el puerto: "+puerto);
            while (true) {
                socket = servidor.accept();
                System.out.println("Nuevo usuario se ha conectado");
                ConexionCliente cc = new ConexionCliente(socket, mensajes);
                cc.start();
                
            }
        } catch (IOException ex) {
        	System.err.println("Error en el servidor.");
        	ex.printStackTrace();
        	
        } finally{
            try {
                socket.close();
                servidor.close();
            } catch (IOException ex) {
            }
        }
    }
}