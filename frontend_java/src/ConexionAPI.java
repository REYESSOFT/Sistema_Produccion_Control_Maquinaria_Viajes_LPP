import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class ConexionAPI {

    private static final String URL_BASE =
            "http://localhost:9090";

    private static final HttpClient CLIENTE =
            HttpClient.newBuilder()
                    .connectTimeout(
                            Duration.ofSeconds(10)
                    )
                    .build();

    private ConexionAPI() {
    }

    public static String get(
            String ruta
    ) throws IOException, InterruptedException {

        HttpRequest solicitud =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        URL_BASE + ruta
                                )
                        )
                        .timeout(
                                Duration.ofSeconds(30)
                        )
                        .header(
                                "Accept",
                                "application/json"
                        )
                        .GET()
                        .build();

        HttpResponse<String> respuesta =
                CLIENTE.send(
                        solicitud,
                        HttpResponse.BodyHandlers.ofString()
                );

        validarRespuesta(respuesta);

        return respuesta.body();
    }
    public static String post(
        String ruta,
        String cuerpoJson
) throws IOException, InterruptedException {

    HttpRequest solicitud =
            HttpRequest.newBuilder()
                    .uri(
                            URI.create(
                                    URL_BASE + ruta
                            )
                    )
                    .timeout(
                            Duration.ofSeconds(30)
                    )
                    .header(
                            "Accept",
                            "application/json"
                    )
                    .header(
                            "Content-Type",
                            "application/json"
                    )
                    .POST(
                            HttpRequest.BodyPublishers.ofString(
                                    cuerpoJson
                            )
                    )
                    .build();

    HttpResponse<String> respuesta =
            CLIENTE.send(
                    solicitud,
                    HttpResponse.BodyHandlers.ofString()
            );

    validarRespuesta(respuesta);

    return respuesta.body();
}
public static String put(
        String ruta,
        String cuerpoJson
) throws IOException, InterruptedException {

    HttpRequest solicitud =
            HttpRequest.newBuilder()
                    .uri(
                            URI.create(
                                    URL_BASE + ruta
                            )
                    )
                    .timeout(
                            Duration.ofSeconds(30)
                    )
                    .header(
                            "Accept",
                            "application/json"
                    )
                    .header(
                            "Content-Type",
                            "application/json"
                    )
                    .PUT(
                            HttpRequest.BodyPublishers.ofString(
                                    cuerpoJson
                            )
                    )
                    .build();

    HttpResponse<String> respuesta =
            CLIENTE.send(
                    solicitud,
                    HttpResponse.BodyHandlers.ofString()
            );

    validarRespuesta(respuesta);

    return respuesta.body();
}
public static void delete(
        String ruta
) throws IOException, InterruptedException {

    HttpRequest solicitud =
            HttpRequest.newBuilder()
                    .uri(
                            URI.create(
                                    URL_BASE + ruta
                            )
                    )
                    .timeout(
                            Duration.ofSeconds(30)
                    )
                    .header(
                            "Accept",
                            "application/json"
                    )
                    .DELETE()
                    .build();

    HttpResponse<String> respuesta =
            CLIENTE.send(
                    solicitud,
                    HttpResponse.BodyHandlers.ofString()
            );

    validarRespuesta(respuesta);
}

    private static void validarRespuesta(
            HttpResponse<String> respuesta
    ) throws IOException {

        int codigo =
                respuesta.statusCode();

        if (codigo < 200 || codigo >= 300) {

            throw new IOException(
                    "Error al consumir la API. "
                            + "Código HTTP: "
                            + codigo
                            + "\nRespuesta: "
                            + respuesta.body()
            );
        }
    }
}