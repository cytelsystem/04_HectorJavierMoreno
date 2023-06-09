
# Parcial BackEndII Segurity

## e-commerce Mercado libre

## KEYCLOAK
- [x] Reino
    - [ ] mercadopago
- [x] Clientes
    - [ ] mspigateway-cliente
        - [x] Settings
            - [x] Client authentication `Activar`
            - [x] Standard flow `activo`
            - [ ] Direct access grants `Desactivar`
            - [x] Server account roles `Activar`
            - Root URL = http://localhost:8090
            - Valid redirect URIs = http://localhost:8090/*
        - [ ] Credentials
          - `Obtenemos en Client secret y pegarlo en aplication.propierties` 
        - [ ] Roles
          - `Crear Rol USER asociado con el rol del Realm App_USER`
    - [ ] msbills-cliente
        - [x] Settings
            - [x] Client authentication `Activar`
            - [x] Standard flow `activo`
            - [ ] Direct access grants `Desactivar`
            - [x] Server account roles `Activar`
        - [ ] Credentials
            - `Obtenemos en Client secret y pegarlo en aplication.propierties` 
        - [ ] Roles
            - `Crear Rol USER asociado con el rol del Realm App_USER`

- [x] Usuarios
    - [ ] usuario1 [Contraseña] `password`
    - [ ] admin    [Contraseña] `password`
- [x] Roles Clientes
    - [ ] USER
    - [ ] ADMIN
- [x] Roles Realm
    - [ ] App_USER `Asociado con el rol del cliente mspigateway-cliente y msbills-cliente ` [ Rol Compuesto ]
    - [ ] App_ADMIN




