# FitZone - Pruebas y documentación del Reto 1

## Archivos entregados

- `FitZone_Reto1_Documentacion.pdf`: documento académico de **5 páginas** basado en la presentación de la Unidad 2 y el código de `FitZone (3).zip`.
- `pruebas/FitZone.postman_collection.json`: colección de **14 peticiones con aserciones automáticas** para importar en Postman.
- `pruebas/fitzone_datos_prueba.sql`: datos temporales para ejecutar la colección sin depender de los IDs existentes.
- Este instructivo.

**Importante:** la colección fue construida y validada como archivo JSON, pero no se ejecutó aquí contra tu MySQL local. Las pruebas manuales que hiciste previamente en Postman sí fueron confirmadas durante nuestra conversación.

## Ejecutar las pruebas automáticas en Postman

1. Antes de empezar, asegúrate de que MySQL está activo, que existe la base de datos `fitzone` y que Spring Boot se ejecuta en `http://localhost:8080`.
2. En **MySQL Workbench**, abre `pruebas/fitzone_datos_prueba.sql` y ejecuta el archivo completo. No modifica registros antiguos, pero agrega socios, membresías y clases de prueba.
3. Copia los **8 identificadores** que devuelve el `SELECT` final (desde `idSocioValido` hasta `idClaseInexistente`).
4. En **Postman**, pulsa **Import** e importa `pruebas/FitZone.postman_collection.json`.
5. Abre la colección `FitZone - Reto 1 | Pruebas de API` > **Variables**. Coloca en `Current value` (o el valor local de cada variable) los ocho números del paso 3. Verifica que `baseUrl` sea `http://localhost:8080`.
6. Abre el **Collection Runner** y ejecuta **toda la colección en orden**, desde `01` hasta `14`. Los casos 06 y 07 deben ir consecutivos porque primero se crea la inscripción y después se comprueba el duplicado.
7. Comprueba que cada aserción indique **PASS** y conserva una captura del resumen del Runner. Un HTTP `400`, `404` o `409` puede ser un resultado **correcto** cuando eso es lo que exige el caso de prueba.

**Para repetir toda la colección:** primero vuelve a ejecutar `fitzone_datos_prueba.sql` para obtener nuevos IDs. No vuelvas a usar la misma clase de registro, porque la primera inscripción exitosa ya habrá ocupado su lugar y el caso 06 pasaría a devolver duplicado.

## Casos cubiertos

| N.º | Operación | Resultado esperado |
|---:|---|---|
| 01 | GET `/api/estado` | 200, aplicación funcionando |
| 02 | GET `/api/clases` | 200, lista JSON |
| 03 | GET cupo de clase nueva | 200, 20 lugares disponibles |
| 04 | GET cupo de clase inexistente | 404, `CLASE_NO_ENCONTRADA` |
| 05 | GET cupo con ID negativo | 400, `ID_INVALIDO` |
| 06 | POST inscripción correcta | 201, registro creado |
| 07 | POST inscripción duplicada | 409, `INSCRIPCION_DUPLICADA` |
| 08 | POST con membresía vencida | 409, `MEMBRESIA_VENCIDA` |
| 09 | POST con socio inexistente | 404, `SOCIO_NO_ENCONTRADO` |
| 10 | POST a una clase que ya comenzó | 409, `CLASE_YA_INICIADA` |
| 11 | POST a clase llena | 409, `CUPO_AGOTADO` |
| 12 | GET cupo de clase llena | 200, cero lugares libres |
| 13 | GET cupo tras registrar | 200, 19 lugares disponibles |
| 14 | POST con identificador cero | 400, validación de entrada |

## Pruebas que se verifican fuera del Runner

- **Confirmación en consola:** al realizar una inscripción nueva y exitosa, en la terminal aparece `[CONFIRMACION FITZONE]`.
- **Recordatorio:** para una clase cuyo inicio sea cercano a dos horas en el futuro, se imprime `[RECORDATORIO FITZONE]` y la fila correspondiente cambia `recordatorio_enviado` a `1`. La aplicación necesita estar encendida.
- **Capacidad máxima:** la restricción `CHECK (capacidad_maxima BETWEEN 1 AND 20)` ya fue creada en tu MySQL local. Un `INSERT` de capacidad `30` debe ser rechazado por MySQL. No se incluye como llamada de Postman porque la API no tiene endpoint para crear clases.
- **Concurrencia:** el proyecto usa transacción y bloqueo pesimista, pero **no se ha ejecutado una prueba de carga simultánea**.

## Observaciones de la versión definitiva del ZIP

- El ZIP `FitZone (3).zip` sigue configurando `@Scheduled(fixedDelay = 10000)`: revisión cada **10 segundos**. Esto no impide los casos principales, pero difiere del ajuste a 60 segundos discutido previamente.
- Tiene un archivo `application.properties` con una contraseña de MySQL escrita directamente. **No compartas o publiques ese archivo sin corregirlo.** La opción recomendada es utilizar `spring.datasource.password=${DB_PASSWORD}` y configurar `$env:DB_PASSWORD="TU_CONTRASENA"` en PowerShell antes de iniciar Spring Boot. Si ya se publicó la contraseña en el historial de Git, cambiar el archivo no borra el secreto de los commits antiguos: también debes cambiar la contraseña real y revisar el historial publicado.
- La documentación académica señala un objetivo de respuesta inferior a dos segundos, pero ese objetivo **no ha sido medido**.

## Recomendación para la entrega

Sube el código del proyecto por un lado y el documento académico PDF por otro, tal como pide el profesor. Puedes agregar la colección Postman y el SQL de pruebas a una carpeta `pruebas/` del repositorio, después de resolver el problema de la contraseña. No necesitas subir el ZIP con la carpeta `.git/` interna.

Las capturas de Postman que compartiste sirven como evidencia visual adicional; **no se han incorporado al PDF académico** para conservar el límite de 5 páginas y priorizar los criterios de la rúbrica.
