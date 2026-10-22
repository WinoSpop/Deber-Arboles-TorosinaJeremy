# Guía de entrega y reflexión

Fecha límite indicada en el enunciado: **jueves 22 de octubre de 2026, 07:00**.

## GitHub
Usa un repositorio **público** `Deber-Arboles-ApellidoNombre`, con rama `desarrollo`, mínimo tres commits auténticos y pull request hacia `main`. Solo cuenta `main` al cierre.

Ejemplo de trabajo real (adaptar mensajes a lo que efectivamente hagas):
```
git init
git checkout -b desarrollo
git add src/enums src/modelo
git commit -m "Crear tipos enumerados y modelo validado"
git add src/negocio
git commit -m "Implementar árbol general y catálogo ABB"
git add src/app README.md TRAZAS.md
git commit -m "Incluir menú, pruebas y documentación"
```
Luego sube `desarrollo` a tu repositorio remoto, abre un PR y fusiónalo a `main` antes del límite. Nunca uses `git push --force` ni `git reset --hard`.

## PDF exigido por el docente
Guardar como `Deber-Arboles-ApellidoNombre.pdf`. Adjuntar: enlace al repo público, capturas **reales** del menú y las pruebas, historial de commits, pull request y reflexión personal. Subir a Moodle.

## Guía conceptual para responder con tus palabras

**1. Complejidad:** buscar en ABB toma O(h) porque compara con un nodo por nivel y elige una sola rama. Buscar en árbol general sin orden de claves es O(N) en el peor caso porque puede recorrer todas las unidades. Si insertas claves ordenadas en el ABB simple, se degenera en cadena y h se aproxima a N−1.

**2. Sucesor inorden:** al borrar nodo con dos hijos, se elige el menor valor del subárbol derecho. Es el inmediato mayor y garantiza que los valores de la izquierda sigan siendo menores y los de la derecha mayores. Un nodo cualquiera del subárbol derecho no garantiza ese orden.

**3. SmartETL:** árbol general para representar flujos, fuentes, procesos y subprocesos; ABB para indexar registros por un código único y buscar por clave. Si el ABB no se equilibra, puede perder eficiencia.

**Reflexión propia:** Explica qué parte programaste y comprendiste mejor, qué dificultad tuviste, cómo la resolviste y dónde lo aplicarías. No presentar esta guía como reflexión personal terminada.
