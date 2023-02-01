import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Tablas {
    String dbURL="jdbc:postgresql://192.168.1.52:5432/jdbc";
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

    public void crearTablas() throws SQLException, IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("src/schema.sql"))) {
            st.execute(br.lines().collect(Collectors.joining(" \n")));

        }
    }

    public void rellenarTablas() throws IOException, SQLException {
        rellenarTablaJugadores();
        rellenarTablaArmas();
        rellenarTablaMapas();
        rellenarTablaPartidas();
        rellenarTablaAgentes();
        rellenarTablaPlayerWeapons();
        rellenarTablaPlayerAgentes();
    }

    public void eliminarTablas() throws SQLException {
        PreparedStatement ps = conn.prepareStatement("DROP TABLE jugadores,mapas, armas, partidas, agentes, playeragentes,playerweapons CASCADE");
        ps.executeUpdate();
    }

    public void selectColumna() throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Escribe la tabla que quieres buscar");
        String tabla = sc.next();
        System.out.println("Escribe la columna que quieres buscar");
        String columna = sc.next();

        ResultSet rs = st.executeQuery("SELECT "+ columna  + " FROM "+ tabla);
        while (rs.next()) {
            System.out.println(rs.getString(columna));
        }
        rs.close();
    }

    public void selectTabla() throws SQLException {
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
    }

    public void selecConcreto() throws SQLException {
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
        System.out.println("¿Que condición quieres?");
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

    }

    public void modificarRegistro() throws SQLException {
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


    }

    public void borrarRegistro() throws SQLException {
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
        PreparedStatement statement = conn.prepareStatement(sql);

        statement.executeUpdate();

        // Ejecutar la consulta
        int rows = statement.executeUpdate();
        System.out.println("Se han eliminado " + rows + " fila(s).");


    }

    public void eliminarUnaTabla() throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.println("¿Que tabla quieres borrar?");
        String tabla = sc.next();

        st.executeUpdate("DROP TABLE"+tabla+" CASCADE");
    }

    public void rellenarTablaJugadores() throws IOException, SQLException {
        String csvFile = "src/CSV/Jugador.csv";
        String line = "";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String sql = "INSERT INTO jugadores VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?) ON CONFLICT DO NOTHING;";

            PreparedStatement pst = conn.prepareStatement(sql);
            br.readLine();  // Salta la primera línea
            while ((line = br.readLine()) != null) {
                pst.clearParameters();
                String[] data = line.split(",");
                pst.setString(1, data[0].replace("\"", ""));
                pst.setString(2, data[1].replace("\"", ""));
                pst.setString(3, data[2].replace("\"", ""));
                pst.setString(4, data[3].replace("\"", ""));
                pst.setString(5, data[4].replace("\"", ""));
                pst.setString(6, data[5].replace("\"", ""));
                pst.setString(7, data[6].replace("\"", ""));
                pst.setString(8, data[7].replace("\"", ""));
                pst.setString(9, data[8].replace("\"", ""));
                pst.setString(10, data[9].replace("\"", ""));
                pst.setString(11, data[10].replace("\"", ""));
                pst.setString(12, data[11].replace("\"", ""));
                pst.setString(13, data[12].replace("\"", ""));
                pst.setString(14, data[13].replace("\"", ""));

                pst.executeUpdate();
            }
        }
    }

    public void rellenarTablaMapas() throws IOException, SQLException {
        String csvFile = "src/CSV/Mapas.csv";
        String line = "";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String sql = "INSERT INTO mapas VALUES (?,?,?,?,?,?,?,?) ON CONFLICT DO NOTHING;";

            PreparedStatement pst = conn.prepareStatement(sql);
            br.readLine();  // Salta la primera línea
            while ((line = br.readLine()) != null) {
                pst.clearParameters();
                String[] data = line.split(",");
                pst.setString(1, data[0].replace("\"", ""));
                pst.setString(2, data[1].replace("\"", ""));
                pst.setString(3, data[2].replace("\"", ""));
                pst.setString(4, data[3].replace("\"", ""));
                pst.setString(5, data[4].replace("\"", ""));
                pst.setString(6, data[5].replace("\"", ""));
                pst.setString(7, data[6].replace("\"", ""));
                pst.setString(8, data[7].replace("\"", ""));

                pst.executeUpdate();
            }
        }
    }

    public void rellenarTablaPartidas() throws IOException, SQLException {
        String csvFile = "src/CSV/Partidas.csv";
        String line = "";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String sql = "INSERT INTO partidas VALUES (?,?,?,?,?) ON CONFLICT DO NOTHING;";

            PreparedStatement pst = conn.prepareStatement(sql);
            br.readLine();  // Salta la primera línea
            while ((line = br.readLine()) != null) {
                pst.clearParameters();
                String[] data = line.split(",");
                pst.setString(1, data[0].replace("\"", ""));
                pst.setString(2, data[1].replace("\"", ""));
                pst.setString(3, data[2].replace("\"", ""));
                pst.setString(4, data[3].replace("\"", ""));
                pst.setString(5, data[4].replace("\"", ""));

                pst.executeUpdate();
            }
        }
    }

    public void rellenarTablaAgentes() throws IOException, SQLException {
        String csvFile = "src/CSV/Agentes.csv";
        String line = "";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String sql = "INSERT INTO agentes VALUES (?,?,?) ON CONFLICT DO NOTHING;";

            PreparedStatement pst = conn.prepareStatement(sql);
            br.readLine();  // Salta la primera línea
            while ((line = br.readLine()) != null) {
                pst.clearParameters();
                String[] data = line.split(",");
                pst.setString(1, data[0].replace("\"", ""));
                pst.setString(2, data[1].replace("\"", ""));
                pst.setString(3, data[2].replace("\"", ""));

                pst.executeUpdate();
            }
        }
    }

    public void rellenarTablaPlayerAgentes() throws IOException, SQLException {
        String csvFile = "src/CSV/PlayerAgentes.csv";
        String line = "";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String sql = "INSERT INTO playeragentes VALUES (?,?,?,?,?,?,?,?,?,?);";

            PreparedStatement pst = conn.prepareStatement(sql);
            br.readLine();  // Salta la primera línea
            while ((line = br.readLine()) != null) {
                pst.clearParameters();
                String[] data = line.split(",");
                pst.setString(1, data[0].replace("\"", ""));
                pst.setString(2, data[1].replace("\"", ""));
                pst.setString(3, data[2].replace("\"", ""));
                pst.setString(4, data[3].replace("\"", ""));
                pst.setString(5, data[4].replace("\"", ""));
                pst.setString(6, data[5].replace("\"", ""));
                pst.setString(7, data[6].replace("\"", ""));
                pst.setString(8, data[7].replace("\"", ""));
                pst.setString(9, data[8].replace("\"", ""));
                pst.setString(10, data[9].replace("\"", ""));

                pst.executeUpdate();
            }
        }
    }

    public void rellenarTablaArmas() throws IOException, SQLException {
        String csvFile = "src/CSV/Armas.csv";
        String line = "";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String sql = "INSERT INTO armas VALUES (?,?,?) ON CONFLICT DO NOTHING;";

            PreparedStatement pst = conn.prepareStatement(sql);
            br.readLine();  // Salta la primera línea
            while ((line = br.readLine()) != null) {
                pst.clearParameters();
                String[] data = line.split(",");
                pst.setString(1, data[0].replace("\"", ""));
                pst.setString(2, data[1].replace("\"", ""));
                pst.setString(3, data[2].replace("\"", ""));

                pst.executeUpdate();
            }
        }
    }

    public void rellenarTablaPlayerWeapons() throws IOException, SQLException {
        String csvFile = "src/CSV/PlayerWeapon.csv";
        String line = "";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String sql = "INSERT INTO playerweapons VALUES (?,?,?,?,?,?,?,?) ON CONFLICT DO NOTHING;";

            PreparedStatement pst = conn.prepareStatement(sql);
            br.readLine();  // Salta la primera línea
            while ((line = br.readLine()) != null) {
                pst.clearParameters();
                String[] data = line.split(",");
                pst.setString(1, data[0].replace("\"", ""));
                pst.setString(2, data[1].replace("\"", ""));
                pst.setString(3, data[2].replace("\"", ""));
                pst.setString(4, data[3].replace("\"", ""));
                pst.setString(5, data[4].replace("\"", ""));
                pst.setString(6, data[5].replace("\"", ""));
                pst.setString(7, data[6].replace("\"", ""));
                pst.setString(8, data[7].replace("\"", ""));


                pst.executeUpdate();
            }
        }
    }



}
