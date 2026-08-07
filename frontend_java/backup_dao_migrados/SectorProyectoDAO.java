import java.util.List;

/*
 * PUENTE TEMPORAL DE COMPATIBILIDAD.
 *
 * Este DAO ya no accede directamente a MySQL.
 * Todas las operaciones se delegan a SectorProyectoAPI.
 *
 * Se conserva temporalmente porque algunas pantallas
 * todavía utilizan su record SectorResumen.
 */
public class SectorProyectoDAO {

    public record SectorResumen(
            int idSector,
            String nombreSector,
            String descripcion
    ) {
    }

    public static List<SectorProyectoDAO.SectorResumen>
            obtenerActivos() throws Exception {

        return SectorProyectoAPI
                .obtenerActivos()
                .stream()
                .map(item ->
                        new SectorProyectoDAO.SectorResumen(
                                item.idSector(),
                                item.nombreSector(),
                                item.descripcion()
                        )
                )
                .toList();
    }

    public static int insertar(
            String nombreSector,
            String descripcion
    ) throws Exception {

        return SectorProyectoAPI
                .insertar(
                        nombreSector,
                        descripcion
                );
    }

    public static SectorProyectoDAO.SectorResumen
            obtenerPorId(
                    int idSector
            ) throws Exception {

        var item =
                SectorProyectoAPI.obtenerPorId(
                        idSector
                );

        return new SectorProyectoDAO.SectorResumen(
                item.idSector(),
                item.nombreSector(),
                item.descripcion()
        );
    }

    public static void actualizar(
            int idSector,
            String nombreSector,
            String descripcion
    ) throws Exception {

        SectorProyectoAPI
                .actualizar(
                        idSector,
                        nombreSector,
                        descripcion
                );
    }

    public static void eliminar(
            int idSector
    ) throws Exception {

        SectorProyectoAPI
                .eliminar(
                        idSector
                );
    }
}