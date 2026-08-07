import java.time.LocalDate;

public record ProyectoDetalle(
        int idProyecto,
        String codigoProyecto,
        String descripcion,
        int idEmpresa,
        Integer idSector,
        Integer idPiscina,
        String ordenCompra,
        Integer idTipoActividad,
        LocalDate fechaInicio,
        LocalDate fechaFinEstimada,
        LocalDate fechaFinReal,
        Integer diasEstimados,
        Double areaM2,
        Double espesor,
        Double factorCompactacion,
        Double cantidadContratada,
        Double metrosLinealesContratados,
        Double precioUnitario,
        String estado,
        String observaciones
) {
}
