from PySide6.QtWidgets import (
    QDialog,
    QLabel,
    QPushButton,
    QVBoxLayout,
    QHBoxLayout,
    QFormLayout,
    QComboBox
)
from forms.form_guia_produccion_volquetas import FormGuiaProduccionVolquetas
from forms.form_control_trabajo_volquetas import FormControlTrabajoVolquetas
from forms.form_guia_trabajo_maquinaria import FormGuiaTrabajoMaquinaria
from forms.form_guia_despacho_material import FormGuiaDespachoMaterial


class FormGuia(QDialog):

    def __init__(self, parent=None):
        super().__init__(parent)

        self.setWindowTitle("Seleccionar Tipo de Guía")
        self.setFixedSize(450, 250)

        self.crear_interfaz()

    def crear_interfaz(self):
        layout = QVBoxLayout(self)

        titulo = QLabel("Nueva Guía")
        titulo.setStyleSheet("font-size: 24px; font-weight: bold;")

        formulario = QFormLayout()

        self.cbo_empresa = QComboBox()
        self.cbo_empresa.addItems([
            "EQUIPOS PRO",
            "DEVIALTRANSPORT"
        ])

        self.cbo_tipo_guia = QComboBox()

        self.cbo_empresa.currentTextChanged.connect(self.cargar_tipos_guia)

        formulario.addRow("Empresa:", self.cbo_empresa)
        formulario.addRow("Tipo de guía:", self.cbo_tipo_guia)

        botones = QHBoxLayout()

        btn_aceptar = QPushButton("Aceptar")
        btn_cancelar = QPushButton("Cancelar")

        btn_aceptar.clicked.connect(self.aceptar)
        btn_cancelar.clicked.connect(self.close)

        botones.addStretch()
        botones.addWidget(btn_aceptar)
        botones.addWidget(btn_cancelar)

        layout.addWidget(titulo)
        layout.addLayout(formulario)
        layout.addLayout(botones)

        self.cargar_tipos_guia()


    def cargar_tipos_guia(self):
        empresa = self.cbo_empresa.currentText()

        self.cbo_tipo_guia.clear()

        if empresa == "EQUIPOS PRO":
            self.cbo_tipo_guia.addItems([
                "Guía Producción Volquetas",
                "Guía Trabajo Diario Maquinaria"
            ])

        elif empresa == "DEVIALTRANSPORT":
            self.cbo_tipo_guia.addItems([
                "Control Trabajo Volquetas",
                "Guía Despacho de Material"
            ])

    def aceptar(self):
        empresa = self.cbo_empresa.currentText()
        tipo_guia = self.cbo_tipo_guia.currentText()

        if empresa == "EQUIPOS PRO" and tipo_guia == "Guía Producción Volquetas":
            ventana = FormGuiaProduccionVolquetas(self)
            ventana.exec()

        elif empresa == "DEVIALTRANSPORT" and tipo_guia == "Control Trabajo Volquetas":
            ventana = FormControlTrabajoVolquetas(self)
            ventana.exec()

        elif empresa == "EQUIPOS PRO" and tipo_guia == "Guía Trabajo Diario Maquinaria":
            ventana = FormGuiaTrabajoMaquinaria(self)
            ventana.exec()
        elif empresa == "DEVIALTRANSPORT" and tipo_guia == "Guía Despacho de Material":
            ventana = FormGuiaDespachoMaterial(self)
            ventana.exec()

        self.close()