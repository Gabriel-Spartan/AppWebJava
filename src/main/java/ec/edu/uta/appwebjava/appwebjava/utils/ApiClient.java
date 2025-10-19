package ec.edu.uta.appwebjava.appwebjava.utils;

import org.json.JSONArray;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class ApiClient {

    private static final String API_URL = "http://localhost/soa/api.php";
    private static final HttpClient HTTP = HttpClient.newHttpClient();

    // Obtener estudiantes
    public static JSONArray getEstudiantes() throws IOException {
        URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        System.out.println("Extrae de la api" + sb.toString());
        br.close();
        conn.disconnect();

        return new JSONArray(sb.toString());
    }

    public static boolean crearEstudiante(String cedula, String nombre,
            String direccion, String telefono)
            throws Exception {

        // Codifica cada valor en UTF-8 para x-www-form-urlencoded
        String form = "cedula="   + URLEncoder.encode(cedula, StandardCharsets.UTF_8)
                    + "&nombre="  + URLEncoder.encode(nombre, StandardCharsets.UTF_8)
                    + "&direccion="+ URLEncoder.encode(direccion, StandardCharsets.UTF_8)
                    + "&telefono="+ URLEncoder.encode(telefono, StandardCharsets.UTF_8);

        HttpRequest req = HttpRequest.newBuilder(URI.create(API_URL))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(form))
                .build();

        HttpResponse<String> resp = HTTP.send(req, HttpResponse.BodyHandlers.ofString());
        // Ajusta esta condición al contrato de tu PHP si devuelve otro status
        return resp.statusCode() / 100 == 2;
    }
}
