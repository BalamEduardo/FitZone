-- FITZONE | Datos controlados para ejecutar la colección de Postman
-- Ejecutar en MySQL Workbench con el servidor Spring Boot detenido o activo.
-- No elimina datos existentes. Cada ejecución genera clases nuevas.
-- IMPORTANTE: copiar al entorno Postman los IDs mostrados por el SELECT final.
USE fitzone;

SET @sufijo = DATE_FORMAT(NOW(6), '%Y%m%d%H%i%s%f');

INSERT INTO socios(nombre, correo)
VALUES (CONCAT('FitZone Pruebas - Socio A ', @sufijo),
        CONCAT('fz-a-', @sufijo, '@example.invalid'));
SET @socio_valido = LAST_INSERT_ID();

INSERT INTO socios(nombre, correo)
VALUES (CONCAT('FitZone Pruebas - Socio B ', @sufijo),
        CONCAT('fz-b-', @sufijo, '@example.invalid'));
SET @socio_segundo = LAST_INSERT_ID();

INSERT INTO socios(nombre, correo)
VALUES (CONCAT('FitZone Pruebas - Socio Vencido ', @sufijo),
        CONCAT('fz-v-', @sufijo, '@example.invalid'));
SET @socio_vencido = LAST_INSERT_ID();

INSERT INTO membresias(fecha_vencimiento, socio_id)
VALUES (DATE_ADD(CURDATE(), INTERVAL 30 DAY), @socio_valido),
       (DATE_ADD(CURDATE(), INTERVAL 30 DAY), @socio_segundo),
       (DATE_SUB(CURDATE(), INTERVAL 1 DAY),  @socio_vencido);

INSERT INTO clases(nombre, sucursal, fecha_hora, capacidad_maxima)
VALUES (CONCAT('FZ Prueba registro ', @sufijo), 'Centro',
        DATE_ADD(NOW(), INTERVAL 3 DAY), 20);
SET @clase_registro = LAST_INSERT_ID();

INSERT INTO clases(nombre, sucursal, fecha_hora, capacidad_maxima)
VALUES (CONCAT('FZ Prueba lleno ', @sufijo), 'Norte',
        DATE_ADD(NOW(), INTERVAL 3 DAY), 1);
SET @clase_llena = LAST_INSERT_ID();

INSERT INTO clases(nombre, sucursal, fecha_hora, capacidad_maxima)
VALUES (CONCAT('FZ Prueba pasada ', @sufijo), 'Sur',
        DATE_SUB(NOW(), INTERVAL 1 DAY), 20);
SET @clase_pasada = LAST_INSERT_ID();

-- La sesión de prueba "llena" queda ocupada desde el comienzo.
INSERT INTO inscripciones(socio_id, clase_id, fecha_inscripcion, recordatorio_enviado)
VALUES (@socio_valido, @clase_llena, NOW(), false);

-- Estos valores se pegan en las variables de la colección de Postman.
SELECT 'idSocioValido' AS variable, @socio_valido AS valor UNION ALL
SELECT 'idSocioSegundo', @socio_segundo UNION ALL
SELECT 'idSocioVencido', @socio_vencido UNION ALL
SELECT 'idSocioInexistente', COALESCE((SELECT MAX(id_socio) FROM socios),0)+1000 UNION ALL
SELECT 'idClaseRegistro', @clase_registro UNION ALL
SELECT 'idClaseLlena', @clase_llena UNION ALL
SELECT 'idClasePasada', @clase_pasada UNION ALL
SELECT 'idClaseInexistente', COALESCE((SELECT MAX(id_clase) FROM clases),0)+1000;
