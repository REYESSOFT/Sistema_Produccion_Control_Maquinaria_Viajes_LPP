import mysql.connector
from mysql.connector import Error


def obtener_conexion():
    try:
        conexion = mysql.connector.connect(
            host="localhost",
            user="FERNANDO",
            password="RVsoft2026@",
            database="lpp_smart_erp"
        )

        return conexion

    except Error as e:
        print("Error de conexión MySQL:", e)
        return None