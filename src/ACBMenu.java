import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Identity;
import java.security.KeyManagementException;

public class ACBMenu {
	private int option;

	public ACBMenu() {
		super();
	}

	public int mainMenu() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		do {
			System.out.println(" \nMENU PRINCIPAL \n");

			System.out.println("1. Crear tablas automaticamente");
			System.out.println("2. Rellenar tablas automaticamente");
			System.out.println("3. Borrar tablas automaticamente");
			System.out.println("4. Listar una columna");
			System.out.println("5. Listar tabla");
			System.out.println("6. Listar texto/num concreto");
			System.out.println("7. Modificar registro");
			System.out.println("8. Eliminar un registro");
			System.out.println("9. Eliminar un conjunto de registros");
			System.out.println("10. Eliminar una tabla");
			System.out.println("11. Sortir");
			System.out.println("Esculli opció: ");
			try {
				option = Integer.parseInt(br.readLine());
			} catch (NumberFormatException | IOException e) {
				System.out.println("valor no vàlid");
				e.printStackTrace();

			}

		} while (option != 1 && option != 2 && option != 3 && option != 4 && option != 5 && option != 6 && option != 7
				&& option != 8 && option != 9 && option != 10);

		return option;
	}

}
