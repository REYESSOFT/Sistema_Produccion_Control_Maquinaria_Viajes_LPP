import java.time.LocalDate;

public record ProyectoResumen(
        int idProyecto,
        String codigoProyecto,
        String empresa,
        String descripcion,
        String sector,
        String piscina,
        LocalDate fechaInicio,
        LocalDate fechaFinEstimada,
        String estado
) {
}
