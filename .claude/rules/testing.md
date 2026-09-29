# Regla: Pruebas

## Estrategia

- Priorizar pruebas unitarias puras (rápidas y deterministas).
- Los tests de servicios usan Mockito puro con
  `@ExtendWith(MockitoExtension.class)`, `@Mock` e `@InjectMocks`. No levantan
  contexto de Spring.
- Los tests de controllers usan el slice web
  `@WebMvcTest(...)` (paquete `org.springframework.boot.webmvc.test.autoconfigure`)
  con `MockMvc` y `@MockitoBean`.
- No usar `@SpringBootTest` para pruebas unitarias; reservarlo para pruebas de
  integración con base de datos.

## Convenciones

- Nombre de clase: `<ClaseBajoPrueba>Test`.
- Nombre de método: `metodo_escenario_resultadoEsperado`
  (ej.: `create_whenDniAlreadyExists_throwsDuplicateCustomerException`).
- Usar `@DisplayName` para describir el comportamiento en lenguaje natural.
- Aseveraciones con AssertJ (`assertThat`) y verificación con Mockito
  (`verify`, `never`).
- Cubrir casos felices, errores de validación y casos borde (p. ej. servicio de
  indicadores sin datos).

## Comandos

```bash
./mvnw test                     # Linux/macOS
.\mvnw.cmd test                 # Windows
```
