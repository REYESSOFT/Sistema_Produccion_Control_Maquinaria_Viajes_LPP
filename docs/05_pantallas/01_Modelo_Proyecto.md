# Modelo del Módulo Proyectos

## 1. Objetivo

Administrar los proyectos ejecutados por las empresas del grupo, permitiendo controlar:

- Datos generales del proyecto.
- Cliente y empresa responsable.
- Sector, ubicación y piscinas.
- Fechas de inicio y finalización.
- Estado del proyecto.
- Presupuesto e ingresos proyectados.
- Maquinaria y volquetas asignadas.
- Avance diario.
- Material pétreo utilizado.
- Costos reales.
- Rentabilidad.

---

## 2. Flujo general

El flujo del módulo será:

Proyecto
→ Asignación de maquinaria y volquetas
→ Registro diario
→ Avance de proyecto
→ Consumo de materiales
→ Costos
→ Rentabilidad
→ Dashboard y reportes

---

## 3. Pantalla principal de Proyectos

La pantalla deberá permitir:

- Nuevo proyecto.
- Editar proyecto.
- Ver detalle.
- Eliminar de forma lógica.
- Buscar proyectos.
- Filtrar por empresa.
- Filtrar por cliente.
- Filtrar por estado.
- Filtrar por fechas.

---

## 4. Columnas de la tabla principal

La tabla mostrará:

- ID
- Código del proyecto
- Empresa
- Cliente
- Nombre del proyecto
- Sector
- Fecha de inicio
- Fecha de finalización
- Estado
- Presupuesto
- Porcentaje de avance

No se mostrarán todos los datos en la tabla principal. La información completa estará disponible en Detalle.

---

## 5. Estados del proyecto

Los estados permitidos serán:

- PLANIFICADO
- EN_EJECUCION
- SUSPENDIDO
- FINALIZADO
- CANCELADO

La eliminación será lógica mediante:

activo = 0

---

## 6. Datos generales del proyecto

Cada proyecto deberá guardar:

- Código del proyecto.
- Empresa responsable.
- Cliente.
- Nombre o descripción.
- Sector.
- Ubicación.
- Sector secundario.
- Piscina.
- Fecha de inicio.
- Fecha prevista de finalización.
- Fecha real de finalización.
- Estado.
- Presupuesto.
- Valor contractual.
- Observaciones.
- Activo.

---

## 7. Relación con otros módulos

### Maquinaria

Un proyecto puede tener varias máquinas asignadas.

Una maquinaria puede participar en varios proyectos en diferentes fechas.

### Volquetas

Un proyecto puede tener varias volquetas asignadas.

### Registro diario

Cada proyecto tendrá registros diarios de:

- Fecha.
- Sector.
- Ubicación.
- Actividad.
- Piscina.
- Maquinaria.
- Volquetas.
- Horas.
- Metros cúbicos.
- Material.

### Costos

Los costos se calcularán a partir de:

- Horas de maquinaria propia.
- Horas de maquinaria alquilada.
- Viajes de volquetas.
- Material pétreo.
- Transporte.
- Otros costos directos.

### Rentabilidad

Se comparará:

- Ingreso proyectado.
- Ingreso real.
- Costo proyectado.
- Costo real.
- Utilidad.
- Margen de rentabilidad.

---

## 8. Reglas principales

- No se eliminarán físicamente proyectos con movimientos.
- No se podrá finalizar un proyecto sin fecha real de finalización.
- Un proyecto finalizado no permitirá nuevos registros diarios.
- Las maquinarias en mantenimiento no podrán asignarse como operativas.
- Los costos históricos no deberán cambiar cuando se actualicen tarifas futuras.
- Cada proyecto tendrá un código único.

---

## 9. Primera versión

La primera versión incluirá:

1. Crear proyecto.
2. Editar proyecto.
3. Ver detalle.
4. Eliminar de forma lógica.
5. Buscar y filtrar.
6. Mostrar registros desde MySQL.
7. Refrescar la tabla automáticamente.

Los módulos de asignación, avance, costos y rentabilidad se conectarán después.