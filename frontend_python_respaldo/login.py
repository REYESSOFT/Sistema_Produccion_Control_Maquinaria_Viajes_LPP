from PySide6.QtWidgets import (
    QWidget,
    QLabel,
    QLineEdit,
    QPushButton,
    QVBoxLayout,
    QMessageBox
)
from PySide6.QtCore import Qt
from menu_principal import MenuPrincipal


class LoginWindow(QWidget):

    def __init__(self):
        super().__init__()

        self.setWindowTitle("LPP Smart ERP - Login")
        self.setFixedSize(450, 300)

        self.crear_interfaz()

    def crear_interfaz(self):

        layout = QVBoxLayout()

        layout.setSpacing(15)

        titulo = QLabel("LPP Smart ERP")
        titulo.setAlignment(Qt.AlignCenter)
        titulo.setStyleSheet("""
            font-size:28px;
            font-weight:bold;
        """)

        subtitulo = QLabel("Inicio de Sesión")
        subtitulo.setAlignment(Qt.AlignCenter)
        subtitulo.setStyleSheet("""
            font-size:16px;
        """)

        self.txt_usuario = QLineEdit()
        self.txt_usuario.setPlaceholderText("Usuario")

        self.txt_clave = QLineEdit()
        self.txt_clave.setPlaceholderText("Contraseña")
        self.txt_clave.setEchoMode(QLineEdit.Password)

        btn_ingresar = QPushButton("Ingresar")
        btn_ingresar.clicked.connect(self.validar_login)

        layout.addStretch()

        layout.addWidget(titulo)
        layout.addWidget(subtitulo)
        layout.addWidget(self.txt_usuario)
        layout.addWidget(self.txt_clave)
        layout.addWidget(btn_ingresar)

        layout.addStretch()

        self.setLayout(layout)

    def validar_login(self):

        usuario = self.txt_usuario.text()
        clave = self.txt_clave.text()

        if usuario == "admin" and clave == "123":

            self.menu = MenuPrincipal()
            self.menu.show()
            self.close()

        else:

            QMessageBox.warning(
                self,
                "Error",
                "Usuario o contraseña incorrectos."
            )