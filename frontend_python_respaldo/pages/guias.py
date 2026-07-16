from PySide6.QtWidgets import (
    QWidget,
    QLabel,
    QVBoxLayout,
    QHBoxLayout,
    QComboBox,
    QLineEdit,
    QPushButton,
    QTableWidget,
    QTableWidgetItem,
    QFrame
)
from PySide6.QtCore import Qt
from forms.form_guia import FormGuia


class GuiasPage(QWidget):

    def __init__(self):
        super().__init__()
        self.crear_interfaz()

    def crear_interfaz(self):
        layout = QVBoxLayout(self)
        layout.setContentsMargins(30, 30, 30, 30)

        titulo = QLabel("Guías de Trabajo")
        titulo.setStyleSheet("font-size: 28px; font-weight: bold;")

        layout.addWidget(titulo)

        filtros = QHBoxLayout()

        self.cbo_empresa = QComboBox()
        self.cbo_empresa.addItems(["Todas", "EQUIPOS PRO", "DEVIALTRANSPORT"])

        self.cbo_tipo_guia = QComboBox()
        self.cbo_tipo_guia.addItems([
            "Todas",
            "Guía Producción Volquetas",
            "Control Trabajo Volquetas",
            "Guía Trabajo Diario Maquinaria",
            "Guía Devialtransport"
        ])

        self.txt_numero_guia = QLineEdit()
        self.txt_numero_guia.setPlaceholderText("Número de guía")

        btn_buscar = QPushButton("Buscar")
        btn_nueva = QPushButton("Nueva Guía")
        btn_nueva.clicked.connect(self.abrir_formulario_guia)

        filtros.addWidget(QLabel("Empresa:"))
        filtros.addWidget(self.cbo_empresa)
        filtros.addWidget(QLabel("Tipo guía:"))
        filtros.addWidget(self.cbo_tipo_guia)
        filtros.addWidget(self.txt_numero_guia)
        filtros.addWidget(btn_buscar)
        filtros.addWidget(btn_nueva)

        layout.addLayout(filtros)

        resumen = QHBoxLayout()
        resumen.addWidget(self.crear_tarjeta("Total Guías", "0"))
        resumen.addWidget(self.crear_tarjeta("Pendientes", "0"))
        resumen.addWidget(self.crear_tarjeta("Verificadas", "0"))
        resumen.addWidget(self.crear_tarjeta("Anuladas", "0"))

        layout.addLayout(resumen)

        self.tabla = QTableWidget()
        self.tabla.setColumnCount(8)
        self.tabla.setHorizontalHeaderLabels([
            "Empresa",
            "Tipo Guía",
            "N° Guía",
            "Fecha",
            "Cliente / Proyecto",
            "Placa / Equipo",
            "Chofer / Operador",
            "Estado"
        ])

        self.tabla.setRowCount(3)

        datos = [
            ["EQUIPOS PRO", "Guía Producción Volquetas", "0013013", "11/06/2026", "Cambugallo", "AAY-0150", "Luis Tamayo", "Pendiente"],
            ["EQUIPOS PRO", "Control Trabajo Volquetas", "0010678", "12/06/2026", "Limbomar", "PCF-4629", "Ángel Paula", "Verificada"],
            ["EQUIPOS PRO", "Guía Trabajo Diario Maquinaria", "0008256", "14/06/2026", "Limbomar", "Motoniveladora XCMG", "Dennys Cevallos", "Pendiente"]
        ]

        for fila, registro in enumerate(datos):
            for columna, valor in enumerate(registro):
                self.tabla.setItem(fila, columna, QTableWidgetItem(valor))

        layout.addWidget(self.tabla)

    def crear_tarjeta(self, titulo, valor):
        tarjeta = QFrame()
        tarjeta.setFixedHeight(90)
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
        lbl_titulo.setStyleSheet("font-size: 14px; font-weight: bold;")

        lbl_valor = QLabel(valor)
        lbl_valor.setAlignment(Qt.AlignCenter)
        lbl_valor.setStyleSheet("font-size: 22px; font-weight: bold;")

        layout.addWidget(lbl_titulo)
        layout.addWidget(lbl_valor)

        return tarjeta
    
    def abrir_formulario_guia(self):
        ventana = FormGuia(self)
        ventana.exec()