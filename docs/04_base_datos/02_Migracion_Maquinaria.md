# Migración del Catálogo de Maquinaria

## Objetivo

Importar el archivo `maquinaria_importar.txt` hacia MySQL, evitando duplicados y corrigiendo inconsistencias del Excel.

---

## Archivo de origen

Formato:

Texto delimitado por tabulaciones (`.txt`)

Columnas esperadas:

1. INTERNO
2. STATUS
3. DESCRIPCION DE MAQUINARIA
4. TIPO DE MAQUINA
5. SERIE DE MAQUINA
6. SERIE ACTUAL
7. HOROMETRO
8. confirmado
9. PROVEEDOR DE MAQUINARIA
10. PROPIETARIO DE MAQUINA
11. Código/ placa
12. Código
13. PRECIO

---

## Correspondencia con MySQL

| Archivo TXT | Tabla maquinaria |
|---|---|
| INTERNO | codigo_interno |
| STATUS | estado_operativo |
| DESCRIPCION DE MAQUINARIA | descripcion |
| TIPO DE MAQUINA | id_tipo_maquinaria |
| SERIE DE MAQUINA | serie_maquina |
| SERIE ACTUAL | serie_actual |
| HOROMETRO | horometro_actual |
| confirmado | horometro_confirmado |
| PROVEEDOR DE MAQUINARIA | id_proveedor |
| PROPIETARIO DE MAQUINA | id_propietario |
| Código/ placa | codigo_placa |
| Código | codigo_actual |
| PRECIO | costo_hora_proveedor |

---

## Reglas de normalización

### Estados

Los siguientes valores deben convertirse a `MANTENIMIENTO`:

- MANT
- MATENIMIENTO
- MANTENIMIENTO

Los valores permitidos serán:

- OPERATIVA
- MANTENIMIENTO
- INACTIVA
- RETIRADA

Si el estado está vacío, el registro se marcará como `INACTIVA`.

---

### Horómetro

Los valores con coma decimal deberán convertirse a punto decimal.

Ejemplos:

- `40,3` → `40.30`
- `48,1` → `48.10`
- `12615,8` → `12615.80`

Si el campo está vacío, se guardará como `NULL`.

---

### Confirmación de horómetro

- `SI` → verdadero
- vacío → falso

---

### Proveedores

Los nombres deberán normalizarse.

Ejemplo:

- `EQUIPOS PRO` → `EQUIPOSPRO`

Los proveedores que no existan deberán crearse en `entidades_maquinaria`.

---

### Propietarios

Los propietarios que no existan deberán crearse en `entidades_maquinaria`.

Si el propietario es `ALQUILADO`, no se creará una entidad con ese nombre.

En ese caso:

- `tipo_propiedad = ALQUILADA`
- `id_propietario = NULL`

---

### Tipo de propiedad

Reglas:

- Proveedor y propietario iguales a EQUIPOSPRO → PROPIA
- Propietario igual a ALQUILADO → ALQUILADA
- Proveedor o propietario externo → TERCERO

---

### Precio

Los valores deben limpiarse antes de guardar.

Ejemplos:

- `$40,00` → `40.00`
- `$52,00` → `52.00`
- vacío → `0.00`

El precio se guardará inicialmente en:

`costo_hora_proveedor`

---

## Reglas para duplicados

El identificador principal será:

`codigo_actual`

Si `codigo_actual` ya existe:

- no se insertará otra fila;
- se actualizarán los datos existentes;
- se incrementará el contador de registros actualizados.

Si `codigo_actual` está vacío, se intentará identificar por:

1. codigo_placa
2. codigo_interno
3. serie_actual
4. serie_maquina

Si no existe ningún identificador, la fila será omitida.

---

## Registros especiales

Las filas completamente vacías deben omitirse.

Las filas sin descripción deben omitirse.

Los tipos de maquinaria que no existan deberán crearse automáticamente.

---

## Resultado de la importación

El sistema deberá mostrar:

- Filas procesadas
- Maquinarias insertadas
- Maquinarias actualizadas
- Proveedores creados
- Propietarios creados
- Tipos creados
- Estados normalizados
- Horómetros normalizados
- Filas omitidas
- Errores encontrados