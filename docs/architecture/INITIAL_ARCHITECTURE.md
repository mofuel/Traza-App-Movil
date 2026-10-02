# TRAZA Admin Mobile — arquitectura inicial

Esta base nació con la arquitectura de presentación, el Design System Compose y la navegación vacía. La segunda iteración añade únicamente networking y el flujo completo de autenticación ADMIN. La fuente visual oficial de Android es `docs/TRAZA_DESIGN_SYSTEM_MASTER_V1.md`.

## Decisiones de esta iteración

- Se mantiene un único módulo Gradle `app`. Los límites se expresan mediante paquetes para evitar modularización prematura sin flujos ni dominio implementados.
- `app` contiene el shell de la aplicación y la navegación.
- `core/designsystem` contiene tokens confirmados, tema y primitivas visuales sin reglas de dominio.
- Cada `feature/<nombre>/presentation` contiene el punto de entrada Compose de un destino.
- Los cinco destinos de primer nivel son `Inicio`, `Onboarding`, `Operaciones`, `DataOps` y `Copilot`.
- La barra inferior no incorpora iconos: el documento no confirma una librería ni un catálogo de iconografía.
- La aplicación usa únicamente el tema claro TRAZA. Dynamic Color y tema oscuro no se habilitan porque no están confirmados.
- Inter no se incluye como asset; se utiliza la familia sans del sistema.
- No se implementan chips de estado de dominio hasta resolver la inconsistencia documentada para `APPROVED` y `PENDING`.
- `core/network` concentra Retrofit, serialización y la política HTTP común; no registra cuerpos, credenciales ni tokens.
- `core/session` conserva el token cifrado con AES/GCM mediante una clave no exportable de Android Keystore.
- `feature/auth` separa contrato de repositorio, modelos de dominio y estado de presentación.
- El shell con sus cinco destinos se crea exclusivamente después de validar `/api/v1/auth/me` y confirmar el rol `ADMIN`.
- No se usa un `Authenticator`: el backend inspeccionado no expone refresh token. Un `401` protegido elimina la sesión; un `403` comunica acceso denegado sin tratarlo como expiración.

## Estructura

```text
com.bancocloud.traza
├── app
│   ├── TrazaAdminApp
│   ├── AppContainer
│   ├── TrazaApplication
│   └── navigation
├── core
│   ├── designsystem
│       ├── component
│       └── theme
│   ├── network
│   │   └── dto
│   └── session
└── feature
    ├── auth
    │   ├── data
    │   ├── domain
    │   └── presentation
    ├── home/presentation
    ├── onboarding/presentation
    ├── operations/presentation
    ├── dataops/presentation
    └── copilot/presentation
```

## Contrato de autenticación verificado

- `POST /api/v1/auth/token`, formulario `application/x-www-form-urlencoded` con `username` y `password`.
- Respuesta: `access_token`, `token_type` y `expires_in`.
- `GET /api/v1/auth/me` con `Authorization: Bearer <token>`.
- Respuesta: `id`, `email`, `role` y `customer_id`.
- Sólo el rol exacto `ADMIN` puede entrar a la aplicación; `CUSTOMER` y roles desconocidos eliminan la sesión local.

La URL se resuelve desde la propiedad Gradle `TRAZA_API_BASE_URL`, luego desde la variable de entorno homónima y, si ninguna existe, usa `http://10.0.2.2:8000/` para el emulador. Ejemplo Azure:

```powershell
.\gradlew.bat -PTRAZA_API_BASE_URL=https://app-bancocloud-api-a7x9.azurewebsites.net/ assembleDebug
```

La URL no es un secreto y queda compilada en `BuildConfig`; nunca deben pasarse credenciales o tokens mediante esa propiedad.

El tráfico HTTP en claro hacia `10.0.2.2` se permite únicamente en el source set `debug`, para conectar el emulador Android con FastAPI en el puerto `8000` del equipo anfitrión. La configuración principal, utilizada también por `release`, bloquea todo tráfico cleartext; una compilación destinada a Azure debe configurar una URL `https://`.

### Smoke test local manual

1. Iniciar el backend FastAPI local en el puerto `8000`.
2. Ejecutar la variante `debug` en un Android Emulator; sin configuración adicional utilizará `http://10.0.2.2:8000/`.
3. Introducir en la aplicación credenciales ADMIN reales administradas por el backend. No hay credenciales de prueba incluidas en Android.
4. La aplicación intercambia el formulario con `/api/v1/auth/token`, conserva el JWT cifrado, consulta `/api/v1/auth/me` y muestra el shell sólo cuando el rol recibido es `ADMIN`.

`10.0.2.2` es el alias del emulador para el equipo anfitrión; no representa una dirección utilizable desde un dispositivo físico. El proceso de FastAPI y cualquier firewall local deben aceptar la conexión al puerto `8000`.

## Fuera de alcance actual

Onboarding, Operaciones, DataOps, Copilot, crédito y cualquier endpoint distinto de autenticación permanecen sin implementar. Tampoco existe renovación de token porque el contrato actual no la ofrece.
