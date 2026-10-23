# FitZone API

API REST para gestionar inscripciones a clases grupales de un gimnasio con varias sucursales. Permite consultar las clases programadas, verificar el cupo disponible y registrar inscripciones de socios con membresía vigente. Incluye controles para evitar inscripciones duplicadas, sobrecupos e inscripciones a clases que ya comenzaron.

Proyecto académico de la asignatura **Arquitectura Orientada a Servicios**, correspondiente al **Reto 1 de la Unidad 2: Diseño de servicios**. Las confirmaciones y los recordatorios se simulan mediante mensajes en la consola; no se integra correo electrónico, mensajería externa, interfaz web ni autenticación.

## Tecnologías

- Java 21
- Spring Boot 4.1.1
- Maven
- Spring Web MVC
- Spring Data JPA y Hibernate
- Bean Validation
- MySQL
- Postman para pruebas de la API

## Instalación y ejecución

1. Instalar **Java 21** y **MySQL** e iniciar el servidor de MySQL.
2. Clonar el repositorio y entrar en su directorio:

   ```bash
   git clone https://github.com/BalamEduardo/FitZone.git
   cd FitZone
   ```

3. Crear la base de datos:

   ```sql
   CREATE DATABASE IF NOT EXISTS fitzone
     CHARACTER SET utf8mb4
     COLLATE utf8mb4_unicode_ci;
   ```

4. Configurar la conexión a MySQL mediante variables de entorno y ejecutar el proyecto. En **PowerShell**:

   ```powershell
   $env:SPRING_DATASOURCE_URL = "jdbc:mysql://localhost:3306/fitzone"
   $env:SPRING_DATASOURCE_USERNAME = "root"
   $env:SPRING_DATASOURCE_PASSWORD = "tu_password"
   .\mvnw.cmd spring-boot:run
   ```

   En **Linux o macOS**:

   ```bash
   export SPRING_DATASOURCE_URL='jdbc:mysql://localhost:3306/fitzone'
   export SPRING_DATASOURCE_USERNAME='root'
   export SPRING_DATASOURCE_PASSWORD='tu_password'
   chmod +x mvnw
   ./mvnw spring-boot:run
   ```

La API se ejecuta por defecto en **`http://localhost:8080`**. El proyecto utiliza `spring.jpa.hibernate.ddl-auto=update`, por lo que Hibernate crea o actualiza las tablas al iniciar. La base de datos debe existir previamente.

> **Seguridad:** no almacenar contraseñas reales en `application.properties` ni incluirlas en los commits. Las variables `SPRING_DATASOURCE_*` permiten sobrescribir la configuración de conexión. Antes de publicar el proyecto, eliminar cualquier credencial escrita en el archivo de configuración y, si ya se compartió en el historial de Git, cambiarla.

## Configuración

| Variable de entorno | Descripción | Valor de referencia |
| --- | --- | --- |
| `SPRING_DATASOURCE_URL` | URL de conexión a MySQL | `jdbc:mysql://localhost:3306/fitzone` |
| `SPRING_DATASOURCE_USERNAME` | Usuario de MySQL | `root` |
| `SPRING_DATASOURCE_PASSWORD` | Contraseña de MySQL | Configurar según la instalación |
| `SERVER_PORT` | Puerto HTTP de la aplicación | `8080` |

Estas variables sobrescriben las propiedades equivalentes de Spring Boot. No se requiere H2 ni un servicio de notificaciones externo.

## Modelo de datos

| Entidad | Descripción |
| --- | --- |
| **Socio** | Identificador, nombre y correo del socio. |
| **Membresía** | Membresía asociada a un socio y fecha de vencimiento. |
| **Clase** | Sesión programada, sucursal, fecha/hora y capacidad máxima. |
| **Inscripción** | Relación entre socio y clase, fecha de registro y estado del recordatorio. |

Cada clase representa una **sesión concreta**, no únicamente una disciplina como yoga o spinning. La capacidad permitida es de **1 a 20 personas**. Se impide registrar dos veces al mismo socio en la misma sesión mediante validaciones de negocio y una restricción de unicidad en la base de datos.

## Endpoints

Todas las rutas parten de `http://localhost:8080`.

| Método | Ruta | Descripción | Éxito |
| --- | --- | --- | --- |
| GET | `/api/estado` | Comprobar que la aplicación responde. | `200 OK` |
| GET | `/api/clases` | Consultar todas las clases registradas. | `200 OK` |
| GET | `/api/clases/{idClase}/cupo` | Consultar la capacidad, los inscritos y los lugares disponibles. | `200 OK` |
| POST | `/api/inscripciones` | Registrar a un socio en una clase programada. | `201 Created` |

El listado `/api/clases` devuelve las sesiones almacenadas, incluidas las que ya comenzaron; la restricción de fecha se aplica al intentar inscribirse. La versión actual no ofrece endpoints para crear socios, membresías o clases: esos registros se cargan directamente en MySQL.

## Datos de prueba

Una vez que la aplicación haya iniciado y creado las tablas, se pueden insertar registros de ejemplo desde MySQL Workbench. Ejecutar este bloque en una misma sesión de SQL:

```sql
USE fitzone;

INSERT INTO socios (nombre, correo)
VALUES ('Socio Demo', 'socio.demo@example.test');
SET @idSocio = LAST_INSERT_ID();

INSERT INTO membresias (fecha_vencimiento, socio_id)
VALUES (DATE_ADD(CURDATE(), INTERVAL 30 DAY), @idSocio);

INSERT INTO clases (nombre, sucursal, fecha_hora, capacidad_maxima)
VALUES ('Yoga', 'Centro', DATE_ADD(NOW(), INTERVAL 1 DAY), 20);
SET @idClase = LAST_INSERT_ID();

SELECT @idSocio AS idSocio, @idClase AS idClase;
```

Usar los identificadores obtenidos en las peticiones siguientes. El correo del ejemplo debe ser único; no ejecutar el mismo bloque varias veces sin modificarlo o limpiar los datos correspondientes.

## Ejemplos en Postman

### Consultar clases

`GET /api/clases`

Respuesta HTTP `200`; ejemplo:

```json
[
  {
    "idClase": 1,
    "nombre": "Yoga",
    "sucursal": "Centro",
    "fechaHora": "2026-11-01T08:00:00",
    "capacidadMaxima": 20
  }
]
```

### Consultar cupo

`GET /api/clases/1/cupo`

Respuesta HTTP `200` para una clase de 20 lugares sin inscritos:

```json
{
  "idClase": 1,
  "capacidadMaxima": 20,
  "inscritos": 0,
  "lugaresDisponibles": 20,
  "hayCupo": true
}
```

Una clase llena también devuelve `200 OK`, con `lugaresDisponibles: 0` y `hayCupo: false`. Un identificador de clase inexistente devuelve `404 Not Found`.

### Registrar una inscripción

`POST /api/inscripciones`

En Postman, seleccionar **Body → raw → JSON**:

```json
{
  "idSocio": 1,
  "idClase": 1
}
```

Respuesta HTTP `201 Created`; ejemplo:

```json
{
  "idInscripcion": 1,
  "idSocio": 1,
  "idClase": 1,
  "fechaInscripcion": "2026-10-23T01:13:29",
  "mensaje": "Inscripcion registrada correctamente"
}
```

Los identificadores y fechas son ilustrativos y dependerán de los registros existentes. Una inscripción exitosa también imprime una confirmación en la consola donde se ejecuta Spring Boot.

### Manejo de errores

Las reglas de negocio se implementan en la capa de servicios y sus excepciones se convierten en respuestas HTTP con un código de error y un mensaje descriptivo.

| HTTP | Código | Situación |
| --- | --- | --- |
| `400` | `ID_INVALIDO` | Identificador no válido en una operación que lo valida. |
| `404` | `SOCIO_NO_ENCONTRADO` | El socio indicado no existe. |
| `404` | `MEMBRESIA_NO_ENCONTRADA` | El socio no tiene membresía registrada. |
| `404` | `CLASE_NO_ENCONTRADA` | La clase indicada no existe. |
| `409` | `MEMBRESIA_VENCIDA` | La fecha de vencimiento de la membresía ya pasó. |
| `409` | `INSCRIPCION_DUPLICADA` | El socio ya está inscrito en la misma sesión. |
| `409` | `CUPO_AGOTADO` | No quedan lugares disponibles. |
| `409` | `CLASE_YA_INICIADA` | La fecha y hora de inicio ya se alcanzaron. |

Ejemplo de una segunda inscripción del mismo socio en la misma clase; HTTP `409`:

```json
{
  "error": "INSCRIPCION_DUPLICADA",
  "mensaje": "El socio ya esta inscrito en esta clase."
}
```

Las validaciones del cuerpo JSON, como un identificador ausente o no positivo, también pueden producir `400 Bad Request` con la respuesta predeterminada de Spring.

## Confirmaciones y recordatorios

- **Confirmación:** al completar exitosamente una inscripción, se imprime un aviso en consola con los identificadores del socio y la clase.
- **Recordatorio:** una tarea programada con `@Scheduled` revisa las inscripciones pendientes y genera un aviso cuando se alcanza el momento correspondiente a **dos horas antes** del inicio, siempre que la clase aún no haya comenzado.
- **Control de repetición:** el campo `recordatorio_enviado` evita volver a generar un recordatorio ya procesado.

En la versión del código incluida en la entrega, la tarea revisa las inscripciones **cada 10 segundos**. La aplicación debe permanecer en ejecución para emitir avisos; si estuvo detenida, puede procesar un recordatorio con retraso mientras la clase no haya comenzado. Los mensajes solo se muestran en consola: no se envían correos ni mensajes reales.

## Arquitectura

El proyecto separa las siguientes responsabilidades:

- **Controllers:** reciben las solicitudes HTTP y devuelven las respuestas.
- **Services:** aplican las reglas de membresía, cupo, inscripción y notificación.
- **Repositories:** realizan consultas y operaciones de persistencia mediante Spring Data JPA.
- **Models:** representan las entidades almacenadas en MySQL.
- **DTOs:** definen los datos de entrada y salida de la API.

El proceso de inscripción coordina la validación de membresía, la verificación de duplicados y cupo, y el registro transaccional. Se utiliza bloqueo pesimista sobre la clase para reducir el riesgo de sobrecupo ante solicitudes concurrentes.

## Comprobaciones realizadas

Se realizaron pruebas manuales con Postman y MySQL de los siguientes escenarios: consulta de estado, consulta de cupo, inscripción exitosa (`201`), membresía vencida, inscripción duplicada, clase sin cupo y clase ya iniciada (`409`). También se verificó la emisión de confirmaciones y recordatorios en consola.

Para ejecutar la prueba de arranque del contexto Spring incluida en el proyecto:

**Windows:**

```powershell
.\mvnw.cmd test
```

**Linux/macOS:**

```bash
./mvnw test
```

La prueba automatizada incluida comprueba la carga del contexto de la aplicación; no sustituye las pruebas de operaciones HTTP. Para ejecutarla debe estar disponible la conexión a MySQL configurada
