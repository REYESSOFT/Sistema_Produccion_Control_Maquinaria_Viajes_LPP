from PySide6.QtWidgets import (
    QDialog, QLabel, QLineEdit, QPushButton,
    QVBoxLayout, QHBoxLayout, QFormLayout,
    QTableWidget, QTableWidgetItem, QTextEdit,
    QMessageBox, QDateEdit, QDoubleSpinBox
)
from utils.conexion_mysql import obtener_conexion
from PySide6.QtCore import QDate

class FormGuiaProduccionVolquetas(QDialog):

    def __init__(self, parent=None):
        super().__init__(parent)

        self.setWindowTitle("Guía Producción Volquetas - EQUIPOS PRO")
        self.setFixedSize(950, 650)

        self.crear_interfaz()

    def crear_interfaz(self):
        layout = QVBoxLayout(self)

        titulo = QLabel("GUÍA PRODUCCIÓN VOLQUETAS - EQUIPOS PRO")
        titulo.setStyleSheet("font-size: 22px; font-weight: bold;")

        formulario = QFormLayout()

        self.txt_numero_guia = QLineEdit()
        self.txt_fecha = QDateEdit()
        self.txt_fecha.setCalendarPopup(True)
        self.txt_fecha.setDisplayFormat("dd/MM/yyyy")
        self.txt_fecha.setDate(QDate.currentDate())
        self.txt_chofer = QLineEdit()
        self.txt_placa = QLineEdit()
        self.txt_m3 = QDoubleSpinBox()
        self.txt_m3.setDecimals(2)
        self.txt_m3.setMaximum(999999.99)

        formulario.addRow("N° Guía:", self.txt_numero_guia)
        formulario.addRow("Fecha:", self.txt_fecha)
        formulario.addRow("Chofer:", self.txt_chofer)
        formulario.addRow("Placa:", self.txt_placa)
        formulario.addRow("M3:", self.txt_m3)

        self.tabla = QTableWidget()
        self.tabla.setColumnCount(7)
        self.tabla.setRowCount(10)

        self.tabla.setHorizontalHeaderLabels([
            "N°",
            "Proyecto",
            "Sector",
            "Cantera",
            "Material",
            "Hora Origen",
            "Hora Destino"
        ])

        for fila in range(10):
            self.tabla.setItem(fila, 0, QTableWidgetItem(str(fila + 1)))

        self.txt_recibi = QLineEdit()
        self.txt_observaciones = QTextEdit()

        formulario_extra = QFormLayout()
        formulario_extra.addRow("Recibí conforme:", self.txt_recibi)
        formulario_extra.addRow("Observaciones:", self.txt_observaciones)

        botones = QHBoxLayout()

        btn_guardar = QPushButton("Guardar")
        btn_cancelar = QPushButton("Cancelar")
        btn_guardar.clicked.connect(self.guardar_guia)

        btn_cancelar.clicked.connect(self.close)

        botones.addStretch()
        botones.addWidget(btn_guardar)
        botones.addWidget(btn_cancelar)

        layout.addWidget(titulo)
        layout.addLayout(formulario)
        layout.addWidget(self.tabla)
        layout.addLayout(formulario_extra)
        layout.addLayout(botones)

    def guardar_guia(self):
        if not self.txt_numero_guia.text().strip():
            QMessageBox.warning(self, "Validación", "Ingrese el número de guía.")
            return

        if not self.txt_chofer.text().strip():
            QMessageBox.warning(self, "Validación", "Ingrese el chofer.")
            return

        if not self.txt_placa.text().strip():
            QMessageBox.warning(self, "Validación", "Ingrese la placa.")
            return

        conexion = obtener_conexion()

        if conexion is None:
            QMessageBox.critical(self, "Error", "No se pudo conectar a MySQL.")
            return

        try:
            cursor = conexion.cursor()

            sql = """
                INSERT INTO guias (
                    id_empresa,
                    tipo_guia,
                    numero_guia,
                    fecha,
                    chofer_operador,
                    placa,
                    m3,
                    recibi_conforme,
                    observaciones,
                    estado
                )
                VALUES (
                    1,
                    'Guía Producción Volquetas',
                    %s,
                    STR_TO_DATE(%s, '%d/%m/%Y'),
                    %s,
                    %s,
                    %s,
                    %s,
                    %s,
                    'PENDIENTE'
                )
            """

            valores = (
                self.txt_numero_guia.text().strip(),
                self.txt_fecha.date().toString("dd/MM/yyyy"),
                self.txt_chofer.text().strip(),
                self.txt_placa.text().strip(),
                self.txt_m3.value(),
                self.txt_recibi.text().strip(),
                self.txt_observaciones.toPlainText().strip()
            )

            cursor.execute(sql, valores)
            conexion.commit()

            QMessageBox.information(
                self,
                "LPP Smart ERP",
                "Guía Producción Volquetas guardada correctamente."
            )

            self.close()

        except Exception as e:
            conexion.rollback()
            QMessageBox.critical(
                self,
                "Error al guardar",
                str(e)
            )

        finally:
            cursor.close()
            conexion.close()