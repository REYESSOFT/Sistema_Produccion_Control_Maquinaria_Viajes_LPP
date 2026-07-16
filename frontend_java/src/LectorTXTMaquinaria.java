import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LectorTXTMaquinaria {

    private static final int TOTAL_COLUMNAS = 13;

    public record FilaMaquinaria(
            int numeroLinea,
            String codigoInterno,
            String estadoOperativo,
            String descripcion,
            String tipoMaquinaria,
            String serieMaquina,
            String serieActual,
            Double horometro,
            boolean horometroConfirmado,
            String proveedor,
            String propietario,
            String codigoPlaca,
            String codigoActual,
            double precio
    ) {
    }

    public record ResultadoLectura(
            List<FilaMaquinaria> filasValidas,
            int filasProcesadas,
            int filasValidasCantidad,
            int filasOmitidas,
            int estadosNormalizados,
            int horometrosNormalizados,
            int preciosNormalizados,
            List<String> errores
    ) {
    }

    public static ResultadoLectura leer(
            File archivo
    ) throws Exception {

        if (archivo == null) {
            throw new IllegalArgumentException(
                    "No se seleccionó ningún archivo."
            );
        }

        if (!archivo.exists()) {
            throw new IOException(
                    "El archivo seleccionado no existe."
            );
        }

        List<String> lineas =
                leerLineasCompatibles(archivo);

        if (lineas.isEmpty()) {
            throw new Exception(
                    "El archivo está vacío."
            );
        }

        List<FilaMaquinaria> filasValidas =
                new ArrayList<>();

        List<String> errores =
                new ArrayList<>();

        int procesadas = 0;
        int omitidas = 0;
        int estadosNormalizados = 0;
        int horometrosNormalizados = 0;
        int preciosNormalizados = 0;

        for (
                int indice = 0;
                indice < lineas.size();
                indice++
        ) {

            int numeroLinea = indice + 1;

            String linea =
                    lineas.get(indice);

            if (
                indice == 0
                && esCabecera(linea)
            ) {
                continue;
            }

            if (
                linea == null
                || linea.trim().isEmpty()
            ) {
                continue;
            }

            procesadas++;

            String[] columnas =
                    linea.split(
                            "\t",
                            -1
                    );

            columnas =
                    completarColumnas(columnas);

            String codigoInterno =
                    limpiarTexto(columnas[0]);

            String estadoOriginal =
                    limpiarTexto(columnas[1]);

            String descripcion =
                    limpiarTexto(columnas[2]);

            String tipoMaquinaria =
                    limpiarTexto(columnas[3]);

            String serieMaquina =
                    limpiarTexto(columnas[4]);

            String serieActual =
                    limpiarTexto(columnas[5]);

            String horometroOriginal =
                    limpiarTexto(columnas[6]);

            String confirmadoOriginal =
                    limpiarTexto(columnas[7]);

            String proveedor =
                    normalizarEntidad(
                            columnas[8]
                    );

            String propietario =
                    normalizarEntidad(
                            columnas[9]
                    );

            String codigoPlaca =
                    limpiarTexto(columnas[10]);

            String codigoActual =
                    limpiarTexto(columnas[11]);

            String precioOriginal =
                    limpiarTexto(columnas[12]);

            if (descripcion.isEmpty()) {

                omitidas++;

                errores.add(
                        "Línea " + numeroLinea
                                + ": fila omitida porque "
                                + "no tiene descripción."
                );

                continue;
            }

            if (
                codigoActual.isEmpty()
                && codigoPlaca.isEmpty()
                && codigoInterno.isEmpty()
                && serieActual.isEmpty()
                && serieMaquina.isEmpty()
            ) {

                omitidas++;

                errores.add(
                        "Línea " + numeroLinea
                                + ": fila omitida porque "
                                + "no tiene un identificador."
                );

                continue;
            }

            String estado =
                    normalizarEstado(
                            estadoOriginal
                    );

            if (
                !estado.equalsIgnoreCase(
                        estadoOriginal
                )
            ) {
                estadosNormalizados++;
            }

            Double horometro;

            try {

                horometro =
                        convertirDecimalOpcional(
                                horometroOriginal
                        );

                if (
                    horometro != null
                    && horometroOriginal.contains(",")
                ) {
                    horometrosNormalizados++;
                }

            } catch (NumberFormatException e) {

                horometro = null;

                errores.add(
                        "Línea " + numeroLinea
                                + ": horómetro inválido \""
                                + horometroOriginal
                                + "\"; se utilizará NULL."
                );
            }

            double precio;

            try {

                precio =
                        convertirPrecio(
                                precioOriginal
                        );

                if (
                    !precioOriginal.isEmpty()
                    && (
                        precioOriginal.contains("$")
                        || precioOriginal.contains(",")
                    )
                ) {
                    preciosNormalizados++;
                }

            } catch (NumberFormatException e) {

                precio = 0.00;

                errores.add(
                        "Línea " + numeroLinea
                                + ": precio inválido \""
                                + precioOriginal
                                + "\"; se utilizará 0.00."
                );
            }

            boolean confirmado =
                    normalizarBooleano(
                            confirmadoOriginal
                    );

            filasValidas.add(
                    new FilaMaquinaria(
                            numeroLinea,
                            codigoInterno,
                            estado,
                            descripcion,
                            tipoMaquinaria,
                            serieMaquina,
                            serieActual,
                            horometro,
                            confirmado,
                            proveedor,
                            propietario,
                            codigoPlaca,
                            codigoActual,
                            precio
                    )
            );
        }

        return new ResultadoLectura(
                filasValidas,
                procesadas,
                filasValidas.size(),
                omitidas,
                estadosNormalizados,
                horometrosNormalizados,
                preciosNormalizados,
                errores
        );
    }

    private static List<String> leerLineasCompatibles(
            File archivo
    ) throws IOException {

        try {

            return Files.readAllLines(
                    archivo.toPath(),
                    StandardCharsets.UTF_8
            );

        } catch (MalformedInputException e) {

            return Files.readAllLines(
                    archivo.toPath(),
                    Charset.forName("windows-1252")
            );
        }
    }

    private static boolean esCabecera(
            String linea
    ) {

        if (linea == null) {
            return false;
        }

        String texto =
                linea.toUpperCase(
                        Locale.ROOT
                );

        return texto.contains("INTERNO")
                && texto.contains("STATUS")
                && texto.contains(
                        "DESCRIPCION DE MAQUINARIA"
                );
    }

    private static String[] completarColumnas(
            String[] columnas
    ) {

        if (columnas.length >= TOTAL_COLUMNAS) {
            return columnas;
        }

        String[] completas =
                new String[TOTAL_COLUMNAS];

        for (
                int i = 0;
                i < TOTAL_COLUMNAS;
                i++
        ) {

            completas[i] =
                    i < columnas.length
                            ? columnas[i]
                            : "";
        }

        return completas;
    }

    private static String limpiarTexto(
            String texto
    ) {

        if (texto == null) {
            return "";
        }

        return texto.trim();
    }

    private static String normalizarEstado(
            String estado
    ) {

        if (
            estado == null
            || estado.trim().isEmpty()
        ) {
            return "INACTIVA";
        }

        String valor =
                estado.trim()
                        .toUpperCase(
                                Locale.ROOT
                        );

        return switch (valor) {

            case "OPERATIVA" ->
                    "OPERATIVA";

            case "MANT",
                 "MATENIMIENTO",
                 "MANTENIMIENTO" ->
                    "MANTENIMIENTO";

            case "RETIRADA" ->
                    "RETIRADA";

            case "INACTIVA" ->
                    "INACTIVA";

            default ->
                    "INACTIVA";
        };
    }

    private static boolean normalizarBooleano(
            String texto
    ) {

        if (texto == null) {
            return false;
        }

        String valor =
                texto.trim()
                        .toUpperCase(
                                Locale.ROOT
                        );

        return valor.equals("SI")
                || valor.equals("SÍ")
                || valor.equals("TRUE")
                || valor.equals("1");
    }

    private static String normalizarEntidad(
            String texto
    ) {

        String valor =
                limpiarTexto(texto)
                        .toUpperCase(
                                Locale.ROOT
                        );

        if (valor.equals("EQUIPOS PRO")) {
            return "EQUIPOSPRO";
        }

        return valor;
    }

    private static Double convertirDecimalOpcional(
            String texto
    ) {

        if (
            texto == null
            || texto.trim().isEmpty()
        ) {
            return null;
        }

        String valor =
                texto.trim()
                        .replace(" ", "")
                        .replace(",", ".");

        return Double.parseDouble(valor);
    }

    private static double convertirPrecio(
            String texto
    ) {

        if (
            texto == null
            || texto.trim().isEmpty()
        ) {
            return 0.00;
        }

        String valor =
                texto.trim()
                        .replace("$", "")
                        .replace(" ", "")
                        .replace(".", "")
                        .replace(",", ".");

        return Double.parseDouble(valor);
    }
}