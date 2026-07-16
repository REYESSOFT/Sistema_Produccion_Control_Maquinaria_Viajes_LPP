from PySide6.QtWidgets import (
    QDialog,
    QLabel,
    QLineEdit,
    QPushButton,
    QVBoxLayout,
    QHBoxLayout,
    QFormLayout,
    QComboBox,
    QTextEdit,
    QMessageBox,
    QDoubleSpinBox
)

from utils.conexion_mysql import obtener_conexion


class FormGuiaDespachoMaterial(QDialog):

    def __init__(self, parent=None):
        super().__init__(parent)

        self.setWindowTitle("Guía de Despacho de Material - DEVIALTRANSPORT")
        self.setFixedSize(820, 720)

        self.crear_interfaz()

    def crear_interfaz(self):
        layout = QVBoxLayout(self)

        titulo = QLabel("GUÍA DE DESPACHO DE MATERIAL - DEVIALTRANSPORT")
        titulo.setStyleSheet("font-size: 22px; font-weight: bold;")

        formulario = QFormLayout()

        self.txt_numero_guia = QLineEdit()

        self.txt_fecha = QLineEdit()
        self.txt_fecha.setInputMask("00/00/0000;_")
        self.txt_fecha.setPlaceholderText("dd/mm/aaaa")

        self.txt_chofer = QLineEdit()
        self.txt_solicitante = QLineEdit()
        self.txt_sector = QLineEdit()

        self.txt_cubicaje = QDoubleSpinBox()
        self.txt_cubicaje.setDecimals(2)
        self.txt_cubicaje.setMaximum(999999.99)

        self.txt_placa = QLineEdit()

        self.cbo_origen = QComboBox()
        self.cbo_origen.addItems([
            "MARTÍNEZ",
            "MONTE AZUL",
            "CANTERA SUÁREZ",
            "URGILES",
            "MARLON",
            "NEICER",
            "OTROS"
        ])

        self.txt_origen_otro = QLineEdit()
        self.txt_origen_otro.setPlaceholderText("Especifique si selecciona OTROS")

        self.txt_destino = QLineEdit()

        self.txt_hora_entrada = QLineEdit()
        self.txt_hora_entrada.setInputMask("00:00;_")
        self.txt_hora_entrada.setPlaceholderText("hh:mm")

        self.txt_hora_salida = QLineEdit()
        self.txt_hora_salida.setInputMask("00:00;_")
        self.txt_hora_salida.setPlaceholderText("hh:mm")

        self.cbo_material = QComboBox()
        self.cbo_material.addItems([
            "PIEDRA 3/4",
            "PIEDRA 3/8",
            "PIEDRA 1/2",
            "PIEDRA 4",
            "PIEDRA 7/8",
            "PIEDRA BASE",
            "PIEDRA ESCOLLERA",
            "CISCO",
            "SUBBASE",
            "BASE CLASE 1",
            "CASCAJO AZUL",
            "CASCAJO AMARILLO",
            "OTROS"
        ])

        self.txt_material_otro = QLineEdit()
        self.txt_material_otro.setPlaceholderText("Especifique si selecciona OTROS")

        self.txt_recibi_conforme = QLineEdit()
        self.txt_observaciones = QTextEdit()

        formulario.addRow("N° Guía:", self.txt_numero_guia)
        formulario.addRow("Fecha:", self.txt_fecha)
        formulario.addRow("Chofer:", self.txt_chofer)
        formulario.addRow("Solicitante:", self.txt_solicitante)
        formulario.addRow("Sector:", self.txt_sector)
        formulario.addRow("Cubicaje:", self.txt_cubicaje)
        formulario.addRow("Placa:", self.txt_placa)
        formulario.addRow("Lugar de origen:", self.cbo_origen)
        formulario.addRow("Otro origen:", self.txt_origen_otro)
        formulario.addRow("Lugar de destino:", self.txt_destino)
        formulario.addRow("Hora de entrada:", self.txt_hora_entrada)
        formulario.addRow("Hora de salida:", self.txt_hora_salida)
        formulario.addRow("Tipo de material:", self.cbo_material)
        formulario.addRow("Otro material:", self.txt_material_otro)
        formulario.addRow("Recibí conforme:", self.txt_recibi_conforme)
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


    def hora_valida(hora):
        if len(hora) != 5 or ":" not in hora:
            return False

        try:
            horas, minutos = map(int, hora.split(":"))
            return 0 <= horas <= 23 and 0 <= minutos <= 59
        except ValueError:
            return False
        
    def guardar_guia(self):
        if not self.txt_numero_guia.text().strip():
            QMessageBox.warning(
                self,
                "Validación",
                "Ingrese el número de guía."
            )
            return

        if not self.txt_fecha.text().strip():
            QMessageBox.warning(
                self,
                "Validación",
                "Ingrese la fecha."
            )
            return

        if not self.txt_chofer.text().strip():
            QMessageBox.warning(
                self,
                "Validación",
                "Ingrese el chofer."
            )
            return

        if not self.txt_placa.text().strip():
            QMessageBox.warning(
                self,
                "Validación",
                "Ingrese la placa."
            )
            return

        origen = self.cbo_origen.currentText()

        if origen == "OTROS":
            origen = self.txt_origen_otro.text().strip()

            if not origen:
                QMessageBox.warning(
                    self,
                    "Validación",
                    "Especifique el lugar de origen."
                )
                return

        material = self.cbo_material.currentText()

        if material == "OTROS":
            material = self.txt_material_otro.text().strip()

            if not material:
                QMessageBox.warning(
                    self,
                    "Validación",
                    "Especifique el tipo de material."
                )
                return

        conexion = obtener_conexion()

        if conexion is None:
            QMessageBox.critical(
                self,
                "Error",
                "No fue posible conectar con MySQL."
            )
            return

        cursor = None

        try:
            cursor = conexion.cursor()

            sql = """
                INSERT INTO guias (
                    id_empresa,
                    tipo_guia,
                    numero_guia,
                    fecha,
                    cliente,
                    placa,
                    chofer_operador,
                    m3,
                    material,
                    sector,
                    origen,
                    destino,
                    hora_inicio,
                    hora_fin,
                    recibi_conforme,
                    observaciones,
                    estado
                )
                VALUES (
                    2,
                    'Guía Despacho de Material',
                    %s,
                    STR_TO_DATE(%s, '%d/%m/%Y'),
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
                self.txt_fecha.text().strip(),
                self.txt_solicitante.text().strip(),
                self.txt_placa.text().strip().upper(),
                self.txt_chofer.text().strip(),
                self.txt_cubicaje.value(),
                material,
                self.txt_sector.text().strip(),
                origen,
                self.txt_destino.text().strip(),
                self.txt_hora_entrada.text().strip(),
                self.txt_hora_salida.text().strip(),
                self.txt_recibi_conforme.text().strip(),
                self.txt_observaciones.toPlainText().strip()
            )

            cursor.execute(sql, valores)
            conexion.commit()

            QMessageBox.information(
                self,
                "LPP Smart ERP",
                "Guía de Despacho de Material guardada correctamente."
            )

            self.accept()

        except Exception as e:
            conexion.rollback()

            QMessageBox.critical(
                self,
                "Error al guardar",
                str(e)
            )

        finally:
            if cursor is not None:
                cursor.close()

            conexion.close()