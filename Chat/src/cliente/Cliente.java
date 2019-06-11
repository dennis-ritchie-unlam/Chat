package cliente;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Cliente {

	private String nombreHost;
	private String nombre;
	private int puerto;

	public Cliente( String n, int p ) {
		this.nombreHost = n;
		this.puerto = p;
	}
	
	public void ejecutar() {
		
		try {
			Socket socket = new Socket(nombreHost, puerto);
			System.out.println("Conectado existosamente al servidor.");
			new HiloLeer( socket, this).start();
			new HiloEscribir( socket, this).start();
		} catch (UnknownHostException e) {
			System.out.println("No se ha encontrado el servidor: "+e.getMessage());
		} catch (IOException e) {
			System.out.println("IO error: "+e.getMessage());
		}
	}
	
	public static void main(String[] args) {

		if(args.length < 2)
			return;
		
		String host = args[0];
		int puerto = Integer.parseInt(args[1]);
		Cliente cliente = new Cliente(host, puerto);
		cliente.ejecutar();
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
