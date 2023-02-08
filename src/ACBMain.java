import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.Scanner;

public class ACBMain {

	public static void main(String[] args) throws IOException, SQLException, InterruptedException {

		/**
		 * Clase Main donde se ejecuta el programa.
		 * Se crea un objeto de la clase ACBMenu y se obtiene una instancia de la conexión con la base de datos mediante el método getInstance() de la clase ConnectionFactory.
		 * Luego se crea un objeto de la clase Tablas y se inicializa con la conexión c.
		 * Se muestra el menú principal con el método mainMenu() de la clase ACBMenu y se guarda en una variable de tipo entero llamada option.
		 * Se entra en un bucle while en el que se ejecutan diferentes métodos de la clase Tablas dependiendo de la opción elegida en el menú. Si la opción es 12, se sale del bucle.
		 * Si la opción elegida no es ninguna de las opciones válidas, se muestra un mensaje de error.
		 */

		ACBMenu menu = new ACBMenu();

		ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
		Connection c = connectionFactory.connect();

		Tablas tablas = new Tablas(c);

		int option = menu.mainMenu();
		while (option > 0 && option < 13) {
			switch (option) {
			case 1:
				tablas.crearTablas();
				break;

			case 2:
				tablas.rellenarTablas();
				break;

			case 3:
				tablas.eliminarTablas();
				break;

			case 4:
 				tablas.selectColumna();
				break;

			case 5:
				tablas.selectTabla();
				break;

			case 6:
				tablas.selectTextoConcreto();
				break;

			case 7:
				tablas.selectCondicion();
				break;

			case 8:
				tablas.modificarRegistro();
				break;

			case 9:
				tablas.borrarRegistro();
				break;

			case 10:
				tablas.borrarConjunto();
				break;

			case 11:
				tablas.eliminarUnaTabla();
				break;

				case 12:
					break;

			default:
				System.out.println("Introduce una de las opciones anteriores");
				break;


			}
			if (option == 12) {
				break;
			}
			option = menu.mainMenu();
		}

	}

}
