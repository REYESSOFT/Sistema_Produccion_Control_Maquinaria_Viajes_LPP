from PySide6.QtWidgets import (
    QMainWindow,
    QWidget,
    QLabel,
    QPushButton,
    QVBoxLayout,
    QHBoxLayout,
    QFrame,
    QStackedWidget,
    QStyle
)
from PySide6.QtCore import Qt
from pages.guias import GuiasPage


class MenuPrincipal(QMainWindow):

    def __init__(self):
        super().__init__()

        self.setWindowTitle("LPP Smart ERP")
        self.resize(1300, 750)

        self.crear_interfaz()

    def crear_interfaz(self):
        contenedor = QWidget()
        self.setCentralWidget(contenedor)

        layout_principal = QVBoxLayout(contenedor)
        layout_principal.setContentsMargins(0, 0, 0, 0)
        layout_principal.setSpacing(0)

        barra_superior = self.crear_barra_superior()
        layout_principal.addWidget(barra_superior)

        cuerpo = QHBoxLayout()
        cuerpo.setContentsMargins(0, 0, 0, 0)
        cuerpo.setSpacing(0)

        menu_lateral = self.crear_menu_lateral()
        cuerpo.addWidget(menu_lateral)

        self.area_central = QStackedWidget()
        self.area_central.addWidget(self.pagina_dashboard())
        self.area_central.addWidget(GuiasPage())
        self.area_central.addWidget(self.pagina_simple("Clientes"))
        self.area_central.addWidget(self.pagina_simple("Proyectos"))
        self.area_central.addWidget(self.pagina_simple("Volquetas"))
        self.area_central.addWidget(self.pagina_simple("Maquinarias"))
        self.area_central.addWidget(self.pagina_simple("Conductores"))
        self.area_central.addWidget(self.pagina_simple("Operadores"))
        self.area_central.addWidget(self.pagina_simple("Ingresos"))
        self.area_central.addWidget(self.pagina_simple("Egresos"))
        self.area_central.addWidget(self.pagina_simple("Reportes"))
        self.area_central.addWidget(self.pagina_simple("Usuarios y Permisos"))

        cuerpo.addWidget(self.area_central)

        layout_principal.addLayout(cuerpo)

        self.setStyleSheet("""
            QMainWindow {
                background-color: #f4f6f8;
            }

            QLabel {
                font-family: Segoe UI;
            }

            QPushButton {
                font-family: Segoe UI;
                font-size: 14px;
                padding: 10px;
                border: none;
                text-align: left;
            }

            QPushButton:hover {
                background-color: #dbeafe;
            }
        """)

    def crear_barra_superior(self):
        barra = QFrame()
        barra.setFixedHeight(60)
        barra.setStyleSheet("""
            QFrame {
                background-color: #1f2937;
            }
        """)

        layout = QHBoxLayout(barra)
        layout.setContentsMargins(20, 0, 20, 0)

        titulo = QLabel("LPP Smart ERP")
        titulo.setStyleSheet("""
            color: white;
            font-size: 22px;
            font-weight: bold;
        """)

        usuario = QLabel("Usuario: admin")
        usuario.setAlignment(Qt.AlignRight)
        usuario.setStyleSheet("""
            color: white;
            font-size: 14px;
        """)

        layout.addWidget(titulo)
        layout.addStretch()
        layout.addWidget(usuario)

        return barra

    def crear_menu_lateral(self):
        menu = QFrame()
        menu.setFixedWidth(250)
        menu.setStyleSheet("""
            QFrame {
                background-color: #111827;
            }

            QPushButton {
                color: white;
                background-color: transparent;
            }

            QPushButton:hover {
                background-color: #374151;
            }
        """)

        layout = QVBoxLayout(menu)
        layout.setContentsMargins(0, 15, 0, 15)
        layout.setSpacing(2)

        opciones = [
            ("Dashboard", 0, QStyle.SP_ComputerIcon),
            ("Guías", 1, QStyle.SP_FileDialogDetailedView),
            ("Clientes", 2, QStyle.SP_DirHomeIcon),
            ("Proyectos", 3, QStyle.SP_DirIcon),
            ("Volquetas", 4, QStyle.SP_DriveHDIcon),
            ("Maquinarias", 5, QStyle.SP_FileDialogContentsView),
            ("Conductores", 6, QStyle.SP_FileIcon),
            ("Operadores", 7, QStyle.SP_FileIcon),
            ("Ingresos", 8, QStyle.SP_DialogApplyButton),
            ("Egresos", 9, QStyle.SP_DialogCancelButton),
            ("Reportes", 10, QStyle.SP_FileDialogInfoView),
            ("Usuarios", 11, QStyle.SP_DesktopIcon),
        ]

        for texto, indice, icono in opciones:
            boton = QPushButton(texto)
            boton.setIcon(self.style().standardIcon(icono))
            boton.setFixedHeight(45)
            boton.clicked.connect(
                lambda checked=False, i=indice: self.area_central.setCurrentIndex(i)
            )
            layout.addWidget(boton)

        layout.addStretch()

        return menu

    def pagina_dashboard(self):
        pagina = QWidget()
        layout = QVBoxLayout(pagina)
        layout.setContentsMargins(30, 30, 30, 30)

        titulo = QLabel("Dashboard Gerencial")
        titulo.setStyleSheet("""
            font-size: 28px;
            font-weight: bold;
        """)

        subtitulo = QLabel("Resumen general de LPP Smart ERP")
        subtitulo.setStyleSheet("""
            font-size: 15px;
            color: #555;
        """)

        layout.addWidget(titulo)
        layout.addWidget(subtitulo)

        tarjetas = QHBoxLayout()

        tarjetas.addWidget(self.crear_tarjeta("Ventas", "$ 0.00"))
        tarjetas.addWidget(self.crear_tarjeta("Compras", "$ 0.00"))
        tarjetas.addWidget(self.crear_tarjeta("Utilidad", "$ 0.00"))
        tarjetas.addWidget(self.crear_tarjeta("Viajes", "0"))
        tarjetas.addWidget(self.crear_tarjeta("Horas Mq.", "0"))

        layout.addLayout(tarjetas)

        espacio = QLabel("Aquí integraremos el Dashboard Gerencial del proyecto actual.")
        espacio.setAlignment(Qt.AlignCenter)
        espacio.setStyleSheet("""
            background-color: white;
            border: 1px solid #ddd;
            font-size: 18px;
            color: #666;
        """)
        layout.addWidget(espacio, stretch=1)

        return pagina

    def crear_tarjeta(self, titulo, valor):
        tarjeta = QFrame()
        tarjeta.setFixedHeight(120)
        tarjeta.setStyleSheet("""
            QFrame {
                background-color: white;
                border: 1px solid #ddd;
                border-radius: 8px;
            }
        """)

        layout = QVBoxLayout(tarjeta)

        lbl_titulo = QLabel(titulo)
        lbl_titulo.setAlignment(Qt.AlignCenter)
        lbl_titulo.setStyleSheet("""
            font-size: 15px;
            font-weight: bold;
        """)

        lbl_valor = QLabel(valor)
        lbl_valor.setAlignment(Qt.AlignCenter)
        lbl_valor.setStyleSheet("""
            font-size: 24px;
            font-weight: bold;
        """)

        layout.addWidget(lbl_titulo)
        layout.addWidget(lbl_valor)

        return tarjeta

    def pagina_simple(self, titulo_texto):
        pagina = QWidget()
        layout = QVBoxLayout(pagina)
        layout.setContentsMargins(30, 30, 30, 30)

        titulo = QLabel(titulo_texto)
        titulo.setStyleSheet("""
            font-size: 28px;
            font-weight: bold;
        """)

        mensaje = QLabel("Módulo en construcción")
        mensaje.setAlignment(Qt.AlignCenter)
        mensaje.setStyleSheet("""
            background-color: white;
            border: 1px solid #ddd;
            font-size: 20px;
            color: #555;
        """)

        layout.addWidget(titulo)
        layout.addWidget(mensaje, stretch=1)

        return pagina