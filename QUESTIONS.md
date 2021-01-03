# PARTE TEORICA

### Arquitecturas de UI: MVP, MVVM y MVI

#### MVVM

##### ¿En qué consiste esta arquitectura?
MVVM son las siglas de Model-View-ViewModel. MVVM es una arquitectura de programación cuyo propósito principal es lograr la separación de problemas a través de una clara distinción entre los roles de cada una de sus capas que la conforman:
- View muestra la interfaz de usuario e informa a las otras capas sobre las acciones del usuario. Usualmente se trata de una Activity o Fragment. Esto facilita que la vista se comunique con más de un modelo.
- ViewModel  recupera la información necesaria de la base de datos DataModel o expone información a la View. La clase ViewModel está especialmente diseñada para administrar y almacenar información durante su propio ciclo de vida, es decir, puede sobrevivir a cambios de configuración / ciclo de vida de la activity/fragment como rotaciones de pantalla
- Model o DataModel que recupera información de su fuente de datos y la expone a los ViewModels. También debe recibir cualquier evento del ViewModel que necesite para crear, leer, actualizar o eliminar cualquier registro del backend.

##### ¿Cuáles son sus ventajas?
- Es posible ejecutar unit test al limitar toda la manipulación de datos al ViewModel y al mantenerlo libre de cualquier código de UI de la View.
- MVVM separa las tareas, simplificando así su correcta gestión.
- Al es estar completamente separado de las Views, se reduce el riesgo de tener demasiado código en las otras capas.
- Se controla mejor el ciclo de vida de las ViewModel, al separarlo de las activity/fragment y se previenen leaks de memoria de otras clases que hagan referencia al ViewModel

##### ¿Qué inconvenientes tiene?
- MVVM puede ser demasiado complejo para aplicaciones con una interfaz de usuario simple

#### MVP

##### ¿En qué consiste esta arquitectura?
MVP que significa Model ViewPresenter, y se compone de las siguientes partes:
- Model o el modelo es la capa de datos, responsable de la parte programática: recuperar los datos, almacenar los datos o modificarlos.
- View que muestra la interfaz de usuario; suele ser una activity (o fragment): oculta y muestra vistas, manej la navegación a otras activities a través de Intents y escucha las interacciones del sistema operativo y la entrada del usuario.
- Presenter que habla con Model y View y controla la lógica de cómo se presentan los datos. El trabajo del presentador es realizar cualquier mapeo o formateo adicional de los datos antes de entregarlo a la View para mostrarlo. Mientras que la View extiende la Activity o Fragmen, el Model y el Presenter no extienden las clases específicas del framework de Android. En otras palabras, no deben contener referencias al paquete com.android. * en el Model o Presenter.

##### ¿Cuáles son sus ventajas?
Al dividir una actividad en clases separadas de model, View y Presenter con interfaces, es posible separar los problemas en partes más delimitadas, como en el caso de MVVM.
Los Models y Presenters son testables por Unit Test.

##### ¿Qué inconvenientes tiene?
Navegar a través de interfaces, lo que puede resultar confuso al principio. En Android Studio, CMD + OPCIÓN + B en Mac o CTRL + ALT + B en PC es el método abreviado de teclado para encontrar el camino a las implementaciones reales de métodos de interfaz.
A diferencia de MVVP, cuando el sistema operativo destruye la Actividad, debe asegurarse de que su Presenter destruya las AsyncTasks o de lo contrario causará problemas cuando la tarea se complete y la Actividad ya no esté allí.
Si alguna clase hace referencia al Presenter es posible que haya un leak de memoria cuando se destruya la activity.  puede intentar guardar algún estado a través de onSaveInstanceState o utilizar loaders, que sobreviven a los cambios de configuración.

#### MVI

##### ¿En qué consiste esta arquitectura?
MVI son las siglas de Model-View-Intent, una reciente arquitectura para Android inspirada en la naturaleza unidireccional y cíclica del marco Cycle.js., programación reactiva en el que reacciona a un cambio, como el valor de una variable o el clic de un botón en su interfaz de usuario. Cuando la app reacciona a este cambio, entra en un nuevo estado. El nuevo estado generalmente, pero no siempre, se representa como un cambio de interfaz de usuario con algo como una barra de progreso, una nueva lista de películas o una pantalla completamente diferente.
MVI es muy diferente a MVP o MVVM. El papel de cada uno de sus componentes es el siguiente:
- Model: acceden al backend pero, a diferencia de MVP o MVVM también representa un estado que deben ser inmutables para garantizar un flujo de datos unidireccional entre ellos y las otras capas de su arquitectura.
- Intent: Deseo de realizar una acción por parte del usuario. Por cada acción del usuario, la View recibirá un Intent, que será observada por el Presenter y traducida a un nuevo estado en sus Models.
Los intents en MVI no representan la clase android.content.Intent habitual que se usa para cosas como comenzar una nueva clase. Las intenciones en MVI representan una acción a realizar que se traduce en un cambio de estado en su aplicación.
- View: al igual que en MVP, están representadas por interfaces, que luego será implementado en una o más Actividades o Fragmentos.

##### ¿Cuáles son sus ventajas?

- Un flujo de datos unidireccional y cíclico para su aplicación.
- Estado único: las estructuras de datos inmutables son muy fáciles de manejar al asegurar que solo habrá un estado único entre todas las capas de su aplicación.
- Modelos inmutables que proporcionan un comportamiento confiable y seguridad de subprocesos en aplicaciones grandes: esto es especialmente útil al trabajar con aplicaciones reactivas que hacen uso de bibliotecas como RxJava o LiveData. Dado que ningún método puede modificar los Models, siempre deberán ser recreados y guardados en un solo lugar, con esto se asegura de que no habrá otros efectos secundarios como diferentes objetos modificando tus Modelos desde diferentes threads.

##### ¿Qué inconvenientes tiene?
- La curva de aprendizaje es un poco más alta, ya que necesita tener una cantidad decente de conocimiento de otros temas intermedios / avanzados como la programación reactiva, multi-threading y RxJava.

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

5 errores encontrados para los que se ha dado solución:
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