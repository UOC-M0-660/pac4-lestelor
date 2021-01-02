# PARTE TEORICA

### Arquitecturas de UI: MVP, MVVM y MVI

#### MVVM

##### ¿En qué consiste esta arquitectura?
MVVM son las siglas de Model-View-ViewModel. MVVM es un patrón arquitectónico cuyo propósito principal es lograr la separación de preocupaciones a través de una clara distinción entre los roles de cada una de sus capas:
- View muestra la interfaz de usuario e informa a las otras capas sobre las acciones del usuario, usualmente mediante una Activity o Fragment. Esto facilita que la vista se comunique con más de un modelo.
- ViewModel  recupera la información necesaria o expone información a la View. La clase ViewModel está especialmente diseñada para administrar y almacenar información durante su propio ciclo de vida, es decir, puede sobrevivir a cambios de configuración / ciclo de vida como rotaciones de pantalla
- Model o DataModel que recupera información de su fuente de datos y la expone a los ViewModels. También debe recibir cualquier evento del ViewModel que necesite para crear, leer, actualizar o eliminar cualquier dato necesario del backend.

##### ¿Cuáles son sus ventajas?
- Al limitar toda la manipulación de datos al ViewModel y al mantenerlo libre de cualquier código de View, es posible ejecutar unit test al no requerir el tiempo de ejecución de Android.
- MVVM separa las tareas, simplificando así el controlador de las tareas.
- Al es estar completamente separado de las Vistas, se reduce el riesgo de tener demasiado código en las otras capas.

##### ¿Qué inconvenientes tiene?
- MVVM puede ser demasiado complejo para aplicaciones con una interfaz de usuario simple

#### MVP

##### ¿En qué consiste esta arquitectura?
Escribe tu respuesta aquí

##### ¿Cuáles son sus ventajas?
Escribe tu respuesta aquí

##### ¿Qué inconvenientes tiene?
Escribe aquí tu respuesta

#### MVI

##### ¿En qué consiste esta arquitectura?
Escribe tu respuesta aquí

##### ¿Cuáles son sus ventajas?
Escribe tu respuesta aquí

##### ¿Qué inconvenientes tiene?
Escribe aquí tu respuesta

---

### Testing

#### ¿Qué tipo de tests se deberían incluir en cada parte de la pirámide de test? Pon ejemplos de librerías de testing para cada una de las partes. 
En la parte baja de la pirámide se encuentran los Small Test, que son mayoría al representar el 70% de los test recomendados por google que se deben realizar. Se tratan en su mayoría de unit Test que comprueban que las diferentes partes de la app funcionan.
Una vez se pasan los unit Test, Google contempla la realización de Medium test, es decir test que comprueban que la app interactúa correctamente con las otras partes del framework de Android.
La mayoría de las apps suelen requerir sólo de small test o medium test pero Google recomienda test extensivos (large tests) que deberían ser el 10% restante de los tests a realizar en una app. En los large test se comprueba que la UI funciona según lo esperado para diferentes tipos de dispositivos, pantallas, etc.

Ejemplos de librerías para cada tipo de test:
- Small test (Unit test)
Para las pruebas que siempre se ejecutan en una máquina de desarrollo con tecnología JVM, puede usar Robolectric.
Robolectric simula el tiempo de ejecución para Android 4.1 (API nivel 16) y permite probar el código sin necesidad de usar un emulador u objetos simulados.

- Medium Test (Integration test)
Espresso ayuda a mantener las tareas sincronizadas mientras realiza interacciones de IU similares a las siguientes en un dispositivo o en Robolectric:
    - Realización de acciones sobre objetos de visualización.
    - Evaluar cómo los usuarios con necesidades de accesibilidad pueden usar su aplicación.
    - Localizar y activar elementos dentro de los objetos RecyclerView y AdapterView.
    - Validar el estado de las intents salientes.

- Large Test (UI Test)
Además de admitir pruebas de integración, Espresso permite:
    - Probar tareas externas a la app . Disponible solo en Android 8.0 (API nivel 26) y superior.
    - Seguimiento de operaciones en segundo plano de larga duración.
    - Realización de pruebas off-device.

#### ¿Por qué los desarrolladores deben centrarse sobre todo en los Unido Tests?
- Es mucho más rápido de ejecutar en comparación con las pruebas de integración o de interfaz de usuario, ya que no se ejecutan en un emulador.
- No requieren bibliotecas de pruebas de Android ya que se está probando código nativo de Java / Kotlin, no código específico del marco de Android. Sólo el framework de prueba para Java es todo lo que se necesita escribir pruebas unitarias. Para probar el código que utiliza clases específicas de Android, se necesita una biblioteca de prueba específica para Android, como Robolectric (robolectric.org) y Espresso.
(https://developer.android.com/training/testing/espresso/).
- Las pruebas unitarias constituyen la base del conjunto de pruebas de una aplicación por lo que si todas las unidades individuales de código funcionan como se espera, es de prever que el conjunto funcionará también.

---

### Inyección de dependencias

#### Explica en qué consiste y por qué nos ayuda a mejorar nuestro código.

Como regla general, al crear una clase, es recomendable separar la parte de definición de las variables con respecto a la inicialización.
De esta manera una misma clase puede tener varias instancias diferentes de una de sus variables (dependencias) sin necesidad de crar dos clases diferentes
Otra ventaja de la instanciación de dependencias fuera del la parte de inicialización de la clase es facilitar los Unit Test ya que se limitan a comprobar el comportamiento de la clase, de manera indepencdiente de las dependencias (que pueden tener sus Unit test independientes no vinculados a la clase)

#### Explica cómo se hace para aplicar inyección de dependencias de forma manual a un proyecto (sin utilizar librerías externas).
- Lo primero es modificar la clase incluyendo un constructor mediante el que se inyectan las dependencias:
ej: class MovieTheatre(val screen: Screen, val projector: Projector, val movie: Movie)

- Seguidamente se pasan las dependencias en la propia activity asociada
setContentView(R.layout.activity_main)
//1 Instantiate dependencies
val screen = Screen()
val projector = Projector()
val movie = Movie()
//2 Instantiate MovieTheatre, passing in dependencies
val movieTheatre = MovieTheatre(screen, projector, movie)

De esta manera es posible que, por ejemplo, la clase MovieTheater tenga dos dependencias Proyector diferentes y que los Unit Test a esta clase sean independientes de la instanciación particular.


### Lint

5 errores:
- Unused import directive inspection
Causa: Esta inspección informa las declaraciones de importación en código Kotlin que no se utilizan.
Solución: Optimize imports

- Function buildOAuthUri could be private.
Causa: La función puede ser privada dentro del método y así reducir el consumo de memoria.
Solución:  Make function private

- implementation "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"
Causa: kotlin library and gradle plugin versions are different. De acuerdo con https://stackoverflow.com/questions/49956051/warning-kotlin-plugin-version-is-not-the-same-as-library-version-but-it-is parece que se trata de un bug.
Solución: Supress lint attribute.

- Unused XLM Schema declaration
Causa: Existen esquemas no usados en los xml que se pueden quitar
Solución:Checks for unused namespace declarations and location hints in XML and remove unused

- 'Missing contentDescription attribute on image' in XML
Causa: Debe añadirse una descripción a aquellas imagenes para que los usarios sepan interpretarlas.
Solución: Añadir content description o supress lint en aquellos casos que las imágenes sean solo decorativas.

Otros:
- Using + in dependencies lets you automatically pick up the latest available version rather than a specific, named version. However, this is not recommended; your builds are not repeatable; you may have tested with a slightly different version than what the build server used. (Using a dynamic version as the major version number is more problematic than using it in the minor version position.)
- Node can be replaced by a TextView with compound drawables  A LinearLayout which contains an ImageView and a TextView can be more efficiently handled as a compound drawable (a single TextView, using the drawableTop, drawableLeft, drawableRight and/or drawableBottom attributes to draw one or more images adjacent to the text).