import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DetalleProyectoDialog extends JDialog {

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern(
                    "dd/MM/yyyy"
            );

    public DetalleProyectoDialog(
            Window parent,
            int idProyecto
    ) {

        super(
                parent,
                "Detalle del proyecto",
                ModalityType.APPLICATION_MODAL
        );

        setSize(900, 680);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(
                DISPOSE_ON_CLOSE
        );

        cargarDetalle(idProyecto);
    }

    private void cargarDetalle(
            int idProyecto
    ) {

        try {

            ProyectoDAO.ProyectoDetalle proyecto =
                    ProyectoDAO.obtenerPorId(
                            idProyecto
                    );

            JPanel principal =
                    new JPanel(
                            new BorderLayout(
                                    10,
                                    10
                            )
                    );

            principal.setBorder(
                    BorderFactory.createEmptyBorder(
                            15,
                            15,
                            15,
                            15
                    )
            );

            JPanel datos =
                    new JPanel(
                            new GridLayout(
                                    17,
                                    2,
                                    10,
                                    8
                            )
                    );

            datos.setBorder(
                    BorderFactory.createTitledBorder(
                            "Información del proyecto"
                    )
            );

            agregarDato(
                    datos,
                    "ID:",
                    String.valueOf(
                            proyecto.idProyecto()
                    )
            );

            agregarDato(
                    datos,
                    "Código:",
                    proyecto.codigoProyecto()
            );

            agregarDato(
                    datos,
                    "Descripción:",
                    proyecto.descripcion()
            );

            agregarDato(
                    datos,
                    "Empresa:",
                    obtenerNombreEmpresa(
                            proyecto.idEmpresa()
                    )
            );

            agregarDato(
                    datos,
                    "Sector:",
                    obtenerNombreSector(
                            proyecto.idSector()
                    )
            );

            agregarDato(
                    datos,
                    "Piscina:",
                    obtenerNombrePiscina(
                            proyecto.idPiscina()
                    )
            );

            agregarDato(
                    datos,
                    "Orden de compra:",
                    proyecto.ordenCompra()
            );

            agregarDato(
                    datos,
                    "Tipo de actividad:",
                    obtenerNombreActividad(
                            proyecto.idTipoActividad()
                    )
            );

            agregarDato(
                    datos,
                    "Fecha de inicio:",
                    formatearFecha(
                            proyecto.fechaInicio()
                    )
            );

            agregarDato(
                    datos,
                    "Fecha fin estimada:",
                    formatearFecha(
                            proyecto.fechaFinEstimada()
                    )
            );

            agregarDato(
                    datos,
                    "Días estimados:",
                    formatearValor(
                            proyecto.diasEstimados()
                    )
            );

            agregarDato(
                    datos,
                    "Área m²:",
                    formatearValor(
                            proyecto.areaM2()
                    )
            );

            agregarDato(
                    datos,
                    "Espesor:",
                    formatearValor(
                            proyecto.espesor()
                    )
            );

            agregarDato(
                    datos,
                    "Factor compactación:",
                    formatearValor(
                            proyecto.factorCompactacion()
                    )
            );

            agregarDato(
                    datos,
                    "Cantidad contratada:",
                    formatearValor(
                            proyecto.cantidadContratada()
                    )
            );

            agregarDato(
                    datos,
                    "Precio unitario:",
                    proyecto.precioUnitario() == null
                            ? "-"
                            : String.format(
                                    "$%.2f",
                                    proyecto.precioUnitario()
                            )
            );

            agregarDato(
                    datos,
                    "Estado:",
                    proyecto.estado()
            );

            JTextArea observaciones =
                    new JTextArea(
                            proyecto.observaciones()
                    );

            observaciones.setEditable(false);
            observaciones.setLineWrap(true);
            observaciones.setWrapStyleWord(true);

            JScrollPane scrollObservaciones =
                    new JScrollPane(
                            observaciones
                    );

            scrollObservaciones.setBorder(
                    BorderFactory.createTitledBorder(
                            "Observaciones"
                    )
            );

            JButton btnCerrar =
                    new JButton(
                            "Cerrar"
                    );

            btnCerrar.addActionListener(
                    e -> dispose()
            );

            JPanel panelBoton =
                    new JPanel(
                            new FlowLayout(
                                    FlowLayout.RIGHT
                            )
                    );

            panelBoton.add(btnCerrar);

            principal.add(
                    new JScrollPane(datos),
                    BorderLayout.CENTER
            );

            JPanel inferior =
                    new JPanel(
                            new BorderLayout(
                                    10,
                                    10
                            )
                    );

            inferior.add(
                    scrollObservaciones,
                    BorderLayout.CENTER
            );

            inferior.add(
                    panelBoton,
                    BorderLayout.SOUTH
            );

            principal.add(
                    inferior,
                    BorderLayout.SOUTH
            );

            setContentPane(principal);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar el detalle del proyecto:\n"
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            dispose();
        }
    }

    private void agregarDato(
            JPanel panel,
            String etiqueta,
            String valor
    ) {

        JLabel lblEtiqueta =
                new JLabel(etiqueta);

        lblEtiqueta.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        JLabel lblValor =
                new JLabel(
                        valor == null
                        || valor.isBlank()
                                ? "-"
                                : valor
                );

        panel.add(lblEtiqueta);
        panel.add(lblValor);
    }

    private String obtenerNombreEmpresa(
            int idEmpresa
    ) throws Exception {

        for (
                ProyectoDAO.EmpresaItem empresa
                : ProyectoDAO.obtenerEmpresas()
        ) {

            if (
                empresa.idEmpresa()
                        == idEmpresa
            ) {

                return empresa.nombre();
            }
        }

        return "-";
    }

    private String obtenerNombreSector(
            Integer idSector
    ) throws Exception {

        if (idSector == null) {
            return "-";
        }

        for (
                ProyectoDAO.SectorItem sector
                : ProyectoDAO.obtenerSectores()
        ) {

            if (
                sector.idSector()
                        == idSector
            ) {

                return sector.nombre();
            }
        }

        return "-";
    }

    private String obtenerNombrePiscina(
            Integer idPiscina
    ) throws Exception {

        if (idPiscina == null) {
            return "-";
        }

        for (
                ProyectoDAO.PiscinaItem piscina
                : ProyectoDAO.obtenerPiscinasPorSector(
                        null
                )
        ) {

            if (
                piscina.idPiscina()
                        == idPiscina
            ) {

                return piscina.nombre();
            }
        }

        return "-";
    }

    private String obtenerNombreActividad(
            Integer idTipoActividad
    ) throws Exception {

        if (idTipoActividad == null) {
            return "-";
        }

        for (
                ProyectoDAO.TipoActividadItem actividad
                : ProyectoDAO.obtenerTiposActividad()
        ) {

            if (
                actividad.idTipoActividad()
                        == idTipoActividad
            ) {

                return actividad.nombre();
            }
        }

        return "-";
    }

    private String formatearFecha(
            LocalDate fecha
    ) {

        return fecha == null
                ? "-"
                : fecha.format(
                        FORMATO_FECHA
                );
    }

    private String formatearValor(
            Object valor
    ) {

        return valor == null
                ? "-"
                : valor.toString();
    }
}