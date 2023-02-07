import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Tablas {
    Connection conn;
    Statement st;
    PreparedStatement pr;

    public Tablas(Connection c) {
        this.conn = c;

        try {
            this.st = this.conn.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Método que permite crear las tablas a partir de un archivo "schema.sql".
     * Si no existe el archivo o hay un error en el proceso, se muestra un mensaje de error.
     */
    public void crearTablas(){
        try (BufferedReader br = new BufferedReader(new FileReader("src/schema.sql"))) {
            pr = conn.prepareStatement(br.lines().collect(Collectors.joining(" \n")));
            pr.execute();
            System.out.println("Se han creado correctamente");
            Thread.sleep(1000);
        }catch (Exception e){
            System.out.println("Comprueba el fichero schema.sql: " + e.getMessage());
        }
    }

    /**
     * Método para rellenar las tablas en la base de datos.
     *
     * Este método llama a los métodos rellenarTablaJugadores(),
     * rellenarTablaArmas(), rellenarTablaMapas(), rellenarTablaPartidas(),
     * rellenarTablaAgentes(), rellenarTablaPlayerWeapons() y
     * rellenarTablaPlayerAgentes() para rellenar cada una de las tablas.
     *
     * En caso de error, se lanza una excepción IOException o SQLException.
     *
     * @throws IOException Si ocurre un error al procesar el archivo de entrada.
     * @throws SQLException Si ocurre un error al acceder a la base de datos.
     */
    public void rellenarTablas() throws IOException, SQLException, InterruptedException {

        rellenarTablaJugadores();
        rellenarTablaMapas();
        rellenarTablaArmas();
        rellenarTablaPartidas();
        rellenarTablaAgentes();
        rellenarTablaPlayerWeapons();
        rellenarTablaPlayerAgentes();
        System.out.println("Se han rellenado correctamente");
        Thread.sleep(1000);
    }

    /**
     * Elimina todas las tablas jugadores, mapas, armas, partidas, agentes, playeragentes y playerweapons de la base de datos, en cascada.
     * En caso de no poder eliminar las tablas, muestra un mensaje de error con el mensaje de la excepción lanzada.
     */
    public void eliminarTablas()  {
        try {
            pr = conn.prepareStatement("DROP TABLE jugadores,mapas, armas, partidas, agentes, playeragentes,playerweapons CASCADE");
            pr.executeUpdate();
            System.out.println("Se han eliminado correctamente");
            Thread.sleep(1000);
        }catch (Exception e){
            System.out.println("No se puede eliminar todas las tablas, mira a ver si existen: " + e.getMessage());

        }
    }

    /**
     * Muestra los datos de una columna específica de una tabla específica de la base de datos.
     * Pide al usuario escribir el nombre de la tabla y la columna que quiere buscar.
     * En caso de haber escrito mal el nombre de la tabla o de la columna, muestra un mensaje de error con el mensaje de la excepción lanzada.
     */
    public void selectColumna() {
        try{
            Scanner sc = new Scanner(System.in);
            System.out.println("Escribe la tabla que quieres buscar: jugadores,mapas,partidas,agentes,armas,playeragentes,playerweapons,");
            String tabla = sc.next();
            ResultSet rs2= st.executeQuery("SELECT *"+ " FROM "+ tabla);
            ResultSetMetaData metaData = rs2.getMetaData();
            int columnCount = metaData.getColumnCount();
            System.out.println();

            // Imprimir el nombre de cada columna
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(metaData.getColumnName(i) + " ");
            }
            System.out.println();

            // Recorrer cada fila del ResultSet
            while (rs2.next()) {
                // Recorrer cada columna de la fila actual
                for (int i = 1; i <= columnCount; i++) {
                    // Imprimir el valor de cada columna
                    System.out.print(rs2.getString(i) + " ");
                }
                System.out.println();
            }
            rs2.close();
            System.out.println();
            System.out.println("Escribe la columna que quieres buscar");

            String columna = sc.next();
            System.out.println();

            ResultSet rs = st.executeQuery("SELECT "+ columna  + " FROM "+ tabla);
            while (rs.next()) {
                System.out.println(rs.getString(columna));
            }
            rs.close();
            Thread.sleep(1000);

        }catch (Exception e){
            System.out.println("Comprueba que se ha escrito bien: " + e.getMessage());

        }
    }

    /**
     * Este método permite seleccionar una tabla completa de una base de datos y mostrar su contenido en consola.
     * @throws SQLException si hay un error con la base de datos.
     */
    public void selectTabla() {
        try{
            Scanner sc = new Scanner(System.in);
            System.out.println("Escribe la tabla que quieres buscar: jugadores,mapas,partidas,agentes,armas,playeragentes,playerweapons,");
            String tabla = sc.next();
            System.out.println();

            ResultSet rs = st.executeQuery("SELECT *"+ " FROM "+ tabla);
            while (rs.next()) {
                // Recorrer cada columna de la fila actual
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    // Imprimir el valor de cada columna
                    System.out.print(rs.getString(i) + " ");
                }
                System.out.println();
            }
            Thread.sleep(1000);

            rs.close();

        }catch (Exception e){
            System.out.println("Comprueba que existe la tabla " + e.getMessage());

        }
    }

    /**
     * Método que permite leer una tabla y una columna concreta de una base de datos.
     * El usuario especifica la tabla y la columna que desea leer, así como una condición para filtrar los resultados.
     * Si la tabla y la columna existen y cumplen con la condición, se mostrarán los valores de los elementos de la columna.
     * En caso contrario, se informará al usuario de un error.
     */
    public void selectTextoConcreto() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Escribe la tabla que quieres buscar: jugadores,mapas,partidas,agentes,armas,playeragentes,playerweapons,");
            String tabla = sc.next();

            ResultSet rs= st.executeQuery("SELECT *"+ " FROM "+ tabla);
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            System.out.println();

            // Imprimir el nombre de cada columna
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(metaData.getColumnName(i) + " ");
            }
            System.out.println();

            // Recorrer cada fila del ResultSet
            while (rs.next()) {
                // Recorrer cada columna de la fila actual
                for (int i = 1; i <= columnCount; i++) {
                    // Imprimir el valor de cada columna
                    System.out.print(rs.getString(i) + " ");
                }
                System.out.println();
            }
            rs.close();
            System.out.println();
            System.out.println("Que columna quieres leer?");
            String columna = sc.next();
            System.out.println("Quieres poner una letra o un numero?. 1-Letra 2-Numero");
            int opcion=sc.nextInt();
            String sql = null;
            if(opcion==1){
                System.out.println("Pon una letra para listar todos los que comiencen por ese numero");
                String letra = sc.next();
                sql="SELECT * FROM " + tabla + " WHERE " + columna + " LIKE '%" + letra + "%'";
            }
            if(opcion==2){
                System.out.println("Pon una numero para listar todos los que comiencen por ese numero");
                int numero = sc.nextInt();
                sql = "SELECT * FROM " + tabla + " WHERE CAST("+ columna +" AS TEXT) LIKE '"+numero+"%'";
            }
            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet resultSet = ps.executeQuery();
            System.out.println();
            while (resultSet.next()) {
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    // Imprimir el valor de cada columna
                    System.out.print(resultSet.getString(i) + " ");
                }
                System.out.println();
            }
            resultSet.close();
            Thread.sleep(1000);

        }catch (Exception e){
            System.out.println("Comprueba la información proporcionada a la base de datos: " + e.getMessage());

        }

    }

    /**
     * Este método permite seleccionar todos los elementos de una tabla que cumplen una determinada condición, especificada por el usuario.
     * El usuario introduce el nombre de la tabla que quiere buscar, la columna que quiere usar para la condición, el operador que quiere usar y el valor que quiere usar para la condición. Luego, se crea una sentencia preparada con esta información y se ejecuta.
     * En caso de que haya un error al ejecutar la consulta, se muestra un mensaje con el error.
     */
    public void selectCondicion() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Escribe la tabla que quieres buscar: jugadores,mapas,partidas,agentes,armas,playeragentes,playerweapons,");
            String tabla = sc.nextLine();
            ResultSet rs= st.executeQuery("SELECT *"+ " FROM "+ tabla);
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            System.out.println();

            // Imprimir el nombre de cada columna
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(metaData.getColumnName(i) + " ");
            }
            System.out.println();

            // Recorrer cada fila del ResultSet
            while (rs.next()) {
                // Recorrer cada columna de la fila actual
                for (int i = 1; i <= columnCount; i++) {
                    // Imprimir el valor de cada columna
                    System.out.print(rs.getString(i) + " ");
                }
                System.out.println();
            }
            rs.close();
            System.out.println();
            System.out.println("Introduce la columna que quieres usar para la condicion: ");
            String column = sc.nextLine();
            System.out.println("Introduce el operador que quieres usar (=, <, >, <=, >=, <>)");
            String operator = sc.nextLine();
            System.out.println("Introduce el valor que quieres usar para la condición: ");
            String value = sc.nextLine();

            PreparedStatement statement = conn.prepareStatement("SELECT * FROM " + tabla + " WHERE " + column + " " + operator + " " + value);
            ResultSet rs2 = statement.executeQuery();
            ResultSetMetaData metaData2 = rs2.getMetaData();
            int columnCount2 = metaData2.getColumnCount();

            System.out.println();
            for (int i = 1; i <= columnCount2; i++) {
                System.out.print(metaData2.getColumnName(i) +" ");

            }
            System.out.println();

            // Recorrer cada fila del ResultSet
            while (rs2.next()) {
                for (int i = 1; i <= rs2.getMetaData().getColumnCount(); i++) {
                    // Imprimir el valor de cada columna
                    System.out.print(rs2.getString(i) + " ");
                }
                System.out.println();
            }
            Thread.sleep(1000);


        } catch (SQLException e) {
            System.out.println("Ocurrió un error: " + e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Modifica un registro en la base de datos.
     * Este método permite modificar un registro en la base de datos seleccionada.
     * El usuario introduce el nombre de la tabla, la columna y el nuevo valor a modificar.
     * Además, se requiere también el nombre de la columna y el valor que se utilizarán para identificar la fila a modificar.
     */
    public void modificarRegistro() {
        try{
            Scanner sc = new Scanner(System.in);
            System.out.println("Escribe la tabla que quieres modificar: jugadores,mapas,partidas,agentes,armas,playeragentes,playerweapons,");
            String table = sc.nextLine();
            ResultSet rs= st.executeQuery("SELECT *"+ " FROM "+ table);
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            System.out.println();

            // Imprimir el nombre de cada columna
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(metaData.getColumnName(i) + " ");
            }
            System.out.println();

            // Recorrer cada fila del ResultSet
            while (rs.next()) {
                // Recorrer cada columna de la fila actual
                for (int i = 1; i <= columnCount; i++) {
                    // Imprimir el valor de cada columna
                    System.out.print(rs.getString(i) + " ");
                }
                System.out.println();
            }
            System.out.println();
            System.out.println("Introduce el nombre de la columna que quieres modificar: ");
            String column = sc.nextLine();
            System.out.println("Introduce el nuevo valor: ");
            String value = sc.nextLine();
            System.out.println("Introduce la columna de al lado des de donde se quiere modificar ");
            String columna = sc.nextLine();
            System.out.println("Introduce el valor de la columna anterior que esta en la misma linea des de donde se quiere modificar ");
            String valor = sc.nextLine();

            // Crear la sentencia preparada
            String sql = "UPDATE " + table + " SET " + column + " = "+"\'"+value+"\'"+" WHERE " + columna+" = "+"\'"+valor+"\'";
            PreparedStatement statement = conn.prepareStatement(sql);

            statement.executeUpdate();

            // Ejecutar la consulta
            int rows = statement.executeUpdate();
            System.out.println("Se han modificado " + rows + " fila(s).");
            Thread.sleep(1000);

        }catch (Exception e){
            System.out.println("Comprueba la información proporcionada a la base de datos: " + e.getMessage());

        }

    }

    /**
     * Este método permite borrar un registro en una tabla específica de una base de datos.
     * La sentencia SQL se construye y se ejecuta para eliminar el registro correspondiente.
     * En caso de éxito, se informa al usuario que el registro se ha eliminado correctamente,de lo contrario se informa un mensaje de error.
     */
    public void borrarRegistro() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Escribe la tabla que quieres borrar: jugadores,mapas,partidas,agentes,armas,playeragentes,playerweapons,");
            String tabla = sc.nextLine();

            ResultSet rs= st.executeQuery("SELECT *"+ " FROM "+ tabla);
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            System.out.println();

            // Imprimir el nombre de cada columna
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(metaData.getColumnName(i) + " ");
            }
            System.out.println();

            // Recorrer cada fila del ResultSet
            while (rs.next()) {
                // Recorrer cada columna de la fila actual
                for (int i = 1; i <= columnCount; i++) {
                    // Imprimir el valor de cada columna
                    System.out.print(rs.getString(i) + " ");
                }
                System.out.println();
            }
            System.out.println();
            System.out.println("Introduce el nombre de la columna donde quieres eliminar: ");
            String columna = sc.nextLine();
            System.out.println("Introduce el valor que desea eliminar (se eliminaran todos que tengan el mismo valor): ");
            String valor = sc.nextLine();
            //"\'"

            // Crear la sentencia preparada
            String sql = "DELETE FROM "+tabla+" WHERE "+columna+" = "+ "\'"+valor+"\'";
            pr = conn.prepareStatement(sql);

            pr.executeUpdate();
            System.out.println("Se ha eliminado correctamente");
            Thread.sleep(1000);


        }catch (Exception e){
            System.out.println("No se ha podido eliminar: " + e.getMessage());

        }

    }

    /**
     * Este método permite borrar un conjunto de registros en una tabla específica de una base de datos.
     * La sentencia SQL se construye y se ejecuta para eliminar los registros correspondientes.
     * Se informa al usuario el número de filas eliminadas. En caso de error, se informa un mensaje correspondiente.
     */
    public void borrarConjunto() throws SQLException {
        Scanner sc = new Scanner(System.in);

        System.out.println("Escribe la tabla que quieres borrar: jugadores,mapas,partidas,agentes,armas,playeragentes,playerweapons,");
        String tabla = sc.nextLine();
        ResultSet rs= st.executeQuery("SELECT *"+ " FROM "+ tabla);
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        System.out.println();

        // Imprimir el nombre de cada columna
        for (int i = 1; i <= columnCount; i++) {
            System.out.print(metaData.getColumnName(i) + " ");
        }
        System.out.println();

        // Recorrer cada fila del ResultSet
        while (rs.next()) {
            // Recorrer cada columna de la fila actual
            for (int i = 1; i <= columnCount; i++) {
                // Imprimir el valor de cada columna
                System.out.print(rs.getString(i) + " ");
            }
            System.out.println();
        }
        System.out.println();

        System.out.println("Ingrese la columna objetivo: ");
        String columnName = sc.nextLine();

        System.out.println("Introduce el operador que quieres usar (=, <, >, <=, >=, <>)");
        String operador = sc.nextLine();

        System.out.println("Ingrese el valor común del conjunto: ");
        String valor = sc.nextLine();


        try {
            String sql = "DELETE FROM " + tabla + " WHERE " + columnName +" "+ operador +" "+ valor;

            PreparedStatement pr1 = conn.prepareStatement(sql);
            int rowsDeleted = pr1.executeUpdate();

            System.out.println(rowsDeleted + " filas eliminadas");
            Thread.sleep(1000);

        } catch (SQLException | InterruptedException e) {
            System.out.println("Ocurrió un error: " + e.getMessage());
        }
    }

    /**
     * Este metodo permite eliminar la tabla que le pasa el usuario.
     */
    public void eliminarUnaTabla(){
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Escribe la tabla que quieres borrar: jugadores,mapas,partidas,agentes,armas,playeragentes,playerweapons,");
            String tabla = sc.next();

            pr = conn.prepareStatement("DROP TABLE "+tabla+" CASCADE");
            pr.executeUpdate();
            System.out.println("La tabla ha sido eliminada correctamente");
            Thread.sleep(1000);

        }catch (Exception e){
            System.out.println("No se puede eliminar esta tabla, mira a ver si existe: " + e.getMessage());

        }

    }

    /**
     * Método para rellenar las tabla jugadores en la base de datos.
     * @throws IOException
     * @throws SQLException
     */
    public void rellenarTablaJugadores() throws IOException, SQLException {
        String csvFile = "src/CSV/Jugador.csv";
        String line = "";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String sql = "INSERT INTO jugadores VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?) ON CONFLICT DO NOTHING;";

            pr = conn.prepareStatement(sql);
            br.readLine();  // Salta la primera línea
            while ((line = br.readLine()) != null) {
                pr.clearParameters();
                String[] data = line.split(",");
                pr.setInt(1, Integer.parseInt(data[0].replace("\"", "")));
                pr.setString(2, data[1].replace("\"", ""));
                pr.setInt(3, Integer.parseInt(data[2].replace("\"", "")));
                pr.setInt(4, Integer.parseInt(data[3].replace("\"", "")));
                pr.setInt(5, Integer.parseInt(data[4].replace("\"", "")));
                pr.setInt(6, Integer.parseInt(data[5].replace("\"", "")));
                pr.setFloat(7, Float.parseFloat(data[6].replace("\"", "")));
                pr.setFloat(8, Float.parseFloat(data[7].replace("\"", "")));
                pr.setFloat(9, Float.parseFloat(data[8].replace("\"", "")));
                pr.setInt(10, Integer.parseInt(data[9].replace("\"", "")));
                pr.setInt(11, Integer.parseInt(data[10].replace("\"", "")));
                pr.setInt(12, Integer.parseInt(data[11].replace("\"", "")));
                pr.setInt(13, Integer.parseInt(data[12].replace("\"", "")));
                pr.setInt(14, Integer.parseInt(data[13].replace("\"", "")));

                pr.executeUpdate();
            }
        }
    }

    /**
     * Método para rellenar las tabla mapas en la base de datos.
     * @throws IOException
     * @throws SQLException
     */
    public void rellenarTablaMapas() throws IOException, SQLException {
        String csvFile = "src/CSV/Mapas.csv";
        String line = "";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String sql = "INSERT INTO mapas VALUES (?,?,?,?,?,?,?,?) ON CONFLICT DO NOTHING;";

            pr = conn.prepareStatement(sql);
            br.readLine();  // Salta la primera línea
            while ((line = br.readLine()) != null) {
                pr.clearParameters();
                String[] data = line.split(",");
                pr.setInt(1, Integer.parseInt(data[0].replace("\"", "")));
                pr.setString(2, data[1].replace("\"", ""));
                pr.setString(3, data[2].replace("\"", ""));
                pr.setInt(4, Integer.parseInt(data[3].replace("\"", "")));
                pr.setInt(5, Integer.parseInt(data[4].replace("\"", "")));
                pr.setDouble(6, Double.parseDouble(data[5].replace("\"", "")));
                pr.setDouble(7, Double.parseDouble(data[6].replace("\"", "")));
                pr.setDouble(8, Double.parseDouble(data[7].replace("\"", "")));

                pr.executeUpdate();
            }
        }
    }

    /**
     * Método para rellenar las tabla partidas en la base de datos.
     * @throws IOException
     * @throws SQLException
     */
    public void rellenarTablaPartidas() throws IOException, SQLException {
        String csvFile = "src/CSV/Partidas.csv";
        String line = "";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String sql = "INSERT INTO partidas VALUES (?,?,?,?,?) ON CONFLICT DO NOTHING;";

            pr = conn.prepareStatement(sql);
            br.readLine();  // Salta la primera línea
            while ((line = br.readLine()) != null) {
                pr.clearParameters();
                String[] data = line.split(",");
                pr.setInt(1, Integer.parseInt(data[0].replace("\"", "")));
                pr.setInt(2, Integer.parseInt(data[1].replace("\"", "")));
                pr.setInt(3, Integer.parseInt(data[2].replace("\"", "")));
                pr.setString(4, data[3].replace("\"", ""));
                pr.setString(5, data[4].replace("\"", ""));

                pr.executeUpdate();
            }
        }
    }

    /**
     * Método para rellenar las tabla agentes en la base de datos.
     * @throws IOException
     * @throws SQLException
     */
    public void rellenarTablaAgentes() throws IOException, SQLException {
        String csvFile = "src/CSV/Agentes.csv";
        String line = "";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String sql = "INSERT INTO agentes VALUES (?,?,?) ON CONFLICT DO NOTHING;";

            pr = conn.prepareStatement(sql);
            br.readLine();  // Salta la primera línea
            while ((line = br.readLine()) != null) {
                pr.clearParameters();
                String[] data = line.split(",");
                pr.setInt(1, Integer.parseInt(data[0].replace("\"", "")));
                pr.setString(2, data[1].replace("\"", ""));
                pr.setString(3, data[2].replace("\"", ""));

                pr.executeUpdate();
            }
        }
    }

    /**
     * Método para rellenar las tabla playeragentes en la base de datos.
     * @throws IOException
     * @throws SQLException
     */
    public void rellenarTablaPlayerAgentes() throws IOException, SQLException {
        String csvFile = "src/CSV/PlayerAgentes.csv";
        String line = "";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String sql = "INSERT INTO playeragentes VALUES (?,?,?,?,?,?,?,?,?,?);";

            pr = conn.prepareStatement(sql);
            br.readLine();  // Salta la primera línea
            while ((line = br.readLine()) != null) {
                pr.clearParameters();
                String[] data = line.split(",");
                pr.setInt(1, Integer.parseInt(data[0].replace("\"", "")));
                pr.setInt(2, Integer.parseInt(data[1].replace("\"", "")));
                pr.setString(3, data[2].replace("\"", ""));
                pr.setInt(4, Integer.parseInt(data[3].replace("\"", "")));
                pr.setString(5, data[4].replace("\"", ""));
                pr.setFloat(6, Float.parseFloat(data[5].replace("\"", "")));
                pr.setFloat(7, Float.parseFloat(data[6].replace("\"", "")));
                pr.setFloat(8, Float.parseFloat(data[7].replace("\"", "")));
                pr.setString(9, data[8].replace("\"", ""));
                pr.setString(10, data[9].replace("\"", ""));

                pr.executeUpdate();
            }
        }
    }

    /**
     * Método para rellenar las tabla armas en la base de datos.
     * @throws IOException
     * @throws SQLException
     */
    public void rellenarTablaArmas() throws IOException, SQLException {
        String csvFile = "src/CSV/Armas.csv";
        String line = "";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String sql = "INSERT INTO armas VALUES (?,?,?) ON CONFLICT DO NOTHING;";

            pr = conn.prepareStatement(sql);
            br.readLine();  // Salta la primera línea
            while ((line = br.readLine()) != null) {
                pr.clearParameters();
                String[] data = line.split(",");
                pr.setInt(1, Integer.parseInt(data[0].replace("\"", "")));
                pr.setString(2, data[1].replace("\"", ""));
                pr.setString(3, data[2].replace("\"", ""));

                pr.executeUpdate();
            }
        }
    }

    /**
     * Método para rellenar las tabla playerweapons en la base de datos.
     * @throws IOException
     * @throws SQLException
     */
    public void rellenarTablaPlayerWeapons() throws IOException, SQLException {
        String csvFile = "src/CSV/PlayerWeapon.csv";
        String line = "";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String sql = "INSERT INTO playerweapons VALUES (?,?,?,?,?,?,?,?) ON CONFLICT DO NOTHING;";

            pr = conn.prepareStatement(sql);
            br.readLine();  // Salta la primera línea
            while ((line = br.readLine()) != null) {
                pr.clearParameters();
                String[] data = line.split(",");
                pr.setInt(1, Integer.parseInt(data[0].replace("\"", "")));
                pr.setInt(2, Integer.parseInt(data[1].replace("\"", "")));
                pr.setInt(3, Integer.parseInt(data[2].replace("\"", "")));
                pr.setInt(4, Integer.parseInt(data[3].replace("\"", "")));
                pr.setString(5, data[4].replace("\"", ""));
                pr.setFloat(6, Float.parseFloat(data[5].replace("\"", "")));
                pr.setFloat(7, Float.parseFloat(data[6].replace("\"", "")));
                pr.setString(8, data[7].replace("\"", ""));


                pr.executeUpdate();
            }
        }
    }


}
