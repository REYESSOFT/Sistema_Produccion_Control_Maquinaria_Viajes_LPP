import java.util.List;

/*
 * PUENTE TEMPORAL DE COMPATIBILIDAD.
 *
 * Este DAO ya no accede directamente a MySQL.
 * Todas las operaciones se delegan a PiscinaProyectoAPI.
 */
public class PiscinaProyectoDAO {

    public record PiscinaResumen(
            int idPiscina,
            int idSector,
            String nombreSector,
            String nombrePiscina,
            String descripcion
    ) {
    }

    public static List<PiscinaProyectoDAO.PiscinaResumen>
            obtenerActivas() throws Exception {

        return PiscinaProyectoAPI
                .obtenerActivas()
                .stream()
                .map(item ->
                        new PiscinaProyectoDAO.PiscinaResumen(
                                item.idPiscina(),
                                item.idSector(),
                                item.nombreSector(),
                                item.nombrePiscina(),
                                item.descripcion()
                        )
                )
                .toList();
    }

    public static PiscinaProyectoDAO.PiscinaResumen
            obtenerPorId(
                    int idPiscina
            ) throws Exception {

        var item =
                PiscinaProyectoAPI
                        .obtenerPorId(
                                idPiscina
                        );

        return new PiscinaProyectoDAO.PiscinaResumen(
                item.idPiscina(),
                item.idSector(),
                item.nombreSector(),
                item.nombrePiscina(),
                item.descripcion()
        );
    }

    public static int insertar(
            int idSector,
            String nombrePiscina,
            String descripcion
    ) throws Exception {

        return PiscinaProyectoAPI
                .insertar(
                        idSector,
                        nombrePiscina,
                        descripcion
                );
    }

    public static void actualizar(
            int idPiscina,
            int idSector,
            String nombrePiscina,
            String descripcion
    ) throws Exception {

        PiscinaProyectoAPI
                .actualizar(
                        idPiscina,
                        idSector,
                        nombrePiscina,
                        descripcion
                );
    }

    public static void eliminar(
            int idPiscina
    ) throws Exception {

        PiscinaProyectoAPI
                .eliminar(
                        idPiscina
                );
    }
}