# LPP SMART ERP

## Visión General del Proyecto

### Objetivo

LPP Smart ERP es un sistema ERP desarrollado para empresas dedicadas al transporte, alquiler de maquinaria pesada, movimiento de tierras, producción de volquetas y ejecución de proyectos.

El sistema permitirá controlar toda la operación de la empresa desde el inicio del proyecto hasta la facturación y el análisis de rentabilidad.

---

## Objetivos Específicos

- Eliminar el uso de múltiples archivos Excel.
- Centralizar toda la información en MySQL.
- Automatizar los procesos operativos.
- Reducir errores de digitación.
- Obtener indicadores en tiempo real.
- Generar reportes automáticos.
- Facilitar el control de proyectos.

---

## Procesos que controlará el ERP

- Gestión de Guías
- Control de Proyectos
- Control de Maquinaria
- Control de Material Pétreo
- Costos
- Rentabilidad
- Dashboard Gerencial
- Facturación
- Reportes
- Administración

---

## Filosofía del sistema

Cada dato debe registrarse una sola vez.

Toda la información ingresada deberá ser reutilizada automáticamente por los demás módulos del ERP, evitando la duplicación de información.

Ejemplo:

La maquinaria registrada en el catálogo será utilizada por:

- Guías
- Proyectos
- Asignación de maquinaria
- Costos
- Dashboard

sin volver a escribir la información.

---

## Estado actual del proyecto

### Módulo Guías

Completado.

Incluye:

- Guía Producción Volquetas
- Control Trabajo Volquetas
- Guía Trabajo Diario Maquinaria
- Guía Despacho Material

Todos los procesos fueron probados y almacenan correctamente la información en MySQL.

---

### Módulo Control y Proyección

En desarrollo.

Actualmente se encuentra en construcción el Catálogo Maestro de Maquinaria.

---

## Base de datos

Motor:

MySQL 8

Lenguaje:

Java Swing

Arquitectura futura:

Java Swing
↓

FastAPI

↓

MySQL

↓

Dashboard Gerencial