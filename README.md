# DepoManager

**DepoManager** es una aplicación web diseñada para gestionar inventarios en depósitos, permitiendo administrar productos, proveedores, movimientos de stock, y más. Su arquitectura sigue un patron hibrido entre tipo **Modulos* y **Modelo-Vista-Controlador (MVC)**, asegurando la separación clara de responsabilidades.

---

## **Instalación**

1. **Clonar el repositorio:**
   ```bash
   git clone https://github.com/usuario/depomana

2. **Configurar la Base de Datos:**

Crear una base de datos en MySQL llamada depomanager.
Asegúrate de actualizar el archivo application.properties con tus credenciales.

3. **Ejecutar la aplicación:**

---

## **Uso**

Navegación Principal:
   Accede a las diferentes secciones del sistema desde la página de inicio (index.html):
      Depósitos: Gestiona los almacenes.
      Productos: Administra los productos del inventario.
      Proveedores: Registra y edita proveedores.
      Stock: Consulta el stock disponible en cada depósito.
      Movimientos de Stock: Registra entradas y salidas de productos.
      Tipos de Producto: Define las categorías de productos.

---

## **Estructura del Proyecto**

1. Controladores: gestionan las peticiones HTTP.
2. Modelos: Representan las entidades del sistema
3. 
   ```bash
   src
   ├── main
   │   ├── java/com/depomanager
   │   │   ├── controller      # Controladores (MVC)
   │   │   ├── model           # Clases modelo (JPA Entities)
   │   │   ├── repository      # Interfaces Repository
   │   │   ├── Security        # Manejo de Seguridad
   │   │   ├── JWT             # Manejo de autenticaciones
   │   └── resources/static    # Archivos HTML, CSS y JS

---

## **Requerimientos**

**Backend:**

- Java 11 con Spring Boot para la lógica del negocio.
- Manejo de seguridad con JWT y Spring Security
- JPA (Java Persistence API) para la interacción con la base de datos.
- MySQL para el almacenamiento de datos.

**Frontend:**

- HTML/CSS con Bootstrap para la interfaz.
- JavaScript para interactividad.

**Integración:**

- Spring MVC para conectar el backend con el frontend.

---

## **Autores**

Joaquín Romero - FullStack Trainee Developer

Gabriela Sasiuk - FullStack Trainee Developer
