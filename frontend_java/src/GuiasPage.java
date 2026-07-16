import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GuiasPage extends JPanel {

    private JComboBox<String> cboEmpresa;
    private JComboBox<String> cboTipoGuia;
    private JTextField txtNumeroGuia;
    private JTable tablaGuias;

    public GuiasPage() {

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        setBackground(new Color(244, 246, 248));

        crearInterfaz();
    }

    private void crearInterfaz() {

        JLabel titulo = new JLabel("Guías de Trabajo");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));

        add(titulo, BorderLayout.NORTH);

        JPanel panelCentral = new JPanel(new BorderLayout(15, 15));
        panelCentral.setOpaque(false);


        JPanel panelFiltros = new JPanel();
        panelFiltros.setLayout(
                new BoxLayout(panelFiltros, BoxLayout.Y_AXIS)
        );
        panelFiltros.setOpaque(false);

        JPanel panelBusqueda = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 10, 5)
        );
        panelBusqueda.setOpaque(false);

        JPanel panelAcciones = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 10, 5)
        );
        panelAcciones.setOpaque(false);

        cboEmpresa = new JComboBox<>(new String[]{
                "Todas",
                "EQUIPOS PRO",
                "DEVIALTRANSPORT"
        });

        cboTipoGuia = new JComboBox<>(new String[]{
                "Todas",
                "Guía Producción Volquetas",
                "Guía Trabajo Diario Maquinaria",
                "Control Trabajo Volquetas",
                "Guía Despacho de Material"
        });

        txtNumeroGuia = new JTextField(12);

        JButton btnBuscar = new JButton("Buscar");
        JButton btnNuevaGuia = new JButton("Nueva Guía");
        JButton btnAprobar = new JButton("Aprobar guía");
        JButton btnEditar = new JButton("Editar guía");
        JButton btnEliminar = new JButton("Eliminar guía");
        JButton btnDetalle = new JButton("Detalle");

        panelBusqueda.add(new JLabel("Empresa:"));
        panelBusqueda.add(cboEmpresa);

        panelBusqueda.add(new JLabel("Tipo de guía:"));
        panelBusqueda.add(cboTipoGuia);

        panelBusqueda.add(new JLabel("N° Guía:"));
        panelBusqueda.add(txtNumeroGuia);

        panelBusqueda.add(btnBuscar);

        panelAcciones.add(btnNuevaGuia);
        panelAcciones.add(btnAprobar);
        panelAcciones.add(btnEditar);
        panelAcciones.add(btnEliminar);
        panelAcciones.add(btnDetalle);

        panelFiltros.add(panelBusqueda);
        panelFiltros.add(panelAcciones);

        panelCentral.add(panelFiltros, BorderLayout.NORTH);


        String[] columnas = {
                "Empresa",
                "Tipo Guía",
                "N° Guía",
                "Fecha",
                "Chofer / Operador",
                "Placa",
                "M3",
                "Estado"
        };

        
        DefaultTableModel modelo =
                new DefaultTableModel(columnas, 0) {

                        @Override
                        public boolean isCellEditable(
                                int fila,
                                int columna
                        ) {
                                return false;
                        }
                };

        tablaGuias = new JTable(modelo);
        tablaGuias.setRowHeight(26);

        tablaGuias.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (int columna = 0; columna < tablaGuias.getColumnCount(); columna++) {

                tablaGuias.getColumnModel()
                        .getColumn(columna)
                        .setPreferredWidth(140);
                }

                tablaGuias.getColumnModel()
                        .getColumn(0)
                        .setPreferredWidth(160);

                tablaGuias.getColumnModel()
                        .getColumn(1)
                        .setPreferredWidth(230);

                tablaGuias.getColumnModel()
                        .getColumn(4)
                        .setPreferredWidth(200);

                tablaGuias.getColumnModel()
                        .getColumn(7)
                        .setPreferredWidth(120);

        int columnaEstado = tablaGuias.getColumnModel()
                .getColumnIndex("Estado");

        tablaGuias.getColumnModel()
                .getColumn(columnaEstado)
                .setCellRenderer(new EstadoGuiaRenderer());

        tablaGuias.getTableHeader().setFont(
                new Font("Segoe UI", Font.BOLD, 13)
        );

        JScrollPane scroll = new JScrollPane(
                tablaGuias,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
        );

        cargarGuiasDesdeMySQL();

        panelCentral.add(scroll, BorderLayout.CENTER);

        add(panelCentral, BorderLayout.CENTER);

        btnNuevaGuia.addActionListener(e -> abrirSelectorGuia());

        btnAprobar.addActionListener(e -> aprobarGuiaSeleccionada());

        btnEditar.addActionListener(e -> editarGuiaSeleccionada());

        btnBuscar.addActionListener(e -> buscarGuias());

        txtNumeroGuia.addActionListener(e -> buscarGuias());

        btnEliminar.addActionListener(e -> eliminarGuiaSeleccionada());

        btnDetalle.addActionListener(e -> mostrarDetalleGuiaSeleccionada()
);
    }

    private void abrirSelectorGuia() {

        String[] empresas = {
                "EQUIPOS PRO",
                "DEVIALTRANSPORT"
        };

        String empresa = (String) JOptionPane.showInputDialog(
                this,
                "Seleccione la empresa:",
                "Nueva Guía",
                JOptionPane.PLAIN_MESSAGE,
                null,
                empresas,
                empresas[0]
        );

        if (empresa == null) {
            return;
        }

        String[] tipos;

        if (empresa.equals("EQUIPOS PRO")) {

            tipos = new String[]{
                    "Guía Producción Volquetas",
                    "Guía Trabajo Diario Maquinaria"
            };

        } else {

            tipos = new String[]{
                    "Control Trabajo Volquetas",
                    "Guía Despacho de Material"
            };
        }

        String tipoGuia = (String) JOptionPane.showInputDialog(
                this,
                "Seleccione el tipo de guía:",
                "Nueva Guía",
                JOptionPane.PLAIN_MESSAGE,
                null,
                tipos,
                tipos[0]
        );

        if (tipoGuia == null) {
            return;
        }


        if (
                empresa.equals("EQUIPOS PRO")
                && tipoGuia.equals("Guía Producción Volquetas")
        ) {

                FormGuiaProduccionVolquetas formulario =
                        new FormGuiaProduccionVolquetas(
                                SwingUtilities.getWindowAncestor(this)
                        );

                formulario.setVisible(true);
                cargarGuiasDesdeMySQL();

                } else if (
                        empresa.equals("EQUIPOS PRO")
                        && tipoGuia.equals("Guía Trabajo Diario Maquinaria")
                ) {

                FormGuiaTrabajoMaquinaria formulario =
                        new FormGuiaTrabajoMaquinaria(
                                SwingUtilities.getWindowAncestor(this)
                        );

                formulario.setVisible(true);
                cargarGuiasDesdeMySQL();

                } else if (
                        empresa.equals("DEVIALTRANSPORT")
                        && tipoGuia.equals("Control Trabajo Volquetas")
                ) {

                FormControlTrabajoVolquetas formulario =
                        new FormControlTrabajoVolquetas(
                                SwingUtilities.getWindowAncestor(this)
                        );

                formulario.setVisible(true);
                cargarGuiasDesdeMySQL();


                } else if (
                        empresa.equals("DEVIALTRANSPORT")
                        && tipoGuia.equals(
                                "Guía Despacho de Material"
                        )
                ) {

                FormGuiaDespachoMaterial formulario =
                        new FormGuiaDespachoMaterial(
                                SwingUtilities.getWindowAncestor(this)
                        );

                formulario.setVisible(true);

                cargarGuiasDesdeMySQL();

        } else {

                JOptionPane.showMessageDialog(
                        this,
                        "El formulario de \"" + tipoGuia
                                + "\" todavía está pendiente.",
                        "LPP Smart ERP",
                        JOptionPane.INFORMATION_MESSAGE
                );
        }
    }


    private void cargarGuiasDesdeMySQL() {

        DefaultTableModel modelo =
                (DefaultTableModel) tablaGuias.getModel();

        modelo.setRowCount(0);

        String sql = """
                SELECT
                        e.nombre_empresa,
                        g.tipo_guia,
                        g.numero_guia,
                        DATE_FORMAT(g.fecha, '%d/%m/%Y') AS fecha,
                        COALESCE(g.chofer_operador, '') AS chofer_operador,
                        COALESCE(g.placa, '') AS placa,
                        COALESCE(g.m3, 0) AS m3,
                        g.estado
                FROM guias g
                INNER JOIN empresas e
                        ON e.id_empresa = g.id_empresa
                ORDER BY g.id_guia DESC
                """;

        try (
                Connection conexion = ConexionDB.obtenerConexion();
                 PreparedStatement statement =
                        conexion.prepareStatement(sql);
                ResultSet resultado =
                        statement.executeQuery()
        ) {

                while (resultado.next()) {

                modelo.addRow(new Object[]{
                        resultado.getString("nombre_empresa"),
                        resultado.getString("tipo_guia"),
                        resultado.getString("numero_guia"),
                        resultado.getString("fecha"),
                        resultado.getString("chofer_operador"),
                        resultado.getString("placa"),
                        resultado.getDouble("m3"),
                        resultado.getString("estado")
                });
                }

        } catch (Exception e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Error al cargar las guías:\n"
                        + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );

                e.printStackTrace();
        }
    }

    



    private void buscarGuias() {

    DefaultTableModel modelo =
            (DefaultTableModel) tablaGuias.getModel();

    modelo.setRowCount(0);

    String empresaSeleccionada =
            cboEmpresa.getSelectedItem().toString();

    String tipoGuiaSeleccionado =
            cboTipoGuia.getSelectedItem().toString();

    String numeroGuia =
            txtNumeroGuia.getText().trim();

    StringBuilder sql = new StringBuilder("""
            SELECT
                    e.nombre_empresa,
                    g.tipo_guia,
                    g.numero_guia,
                    DATE_FORMAT(g.fecha, '%d/%m/%Y') AS fecha,
                    COALESCE(g.chofer_operador, '') AS chofer_operador,
                    COALESCE(g.placa, '') AS placa,
                    COALESCE(g.m3, 0) AS m3,
                    g.estado
            FROM guias g
            INNER JOIN empresas e
                    ON e.id_empresa = g.id_empresa
            WHERE 1 = 1
            """);

    if (!empresaSeleccionada.equals("Todas")) {
        sql.append(" AND e.nombre_empresa = ? ");
    }

    if (!tipoGuiaSeleccionado.equals("Todas")) {
        sql.append(" AND g.tipo_guia = ? ");
    }

    if (!numeroGuia.isEmpty()) {
        sql.append(" AND g.numero_guia LIKE ? ");
    }

    sql.append(" ORDER BY g.id_guia DESC ");

    try (
            Connection conexion =
                    ConexionDB.obtenerConexion();

            PreparedStatement statement =
                    conexion.prepareStatement(sql.toString())
    ) {

        int parametro = 1;

        if (!empresaSeleccionada.equals("Todas")) {

            statement.setString(
                    parametro++,
                    empresaSeleccionada
            );
        }

        if (!tipoGuiaSeleccionado.equals("Todas")) {

            statement.setString(
                    parametro++,
                    tipoGuiaSeleccionado
            );
        }

        if (!numeroGuia.isEmpty()) {

            statement.setString(
                    parametro,
                    "%" + numeroGuia + "%"
            );
        }

        try (
                ResultSet resultado =
                        statement.executeQuery()
        ) {

            while (resultado.next()) {

                modelo.addRow(new Object[]{
                        resultado.getString("nombre_empresa"),
                        resultado.getString("tipo_guia"),
                        resultado.getString("numero_guia"),
                        resultado.getString("fecha"),
                        resultado.getString("chofer_operador"),
                        resultado.getString("placa"),
                        resultado.getDouble("m3"),
                        resultado.getString("estado")
                });
            }
        }

        if (modelo.getRowCount() == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se encontraron guías con los filtros seleccionados.",
                    "Búsqueda",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }

    } catch (Exception e) {

        JOptionPane.showMessageDialog(
                this,
                "Error al buscar las guías:\n"
                        + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );

        e.printStackTrace();
    }
}




    private void aprobarGuiaSeleccionada() {

        int filaSeleccionada = tablaGuias.getSelectedRow();

        if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(
                        this,
                        "Seleccione una guía en la tabla.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
        }



        String empresa = tablaGuias
                .getValueAt(filaSeleccionada, 0)
                .toString();

        String tipoGuia = tablaGuias
                .getValueAt(filaSeleccionada, 1)
                .toString();

        String numeroGuia = tablaGuias
                .getValueAt(filaSeleccionada, 2)
                .toString();

        String estadoActual = tablaGuias
                .getValueAt(filaSeleccionada, 7)
                .toString();


        if (estadoActual.equalsIgnoreCase("APROBADO")) {
                JOptionPane.showMessageDialog(
                        this,
                        "La guía ya está aprobada.",
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Desea aprobar la guía N° " + numeroGuia + "?",
                "Confirmar aprobación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
                return;
        }

       String sql = """
                UPDATE guias g
                INNER JOIN empresas e
                        ON e.id_empresa = g.id_empresa
                SET g.estado = 'APROBADO'
                WHERE e.nombre_empresa = ?
                AND g.tipo_guia = ?
                AND g.numero_guia = ?
                AND g.estado = 'PENDIENTE'
                """;

        try (
                Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement statement = conexion.prepareStatement(sql)
        ) {
                statement.setString(1, empresa);
                statement.setString(2, tipoGuia);
                statement.setString(3, numeroGuia);

                int filasActualizadas = statement.executeUpdate();

                if (filasActualizadas > 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Guía aprobada correctamente.",
                        "LPP Smart ERP",
                        JOptionPane.INFORMATION_MESSAGE
                );

                cargarGuiasDesdeMySQL();

                } else {
                        JOptionPane.showMessageDialog(
                                this,
                                "No se encontró la guía para aprobar.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                }

        } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        this,
                        "Error al aprobar la guía:\n" + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );

                e.printStackTrace();
        }
    }

    private void editarGuiaSeleccionada() {

        int filaSeleccionada = tablaGuias.getSelectedRow();

        if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(
                        this,
                        "Seleccione una guía en la tabla.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
        }

        String numeroGuia = tablaGuias
                .getValueAt(filaSeleccionada, 2)
                .toString();

        String tipoGuia = tablaGuias
                .getValueAt(filaSeleccionada, 1)
                .toString();

        String estado = tablaGuias
                .getValueAt(filaSeleccionada, 7)
                .toString();

        if (estado.equalsIgnoreCase("APROBADO")) {
                JOptionPane.showMessageDialog(
                        this,
                        "Una guía aprobada no puede editarse.",
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return;
        }


        if (tipoGuia.equals("Guía Producción Volquetas")) {

    FormGuiaProduccionVolquetas formulario =
            new FormGuiaProduccionVolquetas(
                    SwingUtilities.getWindowAncestor(this)
            );

    formulario.cargarGuia(numeroGuia);
    formulario.setVisible(true);

    cargarGuiasDesdeMySQL();

} else if (
    tipoGuia.equals("Control Trabajo Volquetas")
) {



} else if (
        tipoGuia.equals(
                "Guía Trabajo Diario Maquinaria"
        )
) {

    FormGuiaTrabajoMaquinaria formulario =
            new FormGuiaTrabajoMaquinaria(
                    SwingUtilities.getWindowAncestor(this)
            );

    formulario.cargarGuia(numeroGuia);
    formulario.setVisible(true);

    cargarGuiasDesdeMySQL();


    } else if (
        tipoGuia.equals(
                "Guía Despacho de Material"
        )
) {

    FormGuiaDespachoMaterial formulario =
            new FormGuiaDespachoMaterial(
                    SwingUtilities.getWindowAncestor(this)
            );

    formulario.cargarGuia(numeroGuia);
    formulario.setVisible(true);

    cargarGuiasDesdeMySQL();

} else {

    JOptionPane.showMessageDialog(
            this,
            "La edición de este tipo de guía todavía no está disponible.",
            "Información",
            JOptionPane.INFORMATION_MESSAGE
    );
}

    }

    private void eliminarGuiaSeleccionada() {

        int filaSeleccionada = tablaGuias.getSelectedRow();

        if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(
                        this,
                        "Seleccione una guía en la tabla.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
        }

        String empresa = tablaGuias
                .getValueAt(filaSeleccionada, 0)
                .toString();

        String tipoGuia = tablaGuias
                .getValueAt(filaSeleccionada, 1)
                .toString();

        String numeroGuia = tablaGuias
                .getValueAt(filaSeleccionada, 2)
                .toString();

        String estado = tablaGuias
                .getValueAt(filaSeleccionada, 7)
                .toString();

        if (estado.equalsIgnoreCase("APROBADO")) {
                JOptionPane.showMessageDialog(
                        this,
                        "No es posible eliminar una guía aprobada.",
                        "Operación no permitida",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de eliminar la guía N° "
                        + numeroGuia
                        + "?\n\nEsta acción no se puede deshacer.",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
                return;
        }

        String sql = """
                DELETE g
                FROM guias g
                INNER JOIN empresas e
                        ON e.id_empresa = g.id_empresa
                WHERE e.nombre_empresa = ?
                AND g.tipo_guia = ?
                AND g.numero_guia = ?
                AND g.estado = 'PENDIENTE'
                """;

        try (
                Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement statement =
                        conexion.prepareStatement(sql)
        ) {

                statement.setString(1, empresa);
                statement.setString(2, tipoGuia);
                statement.setString(3, numeroGuia);

                int filasEliminadas = statement.executeUpdate();

                if (filasEliminadas > 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Guía eliminada correctamente.",
                        "LPP Smart ERP",
                        JOptionPane.INFORMATION_MESSAGE
                );

                cargarGuiasDesdeMySQL();




                } else {

                JOptionPane.showMessageDialog(
                        this,
                        "La guía no pudo eliminarse.\n"
                                + "Puede haber sido aprobada o eliminada previamente.",
                        "Información",
                        JOptionPane.WARNING_MESSAGE
                );
                }

        } catch (Exception e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Error al eliminar la guía:\n"
                                + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );

                e.printStackTrace();
        }
    }



    private void mostrarDetalleGuiaSeleccionada() {

    int filaSeleccionada =
            tablaGuias.getSelectedRow();

    if (filaSeleccionada == -1) {

        JOptionPane.showMessageDialog(
                this,
                "Seleccione una guía en la tabla.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );

        return;
    }


    String empresa = tablaGuias
        .getValueAt(filaSeleccionada, 0)
        .toString();

    String tipoGuia = tablaGuias
        .getValueAt(filaSeleccionada, 1)
        .toString();

    String numeroGuia = tablaGuias
        .getValueAt(filaSeleccionada, 2)
        .toString();


if (tipoGuia.equals("Control Trabajo Volquetas")) {

    DetalleControlTrabajoDialog detalle =
            new DetalleControlTrabajoDialog(
                    SwingUtilities.getWindowAncestor(this),
                    empresa,
                    numeroGuia
            );

    detalle.setVisible(true);
    return;
}


if (
        tipoGuia.equals(
                "Guía Trabajo Diario Maquinaria"
        )
) {

    DetalleGuiaTrabajoMaquinariaDialog detalle =
            new DetalleGuiaTrabajoMaquinariaDialog(
                    SwingUtilities.getWindowAncestor(this),
                    empresa,
                    numeroGuia
            );

    detalle.setVisible(true);
    return;
}


if (
        tipoGuia.equals(
                "Guía Despacho de Material"
        )
) {

    DetalleGuiaDespachoMaterialDialog detalle =
            new DetalleGuiaDespachoMaterialDialog(
                    SwingUtilities.getWindowAncestor(this),
                    empresa,
                    numeroGuia
            );

    detalle.setVisible(true);
    return;
}


if (!tipoGuia.equals("Guía Producción Volquetas")) {

    JOptionPane.showMessageDialog(
            this,
            "La visualización del detalle todavía no está disponible "
                    + "para este tipo de guía.",
            "Información",
            JOptionPane.INFORMATION_MESSAGE
    );

    return;
}


    String sqlCabecera = """
            SELECT
                    g.id_guia,
                    e.nombre_empresa,
                    g.numero_guia,
                    DATE_FORMAT(g.fecha, '%d/%m/%Y') AS fecha,
                    COALESCE(g.chofer_operador, '') AS chofer_operador,
                    COALESCE(g.placa, '') AS placa,
                    COALESCE(g.m3, 0) AS m3,
                    COALESCE(g.recibi_conforme, '') AS recibi_conforme,
                    COALESCE(g.observaciones, '') AS observaciones,
                    g.estado
            FROM guias g
            INNER JOIN empresas e
                    ON e.id_empresa = g.id_empresa
            WHERE e.nombre_empresa = ?
                AND g.numero_guia = ?
                AND g.tipo_guia = ?
            LIMIT 1
            """;

    String sqlDetalle = """
            SELECT
                    numero_fila,
                    COALESCE(proyecto, '') AS proyecto,
                    COALESCE(sector, '') AS sector,
                    COALESCE(cantera, '') AS cantera,
                    COALESCE(material, '') AS material,
                    TIME_FORMAT(hora_origen, '%H:%i') AS hora_origen,
                    TIME_FORMAT(hora_destino, '%H:%i') AS hora_destino
            FROM guia_produccion_detalle
            WHERE id_guia = ?
            ORDER BY numero_fila
            """;

    try (
            Connection conexion =
                    ConexionDB.obtenerConexion();

            PreparedStatement psCabecera =
                    conexion.prepareStatement(sqlCabecera)
    ) {

        psCabecera.setString(1, empresa);
        psCabecera.setString(2, numeroGuia);
        psCabecera.setString(3, tipoGuia);

        try (
                ResultSet cabecera =
                        psCabecera.executeQuery()
        ) {

            if (!cabecera.next()) {

                JOptionPane.showMessageDialog(
                        this,
                        "No se encontró la guía seleccionada.",
                        "Información",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            int idGuia =
                    cabecera.getInt("id_guia");

            JDialog ventanaDetalle =
                    new JDialog(
                            SwingUtilities.getWindowAncestor(this),
                            "Detalle Guía N° " + numeroGuia,
                            Dialog.ModalityType.APPLICATION_MODAL
                    );

            ventanaDetalle.setSize(950, 600);
            ventanaDetalle.setLocationRelativeTo(this);
            ventanaDetalle.setDefaultCloseOperation(
                    JDialog.DISPOSE_ON_CLOSE
            );

            JPanel panelPrincipal =
                    new JPanel(new BorderLayout(10, 10));

            panelPrincipal.setBorder(
                    BorderFactory.createEmptyBorder(
                            15,
                            15,
                            15,
                            15
                    )
            );

            JPanel panelCabecera =
                    new JPanel(new GridLayout(4, 4, 10, 8));

            panelCabecera.add(
                    new JLabel("Empresa:")
            );

            panelCabecera.add(
                    new JLabel(
                            cabecera.getString("nombre_empresa")
                    )
            );

            panelCabecera.add(
                    new JLabel("N° Guía:")
            );

            panelCabecera.add(
                    new JLabel(
                            cabecera.getString("numero_guia")
                    )
            );

            panelCabecera.add(
                    new JLabel("Fecha:")
            );

            panelCabecera.add(
                    new JLabel(
                            cabecera.getString("fecha")
                    )
            );

            panelCabecera.add(
                    new JLabel("Estado:")
            );

            panelCabecera.add(
                    new JLabel(
                            cabecera.getString("estado")
                    )
            );

            panelCabecera.add(
                    new JLabel("Chofer / Operador:")
            );

            panelCabecera.add(
                    new JLabel(
                            cabecera.getString("chofer_operador")
                    )
            );

            panelCabecera.add(
                    new JLabel("Placa:")
            );

            panelCabecera.add(
                    new JLabel(
                            cabecera.getString("placa")
                    )
            );

            panelCabecera.add(
                    new JLabel("M3:")
            );

            panelCabecera.add(
                    new JLabel(
                            String.valueOf(
                                    cabecera.getDouble("m3")
                            )
                    )
            );

            panelCabecera.add(new JLabel());
            panelCabecera.add(new JLabel());

            String[] columnasDetalle = {
                    "N°",
                    "Proyecto",
                    "Sector",
                    "Cantera",
                    "Material",
                    "Hora Origen",
                    "Hora Destino"
            };

            DefaultTableModel modeloDetalle =
                    new DefaultTableModel(
                            columnasDetalle,
                            0
                    ) {

                        @Override
                        public boolean isCellEditable(
                                int fila,
                                int columna
                        ) {
                            return false;
                        }
                    };

            JTable tablaDetalle =
                    new JTable(modeloDetalle);

            tablaDetalle.setRowHeight(26);
            tablaDetalle.setAutoResizeMode(
                    JTable.AUTO_RESIZE_OFF
            );

            tablaDetalle.getColumnModel()
                    .getColumn(0)
                    .setPreferredWidth(50);

            tablaDetalle.getColumnModel()
                    .getColumn(1)
                    .setPreferredWidth(180);

            tablaDetalle.getColumnModel()
                    .getColumn(2)
                    .setPreferredWidth(150);

            tablaDetalle.getColumnModel()
                    .getColumn(3)
                    .setPreferredWidth(170);

            tablaDetalle.getColumnModel()
                    .getColumn(4)
                    .setPreferredWidth(170);

            tablaDetalle.getColumnModel()
                    .getColumn(5)
                    .setPreferredWidth(110);

            tablaDetalle.getColumnModel()
                    .getColumn(6)
                    .setPreferredWidth(110);

            try (
                    PreparedStatement psDetalle =
                            conexion.prepareStatement(
                                    sqlDetalle
                            )
            ) {

                psDetalle.setInt(1, idGuia);

                try (
                        ResultSet detalle =
                                psDetalle.executeQuery()
                ) {

                    while (detalle.next()) {

                        modeloDetalle.addRow(
                                new Object[]{
                                        detalle.getInt(
                                                "numero_fila"
                                        ),
                                        detalle.getString(
                                                "proyecto"
                                        ),
                                        detalle.getString(
                                                "sector"
                                        ),
                                        detalle.getString(
                                                "cantera"
                                        ),
                                        detalle.getString(
                                                "material"
                                        ),
                                        detalle.getString(
                                                "hora_origen"
                                        ),
                                        detalle.getString(
                                                "hora_destino"
                                        )
                                }
                        );
                    }
                }
            }

            JPanel panelInferior =
                    new JPanel(new BorderLayout(10, 10));

            JTextArea txtInformacion =
                    new JTextArea();

            txtInformacion.setEditable(false);
            txtInformacion.setLineWrap(true);
            txtInformacion.setWrapStyleWord(true);

            txtInformacion.setText(
                    "Recibí conforme: "
                            + cabecera.getString(
                                    "recibi_conforme"
                            )
                            + "\n\nObservaciones: "
                            + cabecera.getString(
                                    "observaciones"
                            )
            );

            JButton btnCerrar =
                    new JButton("Cerrar");

            btnCerrar.addActionListener(
                    e -> ventanaDetalle.dispose()
            );

            JPanel panelBoton =
                    new JPanel(
                            new FlowLayout(
                                    FlowLayout.RIGHT
                            )
                    );

            panelBoton.add(btnCerrar);

            panelInferior.add(
                    new JScrollPane(txtInformacion),
                    BorderLayout.CENTER
            );

            panelInferior.add(
                    panelBoton,
                    BorderLayout.SOUTH
            );

            panelPrincipal.add(
                    panelCabecera,
                    BorderLayout.NORTH
            );

            panelPrincipal.add(
                    new JScrollPane(tablaDetalle),
                    BorderLayout.CENTER
            );

            panelPrincipal.add(
                    panelInferior,
                    BorderLayout.SOUTH
            );

            ventanaDetalle.setContentPane(
                    panelPrincipal
            );

            ventanaDetalle.setVisible(true);
        }

    } catch (Exception e) {

        JOptionPane.showMessageDialog(
                this,
                "Error al mostrar el detalle:\n"
                        + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );

        e.printStackTrace();
    }
}

}