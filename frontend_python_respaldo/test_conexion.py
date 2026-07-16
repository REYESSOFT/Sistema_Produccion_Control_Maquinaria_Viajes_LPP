from utils.conexion_mysql import obtener_conexion

conexion = obtener_conexion()

if conexion:
    print("===================================")
    print(" CONEXIÓN EXITOSA A MYSQL")
    print(" Base de datos: lpp_smart_erp")
    print("===================================")

    conexion.close()

else:
    print("No fue posible conectar.")