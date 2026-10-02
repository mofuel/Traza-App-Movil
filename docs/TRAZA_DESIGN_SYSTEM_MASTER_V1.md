# TRAZA — Design System Master para handoff a Android

**Versión:** V1

**Fecha de extracción:** 2026-10-02

**Alcance:** estado real del frontend web en la rama `feature/frontend-redesign-safe`.

**Destino futuro:** referencia visual y funcional para `TRAZA Admin Mobile`; este documento no implementa Android.

# 0. Resumen ejecutivo

El frontend vigente es una SPA con React 19.2.8, TypeScript 6.0.2 y Vite 8.3.0. No usa una librería visual, CSS-in-JS, CSS Modules, Tailwind, Bootstrap ni Material UI: la presentación se resuelve con CSS global propio. El orden efectivo de estilos es `tokens.css` → `index.css` → `globals.css`; `ControlTower.tsx` añade `control-tower.css`.

La identidad visible confirmada es **TRAZA**. El sistema efectivo es claro, con amarillo `#F4C414`, texto antracita `#1A1A1A`, superficies blancas `#FFFFFF` y fondo gris `#F5F5F5`. Los estados usan pares de fondo/texto/borde verdes, amarillos, rojos y neutros. Existe un logo raster oficial en `frontend/src/assets/branding/traza-logo.jfif`, usado en autenticación y en la barra superior.

La aplicación no declara rutas React Router aunque la dependencia está instalada. La navegación es por estado React: `login`/`register`, rol `ADMIN`/`CUSTOMER`, tabs de cliente y acordeones/detalles dentro de ADMIN. ADMIN es una página larga que integra resumen, Control Tower, revisión de onboarding y revisión de créditos.

Se catalogan **29 componentes React nombrados**, **8 superficies funcionales ADMIN internas** y **14 endpoints de dominio ADMIN** consumidos por la web. Se registran por separado tres endpoints compartidos que intervienen en la experiencia ADMIN (`token`, `me` y salud de base de datos) y no se suman a los 14.

No hay proyecto Android, tema oscuro consolidado, sistema de iconos formal ni navegación por URL. La traducción a Compose de las secciones 14–18 es una propuesta; no describe código ya implementado.

# 1. Fuentes inspeccionadas

| Tipo | Archivo | Para qué se usa |
|---|---|---|
| Bootstrap de aplicación | `frontend/src/main.tsx` | Montaje React y orden de hojas globales |
| Configuración | `frontend/package.json` | Versiones, scripts y dependencias |
| Documento HTML | `frontend/index.html` | Viewport, título y favicon |
| Tokens | `frontend/src/styles/tokens.css` | Colores, espaciado, radios, sombras y focus ring |
| Estilos base | `frontend/src/index.css` | Reset mínimo, tipografía y estilos históricos/base |
| Estilos vigentes | `frontend/src/styles/globals.css` | Apariencia TRAZA clara, responsive y modal de tarjeta |
| Estilos ADMIN | `frontend/src/control-tower.css` | Control Tower, DataOps, Copilot y responsive ADMIN |
| Orquestación | `frontend/src/App.tsx` | Auth, sesión, rol, dashboards, onboarding y revisiones ADMIN |
| Página ADMIN | `frontend/src/ControlTower.tsx` | Control Tower, trazabilidad, decisiones, DataOps y Copilot |
| API | `frontend/src/api.ts` | Base URL, JWT, errores y llamadas HTTP |
| Contratos | `frontend/src/types.ts` | Tipos de dominio y respuestas usadas |
| Layout | `frontend/src/components/layout/Topbar.tsx` | Barra superior autenticada y logout |
| UI compartida | `frontend/src/components/ui/Metric.tsx` | KPI/resumen |
| UI compartida | `frontend/src/components/ui/StatusPill.tsx` | Etiquetas y tonos de estado generales |
| UI Control Tower | `frontend/src/features/control-tower/components/CtStatus.tsx` | Etiquetas y tonos operacionales |
| UI Control Tower | `frontend/src/features/control-tower/components/AccordionHeader.tsx` | Encabezado expandible accesible |
| UI Control Tower | `frontend/src/features/control-tower/components/FlowStrip.tsx` | Secuencia visual de trazabilidad |
| Features cliente | `frontend/src/features/accounts/`, `cards/`, `loans/`, `movements/` | Componentes de producto y patrones reutilizables |
| Pages cliente | `frontend/src/pages/customer/` | Resumen, tabs y workspace |
| Asset de marca | `frontend/src/assets/branding/traza-logo.jfif` | Logo visible TRAZA |
| Guía visual | `docs/frontend/TRAZA_PROMPT_MAESTRO_FRONTEND_REDISENO_Y_MODULARIZACION_V1.md` | Intención de diseño; subordinada al código actual |
| Fuente funcional | `docs/project/BancoCloud_FUENTE_UNICA_CONSOLIDADA_ACTUALIZADA_2026-10-02_V11.md` | Estado funcional y reglas de seguridad vigentes |

No se tomaron como fuente visual activa `react.svg`, `vite.svg` ni `hero.png`: no tienen imports vigentes en `frontend/src`. El favicon `/favicon.svg` se declara en `frontend/index.html`; su relación con el logo oficial es **NO CONFIRMADO**.

# 2. Identidad visual vigente

## 2.1 Nombre visible

- Producto visible: **TRAZA**.
- Nombre técnico residual: BancoCloud, visible aún en contratos, nombres internos y algunos textos transaccionales.
- Logo confirmado: `frontend/src/assets/branding/traza-logo.jfif`, importado en `App.tsx:61` y `components/layout/Topbar.tsx:1`.
- Usos confirmados: logo centrado en tarjetas de login/registro y logo de 42 px de alto en Topbar.
- Isotipo independiente: **NO CONFIRMADO**.
- Variantes monocroma/negativa: **NO CONFIRMADO**.
- Favicon: existe una referencia a `/favicon.svg`; consistencia con la marca oficial: **PENDIENTE DE CONSOLIDAR**.

## 2.2 Personalidad visual observada

Institucional y financiera por el uso de superficies blancas, bordes grises, cifras tabulares, tarjetas de métricas y jerarquías sobrias. El amarillo funciona como acento de marca y de acción, no como fondo dominante. Control Tower agrega densidad tecnológica mediante trazabilidad, KPIs, estados, datos monoespaciados y paneles de auditoría.

## 2.3 Principios visuales observados

- Fondo general gris claro y superficies blancas delimitadas.
- Texto principal antracita; texto secundario gris.
- Amarillo TRAZA para acciones principales, indicadores activos y acentos superiores/laterales.
- Estados acompañados por texto; no dependen exclusivamente del color.
- Bordes de 1 px y sombras suaves; elevación más marcada en auth y modal.
- Radios recurrentes de 10, 12, 14, 16 y 18 px; píldoras a 999 px.
- Dashboard por bloques: encabezado, métricas, feedback, contenido operacional.
- Densidad mayor en Control Tower que en las vistas cliente.

# 3. Paleta de colores

## 3.1 Tokens físicos confirmados

| Token propuesto | Variable real | Valor exacto | Uso real | Fuente |
|---|---|---:|---|---|
| `brand-primary` | `--traza-yellow` | `#F4C414` | Botón primario, selección, acentos | `frontend/src/styles/tokens.css:4` |
| `brand-on-primary` | `--traza-text` | `#1A1A1A` | Texto sobre amarillo | `tokens.css:3` |
| `surface` | `--traza-white` | `#FFFFFF` | Cards, Topbar, controles | `tokens.css:2` |
| `background-app` | `--traza-gray-50` | `#F5F5F5` | Fondo de la aplicación | `tokens.css:5` |
| `surface-alt` | `--traza-surface-muted` | `#FAFAFA` | Filas hover y superficies secundarias | `tokens.css:11` |
| `text-primary` | `--traza-text` | `#1A1A1A` | Encabezados, cuerpo y controles | `tokens.css:3` |
| `text-secondary` | `--traza-text-muted` | `#666666` | Ayuda, notas y descripciones | `tokens.css:6` |
| `border` | `--traza-border` | `#E3E3E3` | Bordes generales | `tokens.css:7` |
| `brand-soft` | `--traza-yellow-soft` | `#FFF8D6` | Selección y avisos suaves | `tokens.css:9` |
| `brand-strong` | `--traza-yellow-strong` | `#6B5600` | Eyebrows y texto amarillo accesible | `tokens.css:10` |
| `success-bg` | `--traza-success-bg` | `#EAF7EF` | Fondo de éxito | `tokens.css:12` |
| `success-text` | `--traza-success-text` | `#17643A` | Texto de éxito | `tokens.css:13` |
| `success-border` | `--traza-success-border` | `#BFE5CC` | Borde de éxito | `tokens.css:14` |
| `warning-bg` | `--traza-warning-bg` | `#FFF7D6` | Fondo de advertencia | `tokens.css:15` |
| `warning-text` | `--traza-warning-text` | `#6B5600` | Texto de advertencia | `tokens.css:16` |
| `warning-border` | `--traza-warning-border` | `#F1DC84` | Borde de advertencia | `tokens.css:17` |
| `danger-bg` | `--traza-danger-bg` | `#FDECEC` | Fondo de error/rechazo | `tokens.css:18` |
| `danger-text` | `--traza-danger-text` | `#9F2D2D` | Texto de error/rechazo | `tokens.css:19` |
| `danger-border` | `--traza-danger-border` | `#F2C3C3` | Borde de error/rechazo | `tokens.css:20` |
| `neutral-bg` | `--traza-neutral-bg` | `#F1F3F5` | Estado neutral | `tokens.css:21` |
| `neutral-text` | `--traza-neutral-text` | `#4B5563` | Texto neutral | `tokens.css:22` |
| `neutral-border` | `--traza-neutral-border` | `#D8DDE3` | Borde neutral | `tokens.css:23` |

## 3.2 Valores activos adicionales

| Uso | Valor exacto | Fuente |
|---|---:|---|
| Gradiente auth, gris final | `#F0F0F0` | `globals.css:21` |
| Placeholder | `#8A8A8A` | `globals.css:161` |
| Focus de borde | `#B99700` | `globals.css:165` |
| Borde de primario | `#D8AE00` | `globals.css:177` |
| Hover de borde neutral | `#C7C7C7` | `globals.css:194` |
| Borde de contador amarillo | `#E6CF69` | `globals.css:730` |
| Selección de cuenta | `#D9B000` | `globals.css:756` |
| Tarjeta bancaria, inicio | `#1A1A1A` | `globals.css:859` |
| Tarjeta bancaria, fin | `#343434` | `globals.css:859` |
| Tarjeta bancaria, borde | `#3C3C3C` | `globals.css:858` |
| Tarjeta, texto secundario inverso | `#CFCFCF` | `globals.css:868` |
| Overlay modal | `rgba(26, 26, 26, 0.58)` | `globals.css:1129-1138` |
| Focus ring | `rgba(244, 196, 20, 0.35)` | `tokens.css:29` |

`info` no tiene token semántico TRAZA confirmado. Existen azules históricos hardcodeados en `index.css` y en la primera mitad de `control-tower.css`, pero las reglas claras posteriores son la presentación vigente. Por ello `state.info` queda **PENDIENTE DE CONSOLIDAR**.

Disabled se comunica principalmente mediante `opacity` y cursor/estado nativo; no existe un token cromático dedicado. Hover primario usa `filter: brightness(0.97)` en vez de otro HEX.

# 4. Tokens semánticos

| Token semántico | Mapeo real |
|---|---|
| `brand.primary` | `#F4C414` / `--traza-yellow` |
| `brand.onPrimary` | `#1A1A1A` / `--traza-text` |
| `brand.soft` | `#FFF8D6` / `--traza-yellow-soft` |
| `background.app` | `#F5F5F5` / `--traza-gray-50` |
| `background.surface` | `#FFFFFF` / `--traza-white` |
| `background.elevated` | `#FFFFFF` más sombra |
| `background.muted` | `#FAFAFA` / `--traza-surface-muted` |
| `text.primary` | `#1A1A1A` |
| `text.secondary` y `text.muted` | `#666666` |
| `text.inverse` | `#FFFFFF`, usado en tarjeta oscura |
| `border.default` | `#E3E3E3` |
| `border.strong` | **PENDIENTE DE CONSOLIDAR**; se usan varios hardcodes |
| `state.success.*` | `#EAF7EF` / `#17643A` / `#BFE5CC` |
| `state.warning.*` | `#FFF7D6` / `#6B5600` / `#F1DC84` |
| `state.error.*` | `#FDECEC` / `#9F2D2D` / `#F2C3C3` |
| `state.neutral.*` | `#F1F3F5` / `#4B5563` / `#D8DDE3` |
| `state.info.*` | **NO CONFIRMADO como token vigente** |
| `action.primary` | fondo `#F4C414`, texto `#1A1A1A`, borde `#D8AE00` |
| `action.primaryHover` | mismo color con `brightness(0.97)` |
| `action.secondary` | fondo `#FFFFFF`, texto `#1A1A1A`, borde `#E3E3E3` |
| `action.destructive` | semántica danger; no hay un único botón destructivo compartido |
| `focus.ring` | `0 0 0 3px rgba(244, 196, 20, 0.35)` |

Hay hardcodes dispersos y dos capas históricas de CSS. Esto impide afirmar que todos los componentes consumen exclusivamente tokens: **PENDIENTE DE CONSOLIDAR**.

# 5. Tipografía

Familia global real: `Inter, ui-sans-serif, system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI", sans-serif` (`frontend/src/index.css:2`). No hay import de Google Fonts ni archivo local de Inter; si el sistema no posee Inter se usa el fallback. Monoespaciada: `ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, monospace` (`index.css:116`).

| Rol observado | Tamaño / escala | Peso | Line-height / tracking | Fuente |
|---|---|---|---|---|
| Display auth | `clamp(42px, 5.2vw, 68px)` | heredado/encabezado | `1.02`, `-0.045em` | `globals.css:50-52` |
| H1 auth card | `clamp(27px, 3vw, 34px)` | heredado | `1.15`, `-0.025em` | `globals.css:128-130` |
| H1 dashboard | `clamp(26px, 3vw, 32px)` | heredado | `1.2`, `-0.02em` | `globals.css:640-642` |
| H2 de sección | `22px` habitual | heredado | tracking `-0.015em` en producto | `globals.css:721-724, 906-909` |
| H2 Control Tower | `clamp(24px, 3vw, 30px)` | heredado | `1.2`, `-0.02em` | `control-tower.css:1494-1496` |
| KPI | `21px` | `strong` | `1.25` | `globals.css:470-475` |
| Body destacado | `17px` | normal | `1.7` | `globals.css:58-60` |
| Body | `13–15px` | 400–750 | `1.35–1.6` | varias reglas globales |
| Label | `13–14px` | `700/750` | normal | `globals.css:146-148, 821-824` |
| Caption/help | `11–12px` | 500–800 | tracking hasta `0.11em` | estilos globales |
| Button | heredado; normalmente `13–15px` | `800` en base | normal | `index.css:40` |
| Status | `11px` | `900` en base | `1.2`, `0.04–0.05em` | `index.css:70`, `globals.css:482-490` |
| Numeric/ID | `12px` monoespaciado | normal | normal | `index.css:116` |

Los pesos `650`, `700`, `750`, `800` y `900` aparecen aunque no se carga una familia web con todos esos archivos. El navegador sintetiza pesos cuando sea necesario: **PENDIENTE DE CONSOLIDAR**.

# 6. Geometría del sistema

## 6.1 Espaciado

Escala tokenizada confirmada: `4`, `8`, `12`, `16`, `24`, `32` px (`--traza-space-1` a `--traza-space-6`, `tokens.css:31-36`). También se usan recurrentemente `10`, `14`, `18`, `20`, `28`, `40`, `48` y valores fluidos con `clamp()`; no todos están tokenizados.

## 6.2 Radios

| Nombre descriptivo | Valor real | Uso |
|---|---:|---|
| Control | `10px` | inputs y controles (`--traza-radius-control`) |
| Compacto | `8px` | tab interno |
| Intermedio | `11px`, `12px`, `14px` | botones/filas/paneles |
| Card | `16px` | cards (`--traza-radius-card`) |
| Card destacado | `18px` | auth y tarjeta bancaria |
| Pill | `999px` | estados y tags |

## 6.3 Bordes

- Predominio: `1px solid #E3E3E3`.
- Acento: bordes superiores/laterales de `3px` o `4px` en amarillo/estado.
- Empty states: `1px dashed #E3E3E3`.
- Separadores de tabla: `1px solid #E3E3E3`.

## 6.4 Elevación

| Token/uso | Valor exacto |
|---|---|
| `--traza-shadow-sm` | `0 8px 24px rgba(26, 26, 26, 0.06)` |
| `--traza-shadow-topbar` | `0 1px 0 rgba(26, 26, 26, 0.04)` |
| Auth card | `0 22px 60px rgba(26, 26, 26, 0.1)` |
| Tabbar | `0 4px 16px rgba(26, 26, 26, 0.04)` |
| Tarjeta bancaria | `0 14px 28px rgba(26, 26, 26, 0.14)` |

Radios y sombras fuera de tokens son **PENDIENTE DE CONSOLIDAR**.

# 7. Iconografía

- Librería de iconos: ninguna confirmada en dependencias o imports.
- Iconos/símbolos reales: `✓`, `→`, `×`, puntos de máscara `•` y chevrons dibujados con CSS.
- Chip de tarjeta: elemento decorativo CSS, no asset.
- Los símbolos de acción se acompañan de texto salvo el cierre `×`, que posee `aria-label`.
- Tamaño estándar de icono: **NO CONFIRMADO**; no existe token.
- Color: hereda el texto o usa variables del componente.

**PENDIENTE DE CONSOLIDAR:** mezcla de Unicode y formas CSS sin catálogo, grid ni reglas de trazo.

# 8. Catálogo de componentes

## 8.1 Inventario nombrado (29)

| # | Componente | Ruta | Propósito / uso |
|---:|---|---|---|
| 1 | `App` | `frontend/src/App.tsx` | Restauración de sesión y auth pública |
| 2 | `AuthBrand` | `App.tsx` | Logo en auth |
| 3 | `LoginPanel` | `App.tsx` | Login |
| 4 | `RegisterPanel` | `App.tsx` | Registro de cliente |
| 5 | `Dashboard` | `App.tsx` | Orquestación por rol |
| 6 | `AdminDashboard` | `App.tsx` | Shell y flujos ADMIN |
| 7 | `CustomerDashboard` | `App.tsx` | Shell y orquestación cliente |
| 8 | `LoansPane` | `App.tsx` | Préstamos cliente |
| 9 | `OperationsPane` | `App.tsx` | Operaciones cliente |
| 10 | `ControlTower` | `frontend/src/ControlTower.tsx` | Supervisión operacional ADMIN |
| 11 | `Topbar` | `components/layout/Topbar.tsx` | Marca y logout |
| 12 | `Metric` | `components/ui/Metric.tsx` | KPI card |
| 13 | `StatusPill` | `components/ui/StatusPill.tsx` | Estado general |
| 14 | `AccountSidebar` | `features/accounts/AccountSidebar.tsx` | Selector lateral de cuentas |
| 15 | `AccountsPane` | `features/accounts/AccountsPane.tsx` | Cuentas y alta |
| 16 | `AccountsTable` | `features/accounts/AccountsTable.tsx` | Tabla de cuentas |
| 17 | `CardsPane` | `features/cards/CardsPane.tsx` | Listado/emisión de tarjetas |
| 18 | `CardPreview` | `features/cards/CardPreview.tsx` | Tarjeta visual interactiva |
| 19 | `CardDetailsModal` | `features/cards/CardDetailsModal.tsx` | Diálogo protegido |
| 20 | `ApprovedLoansList` | `features/loans/ApprovedLoansList.tsx` | Préstamos y cuotas |
| 21 | `LoanApplicationForm` | `features/loans/LoanApplicationForm.tsx` | Formulario de solicitud |
| 22 | `LoanApplicationsTable` | `features/loans/LoanApplicationsTable.tsx` | Solicitudes cliente |
| 23 | `MovementsPane` | `features/movements/MovementsPane.tsx` | Movimientos/empty/loading |
| 24 | `CustomerOverview` | `pages/customer/CustomerOverview.tsx` | Hero y métricas cliente |
| 25 | `CustomerTabbar` | `pages/customer/CustomerTabbar.tsx` | Navegación interna cliente |
| 26 | `CustomerWorkspace` | `pages/customer/CustomerWorkspace.tsx` | Sidebar + contenido |
| 27 | `AccordionHeader` | `features/control-tower/components/AccordionHeader.tsx` | Toggle de sección ADMIN |
| 28 | `CtStatus` | `features/control-tower/components/CtStatus.tsx` | Estado de Control Tower |
| 29 | `FlowStrip` | `features/control-tower/components/FlowStrip.tsx` | Flujo numerado |

## 8.2 Perfiles de familias visuales

### Button / IconButton

**Ruta:** estilos en `index.css` y `globals.css`; JSX repetido.

**Variantes:** `primary-button`, `secondary-button`, `text-button`, `compact`, opciones approve/reject, collapse y close.

**Estados:** hover, focus-visible, disabled y busy mediante texto.

**Responsive:** botones de auth pasan a ancho completo en móvil.

**Nota:** `card-modal-close` es el único icon button claro. **IMPLEMENTACIÓN REPETIDA — NO HAY COMPONENTE COMPARTIDO**.

### Input / Select / TextArea

**Ruta:** formularios de `App.tsx`, `AccountsPane`, `LoanApplicationForm` y `OperationsPane`.

**Estados:** required, focus, disabled y mensajes de error externos.

**Visual:** alto mínimo 44 px autenticado y 48 px en auth, borde `#E3E3E3`, radio 10 px.

**Responsive:** grids pasan a una columna.

**Nota:** **IMPLEMENTACIÓN REPETIDA — NO HAY COMPONENTE COMPARTIDO**.

### Checkbox / Radio / Switch

Checkbox nativo confirmado en revisión de identidad ADMIN (`App.tsx:824,852`). Los selectores approve/reject son botones con `aria-pressed`, no radios nativos. Switch no existe. **NO HAY COMPONENTE COMPARTIDO**.

### Badge / StatusBadge

**Componentes:** `StatusPill` y `CtStatus`.

**Variantes:** success, warning, danger, neutral.

**Props:** `value: string` y `value: string | null`.

**Estados:** mapeos descritos en sección 9.

**Responsive:** inline-flex, no cambia de patrón.

### Card / KPI Card

**Componentes:** `Metric`, `CardPreview` y múltiples cards directas de ADMIN/Control Tower.

**Visual:** superficie blanca, borde, radio 16 px y sombra suave; tarjeta bancaria oscura.

**Responsive:** grids reducen columnas.

**Nota:** las cards de Control Tower no están abstraídas individualmente.

### Alert / Toast

`message success/error`, avisos warning y `actionNotice` inline. Usan `role="status"` o `role="alert"` en varios flujos. No hay toast flotante ni sistema global. **IMPLEMENTACIÓN REPETIDA — NO HAY COMPONENTE COMPARTIDO**.

### Modal / Dialog

**Componente:** `CardDetailsModal`.

**Estados:** referencia oculta/revelada por 15 segundos.

**Accesibilidad:** portal, `role="dialog"`, `aria-modal`, title/description, Escape, click fuera, focus inicial, focus trap y restauración.

**Responsive:** modal y acciones se apilan bajo breakpoints.

No hay Drawer.

### Navbar / Header / Footer

`Topbar` es el header autenticado: logo, descriptor y logout. Footer público y footer cliente son texto directo en `App.tsx`. No hay navbar ADMIN con destinos ni sidebar ADMIN.

### Tabs / Sidebar

`CustomerTabbar` usa botones con `aria-pressed` y overflow horizontal. `AccountSidebar` es selector de cuentas, no navegación global. ADMIN usa acordeones, no tabs.

### Table

Tablas reales de cuentas, movimientos, solicitudes y cuotas; Control Tower usa tablas/filas densas. Todas permiten overflow horizontal en web. No hay sorting, pagination, search ni filter UI general.

### EmptyState / ErrorState / Loader

Empty states son párrafos con `customer-empty-state` o `admin-empty-state`. Errores son bloques `message error`. Loaders son texto (`Inicializando…`, `Cargando…`, `Actualizando…`) y botones disabled; no existen skeleton ni spinner compartidos.

### Timeline / Detail panel / Audit panel

`FlowStrip` muestra pasos numerados; el detalle de operación dentro de `ControlTower` contiene transacciones, ledger y auditoría. No existe componente `Timeline` general ni panel de auditoría extraído.

### DataOps run / Quarantine / Gold / Copilot

Implementados directamente dentro de `ControlTower.tsx`: filas de runs, controles de calidad, registros aislados, Gold vigente, confirmaciones de ejecución, análisis Copilot e HITL. Solo `AccordionHeader`, `CtStatus` y `FlowStrip` están extraídos. **IMPLEMENTACIÓN REPETIDA — NO HAY COMPONENTES DE DOMINIO FINOS COMPARTIDOS**.

### Onboarding exception / Credit review

Cards/filas expandibles dentro de `AdminDashboard`, con resumen, evidencia, controles, decisión y feedback. No están extraídas como componentes propios. El estado futuro `REVIEW_REQUIRED` aparece en la fuente maestra, pero no en el mapeo visual actual: **NO CONFIRMADO en UI**.

# 9. Estados y semántica visual

| Estado observado | Texto visible | Fondo | Texto | Icono | Componente |
|---|---|---:|---:|---|---|
| `ACTIVE` | Activa / Activo | `#EAF7EF` | `#17643A` | ninguno | `StatusPill` / `CtStatus` |
| `PENDING` | Pendiente | `#FFF7D6` en `StatusPill`; neutral en `CtStatus` | `#6B5600` o `#4B5563` | ninguno | ambos |
| `VERIFIED` | Verificado | `#EAF7EF` | `#17643A` | ninguno | ambos |
| `APPROVED` | Aprobado | neutral en `StatusPill`; success en `CtStatus` | según componente | ninguno | ambos |
| `REJECTED` | Rechazado | `#FDECEC` | `#9F2D2D` | ninguno | ambos |
| `MATCH` / `CURRENT` / `COINCIDE` | Coincidencia / Vigente / Coincide | `#EAF7EF` | `#17643A` | ninguno | `StatusPill` |
| `MISMATCH` / `NOT_FOUND` / `NOT_CURRENT` | texto localizado | `#FDECEC` | `#9F2D2D` | ninguno | `StatusPill` |
| `BLOCKED` / `FROZEN` | Bloqueada / Congelada | `#FDECEC` | `#9F2D2D` | ninguno | `StatusPill` |
| `COMPLETED` / `PAID` | Completada / Pagada | `#EAF7EF` | `#17643A` | ninguno | `StatusPill` |
| `SCHEDULED` / `PARTIAL` | Programada / Parcial | `#FFF7D6` | `#6B5600` | ninguno | `StatusPill` |
| `UP` / `SUCCEEDED` | Operativo / Correcto | `#EAF7EF` | `#17643A` | ninguno | `CtStatus` |
| `PUBLISHED` | Publicado | `#EAF7EF` | `#17643A` | ninguno | `CtStatus` |
| `ACKNOWLEDGED` | Revisado | `#EAF7EF` | `#17643A` | ninguno | `CtStatus` |
| `QUALITY_GATE_FAILED` / `FAILED` | Control no aprobado / Fallido | `#FDECEC` | `#9F2D2D` | ninguno | `CtStatus` |

`INACTIVE`, `REVIEW_REQUIRED`, `OPEN`, `ASSIGNED`, `RESOLVED`, `DENIED` y `QUARANTINED` no tienen mapeo confirmado en los componentes de estado inspeccionados. Quarantine sí aparece como concepto y panel, pero no como valor mapeado de badge. Esta lista no debe ampliarse en mobile como si estuviera implementada.

Inconsistencia funcional-visual confirmada: `APPROVED` resulta neutral en `StatusPill`, pero success en `CtStatus`; `PENDING` es warning en `StatusPill`, pero neutral en `CtStatus`.

# 10. Layout y navegación

## 10.1 Estructura real

```text
TRAZA SPA (sin rutas URL declaradas)
├── Sesión no autenticada
│   ├── Login
│   └── Registro
├── Rol CUSTOMER
│   ├── Onboarding / RENIEC Simulator, si no está VERIFIED
│   └── Workspace verificado
│       ├── Cuentas
│       ├── Movimientos
│       ├── Tarjetas
│       ├── Préstamos
│       └── Operaciones
└── Rol ADMIN
    └── Panel administrativo
        ├── Resumen
        ├── Centro de Operaciones y Trazabilidad
        │   ├── Trazabilidad de operaciones
        │   ├── Decisiones administrativas
        │   ├── Calidad y publicación de datos
        │   └── Copilot operacional / revisión humana
        ├── Cola de verificación de identidad
        └── Solicitudes de préstamo
```

## 10.2 Medidas y comportamiento

- Auth: ancho `min(1240px, calc(100% - 48px))`; dos columnas, una bajo 900 px.
- Dashboard base histórico: ancho `min(1180px, calc(100% - 40px))` en `index.css`.
- Topbar: mínimo 68 px, padding horizontal fluido `clamp(20px, 5vw, 64px)`; no es sticky/fixed.
- Cliente: sidebar de 260–292 px + contenido; pasa a una columna bajo 1100 px.
- Tabs cliente: una fila con scroll horizontal.
- Tablas: `overflow-x: auto`; algunas conservan `min-width`.
- Control Tower: breakpoints confirmados en 1100, 900, 760 y 480 px.
- Global/auth: breakpoints 1100, 900, 560 y 390 px; además existen históricos 860 y 620 px.
- ADMIN no posee menú persistente ni destinos URL. La página se recorre verticalmente y sus secciones se expanden/colapsan.

No existe modo oscuro seleccionable. Hay una base oscura histórica en `index.css`, sobreescrita por reglas claras de `globals.css` y reglas claras tardías en Control Tower: **PENDIENTE DE CONSOLIDAR**.

# 11. Inventario de pantallas ADMIN

La web tiene **una sola página ADMIN sin URL propia**, compuesta por **8 superficies funcionales internas**. “Ruta web” se expresa como estado porque no existe router.

## 11.1 Dashboard / Home ADMIN

**Ruta web:** usuario autenticado con `role === 'ADMIN'`.

**Archivo:** `App.tsx:323-1274`.

**Objetivo:** resumen de rol, cuenta, pendientes y salud DB.

**Datos:** usuario, conexión, conteos.

**Acciones:** logout y refresh indirecto tras decisiones.

**Estados:** métricas, feedback success/error.

**Endpoints:** `/health/db`, pendientes de onboarding y préstamos.

## 11.2 Control Tower — overview

**Ruta web:** bloque embebido `#operations-control-tower`.

**Archivo:** `ControlTower.tsx:1145+`.

**Objetivo:** salud API/OLTP, último DataOps, Gold y conteos.

**Acciones:** actualizar, mostrar/ocultar; link Power BI si hay variable.

**Estados:** loading textual, error y health cards.

**Endpoint:** `GET /api/v1/admin/control-tower/overview`.

## 11.3 Trazabilidad de operaciones

**Ruta web:** acordeón “Trazabilidad de operaciones”.

**Objetivo:** listar operaciones y abrir detalle.

**Datos:** operación, actor, clientes, transacciones, ledger, auditoría y flujo.

**Acciones:** expandir/cerrar detalle.

**Estados:** trace loading/error.

**Endpoints:** listado y detalle de operación.

## 11.4 Decisiones administrativas

**Ruta web:** acordeón “Decisiones administrativas”.

**Objetivo:** visualizar decisiones humanas de identidad/crédito y evidencia.

**Acciones:** expandir sección y ver más/menos.

**Estados:** lista o ausencia de datos.

**Endpoint:** `GET /api/v1/admin/control-tower/decisions?limit=8`.

## 11.5 DataOps, Quality, Quarantine y Gold

**Ruta web:** acordeón “Calidad y publicación de datos”.

**Objetivo:** runs, gates, registros aislados y versión Gold vigente.

**Acciones:** ejecutar DataOps, ejecutar prueba de calidad con confirmación, mostrar más/menos.

**Estados:** running, success/warning, protected Gold, failed run.

**Endpoints:** dataops, run-dataops y run-quality-test.

## 11.6 Copilot operacional / HITL

**Ruta web:** detalle embebido por run DataOps.

**Objetivo:** generar/consultar análisis y registrar revisión humana.

**Datos:** categoría, confianza, riesgos, hallazgos, pasos y metadata del modelo/prompt.

**Acciones:** analizar/abrir/cerrar y marcar `ACKNOWLEDGED`.

**Estados:** loading, análisis existente, requiere revisión y revisado.

**Endpoints:** analyze, history y review.

## 11.7 Cola de verificación de identidad

**Ruta web:** sección embebida “Cola de verificación”.

**Archivo:** `App.tsx:614+`.

**Objetivo:** revisar evidencia RENIEC y decidir VERIFIED/REJECTED.

**Acciones:** abrir/cerrar, checks, notas, decisión y submit.

**Estados:** empty, pending, success/error, busy.

**Endpoints:** pending y review de onboarding.

## 11.8 Cola de revisión crediticia

**Ruta web:** sección embebida “Solicitudes de préstamo”.

**Archivo:** `App.tsx:955+`.

**Objetivo:** aprobar/rechazar, configurar TEA y dejar notas.

**Acciones:** abrir/cerrar, aprobar/rechazar y submit.

**Estados:** empty, pending, validación de TEA, busy, feedback.

**Endpoints:** pending y review de préstamos.

Power BI no es una pantalla local: es un enlace externo condicional “Abrir análisis en Power BI”, visible solo cuando `VITE_POWER_BI_REPORT_URL` no está vacío (`ControlTower.tsx:1636-1677`).

# 12. Contratos API consumidos por ADMIN

## 12.1 Infraestructura HTTP y sesión

- `baseURL`: `import.meta.env.VITE_API_BASE_URL ?? ''`, sin slash final (`api.ts:22-28`).
- Transporte: `fetch`; JSON cuando `content-type` lo indica.
- JWT: guardado en `sessionStorage` con clave técnica `bancocloud_access_token`; se adjunta como `Authorization: Bearer <token>`.
- `401`: `request` lanza `ApiError`. En restauración inicial se limpia sesión; en llamadas posteriores no existe interceptor global que fuerce logout. En login se traduce a “Correo o contraseña incorrectos.”
- `403`: se presenta “Acceso denegado: …” donde se usa `readableError`.
- Error de red: no hay wrapper específico; App muestra fallback general en varios flujos y Control Tower puede mostrar `Error.message`.
- Timeout: no existe `AbortController` ni timeout configurado.
- Retry: no existe retry automático.
- Secretos/tokens reales: no se incluyen en este documento.

## 12.2 Endpoints de dominio ADMIN (14)

| Pantalla | Método | Endpoint | Request | Response usada | Auth |
|---|---|---|---|---|---|
| Onboarding | GET | `/api/v1/admin/onboarding/pending` | — | `PendingOnboarding[]` | Bearer |
| Onboarding | POST | `/api/v1/admin/onboarding/{customerId}/review` | `ReviewPayload` | `ReviewResponse` | Bearer |
| Créditos | GET | `/api/v1/admin/loans/pending` | — | `PendingLoanApplication[]` | Bearer |
| Créditos | POST | `/api/v1/admin/loans/{applicationId}/review` | `LoanAdminReviewPayload` | `LoanAdminReviewResponse` | Bearer |
| CT overview | GET | `/api/v1/admin/control-tower/overview` | — | `ControlTowerOverview` | Bearer |
| Operaciones | GET | `/api/v1/admin/control-tower/operations?limit={limit}` | query `limit` | `ControlTowerOperation[]` | Bearer |
| Trazabilidad | GET | `/api/v1/admin/control-tower/operations/{operationId}` | path ID | `ControlTowerOperationTrace` | Bearer |
| Decisiones | GET | `/api/v1/admin/control-tower/decisions?limit={limit}` | query `limit` | `ControlTowerDecision[]` | Bearer |
| DataOps | GET | `/api/v1/admin/control-tower/dataops?limit={limit}` | query `limit` | `ControlTowerDataOps` | Bearer |
| DataOps | POST | `/api/v1/admin/control-tower/actions/run-dataops` | sin body | `ControlTowerActionResult` | Bearer |
| Quality | POST | `/api/v1/admin/control-tower/actions/run-quality-test` | sin body | `ControlTowerActionResult` | Bearer |
| Copilot | POST | `/api/v1/admin/copilot/dataops/{runId}/analyze` | sin body | `CopilotAnalysisResponse` | Bearer |
| Copilot HITL | POST | `/api/v1/admin/copilot/{correlationId}/review` | `CopilotReviewPayload` | `CopilotReviewResponse` | Bearer |
| Copilot | GET | `/api/v1/admin/copilot/history?limit={limit}` | query `limit` | `CopilotHistoryResponse` | Bearer |

## 12.3 Endpoints compartidos relevantes, no incluidos en el conteo 14

| Uso | Método | Endpoint | Auth |
|---|---|---|---|
| Inicio de sesión | POST | `/api/v1/auth/token` | No; form-urlencoded |
| Restauración/perfil | GET | `/api/v1/auth/me` | Bearer |
| Salud DB mostrada | GET | `/health/db` | No en el cliente actual |

# 13. Elementos web que requieren adaptación móvil

## 13.1 Tablas anchas

**WEB ACTUAL:** tablas con scroll horizontal y anchos mínimos.

**RIESGO EN MOBILE:** comparación difícil, targets pequeños y pérdida de contexto.

**RECOMENDACIÓN PARA MOBILE:** `LazyColumn` de cards-resumen, detalle bajo demanda y opción copiar IDs; conservar todos los campos.

## 13.2 Página ADMIN extensa

**WEB ACTUAL:** todas las áreas están en una página vertical.

**RIESGO EN MOBILE:** carga cognitiva y scroll muy largo.

**RECOMENDACIÓN PARA MOBILE:** destinos de primer nivel y pantallas de detalle; no replicar el documento largo.

## 13.3 Control Tower multicolumna

**WEB ACTUAL:** health grid, KPIs, paneles y tablas.

**RIESGO EN MOBILE:** densidad y wrapping impredecible.

**RECOMENDACIÓN PARA MOBILE:** resumen crítico arriba, listas de dos niveles y secciones colapsables.

## 13.4 Tabs horizontales y sidebar

**WEB ACTUAL:** tabs cliente con scroll y selector lateral de cuentas.

**RIESGO EN MOBILE:** no corresponde a la app exclusivamente ADMIN.

**RECOMENDACIÓN PARA MOBILE:** no copiar estas capacidades cliente.

## 13.5 Hover y detalles expandibles

**WEB ACTUAL:** hover refuerza selección; varios detalles se abren inline.

**RIESGO EN MOBILE:** hover no existe y el inline puede quedar lejos del disparador.

**RECOMENDACIÓN PARA MOBILE:** estado pressed/selected y navegación a detail screen o bottom sheet según volumen.

## 13.6 Confirmaciones DataOps

**WEB ACTUAL:** paneles de confirmación dentro de la sección.

**RIESGO EN MOBILE:** acción accidental o pérdida de contexto.

**RECOMENDACIÓN PARA MOBILE:** diálogo explícito con consecuencia, acción destructiva/operacional claramente nombrada y loading bloqueante local.

## 13.7 Copilot denso

**WEB ACTUAL:** análisis largo con riesgos, hallazgos, pasos y metadata.

**RIESGO EN MOBILE:** bloques extensos y mala escaneabilidad.

**RECOMENDACIÓN PARA MOBILE:** resumen, evidencia y recomendación en secciones; HITL siempre explícito y separado.

# 14. Traducción del Design System a Android

Todo este capítulo es **RECOMENDACIÓN PARA MOBILE**.

```text
CSS token vigente → Compose token → Material 3 role cuando corresponda
```

| Web | Compose propuesto | Material 3 |
|---|---|---|
| `--traza-yellow #F4C414` | `TrazaColors.BrandPrimary` | `primary` |
| `--traza-text #1A1A1A` | `TrazaColors.TextPrimary` | `onPrimary` y `onSurface` según contexto |
| `--traza-white #FFFFFF` | `TrazaColors.Surface` | `surface` |
| `--traza-gray-50 #F5F5F5` | `TrazaColors.AppBackground` | `background` |
| `--traza-surface-muted #FAFAFA` | `TrazaColors.SurfaceMuted` | `surfaceVariant` aproximado |
| danger text `#9F2D2D` | `TrazaColors.Error` | `error` |
| danger bg `#FDECEC` | `TrazaColors.ErrorContainer` | `errorContainer` |
| success triplet | `TrazaColors.Success*` | custom; no forzar a primary |
| warning triplet | `TrazaColors.Warning*` | custom |

Esqueleto propuesto, no código implementado:

```kotlin
object TrazaColors
object TrazaTypography
object TrazaShapes
object TrazaSpacing

@Composable
fun TrazaTheme(content: @Composable () -> Unit)
```

- `TrazaSpacing`: 4, 8, 12, 16, 24 y 32 dp.
- `TrazaShapes`: control 10 dp, card 16 dp, pill 999 dp/CircleShape.
- `TrazaTypography`: conservar jerarquías y familia sans del sistema si Inter no se incorpora explícitamente; no afirmar que Inter está embebida.
- Preservar identidad TRAZA antes que forzar una equivalencia Material 3 incorrecta.
- No habilitar Dynamic Color por defecto si reemplaza el amarillo/antracita confirmado; decisión futura, **NO CONFIRMADA**.

# 15. Mapeo Web → Android

Todo este capítulo es **RECOMENDACIÓN PARA MOBILE**.

| Web | Android Compose recomendado | Adaptación |
|---|---|---|
| `Topbar` | `TopAppBar` custom | Logo compacto, título contextual y logout en menú explícito |
| Página ADMIN larga | `Scaffold` + destinos | Separar resumen y dominios |
| Accordion | `Card` expandible | Mantener `stateDescription` y chevron |
| KPI card | `Card` custom | 1–2 columnas según ancho |
| `StatusPill` / `CtStatus` | chip custom no interactivo | Texto + color; unificar semántica antes de implementar |
| Tabla | `LazyColumn` + cards | Resumen por fila y detail screen |
| FlowStrip | stepper vertical | Evitar flujo horizontal estrecho |
| Modal pequeño | `AlertDialog` | Confirmación o información breve |
| Detalle largo | pantalla o `ModalBottomSheet` grande | Preferir pantalla para trazabilidad/Copilot |
| Alert inline | banner/card semántica | Anunciar con live semantics cuando aplique |
| Loader textual | `CircularProgressIndicator` + texto | Mantener contexto de la acción |
| Empty state | bloque con título/copy | Sin inventar ilustración |
| DataOps row | card operacional | Estado, conteos y timestamps primero |
| Copilot panel | detail screen seccionada | Mantener evidencia, confianza y HITL |
| Onboarding row | card de excepción | Datos mínimos, evidencia y decisión explícita |

# 16. Navegación móvil recomendada

**RECOMENDACIÓN PARA MOBILE — no implementada.**

```text
TRAZA Admin Mobile
├── Inicio (M0)
├── Onboarding (M0)
│   └── Detalle de revisión/excepción
├── Operaciones (M0)
│   └── Trazabilidad de operación
├── DataOps (M1)
│   └── Run / Quarantine / Gold
└── Copilot (M1)
    └── Análisis / revisión humana
```

En teléfonos, usar navegación inferior solo si el número final de destinos estables no supera cinco; de lo contrario combinar destinos principales con navegación secundaria. Power BI debe abrirse como enlace externo controlado, no recrearse como WebView sin revisión de seguridad. No incluir Cuentas, Tarjetas, Préstamos cliente ni Operaciones bancarias de cliente.

# 17. Principios para TRAZA Admin Mobile

1. Misma identidad visual, distinta composición.
2. Mobile-first.
3. Información operacional priorizada.
4. Acciones sensibles explícitas.
5. Estados críticos visibles sin depender solo del color.
6. Nada esencial depende de hover.
7. Datos largos deben poder envolverse/copiarse.
8. IDs técnicos deben mostrarse de forma secundaria.
9. Resumen primero, detalle bajo demanda.
10. No reducir seguridad para facilitar UX.
11. No duplicar reglas del backend en Android.
12. Android nunca se conecta directamente a PostgreSQL.
13. La app consume FastAPI por HTTPS.
14. El backend sigue siendo la autoridad funcional.

# 18. Priorización móvil

| Prioridad | Funcionalidad real | Justificación |
|---|---|---|
| M0 | Login, restauración segura y logout | Acceso imprescindible; contrato compartido |
| M0 | Inicio ADMIN con salud y pendientes | Resumen operacional mínimo |
| M0 | Cola y detalle de onboarding | Trabajo ADMIN principal |
| M0 | Decisión humana de onboarding | Mantener guardrails y evidencia |
| M0 | Lista y detalle de operaciones | Supervisión y trazabilidad |
| M0 | Errores/loading/empty y sesión expirada | UX operable y segura |
| M1 | Revisión crediticia | Flujo real, sensible y de riesgo medio |
| M1 | Decisiones administrativas históricas | Auditoría útil |
| M1 | DataOps/Quality/Quarantine/Gold read-only | Observabilidad operacional |
| M1 | Copilot history/análisis/HITL | Útil, pero posterior al núcleo de supervisión |
| M2 | Ejecutar DataOps/quality desde móvil | Acción sensible; no necesaria en primer MVP |
| M2 | Link Power BI | Puede delegarse al navegador y depende de configuración |
| M2 | Capacidades CUSTOMER | Fuera del alcance ADMIN Mobile |

# 19. Accesibilidad

## 19.1 Observado en web

- Focus visible: outline antracita de 2 px y focus ring amarillo.
- Reduced motion: media query en auth y app shell.
- Forms: labels envolventes, `required`, patrones, ayudas y disabled.
- Feedback: varios `role="alert"`, `role="status"` y `aria-live`.
- Acordeones: `aria-expanded`; onboarding usa `aria-controls`.
- Tabs cliente: `aria-pressed` y nav con label.
- Modal de tarjeta: semántica dialog, focus trap, Escape, restauración de foco y scroll lock.
- Estados: texto localizado además de color.
- Tamaño de controles: 44–48 px en las áreas vigentes principales.
- Tablas: scroll horizontal, pero no hay caption confirmado en todas.
- Idioma HTML: `lang="en"` aunque la interfaz está en español: inconsistencia.
- Control Tower muestra un error sin `role="alert"` en su bloque principal: **PENDIENTE DE CONSOLIDAR**.

## 19.2 Recomendación para Android

**RECOMENDACIÓN PARA MOBILE:**

- `contentDescription` solo para imágenes/acciones que lo necesiten; marcar decorativos como no semánticos.
- Touch target mínimo de 48 dp.
- Verificar TalkBack y orden de foco en cada flujo.
- Respetar font scaling sin truncar estados, montos ni decisiones.
- Mantener contraste y texto/forma además del color.
- Anunciar cambios de loading, éxito y error.
- Exponer `stateDescription` en expandibles y estados.
- Permitir seleccionar/copiar IDs y evidencias sin exponer secretos.

# 20. Inconsistencias / deuda visual

1. `index.css` conserva una base oscura y numerosos azules históricos; `globals.css` y la capa tardía de `control-tower.css` implantan el sistema claro. No hay tema explícito.
2. `:root` mantiene colores oscuros mientras shells específicos los sobrescriben; los controles no cubiertos pueden heredar distinto.
3. Muchos HEX, radios, sombras y espaciados siguen hardcodeados fuera de `tokens.css`.
4. `StatusPill` y `CtStatus` asignan tonos distintos a `APPROVED` y `PENDING`.
5. El color info no tiene token consolidado.
6. No hay componente compartido para Button, Field, Alert, EmptyState, Loader, Table o las cards ADMIN.
7. Los pesos tipográficos 650/750/800/900 pueden sintetizarse; Inter no se carga como asset.
8. No existe catálogo/librería de iconos; se mezclan Unicode y CSS.
9. Breakpoints superpuestos: 1100, 900, 860, 760, 620, 560, 480 y 390 px.
10. Radios recurrentes de 8, 10, 11, 12, 14, 16 y 18 px sin taxonomía completa.
11. React Router está instalado pero no usado; no existen deep links ni rutas ADMIN.
12. `AdminDashboard` y `ControlTower` siguen siendo componentes extensos con UI de dominio inline.
13. El HTML declara `lang="en"` con contenido español.
14. El favicon declarado no está confirmado como variante oficial del logo TRAZA.
15. Power BI depende de una variable build-time y el botón desaparece cuando está vacía; no hay placeholder ni explicación visible.
16. Hay texto técnico residual “BancoCloud” en flujos cliente aunque la marca visible es TRAZA.
17. No hay paginación/search/filter de UI para listas ADMIN; los límites se fijan en las llamadas.
18. El manejo de `401` posterior al login no está centralizado como expiración de sesión.

No se corrige ninguna de estas deudas en esta tarea.

# 21. Checklist de extracción

- [x] paleta exacta extraída;
- [x] tokens semánticos mapeados;
- [x] tipografía documentada;
- [x] spacing/radius/shadows documentados;
- [x] iconografía documentada;
- [x] componentes compartidos inventariados;
- [x] estados visuales inventariados;
- [x] navegación ADMIN documentada;
- [x] pantallas ADMIN documentadas;
- [x] APIs realmente consumidas documentadas;
- [x] estrategia responsive documentada;
- [x] inconsistencias registradas;
- [x] mapeo a Compose propuesto;
- [x] ninguna lógica funcional modificada;
- [x] ningún secreto incluido.

# 22. Archivos que una IA Android debe leer

## OBLIGATORIOS

1. `docs/TRAZA_DESIGN_SYSTEM_MASTER_V1.md` — fuente de verdad visual y mapeo de handoff.
2. `docs/project/BancoCloud_FUENTE_UNICA_CONSOLIDADA_ACTUALIZADA_2026-10-02_V11.md` — autoridad funcional, arquitectura, seguridad y estado vigente.
3. `frontend/src/api.ts` — endpoints realmente consumidos, sesión, headers y errores; debe orientar Retrofit sin copiar almacenamiento web.
4. `frontend/src/types.ts` — contratos que la web usa; deben contrastarse con OpenAPI antes de generar DTOs Android.
5. `frontend/src/styles/tokens.css` — valores físicos canónicos confirmados.
6. `frontend/src/styles/globals.css` — aplicación efectiva de tokens, estados, responsive y accesibilidad.
7. `frontend/src/control-tower.css` — geometría y estados específicos ADMIN/Control Tower.
8. `frontend/src/App.tsx` — auth, rol, flujos ADMIN de onboarding/crédito y estados de pantalla.
9. `frontend/src/ControlTower.tsx` — dominios operacionales, DataOps, Copilot, Power BI y acciones sensibles.
10. `frontend/src/components/ui/StatusPill.tsx` y `frontend/src/features/control-tower/components/CtStatus.tsx` — semántica real de estados e inconsistencia que debe resolverse conscientemente.
11. `frontend/src/assets/branding/traza-logo.jfif` — referencia de marca vigente; validar formato/densidades antes de incorporarlo a Android.

## ÚTILES SOLO COMO REFERENCIA SECUNDARIA

- `frontend/src/components/layout/Topbar.tsx` — branding y logout.
- `frontend/src/features/control-tower/components/AccordionHeader.tsx` — patrón expandible.
- `frontend/src/features/control-tower/components/FlowStrip.tsx` — trazabilidad secuencial.
- `docs/frontend/TRAZA_PROMPT_MAESTRO_FRONTEND_REDISENO_Y_MODULARIZACION_V1.md` — intención histórica; nunca prevalece sobre código o V11.

## NO NECESARIOS / NO ENTREGAR A LA IA ANDROID

- `frontend/node_modules/`, `frontend/dist/` y caches.
- `.env`, tokens, contraseñas, JWT, secretos, perfiles de publicación.
- dumps de base de datos, backups, logs sensibles y datos personales reales.
- `*.tfstate`, credenciales Azure o archivos de Service Principal.
- `powerbi/BancoCloud_Analytics.pbix`: no es necesario para construir UI Android.
- assets Vite/React no usados y screenshots como fuente de valores exactos.

La app Android futura será otro cliente de FastAPI por HTTPS. No debe conectarse directamente a PostgreSQL, duplicar reglas bancarias/STP, decidir `VERIFIED` de forma autónoma ni trasladar lógica de ledger, auditoría, DataOps o Copilot al dispositivo.
