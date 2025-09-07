# SaludTotal - HackMiguel

🏥 **Aplicación Android para gestión de salud integral**

Una aplicación móvil desarrollada en **Kotlin** con **Android Studio** que integra **Firebase** para ofrecer una solución completa de gestión de salud personal.

## 📱 Características del Proyecto

### 🎯 **Funcionalidades Principales**
- 🏠 **Home**: Panel principal con información de salud
- 📊 **Dashboard**: Visualización de datos y métricas de salud
- 🔔 **Notificaciones**: Alertas y recordatorios médicos
- ☁️ **Firebase**: Integración con backend en la nube

### 🛠️ **Stack Tecnológico**
- **Lenguaje**: Kotlin
- **Framework**: Android SDK
- **UI**: Bottom Navigation + Fragments
- **Backend**: Firebase
- **Build System**: Gradle (Kotlin DSL)
- **IDE**: Android Studio

## 🚀 Cómo Ejecutar el Proyecto

### 📋 **Requisitos Previos**

1. **Android Studio** (versión más reciente)
2. **JDK 8 o superior**
3. **Android SDK** (API nivel 24+)
4. **Conexión a Internet** (para Firebase)

### ⚙️ **Configuración e Instalación**

1. **Clonar el repositorio:**
   ```bash
   git clone https://github.com/fernandolpz-nitbit/HackMiguel-.git
   cd HackMiguel-
   ```

2. **Abrir en Android Studio:**
   - Abre Android Studio
   - Selecciona "Open an existing project"
   - Navega y selecciona la carpeta del proyecto

3. **Sincronizar el proyecto:**
   - Android Studio descargará automáticamente las dependencias
   - Espera a que termine la sincronización de Gradle

4. **Configurar Firebase:**
   - El archivo `google-services.json` ya está incluido
   - Verifica que las dependencias de Firebase estén en `app/build.gradle.kts`

5. **Ejecutar la aplicación:**
   - Conecta un dispositivo Android o inicia un emulador
   - Presiona el botón "Run" (▶️) o usa `Shift + F10`

### 📱 **Ejecutar en Dispositivo Físico**

1. **Habilitar modo desarrollador:**
   - Ve a Configuración > Acerca del teléfono
   - Toca 7 veces en "Número de compilación"

2. **Habilitar depuración USB:**
   - Ve a Configuración > Opciones de desarrollador
   - Activa "Depuración USB"

3. **Conectar y ejecutar:**
   - Conecta el dispositivo por USB
   - Acepta la depuración USB
   - Ejecuta desde Android Studio

### 🖥️ **Ejecutar en Emulador**

1. **Abrir AVD Manager:**
   - En Android Studio: Tools > AVD Manager

2. **Crear dispositivo virtual:**
   - Create Virtual Device > Seleccionar dispositivo
   - Descargar imagen del sistema (API 24+)
   - Configurar y finalizar

3. **Ejecutar:**
   - Iniciar el emulador
   - Ejecutar la app desde Android Studio

## 📁 Estructura del Proyecto

```
app/src/main/java/com/example/saludtotal/
├── MainActivity.kt                 # Actividad principal
├── ui/
│   ├── home/
│   │   ├── HomeFragment.kt        # Fragment de inicio
│   │   └── HomeViewModel.kt       # ViewModel de inicio
│   ├── dashboard/
│   │   ├── DashboardFragment.kt   # Fragment del dashboard
│   │   └── DashboardViewModel.kt  # ViewModel del dashboard
│   └── notifications/
│       ├── NotificationsFragment.kt  # Fragment de notificaciones
│       └── NotificationsViewModel.kt # ViewModel de notificaciones
└── res/
    ├── layout/                    # Archivos de diseño XML
    ├── navigation/                # Configuración de navegación
    └── values/                    # Recursos (strings, colores, etc.)
```

## 👥 Historia del Desarrollo

### 🔄 **Evolución del Proyecto**

1. **Commit Inicial** - Eder.melo
   - Creación de la aplicación Android base
   - Estructura con Bottom Navigation
   - Paquete original: `com.example.myapplication`

2. **Merge y Organización** - Fernando Nitbit
   - Consolidación de ramas master → main
   - Eliminación de rama master
   - Creación de rama de respaldo

3. **Refactorización Completa** - Fernando Nitbit
   - Renombrado: `myapplication` → `saludtotal`
   - Integración con Firebase
   - Reestructuración del proyecto

### 🌟 **Colaboradores**

- **Eder Melo** (eder.melo@nitbit.mx) - Desarrollo inicial
- **Fernando Nitbit** (fernando.rodriguez@nitbit.mx) - Refactorización y configuración

## 🔧 Comandos de Desarrollo

### 📦 **Build del Proyecto**
```bash
# Limpiar y compilar
./gradlew clean build

# Solo compilar
./gradlew assembleDebug

# Ejecutar tests
./gradlew test
```

### 🔄 **Git Workflows**
```bash
# Cambiar a rama de desarrollo
git checkout -b feature/nueva-funcionalidad

# Ver ramas disponibles
git branch -a

# Restaurar desde respaldo (si es necesario)
git checkout respaldo
```

## 📚 Recursos Adicionales

- [Documentación de Android](https://developer.android.com/)
- [Firebase para Android](https://firebase.google.com/docs/android/setup)
- [Kotlin Documentation](https://kotlinlang.org/docs/)
- [Material Design](https://material.io/develop/android)

## 📄 Licencia

Este proyecto fue desarrollado como parte del HackMiguel y está disponible bajo los términos acordados por el equipo de desarrollo.

---

🏥 **SaludTotal** - *Tu salud en tus manos*
