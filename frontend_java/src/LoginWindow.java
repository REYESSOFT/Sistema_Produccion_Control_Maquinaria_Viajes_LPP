import javax.swing.*;
import java.awt.*;

public class LoginWindow extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtClave;

    public LoginWindow() {

        setTitle("LPP Smart ERP - Login");
        setSize(450, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        crearInterfaz();
    }

    private void crearInterfaz() {

        JPanel panel = new JPanel(new BorderLayout());

        JLabel titulo = new JLabel(
                "LPP Smart ERP",
                SwingConstants.CENTER
        );

        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));

        panel.add(titulo, BorderLayout.NORTH);

        JPanel centro = new JPanel(new GridLayout(4,2,10,10));
        centro.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        centro.add(new JLabel("Usuario"));

        txtUsuario = new JTextField();
        centro.add(txtUsuario);

        centro.add(new JLabel("Contraseña"));

        txtClave = new JPasswordField();
        centro.add(txtClave);

        JButton btnIngresar = new JButton("Ingresar");

        btnIngresar.addActionListener(e -> validar());

        centro.add(new JLabel());
        centro.add(btnIngresar);

        panel.add(centro, BorderLayout.CENTER);

        add(panel);
    }

    private void validar() {

    String nombreUsuario =
            txtUsuario.getText().trim();

    String clave =
            new String(txtClave.getPassword());

    if (nombreUsuario.isEmpty()) {

        JOptionPane.showMessageDialog(
                this,
                "Ingrese el usuario.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );

        txtUsuario.requestFocus();
        return;
    }

    if (clave.isEmpty()) {

        JOptionPane.showMessageDialog(
                this,
                "Ingrese la contraseña.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );

        txtClave.requestFocus();
        return;
    }

    UsuarioDAO usuarioDAO =
            new UsuarioDAO();

    Usuario usuarioAutenticado =
            usuarioDAO.autenticar(
                    nombreUsuario,
                    clave
            );

    if (usuarioAutenticado == null) {

        JOptionPane.showMessageDialog(
                this,
                "Usuario o contraseña incorrectos.",
                "Acceso denegado",
                JOptionPane.ERROR_MESSAGE
        );

        txtClave.setText("");
        txtClave.requestFocus();
        return;
    }

    SesionUsuario.iniciarSesion(usuarioAutenticado);

MenuPrincipal menu =
        new MenuPrincipal();

menu.setVisible(true);

dispose();
}

}