# ARQUITECTURA GENERAL

## LPP SMART ERP

---

# Arquitectura General del Sistema

```
                       LPP SMART ERP

                              │
                              │
            ┌─────────────────┴─────────────────┐
            │                                   │
      DASHBOARD GERENCIAL               ADMINISTRACIÓN
            │                                   │
            │                                   │
            └──────────────┬────────────────────┘
                           │
                    OPERACIÓN DIARIA
                           │
        ┌──────────────────┼──────────────────┐
        │                  │                  │
      GUÍAS          CONTROL PROYECTOS     CATÁLOGOS
        │                  │                  │
        └──────────────────┼──────────────────┘
                           │
                    BASE DE DATOS
                           │
                          MYSQL
```

---

# Arquitectura de Operación

```
CLIENTE

↓

CONTRATO

↓

PROYECTO

↓

ASIGNACIÓN DE RECURSOS

↓

GUÍAS

↓

CONTROL DIARIO

↓

FACTURACIÓN

↓

COBRO

↓

DASHBOARD
```

---

# Arquitectura de Guías

```
Guía Producción Volquetas

↓

Guía Trabajo Maquinaria

↓

Control Trabajo Volquetas

↓

Guía Despacho Material
```

Estas guías constituyen el origen de toda la operación del ERP.

Toda la información registrada en ellas deberá ser reutilizada automáticamente por los demás módulos.

---

# Arquitectura del módulo Control y Proyección

```
Control y Proyección

│

├── Dashboard del Proyecto

├── Proyectos

├── Asignación de maquinaria

├── Control Diario

│      ├── Avance

│      ├── Horas maquinaria

│      ├── Material pétreo

│      └── Registro diario

├── Catálogos

│      ├── Maquinaria

│      ├── Proveedores

│      ├── Propietarios

│      ├── Materiales

│      ├── Canteras

│      ├── Tarifas

│      └── Códigos históricos

├── Costos

└── Reportes
```

---

# Arquitectura de Catálogos

Todos los módulos deberán utilizar los mismos catálogos.

```
Empresas

↓

Clientes

↓

Proyectos

↓

Maquinaria

↓

Volquetas

↓

Choferes

↓

Operadores

↓

Materiales

↓

Canteras

↓

Tarifas
```

Ningún módulo deberá volver a escribir esta información.

---

# Arquitectura de Datos

Toda la información deberá registrarse una sola vez.

Ejemplo:

```
MAQUINARIA

↓

GUÍAS

↓

PROYECTOS

↓

COSTOS

↓

DASHBOARD
```

Nunca se deberá volver a ingresar la misma maquinaria.

Lo mismo aplica para:

- Clientes
- Operadores
- Choferes
- Materiales
- Canteras

---

# Arquitectura Tecnológica

Frontend

Java Swing

↓

API

FastAPI

↓

Base de datos

MySQL

↓

Dashboard

Java Swing

---

# Filosofía del ERP

El ERP será desarrollado bajo el principio de:

"Un dato, múltiples procesos."

Esto significa que cada dato será registrado una sola vez y reutilizado por todos los módulos del sistema.