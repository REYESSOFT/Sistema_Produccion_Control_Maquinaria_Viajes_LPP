import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class CatalogoCanteraMaterialAPI {

    private static final Gson GSON =
            new Gson();

    private CatalogoCanteraMaterialAPI() {
    }

    public record TarifaResumen(
            int idTarifa,
            String cantera,
            String material,
            double costoUnitarioMaterial,
            boolean activo
    ) {
    }

    public record TarifaDetalle(
            int idTarifa,
            String cantera,
            String material,
            double costoUnitarioMaterial,
            boolean activo
    ) {
    }

    public record TarifaOperacion(
            int idTarifaMaterial,
            int idTarifaTransporte,
            String cantera,
            String material,
            String destinoSector,
            double costoUnitarioMaterial,
            double costoUnitarioTransporte
    ) {
    }

    public static List<TarifaResumen> buscar(
            String cantera,
            String material,
            String estado
    ) throws Exception {

        int idEmpresa =
                obtenerIdEmpresaSesion();

        String ruta =
                "/api/v1/catalogo-cantera-material"
                        + "?idEmpresa="
                        + idEmpresa
                        + "&cantera="
                        + codificar(cantera)
                        + "&material="
                        + codificar(material)
                        + "&estado="
                        + codificar(estado);

        String respuestaJson =
                ConexionAPI.get(ruta);

        JsonObject respuesta =
                convertirRespuesta(
                        respuestaJson
                );

        validarRespuesta(
                respuesta
        );

        JsonArray datos =
                obtenerArregloDatos(
                        respuesta
                );

        List<TarifaResumen> lista =
                new ArrayList<>();

        if (datos == null) {
            return lista;
        }

        for (JsonElement elemento : datos) {

            JsonObject item =
                    elemento.getAsJsonObject();

            lista.add(
                    new TarifaResumen(
                            obtenerEntero(
                                    item,
                                    "idTarifa"
                            ),
                            obtenerTexto(
                                    item,
                                    "cantera"
                            ),
                            obtenerTexto(
                                    item,
                                    "material"
                            ),
                            obtenerDouble(
                                    item,
                                    "costoUnitarioMaterial"
                            ),
                            obtenerBoolean(
                                    item,
                                    "activo"
                            )
                    )
            );
        }

        return lista;
    }

    public static List<TarifaResumen>
            obtenerActivas() throws Exception {

        return buscar(
                "",
                "",
                "ACTIVO"
        );
    }

    public static TarifaDetalle obtenerPorId(
            int idTarifa
    ) throws Exception {

        int idEmpresa =
                obtenerIdEmpresaSesion();

        String respuestaJson =
                ConexionAPI.get(
                        "/api/v1/catalogo-cantera-material/"
                                + idTarifa
                                + "?idEmpresa="
                                + idEmpresa
                );

        JsonObject respuesta =
                convertirRespuesta(
                        respuestaJson
                );

        validarRespuesta(
                respuesta
        );

        JsonObject datos =
                obtenerObjetoDatos(
                        respuesta
                );

        if (datos == null) {

            throw new Exception(
                    "No se encontró la tarifa seleccionada."
            );
        }

        return new TarifaDetalle(
                obtenerEntero(
                        datos,
                        "idTarifa"
                ),
                obtenerTexto(
                        datos,
                        "cantera"
                ),
                obtenerTexto(
                        datos,
                        "material"
                ),
                obtenerDouble(
                        datos,
                        "costoUnitarioMaterial"
                ),
                obtenerBoolean(
                        datos,
                        "activo"
                )
        );
    }

    public static int insertar(
            String cantera,
            String material,
            double costoUnitarioMaterial
    ) throws Exception {

        int idEmpresa =
                obtenerIdEmpresaSesion();

        JsonObject cuerpo =
                crearCuerpoTarifa(
                        cantera,
                        material,
                        costoUnitarioMaterial
                );

        String respuestaJson =
                ConexionAPI.post(
                        "/api/v1/catalogo-cantera-material"
                                + "?idEmpresa="
                                + idEmpresa,
                        GSON.toJson(cuerpo)
                );

        JsonObject respuesta =
                convertirRespuesta(
                        respuestaJson
                );

        validarRespuesta(
                respuesta
        );

        JsonObject datos =
                obtenerObjetoDatos(
                        respuesta
                );

        if (
                datos == null
                || !datos.has("idTarifa")
                || datos.get("idTarifa").isJsonNull()
        ) {

            throw new Exception(
                    "La tarifa fue guardada, "
                            + "pero no se pudo obtener su ID."
            );
        }

        return datos
                .get("idTarifa")
                .getAsInt();
    }

    public static void actualizar(
            int idTarifa,
            String cantera,
            String material,
            double costoUnitarioMaterial
    ) throws Exception {

        int idEmpresa =
                obtenerIdEmpresaSesion();

        JsonObject cuerpo =
                crearCuerpoTarifa(
                        cantera,
                        material,
                        costoUnitarioMaterial
                );

        String respuestaJson =
                ConexionAPI.put(
                        "/api/v1/catalogo-cantera-material/"
                                + idTarifa
                                + "?idEmpresa="
                                + idEmpresa,
                        GSON.toJson(cuerpo)
                );

        JsonObject respuesta =
                convertirRespuesta(
                        respuestaJson
                );

        validarRespuesta(
                respuesta
        );
    }

    public static void desactivar(
            int idTarifa
    ) throws Exception {

        int idEmpresa =
                obtenerIdEmpresaSesion();

        ConexionAPI.delete(
                "/api/v1/catalogo-cantera-material/"
                        + idTarifa
                        + "?idEmpresa="
                        + idEmpresa
        );
    }

    public static void reactivar(
            int idTarifa
    ) throws Exception {

        int idEmpresa =
                obtenerIdEmpresaSesion();

        String respuestaJson =
                ConexionAPI.put(
                        "/api/v1/catalogo-cantera-material/"
                                + idTarifa
                                + "/reactivar"
                                + "?idEmpresa="
                                + idEmpresa,
                        "{}"
                );

        JsonObject respuesta =
                convertirRespuesta(
                        respuestaJson
                );

        validarRespuesta(
                respuesta
        );
    }

    public static List<String> obtenerCanterasActivas(
            int idControl
    ) throws Exception {

        String respuestaJson =
                ConexionAPI.get(
                        "/api/v1/catalogo-cantera-material/control/"
                                + idControl
                                + "/canteras"
                );

        return obtenerListaTexto(
                respuestaJson
        );
    }

    public static List<String>
            obtenerMaterialesPorCantera(
                    int idControl,
                    String cantera
            ) throws Exception {

        String respuestaJson =
                ConexionAPI.get(
                        "/api/v1/catalogo-cantera-material/control/"
                                + idControl
                                + "/materiales"
                                + "?cantera="
                                + codificar(cantera)
                );

        return obtenerListaTexto(
                respuestaJson
        );
    }

    public static List<String> obtenerDestinos(
            int idControl
    ) throws Exception {

        String respuestaJson =
                ConexionAPI.get(
                        "/api/v1/catalogo-cantera-material/control/"
                                + idControl
                                + "/destinos"
                );

        return obtenerListaTexto(
                respuestaJson
        );
    }

    public static TarifaOperacion
            obtenerTarifaActiva(
                    int idControl,
                    String cantera,
                    String material,
                    String destinoSector
            ) throws Exception {

        String ruta =
                "/api/v1/catalogo-cantera-material/control/"
                        + idControl
                        + "/tarifa"
                        + "?cantera="
                        + codificar(cantera)
                        + "&material="
                        + codificar(material)
                        + "&destinoSector="
                        + codificar(destinoSector);

        String respuestaJson =
                ConexionAPI.get(ruta);

        JsonObject respuesta =
                convertirRespuesta(
                        respuestaJson
                );

        validarRespuesta(
                respuesta
        );

        JsonObject datos =
                obtenerObjetoDatos(
                        respuesta
                );

        if (datos == null) {

            throw new Exception(
                    "No se encontró una tarifa activa."
            );
        }

        return new TarifaOperacion(
                obtenerEntero(
                        datos,
                        "idTarifaMaterial"
                ),
                obtenerEntero(
                        datos,
                        "idTarifaTransporte"
                ),
                obtenerTexto(
                        datos,
                        "cantera"
                ),
                obtenerTexto(
                        datos,
                        "material"
                ),
                obtenerTexto(
                        datos,
                        "destinoSector"
                ),
                obtenerDouble(
                        datos,
                        "costoUnitarioMaterial"
                ),
                obtenerDouble(
                        datos,
                        "costoUnitarioTransporte"
                )
        );
    }

    private static JsonObject crearCuerpoTarifa(
            String cantera,
            String material,
            double costoUnitarioMaterial
    ) {

        JsonObject cuerpo =
                new JsonObject();

        agregarTexto(
                cuerpo,
                "cantera",
                cantera
        );

        agregarTexto(
                cuerpo,
                "material",
                material
        );

        cuerpo.addProperty(
                "costoUnitarioMaterial",
                costoUnitarioMaterial
        );

        return cuerpo;
    }

    private static List<String> obtenerListaTexto(
            String respuestaJson
    ) throws Exception {

        JsonObject respuesta =
                convertirRespuesta(
                        respuestaJson
                );

        validarRespuesta(
                respuesta
        );

        JsonArray datos =
                obtenerArregloDatos(
                        respuesta
                );

        List<String> lista =
                new ArrayList<>();

        if (datos == null) {
            return lista;
        }

        for (JsonElement elemento : datos) {

            if (
                    elemento != null
                    && !elemento.isJsonNull()
            ) {

                lista.add(
                        elemento.getAsString()
                );
            }
        }

        return lista;
    }

    private static int obtenerIdEmpresaSesion()
            throws Exception {

        Usuario usuarioActual =
                SesionUsuario.getUsuarioActual();

        if (
                usuarioActual == null
                || usuarioActual.getIdEmpresa() == null
        ) {

            throw new Exception(
                    "No se pudo determinar la empresa del usuario."
            );
        }

        return usuarioActual.getIdEmpresa();
    }

    private static String codificar(
            String valor
    ) {

        return URLEncoder.encode(
                valor == null
                        ? ""
                        : valor,
                StandardCharsets.UTF_8
        );
    }

    private static JsonObject convertirRespuesta(
            String respuestaJson
    ) throws Exception {

        if (
                respuestaJson == null
                || respuestaJson.isBlank()
        ) {

            throw new Exception(
                    "La API no devolvió una respuesta."
            );
        }

        JsonObject respuesta =
                GSON.fromJson(
                        respuestaJson,
                        JsonObject.class
                );

        if (respuesta == null) {

            throw new Exception(
                    "La API no devolvió una respuesta válida."
            );
        }

        return respuesta;
    }

    private static void validarRespuesta(
            JsonObject respuesta
    ) throws Exception {

        if (
                respuesta == null
                || !respuesta.has("exito")
                || respuesta.get("exito").isJsonNull()
                || !respuesta
                        .get("exito")
                        .getAsBoolean()
        ) {

            throw new Exception(
                    obtenerMensajeError(
                            respuesta
                    )
            );
        }
    }

    private static JsonArray obtenerArregloDatos(
            JsonObject respuesta
    ) {

        if (
                respuesta == null
                || !respuesta.has("datos")
                || respuesta.get("datos").isJsonNull()
        ) {

            return null;
        }

        return respuesta
                .getAsJsonArray("datos");
    }

    private static JsonObject obtenerObjetoDatos(
            JsonObject respuesta
    ) {

        if (
                respuesta == null
                || !respuesta.has("datos")
                || respuesta.get("datos").isJsonNull()
        ) {

            return null;
        }

        return respuesta
                .getAsJsonObject("datos");
    }

    private static String obtenerMensajeError(
            JsonObject respuesta
    ) {

        if (
                respuesta != null
                && respuesta.has("mensaje")
                && !respuesta.get("mensaje").isJsonNull()
        ) {

            return respuesta
                    .get("mensaje")
                    .getAsString();
        }

        return "La API no devolvió una respuesta válida.";
    }

    private static int obtenerEntero(
            JsonObject objeto,
            String propiedad
    ) {

        if (
                objeto == null
                || !objeto.has(propiedad)
                || objeto.get(propiedad).isJsonNull()
        ) {

            return 0;
        }

        return objeto
                .get(propiedad)
                .getAsInt();
    }

    private static double obtenerDouble(
            JsonObject objeto,
            String propiedad
    ) {

        if (
                objeto == null
                || !objeto.has(propiedad)
                || objeto.get(propiedad).isJsonNull()
        ) {

            return 0.00;
        }

        return objeto
                .get(propiedad)
                .getAsDouble();
    }

    private static boolean obtenerBoolean(
            JsonObject objeto,
            String propiedad
    ) {

        if (
                objeto == null
                || !objeto.has(propiedad)
                || objeto.get(propiedad).isJsonNull()
        ) {

            return false;
        }

        return objeto
                .get(propiedad)
                .getAsBoolean();
    }

    private static String obtenerTexto(
            JsonObject objeto,
            String propiedad
    ) {

        if (
                objeto == null
                || !objeto.has(propiedad)
                || objeto.get(propiedad).isJsonNull()
        ) {

            return "";
        }

        return objeto
                .get(propiedad)
                .getAsString();
    }

    private static void agregarTexto(
            JsonObject objeto,
            String propiedad,
            String valor
    ) {

        if (
                valor == null
                || valor.isBlank()
        ) {

            objeto.add(
                    propiedad,
                    JsonNull.INSTANCE
            );

        } else {

            objeto.addProperty(
                    propiedad,
                    valor.trim()
            );
        }
    }
}
