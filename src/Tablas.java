import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Tablas {
    String dbURL="jdbc:postgresql://192.168.22.121:5432/jdbc";
    Connection conn;
    {
        try {
            conn = DriverManager.getConnection( dbURL, "sergio","password");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    Statement st;

    {
        try {
            st = conn.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    PreparedStatement pr;
    /**
     * Método que permite crear las tablas a partir de un archivo "schema.sql".
     * Si no existe el archivo o hay un error en el proceso, se muestra un mensaje de error.
     */
    public void crearTablas(){
        try (BufferedReader br = new BufferedReader(new FileReader("src/schema.sql"))) {
            pr = conn.prepareStatement(br.lines().collect(Collectors.joining(" \n")));
            pr.execute();
            System.out.println("Se han creado correctamente");
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
    public void rellenarTablas() throws IOException, SQLException {

        rellenarTablaJugadores();
        rellenarTablaArmas();
        rellenarTablaMapas();
        rellenarTablaPartidas();
        rellenarTablaAgentes();
        rellenarTablaPlayerWeapons();
        rellenarTablaPlayerAgentes();

        System.out.println("Se han rellenado correctamente");
    }

    public void eliminarTablas()  {
        try {
        pr = conn.prepareStatement("DROP TABLE jugadores,mapas, armas, partidas, agentes, playeragentes,playerweapons CASCADE");
        pr.executeUpdate();
            System.out.println("Se han eliminado correctamente");
        }catch (Exception e){
            System.out.println("No se puede eliminar todas las tablas, mira a ver si existen: " + e.getMessage());

        }
    }

    public void selectColumna() {
        try{
        Scanner sc = new Scanner(System.in);
        System.out.println("Escribe la tabla que quieres buscar");
        String tabla = sc.next();
        System.out.println("Escribe la columna que quieres buscar");
        String columna = sc.next();
        System.out.println();

        ResultSet rs = st.executeQuery("SELECT "+ columna  + " FROM "+ tabla);
        while (rs.next()) {
            System.out.println(rs.getString(columna));
        }
        rs.close();

        }catch (Exception e){
            System.out.println("Comprueba que se ha escrito bien: " + e.getMessage());

        }
    }

    public void selectTabla() {
        try{
        Scanner sc = new Scanner(System.in);
        System.out.println("Escribe la tabla que quieres buscar");
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

        rs.close();
        }catch (Exception e){
            System.out.println("Comprueba que existe la tabla " + e.getMessage());

        }
    }

    public void selecConcreto() {
        try {
        Scanner sc = new Scanner(System.in);
        System.out.println("¿Que tabla quieres leer?");
        String tabla = sc.next();
        System.out.println("¿Que columna quieres leer?");

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

        String columna = sc.next();
        System.out.println("Pon una letra o numero para listar todos los que comiencen por ese numero");
        String condicion = sc.next();

        PreparedStatement ps = conn.prepareStatement("SELECT * FROM " + tabla + " WHERE " + columna + " LIKE '%" + condicion + "%'");
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
        }catch (Exception e){
            System.out.println("Comprueba la información proporcionada a la base de datos: " + e.getMessage());

        }

    }

    public void modificarRegistro() {
        try{
        Scanner sc = new Scanner(System.in);
        System.out.println("Introduce el nombre de la tabla que quieres modificar: ");
        String table = sc.nextLine();
        System.out.println("Introduce el nombre de la columna que quieres modificar: ");
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
        String column = sc.nextLine();
        System.out.println("Introduce el nuevo valor: ");
        String value = sc.nextLine();
        System.out.println("Introduce la columna desde donde se quiere modificar ");
        String columna = sc.nextLine();
        System.out.println("Introduce el valor desde donde se quiere modificar ");
        String valor = sc.nextLine();

        // Crear la sentencia preparada
        String sql = "UPDATE " + table + " SET " + column + " = "+"\'"+value+"\'"+" WHERE " + columna+" = "+"\'"+valor+"\'";
        PreparedStatement statement = conn.prepareStatement(sql);

        statement.executeUpdate();

        // Ejecutar la consulta
        int rows = statement.executeUpdate();
        System.out.println("Se han modificado " + rows + " fila(s).");
        }catch (Exception e){
            System.out.println("Comprueba la información proporcionada a la base de datos: " + e.getMessage());

        }

    }

    public void borrarRegistro() {
        try {

        Scanner sc = new Scanner(System.in);
        System.out.println("Introduce el nombre de la tabla donde quieres eliminar: ");
        String tabla = sc.nextLine();
        System.out.println("Introduce el nombre de la columna donde quieres eliminar: ");
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
        String columna = sc.nextLine();
        System.out.println("Introduce el valor que desea eliminar: ");
        String valor = sc.nextLine();
        //"\'"

        // Crear la sentencia preparada
        String sql = "DELETE FROM "+tabla+" WHERE "+columna+" = "+ "\'"+valor+"\'";
        pr = conn.prepareStatement(sql);

        pr.executeUpdate();
        System.out.println("Se ha eliminado correctamente");

        }catch (Exception e){
            System.out.println("No se ha podido eliminar: " + e.getMessage());

        }

    }
    public void borrarConjunto(){
        Scanner sc = new Scanner(System.in);

        System.out.println("Ingrese el nombre de la tabla: ");
        String tableName = sc.nextLine();

        System.out.println("Ingrese la columna objetivo: ");
        String columnName = sc.nextLine();

        System.out.println("Ingrese el valor común del conjunto: ");
        String valor = sc.nextLine();


        try {
            String sql = "DELETE FROM " + tableName + " WHERE " + columnName + " = '" + valor + "'";

            int rowsDeleted = pr.executeUpdate(sql);

            System.out.println(rowsDeleted + " filas eliminadas");
        } catch (SQLException e) {
            System.out.println("Ocurrió un error: " + e.getMessage());
        }
    }

    public void eliminarUnaTabla(){
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("¿Que tabla quieres borrar?");
            String tabla = sc.next();

            pr = conn.prepareStatement("DROP TABLE "+tabla+" CASCADE");
            pr.executeUpdate();
            System.out.println("La tabla ha sido eliminada correctamente");

        }catch (Exception e){
            System.out.println("No se puede eliminar esta tabla, mira a ver si existe: " + e.getMessage());

        }

    }

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
                pr.setString(1, data[0].replace("\"", ""));
                pr.setString(2, data[1].replace("\"", ""));
                pr.setString(3, data[2].replace("\"", ""));
                pr.setString(4, data[3].replace("\"", ""));
                pr.setString(5, data[4].replace("\"", ""));
                pr.setString(6, data[5].replace("\"", ""));
                pr.setString(7, data[6].replace("\"", ""));
                pr.setString(8, data[7].replace("\"", ""));
                pr.setString(9, data[8].replace("\"", ""));
                pr.setString(10, data[9].replace("\"", ""));
                pr.setString(11, data[10].replace("\"", ""));
                pr.setString(12, data[11].replace("\"", ""));
                pr.setString(13, data[12].replace("\"", ""));
                pr.setString(14, data[13].replace("\"", ""));

                pr.executeUpdate();
            }
        }
    }

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
                pr.setString(1, data[0].replace("\"", ""));
                pr.setString(2, data[1].replace("\"", ""));
                pr.setString(3, data[2].replace("\"", ""));
                pr.setString(4, data[3].replace("\"", ""));
                pr.setString(5, data[4].replace("\"", ""));
                pr.setString(6, data[5].replace("\"", ""));
                pr.setString(7, data[6].replace("\"", ""));
                pr.setString(8, data[7].replace("\"", ""));

                pr.executeUpdate();
            }
        }
    }

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
                pr.setString(1, data[0].replace("\"", ""));
                pr.setString(2, data[1].replace("\"", ""));
                pr.setString(3, data[2].replace("\"", ""));
                pr.setString(4, data[3].replace("\"", ""));
                pr.setString(5, data[4].replace("\"", ""));

                pr.executeUpdate();
            }
        }
    }

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
                pr.setString(1, data[0].replace("\"", ""));
                pr.setString(2, data[1].replace("\"", ""));
                pr.setString(3, data[2].replace("\"", ""));

                pr.executeUpdate();
            }
        }
    }

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
                pr.setString(1, data[0].replace("\"", ""));
                pr.setString(2, data[1].replace("\"", ""));
                pr.setString(3, data[2].replace("\"", ""));
                pr.setString(4, data[3].replace("\"", ""));
                pr.setString(5, data[4].replace("\"", ""));
                pr.setString(6, data[5].replace("\"", ""));
                pr.setString(7, data[6].replace("\"", ""));
                pr.setString(8, data[7].replace("\"", ""));
                pr.setString(9, data[8].replace("\"", ""));
                pr.setString(10, data[9].replace("\"", ""));

                pr.executeUpdate();
            }
        }
    }

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
                pr.setString(1, data[0].replace("\"", ""));
                pr.setString(2, data[1].replace("\"", ""));
                pr.setString(3, data[2].replace("\"", ""));

                pr.executeUpdate();
            }
        }
    }

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
                pr.setString(1, data[0].replace("\"", ""));
                pr.setString(2, data[1].replace("\"", ""));
                pr.setString(3, data[2].replace("\"", ""));
                pr.setString(4, data[3].replace("\"", ""));
                pr.setString(5, data[4].replace("\"", ""));
                pr.setString(6, data[5].replace("\"", ""));
                pr.setString(7, data[6].replace("\"", ""));
                pr.setString(8, data[7].replace("\"", ""));


                pr.executeUpdate();
            }
        }
    }


}
