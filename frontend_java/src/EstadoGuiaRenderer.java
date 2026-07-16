import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class EstadoGuiaRenderer extends DefaultTableCellRenderer {

    private static final Color AMARILLO = new Color(255, 243, 176);
    private static final Color VERDE = new Color(198, 239, 206);

    @Override
    public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column
    ) {

        Component componente = super.getTableCellRendererComponent(
                table,
                value,
                isSelected,
                hasFocus,
                row,
                column
        );

        setHorizontalAlignment(SwingConstants.CENTER);
        setFont(new Font("Segoe UI", Font.BOLD, 12));

        if (!isSelected) {
            componente.setForeground(new Color(30, 41, 59));

            String estado = value == null
                    ? ""
                    : value.toString().trim().toUpperCase();

            if (estado.equals("PENDIENTE")) {
                componente.setBackground(AMARILLO);

            } else if (estado.equals("APROBADO")) {
                componente.setBackground(VERDE);

            } else {
                componente.setBackground(Color.WHITE);
            }
        }

        return componente;
    }
}
