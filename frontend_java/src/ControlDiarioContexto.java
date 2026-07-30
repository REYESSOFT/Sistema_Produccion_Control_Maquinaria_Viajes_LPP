public final class ControlDiarioContexto {

    private static Integer idGuia;
    private static String numeroGuia;
    private static String fecha;
    private static String empresa;
    private static String tipoGuia;
    private static String proyecto;
    private static String sector;
    private static String material;
    private static String chofer;
    private static String placa;
    private static double volumenM3;

    private ControlDiarioContexto() {
        // Evita que esta clase pueda ser instanciada.
    }

    public static void establecerGuia(
            Integer idGuiaSeleccionada,
            String numeroGuiaSeleccionada,
            String fechaSeleccionada,
            String empresaSeleccionada,
            String tipoGuiaSeleccionada,
            String proyectoSeleccionado,
            String sectorSeleccionado,
            String materialSeleccionado,
            String choferSeleccionado,
            String placaSeleccionada,
            double volumenSeleccionado
    ) {

        idGuia = idGuiaSeleccionada;
        numeroGuia = limpiarTexto(numeroGuiaSeleccionada);
        fecha = limpiarTexto(fechaSeleccionada);
        empresa = limpiarTexto(empresaSeleccionada);
        tipoGuia = limpiarTexto(tipoGuiaSeleccionada);
        proyecto = limpiarTexto(proyectoSeleccionado);
        sector = limpiarTexto(sectorSeleccionado);
        material = limpiarTexto(materialSeleccionado);
        chofer = limpiarTexto(choferSeleccionado);
        placa = limpiarTexto(placaSeleccionada);
        volumenM3 = volumenSeleccionado;
    }

    public static boolean tieneGuiaSeleccionada() {

        return idGuia != null
                && idGuia > 0;
    }

    public static void limpiar() {

        idGuia = null;
        numeroGuia = "";
        fecha = "";
        empresa = "";
        tipoGuia = "";
        proyecto = "";
        sector = "";
        material = "";
        chofer = "";
        placa = "";
        volumenM3 = 0.00;
    }

    public static Integer getIdGuia() {

        return idGuia;
    }

    public static String getNumeroGuia() {

        return numeroGuia;
    }

    public static String getFecha() {

        return fecha;
    }

    public static String getEmpresa() {

        return empresa;
    }

    public static String getTipoGuia() {

        return tipoGuia;
    }

    public static String getProyecto() {

        return proyecto;
    }

    public static String getSector() {

        return sector;
    }

    public static String getMaterial() {

        return material;
    }

    public static String getChofer() {

        return chofer;
    }

    public static String getPlaca() {

        return placa;
    }

    public static double getVolumenM3() {

        return volumenM3;
    }

    private static String limpiarTexto(
            String valor
    ) {

        return valor == null
                ? ""
                : valor.trim();
    }
}
