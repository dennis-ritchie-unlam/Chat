import java.io.Console;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class HiloEscribir extends Thread{

	private PrintWriter escritor;
	private Socket socket;
	private Cliente cliente;
	
	public HiloEscribir( Socket s, Cliente c ) {
		this.socket = s;
		this.cliente = c;
		
		try {
			OutputStream o = socket.getOutputStream();
			escritor =  new PrintWriter( o, true);
		} catch (IOException e) {
			System.out.println("Error obteniendo el OutputStream: "+ e.getMessage() );
			e.printStackTrace();
		}
	}
	
	public void run() {
		
		Console consola = System.console();
		String nombre = consola.readLine("\nIngresa tu nombre: ");
		cliente.setNombre( nombre );
		escritor.println(nombre);
		
		String cadena;
		
		do {
			cadena = consola.readLine("["+nombre+"]: ");
			escritor.println( cadena );
		} while (!cadena.equals("Me voy"));
		
		try {
			socket.close();
		} catch (IOException e) {
			System.out.println("Error escribiendo en el servidor: "+e.getMessage());
		}
	}
}
