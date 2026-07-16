import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LectorTXTCodigosHistoricos {

    private static final int TOTAL_COLUMNAS = 5;

    public enum EstadoFila {
        VINCULABLE,
        SIN_CODIGO_ACTUAL,
        INVALIDA
    }

    public record FilaCodigoHistorico(
            int numeroLinea,
            String razonSocial,
            String codigoAnterior,
            String codigoActual,
            String descripcion,
            double costoHora,
            EstadoFila estadoFila
    ) {
    }

    public record ResultadoLectura(
            List<FilaCodigoHistorico> filasValidas,
            int filasProcesadas,
            int filasVinculables,
            int filasSinCodigoActual,
            int filasInvalidas,
            int preciosNormalizados,
            List<String> advertencias
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

        List<FilaCodigoHistorico> filasValidas =
                new ArrayList<>();

        List<String> advertencias =
                new ArrayList<>();

        int procesadas = 0;
        int vinculables = 0;
        int sinCodigoActual = 0;
        int invalidas = 0;
        int preciosNormalizados = 0;

        for (
                int indice = 0;
                indice < lineas.size();
                indice++
        ) {

            int numeroLinea =
                    indice + 1;

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

            String razonSocial =
                    normalizarEntidad(
                            columnas[0]
                    );

            String codigoAnterior =
                    limpiarTexto(
                            columnas[1]
                    );

            String codigoActual =
                    limpiarTexto(
                            columnas[2]
                    );

            String descripcion =
                    limpiarTexto(
                            columnas[3]
                    );

            String precioOriginal =
                    limpiarTexto(
                            columnas[4]
                    );

            if (codigoAnterior.isEmpty()) {

                invalidas++;

                advertencias.add(
                        "Línea "
                                + numeroLinea
                                + ": no tiene código anterior."
                );

                continue;
            }

            if (descripcion.isEmpty()) {

                invalidas++;

                advertencias.add(
                        "Línea "
                                + numeroLinea
                                + ": no tiene descripción."
                );

                continue;
            }

            double costoHora;

            try {

                costoHora =
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

                costoHora = 0.00;

                advertencias.add(
                        "Línea "
                                + numeroLinea
                                + ": tarifa inválida \""
                                + precioOriginal
                                + "\"; se utilizará 0.00."
                );
            }

            EstadoFila estadoFila;

            if (codigoActual.isEmpty()) {

                estadoFila =
                        EstadoFila.SIN_CODIGO_ACTUAL;

                sinCodigoActual++;

                advertencias.add(
                        "Línea "
                                + numeroLinea
                                + ": el código anterior "
                                + codigoAnterior
                                + " no tiene código actual."
                );

            } else {

                estadoFila =
                        EstadoFila.VINCULABLE;

                vinculables++;
            }

            filasValidas.add(
                    new FilaCodigoHistorico(
                            numeroLinea,
                            razonSocial,
                            codigoAnterior,
                            codigoActual,
                            descripcion,
                            costoHora,
                            estadoFila
                    )
            );
        }

        return new ResultadoLectura(
                filasValidas,
                procesadas,
                vinculables,
                sinCodigoActual,
                invalidas,
                preciosNormalizados,
                advertencias
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
                    Charset.forName(
                            "windows-1252"
                    )
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

        return texto.contains("RAZON SOCIAL")
                && texto.contains("CODIGO ANTERIOR")
                && texto.contains("CODIGO ACTUAL");
    }

    private static String[] completarColumnas(
            String[] columnas
    ) {

        if (
            columnas.length
            >= TOTAL_COLUMNAS
        ) {
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

    private static String normalizarEntidad(
            String texto
    ) {

        String valor =
                limpiarTexto(texto)
                        .toUpperCase(
                                Locale.ROOT
                        );

        if (valor.equals("CHILAN ALEX")) {
            return "ALEX CHILAN";
        }

        if (valor.equals("LALVAY")) {
            return "PATRICIO LALVAY";
        }

        if (valor.equals("EQUIPOS PRO")) {
            return "EQUIPOSPRO";
        }

        return valor;
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
                        .replace(" ", "");

        /*
         * Ejemplos:
         * 35,00    -> 35.00
         * 1.250,50 -> 1250.50
         * 35.00    -> 35.00
         */
        if (
            valor.contains(",")
            && valor.contains(".")
        ) {

            valor =
                    valor.replace(".", "")
                            .replace(",", ".");

        } else if (valor.contains(",")) {

            valor =
                    valor.replace(",", ".");
        }

        return Double.parseDouble(valor);
    }
}