package servidorchat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

public class ConexionCliente extends Thread implements Observer {

	private Socket socket;
	private MensajesChat mensajes;
	private DataInputStream entradaDatos;
	private DataOutputStream salidaDatos;

	public ConexionCliente(Socket socket, MensajesChat mensajes) {
		this.socket = socket;
		this.mensajes = mensajes;

		try {
			entradaDatos = new DataInputStream(socket.getInputStream());
			salidaDatos = new DataOutputStream(socket.getOutputStream());
		} catch (IOException ex) {
		}
	}

	@Override
	public void run() {
		String mensajeRecibido;
		boolean conectado = true;
		// Se apunta a la lista de observadores de mensajes
		mensajes.addObserver(this);

		while (conectado) {
			try {
				// Lee un mensaje enviado por el cliente
				mensajeRecibido = entradaDatos.readUTF();
				// Pone el mensaje recibido en mensajes para que se notifique
				// a sus observadores que hay un nuevo mensaje.
				mensajes.setMensaje(mensajeRecibido);
			} catch (IOException ex) {
				conectado = false;
				// Si se ha producido un error al recibir datos del cliente se cierra la
				// conexion con el.
				try {
					entradaDatos.close();
					salidaDatos.close();
				} catch (IOException ex2) {
				}
			}
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		try {
			// Envia el mensaje al cliente
			salidaDatos.writeUTF(arg.toString());
		} catch (IOException ex) {
		}
	}
}