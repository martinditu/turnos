# 📅 Sistema de Gestión de Turnos

Sistema completo de gestión de turnos con backend en Spring Boot y frontend en React + TypeScript.

## 📋 Índice

- [Características](#-características)
- [Tecnologías](#-tecnologías)
- [Requisitos Previos](#-requisitos-previos)
- [Instalación](#-instalación)
- [Configuración](#-configuración)
- [Ejecución](#-ejecución)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Usuarios de Prueba](#-usuarios-de-prueba)
- [Problemas Comunes](#-problemas-comunes)

## ✨ Características

- ✅ Autenticación y autorización con JWT
- ✅ Roles de usuario (ADMIN y CLIENTE)
- ✅ Registro público de nuevos clientes
- ✅ Gestión completa de turnos
- ✅ Calendario interactivo para reservas
- ✅ Navegación fluida con opción de volver atrás
- ✅ Notificaciones por email
- ✅ Manejo de errores personalizado (403, 404, 500)
- ✅ Persistencia de datos con MySQL
- ✅ Diseño moderno y responsive

## 🛠 Tecnologías

### Backend
- Java 17
- Spring Boot 3.5.0
- Spring Security + JWT
- Spring Data JPA
- MySQL 8
- Maven
- Lombok
- ModelMapper

### Frontend
- React 19.1.0
- TypeScript
- React Router DOM
- Axios
- React DatePicker
- CSS moderno

## 📦 Requisitos Previos

Antes de comenzar, asegúrate de tener instalado:

1. **Java JDK 17+**
   ```bash
   java -version
   ```

2. **Maven 3.6+**
   ```bash
   mvn -version
   ```

3. **Node.js 16+ y npm**
   ```bash
   node -v
   npm -v
   ```

4. **MySQL 8+**
   - Servidor MySQL en ejecución
   - Usuario root con acceso

5. **Git** (opcional)
   ```bash
   git --version
   ```

## 🚀 Instalación

### 1. Clonar el repositorio (si aplica)
```bash
git clone <url-del-repositorio>
cd <nombre-del-proyecto>
```

### 2. Configurar la base de datos

Crea la base de datos en MySQL:

```sql
CREATE DATABASE turnos;
```

**IMPORTANTE**: No es necesario crear tablas manualmente. Hibernate las creará automáticamente.

### 3. Instalar dependencias del frontend

```bash
cd front
npm install
cd ..
```

## ⚙ Configuración

### Backend - Variables de Entorno

El archivo `back/.env` contiene las variables de entorno necesarias. **Edita este archivo con tus credenciales**:

```env
# Configuración de Base de Datos
DB_URL=jdbc:mysql://localhost:3306/turnos?useSSL=false&serverTimezone=UTC
DB_USERNAME=root
DB_PASSWORD=tu_contraseña_mysql

# Configuración de Email (Gmail recomendado)
MAIL_USERNAME=tucorreo@gmail.com
MAIL_PASSWORD=tu_codigo_aplicacion_gmail

# Configuración JWT (puedes dejarlo por defecto)
JWT_SECRET=hg76Kjsh983hfd!GJgPskdfnQW3498hgf4JHGFh9asdFNLkHWe98hWEkjsdfhJGH8
JWT_EXPIRATION=86400000

# Puerto del servidor (por defecto 8080)
SERVER_PORT=8080
```

#### 🔐 Cómo obtener el código de aplicación de Gmail:

1. Ve a tu cuenta de Google → Seguridad
2. Activa la verificación en 2 pasos
3. Busca "Contraseñas de aplicaciones"
4. Genera una nueva contraseña para "Correo"
5. Copia el código de 16 caracteres generado
6. Pégalo en `MAIL_PASSWORD`

**Nota**: Si no configuras el email, el sistema funcionará pero no enviará notificaciones.

### Frontend - Configuración (Opcional)

El frontend usa proxy configurado en `front/package.json` que apunta a `http://localhost:8080`.

Si cambias el puerto del backend, actualiza:

```json
{
  "proxy": "http://localhost:TU_PUERTO"
}
```

## ▶ Ejecución

### Opción 1: Ejecución Manual (Recomendada)

#### 1. Iniciar el Backend

En una terminal, desde la raíz del proyecto:

```bash
cd back
mvn spring-boot:run
```

**Espera a ver**: `Started Grupo16Application in X seconds`

El backend estará disponible en: `http://localhost:8080`

#### 2. Iniciar el Frontend

En **otra terminal**, desde la raíz del proyecto:

```bash
cd front
npm start
```

El navegador se abrirá automáticamente en: `http://localhost:3000`

### Opción 2: Usando Scripts

**Linux/Mac**:
```bash
# Backend
cd back && mvn spring-boot:run

# Frontend (en otra terminal)
cd front && npm start
```

**Windows**:
```cmd
rem Backend
cd back
mvn spring-boot:run

rem Frontend (en otra ventana de comandos)
cd front
npm start
```

## 📁 Estructura del Proyecto

```
proyecto/
├── back/                           # Backend Spring Boot
│   ├── src/
│   │   └── main/
│   │       ├── java/com/unla/grupo16/
│   │       │   ├── configurations/   # Configuraciones (Security, CORS, JWT)
│   │       │   ├── controllers/      # Controladores REST
│   │       │   ├── models/           # Entidades y DTOs
│   │       │   ├── repositories/     # Repositorios JPA
│   │       │   ├── services/         # Lógica de negocio
│   │       │   └── exception/        # Manejo de excepciones
│   │       └── resources/
│   │           └── application.yml   # Configuración principal
│   ├── .env                          # Variables de entorno (EDITAR AQUÍ)
│   ├── .env.example                  # Ejemplo de variables
│   └── pom.xml                       # Dependencias Maven
│
├── front/                          # Frontend React
│   ├── src/
│   │   ├── api/                    # Servicios API
│   │   ├── components/             # Componentes React
│   │   ├── context/                # Context API (Auth)
│   │   ├── hooks/                  # Custom hooks
│   │   ├── pages/                  # Páginas principales
│   │   ├── routes/                 # Configuración de rutas
│   │   ├── styles/                 # Estilos CSS
│   │   └── types/                  # Tipos TypeScript
│   ├── package.json                # Dependencias npm
│   └── tsconfig.json               # Configuración TypeScript
│
└── README.md                       # Este archivo
```

## 👤 Usuarios de Prueba

El sistema crea automáticamente usuarios de prueba al iniciar:

### Administrador
- **Email**: `admin@mail.com`
- **Contraseña**: `admin123`
- **Rol**: ADMIN
- **Funcionalidades**:
  - Gestión de clientes
  - Visualización de todos los turnos
  - Administración del sistema

### Cliente de Prueba
- **Email**: `cliente@mail.com`
- **Contraseña**: `cliente123`
- **Rol**: CLIENTE
- **Funcionalidades**:
  - Reservar turnos
  - Ver mis turnos
  - Anular turnos

### Registro de Nuevos Clientes

También puedes registrar nuevos clientes desde la interfaz:

1. Ve a `http://localhost:3000/register`
2. Completa el formulario
3. Inicia sesión con las credenciales creadas

## 🔧 Problemas Comunes

### ❌ Error de conexión a MySQL

**Síntoma**: `Communications link failure`

**Solución**:
1. Verifica que MySQL esté en ejecución
2. Confirma usuario y contraseña en `back/.env`
3. Asegúrate de que la base de datos `turnos` exista

### ❌ Puerto 8080 ya en uso

**Síntoma**: `Port 8080 is already in use`

**Solución**:
```bash
# Linux/Mac - Liberar puerto
lsof -ti:8080 | xargs kill -9

# Windows - Liberar puerto
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

O cambia el puerto en `back/.env`:
```env
SERVER_PORT=8081
```

### ❌ Error al compilar frontend

**Síntoma**: Errores de TypeScript o dependencias faltantes

**Solución**:
```bash
cd front
rm -rf node_modules package-lock.json
npm install
```

### ❌ Error 401 Unauthorized

**Síntoma**: No puedes acceder a ninguna ruta protegida

**Solución**:
1. Verifica que el token JWT esté configurado correctamente
2. Prueba cerrar sesión y volver a iniciar
3. Limpia el localStorage del navegador (F12 → Application → Local Storage)

### ❌ Email no se envía

**Síntoma**: Los turnos se crean pero no llegan emails

**Solución**:
1. Verifica las credenciales de Gmail en `back/.env`
2. Asegúrate de usar un código de aplicación (no tu contraseña de Gmail)
3. Revisa los logs del backend para más detalles

### ❌ Base de datos se borra al reiniciar

**Síntoma**: Los datos desaparecen al reiniciar el backend

**Solución**:
Verifica que en `back/src/main/resources/application.yml` esté configurado:
```yaml
jpa:
  hibernate:
    ddl-auto: update  # NO debe ser 'create'
```

## 📚 Endpoints API Principales

### Autenticación
- `POST /api/auth/login` - Iniciar sesión
- `POST /api/auth/register` - Registrar nuevo cliente

### Cliente (requiere rol CLIENTE)
- `GET /api/cliente/servicios` - Listar servicios
- `GET /api/cliente/servicios/{id}/ubicaciones` - Ubicaciones por servicio
- `GET /api/cliente/servicios/{id}/dias-disponibles` - Días disponibles
- `POST /api/cliente` - Crear turno
- `GET /api/cliente/mis-turnos` - Ver mis turnos
- `POST /api/cliente/anular` - Anular turno

### Admin (requiere rol ADMIN)
- `GET /api/admin/clientes` - Listar todos los clientes
- `GET /api/admin/turnos` - Ver todos los turnos
- `PUT /api/admin/cliente/{id}/baja` - Dar de baja cliente
- `PUT /api/admin/cliente/{id}/alta` - Dar de alta cliente

## 📄 Documentación API

Una vez que el backend esté en ejecución, accede a Swagger UI:

🔗 http://localhost:8080/swagger-ui.html

## 🎨 Funcionalidades Implementadas

### ✅ Resuelve: "BD se borra cada vez que se reinicia"
- Cambiado `ddl-auto` de `create` a `update`
- Los datos ahora persisten entre reinicios

### ✅ Resuelve: "No hay variables de entorno"
- Implementado sistema de variables de entorno con `.env`
- Credenciales sensibles ya no están hardcodeadas

### ✅ Resuelve: "Desloguea cuando no tienes permisos"
- Creadas páginas de error personalizadas (403, 404, 500)
- Ahora muestra mensaje de error sin desloguear

### ✅ Resuelve: "No hay registro de clientes"
- Implementado endpoint público `/api/auth/register`
- Página de registro en frontend `/register`

### ✅ Resuelve: "No hay navegación entre vistas"
- Botones "Volver" en cada paso del flujo de turnos
- Navegación fluida sin perder el progreso

### ✅ Resuelve: "No hay diseño de vistas"
- Estilos CSS modernos y responsive
- Gradientes, animaciones y transiciones
- Experiencia de usuario mejorada

### ✅ Resuelve: "README insuficiente"
- Documentación completa y detallada
- Instrucciones paso a paso
- Solución de problemas comunes

## 🤝 Soporte

Si encuentras algún problema:

1. Revisa la sección de [Problemas Comunes](#-problemas-comunes)
2. Verifica los logs del backend en la consola
3. Revisa la consola del navegador (F12) para errores del frontend
4. Asegúrate de seguir todos los pasos de instalación

## 📝 Notas Importantes

- ⚠️ **Primera ejecución**: La base de datos tardará unos segundos en inicializarse
- ⚠️ **Datos de prueba**: Se crean automáticamente al iniciar (usuarios, servicios, etc.)
- ⚠️ **Producción**: Cambia las claves JWT y credenciales antes de desplegar
- ⚠️ **CORS**: Configurado para desarrollo local (localhost:3000)

---

**Desarrollado con ❤️ usando Spring Boot y React**