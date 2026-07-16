from PySide6.QtWidgets import (
    QDialog, QLabel, QLineEdit, QPushButton,
    QVBoxLayout, QHBoxLayout, QFormLayout,
    QTextEdit, QMessageBox, QDateEdit, QTimeEdit, QDoubleSpinBox
)
from PySide6.QtCore import QDate


class FormControlTrabajoVolquetas(QDialog):

    def __init__(self, parent=None):
        super().__init__(parent)

        self.setWindowTitle("Control Trabajo Volquetas - DEVIALTRANSPORT")
        self.setFixedSize(750, 600)

        self.crear_interfaz()

    def crear_interfaz(self):
        layout = QVBoxLayout(self)

        titulo = QLabel("CONTROL TRABAJO VOLQUETAS - DEVIALTRANSPORT")
        titulo.setStyleSheet("font-size: 22px; font-weight: bold;")

        formulario = QFormLayout()

        self.txt_numero_guia = QLineEdit()
        self.txt_fecha = QDateEdit()
        self.txt_fecha.setCalendarPopup(True)
        self.txt_fecha.setDisplayFormat("dd/MM/yyyy")
        self.txt_fecha.setDate(QDate.currentDate())
        self.txt_cliente = QLineEdit()
        self.txt_solicitante = QLineEdit()
        self.txt_placa = QLineEdit()
        self.txt_chofer = QLineEdit()
        self.txt_turno = QLineEdit()
        self.txt_hora_inicio = QTimeEdit()
        self.txt_hora_inicio.setDisplayFormat("HH:mm")
        self.txt_hora_fin = QTimeEdit()
        self.txt_hora_fin.setDisplayFormat("HH:mm")
        self.txt_horas_trabajadas = QDoubleSpinBox()
        self.txt_horas_trabajadas.setDecimals(2)
        self.txt_horas_trabajadas.setMaximum(9999.99)
        self.txt_combustible = QLineEdit()
        self.txt_mantenimiento = QLineEdit()
        self.txt_danos = QLineEdit()

        self.txt_observaciones = QTextEdit()

        formulario.addRow("N° Guía:", self.txt_numero_guia)
        formulario.addRow("Fecha:", self.txt_fecha)
        formulario.addRow("Cliente:", self.txt_cliente)
        formulario.addRow("Solicitante:", self.txt_solicitante)
        formulario.addRow("Placa:", self.txt_placa)
        formulario.addRow("Chofer:", self.txt_chofer)
        formulario.addRow("Turno:", self.txt_turno)
        formulario.addRow("Hora inicio:", self.txt_hora_inicio)
        formulario.addRow("Hora fin:", self.txt_hora_fin)
        formulario.addRow("Horas trabajadas:", self.txt_horas_trabajadas)
        formulario.addRow("Combustible:", self.txt_combustible)
        formulario.addRow("Mantenimiento:", self.txt_mantenimiento)
        formulario.addRow("Daños:", self.txt_danos)
        formulario.addRow("Observaciones:", self.txt_observaciones)

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
        layout.addLayout(botones)
    
    def guardar_guia(self):

        if not self.txt_numero_guia.text().strip():
            QMessageBox.warning(self, "Validación", "Ingrese el número de guía.")
            return

        conexion = obtener_conexion()

        if conexion is None:
            QMessageBox.critical(self, "Error", "No fue posible conectar con MySQL.")
            return

        try:

            cursor = conexion.cursor()

            sql = """
            INSERT INTO guias
            (
                id_empresa,
                tipo_guia,
                numero_guia,
                fecha,
                cliente,
                placa,
                chofer_operador,
                hora_inicio,
                hora_fin,
                horas,
                combustible,
                mantenimiento,
                danos,
                observaciones,
                estado
            )
            VALUES
            (
                2,
                'Control Trabajo Volquetas',
                %s,
                STR_TO_DATE(%s,'%d/%m/%Y'),
                %s,
                %s,
                %s,
                %s,
                %s,
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
                self.txt_cliente.text().strip(),
                self.txt_placa.text().strip(),
                self.txt_chofer.text().strip(),
                self.txt_hora_inicio.time().toString("HH:mm"),
                self.txt_hora_fin.time().toString("HH:mm"),
                self.txt_horas_trabajadas.value(),
                self.txt_combustible.text().strip(),
                self.txt_mantenimiento.text().strip(),
                self.txt_danos.text().strip(),
                self.txt_observaciones.toPlainText().strip()

            )

            cursor.execute(sql, valores)

            conexion.commit()

            QMessageBox.information(
                self,
                "LPP Smart ERP",
                "Control Trabajo Volquetas guardado correctamente."
            )

            self.close()

        except Exception as e:

            conexion.rollback()

            QMessageBox.critical(
                self,
                "Error",
                str(e)
            )

        finally:

            cursor.close()
            conexion.close()