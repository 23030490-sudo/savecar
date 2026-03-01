# SaveCar 🚗

Aplicación móvil Android para el cuidado y mantenimiento de tu vehículo.

## Descripción

**SaveCar** es una aplicación Android que te ayuda a llevar un registro completo del mantenimiento de tus vehículos. Puedes registrar múltiples vehículos, agregar registros de mantenimiento con fechas, costos y notas, y recibir recordatorios de próximos servicios.

## Características

- **Gestión de vehículos**: Registra marca, modelo, año, placa, color, kilometraje y tipo de combustible.
- **Historial de mantenimiento**: Lleva un registro detallado de cada servicio (cambio de aceite, frenos, neumáticos, etc.).
- **Próximos servicios**: Define cuándo es el próximo mantenimiento por kilometraje o fecha.
- **Costo de servicios**: Registra el costo de cada mantenimiento y el taller donde se realizó.
- **Múltiples vehículos**: Administra todos tus vehículos desde una sola aplicación.

## Arquitectura

- **Patrón**: MVVM (Model-View-ViewModel)
- **UI**: Jetpack Compose + Material Design 3
- **Base de datos**: Room (SQLite)
- **Navegación**: Navigation Compose
- **Concurrencia**: Kotlin Coroutines + StateFlow
- **Lenguaje**: Kotlin

## Estructura del proyecto

```
app/
├── data/
│   ├── local/
│   │   ├── dao/          # Interfaces de acceso a datos (VehicleDao, MaintenanceRecordDao)
│   │   ├── entity/       # Entidades Room (Vehicle, MaintenanceRecord)
│   │   └── SaveCarDatabase.kt
│   └── repository/
│       └── SaveCarRepository.kt
└── ui/
    ├── navigation/       # Definición de rutas y NavGraph
    ├── screen/           # Pantallas Compose (Home, Detalle, Formularios)
    ├── theme/            # Tema Material 3 (colores, tipografía)
    └── viewmodel/        # SaveCarViewModel
```

## Requisitos

- Android 7.0 (API 24) o superior
- Android Studio Hedgehog o superior

## Compilación

```bash
./gradlew assembleDebug
```

## Tests

```bash
./gradlew test
```