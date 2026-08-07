import java.util.List;

/*
 * PUENTE TEMPORAL DE COMPATIBILIDAD.
 *
 * Este DAO ya no accede directamente a MySQL.
 * Todas las operaciones se delegan
 * a TipoActividadProyectoAPI.
 */
public class TipoActividadProyectoDAO {

    public record TipoActividadResumen(
            int idTipoActividad,
            String nombreActividad,
            String descripcion
    ) {
    }

    public static List<
            TipoActividadProyectoDAO.TipoActividadResumen
    > obtenerActivos() throws Exception {

        return TipoActividadProyectoAPI
                .obtenerActivos()
                .stream()
                .map(item ->
                        new TipoActividadProyectoDAO
                                .TipoActividadResumen(
                                        item.idTipoActividad(),
                                        item.nombreActividad(),
                                        item.descripcion()
                                )
                )
                .toList();
    }

    public static TipoActividadProyectoDAO
            .TipoActividadResumen
            obtenerPorId(
                    int idTipoActividad
            ) throws Exception {

        var item =
                TipoActividadProyectoAPI
                        .obtenerPorId(
                                idTipoActividad
                        );

        return new TipoActividadProyectoDAO
                .TipoActividadResumen(
                        item.idTipoActividad(),
                        item.nombreActividad(),
                        item.descripcion()
                );
    }

    public static int insertar(
            String nombreActividad,
            String descripcion
    ) throws Exception {

        return TipoActividadProyectoAPI
                .insertar(
                        nombreActividad,
                        descripcion
                );
    }

    public static void actualizar(
            int idTipoActividad,
            String nombreActividad,
            String descripcion
    ) throws Exception {

        TipoActividadProyectoAPI
                .actualizar(
                        idTipoActividad,
                        nombreActividad,
                        descripcion
                );
    }

    public static int contarProyectosRelacionados(
            int idTipoActividad
    ) throws Exception {

        return TipoActividadProyectoAPI
                .contarProyectosRelacionados(
                        idTipoActividad
                );
    }

    public static void eliminar(
            int idTipoActividad
    ) throws Exception {

        TipoActividadProyectoAPI
                .eliminar(
                        idTipoActividad
                );
    }
}