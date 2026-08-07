import java.time.LocalDate;
import java.util.List;

/*
 * PUENTE TEMPORAL DE COMPATIBILIDAD.
 *
 * Este DAO ya no accede directamente a MySQL.
 * Todas las operaciones se delegan a ProyectoAPI.
 *
 * Se conserva temporalmente porque varias pantallas del ERP
 * todavía utilizan sus records y sus métodos públicos.
 *
 * En la etapa de limpieza final, las pantallas se desacoplarán
 * de ProyectoDAO y este archivo podrá moverse al respaldo.
 */
public class ProyectoDAO {

    public record EmpresaItem(
            int idEmpresa,
            String nombre
    ) {

        @Override
        public String toString() {
            return nombre;
        }
    }

    public record SectorItem(
            int idSector,
            String nombre
    ) {

        @Override
        public String toString() {
            return nombre;
        }
    }

    public record PiscinaItem(
            int idPiscina,
            int idSector,
            String nombre
    ) {

        @Override
        public String toString() {
            return nombre;
        }
    }

    public record TipoActividadItem(
            int idTipoActividad,
            String nombre
    ) {

        @Override
        public String toString() {
            return nombre;
        }
    }

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

    /*
     * ============================================================
     * CATÁLOGOS PARA FORM PROYECTO
     * ============================================================
     */

    public static List<ProyectoDAO.EmpresaItem>
            obtenerEmpresas() throws Exception {

        return ProyectoAPI
                .obtenerEmpresas()
                .stream()
                .map(item ->
                        new ProyectoDAO.EmpresaItem(
                                item.idEmpresa(),
                                item.nombre()
                        )
                )
                .toList();
    }

    public static List<ProyectoDAO.SectorItem>
            obtenerSectores() throws Exception {

        return ProyectoAPI
                .obtenerSectores()
                .stream()
                .map(item ->
                        new ProyectoDAO.SectorItem(
                                item.idSector(),
                                item.nombre()
                        )
                )
                .toList();
    }

    public static List<ProyectoDAO.PiscinaItem>
            obtenerPiscinasPorSector(
                    Integer idSector
            ) throws Exception {

        return ProyectoAPI
                .obtenerPiscinasPorSector(
                        idSector
                )
                .stream()
                .map(item ->
                        new ProyectoDAO.PiscinaItem(
                                item.idPiscina(),
                                item.idSector(),
                                item.nombre()
                        )
                )
                .toList();
    }

    public static List<ProyectoDAO.TipoActividadItem>
            obtenerTiposActividad() throws Exception {

        return ProyectoAPI
                .obtenerTiposActividad()
                .stream()
                .map(item ->
                        new ProyectoDAO.TipoActividadItem(
                                item.idTipoActividad(),
                                item.nombre()
                        )
                )
                .toList();
    }

    /*
     * ============================================================
     * PROYECTOS
     * ============================================================
     */

    public static List<ProyectoDAO.ProyectoResumen>
            obtenerActivos() throws Exception {

        return ProyectoAPI
                .obtenerResumen()
                .stream()
                .map(item ->
                        new ProyectoDAO.ProyectoResumen(
                                item.idProyecto(),
                                item.codigoProyecto(),
                                item.empresa(),
                                item.descripcion(),
                                item.sector(),
                                item.piscina(),
                                item.fechaInicio(),
                                item.fechaFinEstimada(),
                                item.estado()
                        )
                )
                .toList();
    }

    public static ProyectoDAO.ProyectoDetalle
            obtenerPorId(
                    int idProyecto
            ) throws Exception {

        /*
         * IMPORTANTE:
         *
         * ProyectoAPI devuelve el nuevo ProyectoDetalle
         * independiente.
         *
         * Usamos var para evitar que Java interprete
         * ProyectoDetalle como ProyectoDAO.ProyectoDetalle.
         */
        var item =
                ProyectoAPI.obtenerPorId(
                        idProyecto
                );

        return new ProyectoDAO.ProyectoDetalle(
                item.idProyecto(),
                item.codigoProyecto(),
                item.descripcion(),
                item.idEmpresa(),
                item.idSector(),
                item.idPiscina(),
                item.ordenCompra(),
                item.idTipoActividad(),
                item.fechaInicio(),
                item.fechaFinEstimada(),
                item.fechaFinReal(),
                item.diasEstimados(),
                item.areaM2(),
                item.espesor(),
                item.factorCompactacion(),
                item.cantidadContratada(),
                item.metrosLinealesContratados(),
                item.precioUnitario(),
                item.estado(),
                item.observaciones()
        );
    }

    public static int insertar(
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
    ) throws Exception {

        return ProyectoAPI
                .crearProyecto(
                        codigoProyecto,
                        descripcion,
                        idEmpresa,
                        idSector,
                        idPiscina,
                        ordenCompra,
                        idTipoActividad,
                        fechaInicio,
                        fechaFinEstimada,
                        fechaFinReal,
                        diasEstimados,
                        areaM2,
                        espesor,
                        factorCompactacion,
                        cantidadContratada,
                        metrosLinealesContratados,
                        precioUnitario,
                        estado,
                        observaciones
                );
    }

    public static void actualizar(
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
    ) throws Exception {

        ProyectoAPI
                .actualizarProyecto(
                        idProyecto,
                        codigoProyecto,
                        descripcion,
                        idEmpresa,
                        idSector,
                        idPiscina,
                        ordenCompra,
                        idTipoActividad,
                        fechaInicio,
                        fechaFinEstimada,
                        fechaFinReal,
                        diasEstimados,
                        areaM2,
                        espesor,
                        factorCompactacion,
                        cantidadContratada,
                        metrosLinealesContratados,
                        precioUnitario,
                        estado,
                        observaciones
                );
    }

    public static void eliminar(
            int idProyecto
    ) throws Exception {

        ProyectoAPI
                .eliminarProyecto(
                        idProyecto
                );
    }
}