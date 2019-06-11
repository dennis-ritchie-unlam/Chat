package cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class HiloLeer extends Thread{

	private BufferedReader lector;
	private Socket socket;
	private Cliente cliente;
	
	public HiloLeer( Socket s, Cliente c ) {
		this.socket = s;
		this.cliente = c;
		
		try {
			InputStream i = socket.getInputStream();
			lector =  new BufferedReader( new InputStreamReader(i) );
		} catch (IOException e) {
			System.out.println("Error obteniendo el InputStream: "+ e.getMessage() );
			e.printStackTrace();
		}
	}
	
	public void run() {
		
		while( true ) {
			try { 
				String respuesta = lector.readLine();
				System.out.println("\n" + respuesta);
				
				if( cliente.getNombre() != null )
					System.out.println("["+cliente.getNombre()+"]: " );
			} catch( IOException e) {
				System.out.println("Error leyendo del servidor: "+e.getMessage());
				e.printStackTrace();
				break;
			}
		}
	}
}
