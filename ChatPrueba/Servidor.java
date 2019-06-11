import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Servidor {

	private int puerto;
	private Set<String> usuarios = new HashSet<>();
	private Set<HiloCliente> clientes = new HashSet<>();
	
	public Servidor( int p ) {
		this.puerto = p;
	}
	
	public void ejecutar() {
		
		try( ServerSocket serversocket = new ServerSocket(puerto) ) {
			 System.out.println("Servidor funcionando en el puerto: "+puerto);
			 
			 while( true ) {
				 Socket socket = serversocket.accept();
				 System.out.println("Nuevo usuario se ha conectado");
				 
				 HiloCliente nuevo = new HiloCliente( socket, this );
				 clientes.add( nuevo );
				 nuevo.start();
			 }
			 
		} catch (IOException e) {
			System.err.println("Error en el servidor.");
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {

		if( args.length < 1 ) {
			System.out.println("Syntax: java Servidor < puerto >");
			System.exit(0);
		}
		
		int puerto = Integer.parseInt( args[0] );
		
		Servidor servidor = new Servidor(puerto);
		servidor.ejecutar();
	}

	// Envia msj de un usuario a todos.
	void unoATodos( String mensaje, HiloCliente cliente) {
		
		for( HiloCliente c : clientes ) {
			if( c != cliente ) {
				c.enviarMensaje( mensaje );
			}
		}
	}
	
	// Guarda nombre de usuario del nuevo cliente.
	void agregarUsuario( String nuevo ) {
		usuarios.add(nuevo);
	}
	
	// Elimina usuario cuando se va de los sets.
	void eliminarUsuario( String nombre, HiloCliente cliente ) {
		boolean seElimino = usuarios.remove(nombre);
		if( seElimino ) {
			clientes.remove(cliente);
			System.out.println("El usuario "+nombre+" ha abandonado la sala.");
		}
	}
	
	Set<String> getUsuarios(){
		return this.usuarios;
	}
	
	boolean hayUsuariosConectados() {
		return !( this.usuarios.isEmpty() );
	}
}
