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
import java.net.http.HttpResponse.BodyHandlers;
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
        br.close();
        conn.disconnect();

        return new JSONArray(sb.toString());
    }

    public static boolean crearEstudiante(String cedula, String nombre,
            String direccion, String telefono) throws Exception {

        String parametros = parametrosCompletos(cedula, nombre, direccion, telefono);

        HttpRequest req = HttpRequest.newBuilder(new URI(API_URL))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(parametros))
                .build();

        HttpResponse<String> resp = HTTP.send(req, BodyHandlers.ofString());
        System.out.println("Respuesta POST: " + resp.body());
        return resp.statusCode() == 200;
    }

    public static boolean actualizarEstudiante(String cedula, String nombre,
            String direccion, String telefono) throws Exception {
        String form = parametrosCompletos(cedula, nombre, direccion, telefono);

        HttpRequest req = HttpRequest.newBuilder(URI.create(API_URL))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .PUT(HttpRequest.BodyPublishers.ofString(form))
                .build();

        HttpResponse<String> resp = HTTP.send(req, HttpResponse.BodyHandlers.ofString());
        return resp.statusCode() / 100 == 2;
    }

    public static boolean eliminarEstudiante(String cedula) throws Exception {
        String url = API_URL + "?cedula=" + enc(cedula);

        HttpRequest req = HttpRequest.newBuilder(URI.create(url))
                .DELETE()
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> resp = HTTP.send(req, HttpResponse.BodyHandlers.ofString());
        System.out.println("Respuesta DELETE: " + resp.body());
        return resp.statusCode() / 100 == 2;
    }

    private static String enc(String valor) {
        return URLEncoder.encode(valor, StandardCharsets.UTF_8);
    }

    private static String parametrosCompletos(String cedula, String nombre, String direccion, String telefono) {
        return "cedula=" + enc(cedula)
                + "&nombre=" + enc(nombre)
                + "&direccion=" + enc(direccion)
                + "&telefono=" + enc(telefono);
    }
}
