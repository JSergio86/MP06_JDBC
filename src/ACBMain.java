import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.Scanner;

public class ACBMain {

	public static void main(String[] args) throws IOException, SQLException, ParseException, ClassNotFoundException {
		ACBMenu menu = new ACBMenu();
		Scanner sc = new Scanner(System.in);

		ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
		Connection c = connectionFactory.connect();

		Tablas tablas = new Tablas();

		Class.forName( "org.postgresql.Driver" );


		int option = menu.mainMenu();
		while (option > 0 && option < 12) {
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
				tablas.buscarElemento();
				break;

			case 7:
				break;

			case 8:
				break;

			case 9:
				tablas.eliminarUnaTabla();
				break;

			case 10:
				break;

			case 11:
				break;

			default:
				System.out.println("Introdueixi una de les opcions anteriors");
				break;

			}
			option = menu.mainMenu();
		}

	}

}