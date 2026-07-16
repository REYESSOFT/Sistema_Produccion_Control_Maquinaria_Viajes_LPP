from PySide6.QtWidgets import (
    QDialog, QLabel, QLineEdit, QPushButton,
    QVBoxLayout, QHBoxLayout, QFormLayout,
    QTextEdit, QMessageBox,
    QDateEdit, QTimeEdit, QDoubleSpinBox
)

from PySide6.QtCore import QDate
from utils.conexion_mysql import obtener_conexion


class FormGuiaTrabajoMaquinaria(QDialog):

    def __init__(self, parent=None):
        super().__init__(parent)

        self.setWindowTitle("Guía Trabajo Diario Maquinaria - EQUIPOS PRO")
        self.setFixedSize(800, 650)

        self.crear_interfaz()

    def crear_interfaz(self):
        layout = QVBoxLayout(self)

        titulo = QLabel("GUÍA TRABAJO DIARIO MAQUINARIA - EQUIPOS PRO")
        titulo.setStyleSheet("font-size: 22px; font-weight: bold;")

        formulario = QFormLayout()

        self.txt_numero_guia = QLineEdit()
        self.txt_fecha = QDateEdit()
        self.txt_fecha.setCalendarPopup(True)
        self.txt_fecha.setDisplayFormat("dd/MM/yyyy")
        self.txt_fecha.setDate(QDate.currentDate())
        self.txt_equipo = QLineEdit()
        self.txt_marca = QLineEdit()
        self.txt_modelo = QLineEdit()
        self.txt_cliente = QLineEdit()
        self.txt_proyecto = QLineEdit()
        self.txt_operador = QLineEdit()
        self.txt_trabajo_realizado = QLineEdit()
        self.txt_hora_inicio = QTimeEdit()
        self.txt_hora_inicio.setDisplayFormat("HH:mm")
        self.txt_hora_fin = QTimeEdit()
        self.txt_hora_fin.setDisplayFormat("HH:mm")
        self.txt_total_horas = QDoubleSpinBox()
        self.txt_total_horas.setDecimals(2)
        self.txt_total_horas.setMaximum(9999.99)
        self.txt_horometro_inicial = QDoubleSpinBox()
        self.txt_horometro_inicial.setDecimals(2)
        self.txt_horometro_inicial.setMaximum(999999.99)
        self.txt_horometro_final = QDoubleSpinBox()
        self.txt_horometro_final.setDecimals(2)
        self.txt_horometro_final.setMaximum(999999.99)
        self.txt_combustible = QLineEdit()
        self.txt_recibi = QLineEdit()

        self.txt_observaciones = QTextEdit()

        formulario.addRow("N° Guía:", self.txt_numero_guia)
        formulario.addRow("Fecha:", self.txt_fecha)
        formulario.addRow("Equipo:", self.txt_equipo)
        formulario.addRow("Marca:", self.txt_marca)
        formulario.addRow("Modelo:", self.txt_modelo)
        formulario.addRow("Cliente:", self.txt_cliente)
        formulario.addRow("Proyecto:", self.txt_proyecto)
        formulario.addRow("Operador:", self.txt_operador)
        formulario.addRow("Trabajo realizado:", self.txt_trabajo_realizado)
        formulario.addRow("Hora inicio:", self.txt_hora_inicio)
        formulario.addRow("Hora fin:", self.txt_hora_fin)
        formulario.addRow("Total horas:", self.txt_total_horas)
        formulario.addRow("Horómetro inicial:", self.txt_horometro_inicial)
        formulario.addRow("Horómetro final:", self.txt_horometro_final)
        formulario.addRow("Combustible:", self.txt_combustible)
        formulario.addRow("Recibí conforme:", self.txt_recibi)
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
                proyecto,
                equipo,
                chofer_operador,
                hora_inicio,
                hora_fin,
                horas,
                horometro_inicial,
                horometro_final,
                combustible,
                recibi_conforme,
                observaciones,
                estado
            )
            VALUES
            (
                1,
                'Guía Trabajo Diario Maquinaria',
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
                %s,
                %s,
                'PENDIENTE'
            )
            """

            valores = (

                self.txt_numero_guia.text().strip(),
                self.txt_fecha.date().toString("dd/MM/yyyy"),
                self.txt_cliente.text().strip(),
                self.txt_proyecto.text().strip(),
                self.txt_equipo.text().strip(),
                self.txt_operador.text().strip(),
                self.txt_hora_inicio.time().toString("HH:mm"),
                self.txt_hora_fin.time().toString("HH:mm"),
                self.txt_total_horas.value(),
                self.txt_horometro_inicial.value(),
                self.txt_horometro_final.value(),
                self.txt_combustible.text().strip(),
                self.txt_recibi.text().strip(),
                self.txt_observaciones.toPlainText().strip()

            )

            cursor.execute(sql, valores)

            conexion.commit()

            QMessageBox.information(
                self,
                "LPP Smart ERP",
                "Guía Trabajo Diario Maquinaria guardada correctamente."
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