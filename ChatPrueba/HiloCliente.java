import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class HiloCliente extends Thread {

	private Socket socket;
	private Servidor servidor;
	private PrintWriter escritor;
	
	public HiloCliente( Socket so , Servidor se) {
		this.socket = so;
		this.servidor = se;
	}
	
	public void run() {
		
		try {
			
			InputStream input = socket.getInputStream();
			BufferedReader lector = new BufferedReader( new InputStreamReader( input ) );
			
			OutputStream output = socket.getOutputStream();
			escritor = new PrintWriter( output, true );
			
			imprimirUsuarios();
			
			String nombre = lector.readLine();
			servidor.agregarUsuario(nombre);
			
			String msjServidor = "Nuevo usuario conectado: "+ nombre;
			servidor.unoATodos(msjServidor, this);

			String msjCliente;
			
			do {
				msjCliente = lector.readLine();
				msjServidor = "[" + nombre + "]:" + msjCliente;
				servidor.unoATodos(msjServidor, this);
			} while ( !msjCliente.equals("Me voy") );
			
			servidor.eliminarUsuario(nombre, this);
			socket.close();
			msjServidor = nombre + " ha salido de la sala.";
			servidor.unoATodos(msjServidor, this);
			
		} catch (IOException e) {
			System.out.println(" Error en el cliente: " + e.getMessage() );
			e.printStackTrace();
		}
		
	}
	
	// Muestra los usuarios conectados al nuevo usuario.
	void imprimirUsuarios() {
		if( servidor.hayUsuariosConectados() ) {
			escritor.println("Usuarios conectados: " + servidor.getUsuarios() );
		} else {
			escritor.println("No hay ningun usuario conectado.");
		}
	}
	
	// Envia mensaje al cliente.
	void enviarMensaje( String msj ) {
		escritor.println( msj );
	}
}