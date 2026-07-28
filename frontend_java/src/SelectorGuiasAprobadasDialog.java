import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SelectorGuiasAprobadasDialog extends JDialog {

    private JTable tabla;
    private DefaultTableModel modelo;

    private SelectorGuiasAprobadasDAO.GuiaAprobadaItem guiaSeleccionada;

    private static final DateTimeFormatter FORMATO =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public SelectorGuiasAprobadasDialog(Window owner) {

        super(
                owner,
                "Seleccionar Guía Aprobada",
                ModalityType.APPLICATION_MODAL
        );

        setSize(1150, 550);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        crearInterfaz();

        cargarGuias();
    }

    private void crearInterfaz() {

        setLayout(new BorderLayout(10,10));

        JLabel titulo = new JLabel(
                "GUÍAS APROBADAS DISPONIBLES",
                SwingConstants.CENTER
        );

        titulo.setFont(new Font("Segoe UI",Font.BOLD,22));

        add(titulo,BorderLayout.NORTH);

        modelo = new DefaultTableModel(
                new String[]{
                        "ID",
                        "Fecha",
                        "Empresa",
                        "Tipo",
                        "N° Guía",
                        "Proyecto",
                        "Sector",
                        "Material",
                        "Chofer",
                        "Placa",
                        "m³"
                },0){

            @Override
            public boolean isCellEditable(int r,int c){
                return false;
            }
        };

        tabla = new JTable(modelo);

        tabla.setRowHeight(26);

        JScrollPane scroll = new JScrollPane(tabla);

        add(scroll,BorderLayout.CENTER);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnAceptar = new JButton("Aceptar");

        JButton btnCancelar = new JButton("Cancelar");

        botones.add(btnAceptar);
        botones.add(btnCancelar);

        add(botones,BorderLayout.SOUTH);

        btnCancelar.addActionListener(e->dispose());

        btnAceptar.addActionListener(e->seleccionar());
    }

    private void cargarGuias(){

        try{

            modelo.setRowCount(0);

            List<SelectorGuiasAprobadasDAO.GuiaAprobadaItem> lista =
                    SelectorGuiasAprobadasDAO.obtenerGuiasAprobadas();

            for(var g:lista){

                modelo.addRow(new Object[]{
                        g.idGuia(),
                        g.fecha()==null ? "" : g.fecha().format(FORMATO),
                        g.empresa(),
                        g.tipoGuia(),
                        g.numeroGuia(),
                        g.proyectoReferencia(),
                        g.sector(),
                        g.material(),
                        g.choferOperador(),
                        g.placa(),
                        g.m3()
                });

            }

        }catch(Exception ex){

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

        }

    }

    private void seleccionar(){

        int fila = tabla.getSelectedRow();

        if(fila==-1){

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una guía.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        try{

            List<SelectorGuiasAprobadasDAO.GuiaAprobadaItem> lista =
                    SelectorGuiasAprobadasDAO.obtenerGuiasAprobadas();

            guiaSeleccionada =
                    lista.get(fila);

            dispose();

        }catch(Exception ex){

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

        }

    }

    public SelectorGuiasAprobadasDAO.GuiaAprobadaItem
    getGuiaSeleccionada(){

        return guiaSeleccionada;
    }

}