
<p align="center">
  <img src="logo.png" alt="SaTix" width="220">
</p>

## Índice
- [Descripción 📄](#descripción)
- [Funcionalidades principales ⚙](Funcionalidades-Principales)
- [Requisitos para la instalación y puesta en marcha 🔑](#Requisitos-para-la-instalación-y-puesta-en-marcha)
- [Dependencias 🔗](#dependencias)
- [Webgrafía 🌐](#Webgrafía)
- [Memoria del proyecto 🖋](#Memoria-del-proyecto)
- [Posibles ampliaciones 🔧](#Posibles-ampliaciones)
- [Memoria 🧠](#memoria)
- [Licencia ⚖️](#licencia)
- [Contacto 📞](#contacto)

## Descripción
 El proyecto SaTix (Safe-Tickets) se ha desarrollado como proyecto para el curso de desarrollo de aplicaciones multiplataforma en el año 2023 del I.E.S Monte Naranco.

Se basa en las necesidades del ámbito de eventos, en el cual se encuentra el problema de la reventa de entradas.
En cuanto las tecnologías que existen actualmente en el mercado no existen una que garantice la exclusividad de dicha entrada, en la cual solo el usuario que compro la entrada no se pueda replicar o transferir. Por culpa de este hecho, existen grupos de personas que compran un número de entradas y las venden a otra gente más caras o incluso vender a dos personas la misma entrada.
La idea principal se basa en este hecho, con la idea de garantizar a los usuarios y dueños de eventos la seguridad de una venta y gestión de entradas segura.

Su principal funcionalidad es la implementación de un panel administrativo donde los dueños de eventos puedan gestionar los eventos y una aplicación móvil donde los usuarios puedan registrarse y unirse a eventos. 
Al unirse a un evento el usuario, obtendrá una entrada dinámica con el objetivo de hacerla totalmente exclusiva en un periodo de tiempo determinado por la aplicación y de este modo evitar que se pueda duplicar.
La aplicación genera codigos QR con identificadores que será comprobado por el sistema de SaTix en el momento que un Operario escanee el código. El operario tendrá acceso a revisar la información de la entrada en el momento de escaneo de esta. En el caso de que no sea valido el codigo o este caducado no se mostrara nada.

Se recomienda visitar el apartado de [Memoria](#memoria) para revistar todo el análisis de esta aplicación, sus diagramas y pruebas realizadas.

## Funcionalidades principales
Estas son algunas de la funcionalidades principales, revisar el documento de [Memoria](#memoria) para saber en profundidad todas:

- Implementación de un panel administrativo donde los dueños de eventos puedan gestionar los eventos.
- Aplicación móvil donde los usuarios puedan registrarse y unirse a eventos.
- Generación de entradas dinámicas y exclusivas mediante códigos QR.
- Verificación de entradas por parte de operarios a través del escaneo de códigos QR.
- Acceso a la información de la entrada en el momento del escaneo.
- Restricción de duplicación de entradas y caducidad de códigos.

## Requisitos para la instalación y puesta en marcha
| Requisito | Descripción |
|-----------|-------------|
| Versión de Android | La aplicación está desarrollada en el SDK 26 para las siguientes versiones de Android: Android 8.0 Oreo, Android 8.1 Oreo, Android 9.0 Pie, Android 10 Q, Android 11 R, Android 12 S. |
| Cámara trasera | En el caso de uso como operador de la aplicación, será necesario que el teléfono tenga una cámara trasera. |
| Conexión a internet | El dispositivo necesitará conexión a internet para utilizar la aplicación. |
| Configuración del servidor | Configurar la dirección IP de tu servidor con SaTix-Core en el archivo Config.java del parque API de la aplicación. |
## Dependencias
 - Es importante tener instalado en la máquina donde vaya a ejecutarse el SDK 17 de Java [Descargar SDK de Java]
## Webgrafía
Aquí se incluyen las referencias utilizadas para el desarrollo de este proyecto:

- Android Developers Team. (s.f.). Documentación para desarrolladores de apps. Recuperado el 2023, de [Android Developers](https://developer.android.com/docs?hl=es-419)
- Baeldung. (s.f.). Spring Courses. Recuperado el 2023, de [Baeldung](https://www.baeldung.com/)
- Bootstrap Team. (s.f.). Bootstrap 5 Docs. Recuperado el 2023, de [Bootstrap](https://getbootstrap.com/docs/5.0/getting-started/introduction/)
- Budiyev, Y. (s.f.). Code scanner. Obtenido de [Github](https://github.com/yuriy-budiyev/code-scanner)
- Caldwell, C. (4 de Julio de 2011). How to export html table to excel using javascript. Obtenido de [StackOverflow](https://stackoverflow.com/questions/6566831/how-to-export-html-table-to-excel-using-javascript)
- Contributors. (s.f.). Repositories. Recuperado el 2023, de [Maven Repositories](https://mvnrepository.com/)
- Ekuan, M., Bouska, J., Alberts, M., Sherer, T., & others, a. (03 de Marzo de 2023). RESTful web API design, de [Learn Microsoft](https://learn.microsoft.com/en-us/azure/architecture/best-practices/api-design)
- Jetbrains Team. (21 de Abril de 2023). Intellij IDE Spring. Obtenido de [Jetbrains](https://www.jetbrains.com/help/idea/spring-support.html)
- Kowal, & Grzegorz. (s.f.). Cross-platform Java executable wrapper. Recuperado el 2023, de [Launch4j](https://launch4j.sourceforge.net/)
- López, L. M. (s.f.). Curso de Introducción a Thymeleaf. Recuperado el 2023, de [OpenWebinars](https://openwebinars.net/academia/portada/introduccion-thymeleaf/)
- López, L. M. (s.f.). Curso de seguridad en tu API REST con Spring Boot. Obtenido de [OpenWebinars](https://openwebinars.net/academia/aprende/seguridad-api-rest-spring-boot/)
- López, L. M. (s.f.). Curso de Spring Core 5. Recuperado el 2023, de [OpenWebinars](https://openwebinars.net/academia/portada/spring-core/)
- Rusell, J. (s.f.). Inno Setup. Recuperado el 2023, de [JrSoftware](https://jrsoftware.org/isinfo.php)
- Segijn, D. (s.f.). DanielMartinus/Konfetti. Recuperado el 2023, de [Github](https://github.com/DanielMartinus/Konfetti)
- Square Inc. (s.f.). Picasso. Obtenido de [MVN Repository](https://mvnrepository.com/artifact/com.squareup.picasso/picasso/2.71828)
- StartBootstrap. (14 de Enero de 2014). SBAdmin Theme. Obtenido de [StartBootstrap](https://startbootstrap.com/theme/sb-admin-2)
- StorySet Team. (s.f.). StorySet. Recuperado el 2023, de [StorySet](https://storyset.com/)
- The Android Open Source Project. (s.f.). SwipereFreshLayout. Obtenido de [MVN Repository](https://mvnrepository.com/artifact/androidx.swiperefreshlayout/swiperefreshlayout/1.1.0)
- Varma, S. (s.f.). Local Storage vs. Session Storage vs. Cookies. Recuperado el 2023, de [LoginRadius](https://www.loginradius.com/blog/engineering/guest-post/local-storage-vs-session-storage-vs-cookies/)
- VMware Tanzu. (2023). Spring Docs. Obtenido de [spring.io](https://docs.spring.io/spring-framework/reference/index.html)
- W3Schools. (s.f.). Tutoriales CSS. Recuperado el 2023, de [W3Schools](https://www.w3schools.com/css/)
- W3Schools. (s.f.). Tutoriales HTML. Recuperado el 2023, de [W3Schools](https://www.w3schools.com/html/)

## Memoria
Descarga la memoria para ver toda la información del proyecto. Se incluyen los manuales de instalación y uso en la memoria:
- [Memoria-SaTix.pdf](https://github.com/ShxwZ/SaTix-Web-Api/files/11642909/Memoria-SaTix.pdf)

## Posibles ampliaciones

| Ampliaciones para el Proyecto |
| ------------------------ |
| Sistema de filtros para las entradas y eventos en el dispositivo móvil. |
| Implementar la funcionalidad de emitir códigos de las entradas a través de NFC y poder usarlas en controles sin necesidad de un operario presente. |
| Añadir un registro de inicio de sesión y limitar el número máximo de inicios de sesión por dispositivo al día. |
| Verificación de correo y número de teléfono. |
| Restablecimiento de contraseñas. |
| Modificar perfil del usuario. |
| Añadir un servicio de notificación para avisar de las fechas de eventos. |
| Añadir paginaciones a las tablas de la página web. |
| Incluir más tiempos de cuenta para los administradores teniendo en cuenta el sistema de planes de pago mencionado en el ámbito empresarial. |
| Incluir la verificación de la cuenta por correo y móvil, incluyendo la recuperación de contraseña. |
| Opciones de personalización de perfil de usuario. |
| Asistencia directa con el soporte desde el panel de administradores. |
| Mejorar las partes visuales de las interfaces. |
| Implementar la generación de códigos QR directamente como parte del servicio en el Back-End, sin depender de una API externa. |
| Crear un panel para gestionar las cuentas de administradores. |
| Agregar una pestaña para gestionar las entradas desde el panel de administradores. |
| Migrar la web a un framework como Angular o React. |
| Incorporar la biblioteca de Tensor Flow para verificar DNI mediante la cámara del dispositivo (mejora avanzada y de interés propio). |

## Licencia
[![Licencia: CC BY-NC 4.0](https://img.shields.io/badge/Licencia-CC%20BY--NC%204.0-blue.svg)](https://creativecommons.org/licenses/by-nc/4.0/legalcode)
Este proyecto está bajo la Licencia Creative Commons Atribución-NoComercial 4.0 Internacional. Para más detalles, consulta el archivo [LICENSE](LICENSE).

## Contacto

Si tienes alguna pregunta o estás interesado en colaborar en el proyecto SaTix, no dudes en ponerte en contacto conmigo:

- :email: Correo Electrónico: [gabriagp02@gmail.com](mailto:gabriagp02@gmail.com)
- :briefcase: LinkedIn: [Gabriel GP](https://www.linkedin.com/in/gabriel-gp/)

¡Estaré encantado de hablar contigo y responder a tus consultas!



