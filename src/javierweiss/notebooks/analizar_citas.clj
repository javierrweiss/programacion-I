^{:nextjournal.clerk/visibility {:code :hide}}
(ns javierweiss.notebooks.analizar-citas
  (:require [nextjournal.clerk :as clerk]
            [clojure.java.io :as io]
            [clj-ocr.core :as ocr] 
            [clojure.string :refer [ends-with? starts-with? upper-case split-lines blank?]])
  (:import (net.sourceforge.tess4j Tesseract) 
           (net.sourceforge.tess4j.util LoadLibs)
           (javax.imageio ImageIO)))
 
^{:nextjournal.clerk/visibility {:code :hide}} 
(ImageIO/read (io/file "resources/circuito.jpg"))

;;  # Nuestro Pequeño Asistente de Investigación
;;
;; Como científicos sociales, en nuestras investigaciones a menudo tomamos notas y citas textuales de 
;; nuestras lecturas. A la hora de escribir un libro o un artículo resulta muy importante tener estas 
;; citas organizadas, tanto para realizar un análisis profundo de la literatura revisada como para armar  
;;  nuestras referencias bibliográficas.
;;
;; ¿Qué tal si pudiésemos automatizar parte de este proceso? Imaginemos que no tuviésemos que copiar a mano
;; aquellos pasajes interesantes de algún libro en préstamo bibliotecario o de lectura solo en sala. O 
;; tener que revisar y clasificar una por una todas las fotos que tomamos durante nuestra jornada de 
;; investigación. ¿No sería genial? Incluso (aunque eso quedaría para una futura lección) podríamos utilizar
;; el material así obtenido para alimentar y entrenar a algún modelo de Machine o Deep Learning de tipo 
;; NLP (_Natural Language Processing_). Las posibilidades son enormes.
;;
;; Sin embargo, para sacarle provecho a tales posibilidades debemos hacernos con una herramienta poderosísima:
;; la programación. Aprender a programar es como aprender un lenguaje natural, pero en cierto sentido mucho más
;; fácil pues el lenguaje de programación promedio tiene mucho menos vocabulario y muchas menos reglas que un 
;; lenguaje natural. Un lenguaje de programación es un lenguaje formal constituido por un conjunto finito de 
;; símbolos y regido por un conjunto de reglas de transformación que permiten formar expresiones más complejas.
;; 
;; Los lenguajes de programación son completos en Turing (_Turing complete_), lo que quiere decir, quizá simplificando
;; demasiado, que cada lenguaje puede reproducir la conducta de cualquier otro inclusive hasta el punto de poder clonarlo 
;; dentro de sí, como un subconjunto de sí mismo. Como corolario, lo que puede hacer un lenguaje de programación _X_ lo 
;; puede hacer cualquier otro.
;;
;; De igual modo que cuando aprendemos un lenguaje, lo fundamental es la acción comunicativa. Nos estamos comunicando con
;; un computador, el cual ejecuta las instrucciones que le hacemos llegar codificadas en el lenguaje de programación que
;; hayamos elegido para escribir el programa en cuestión. 
;;
;; Así que ¿por qué no probar y pedirle al computador que haga algo por nosotros? 
;;
;; Lo más simple que podemos hacer, y por lo que usualmente se empieza, es pedirle que imprima algún mensaje por pantalla.

"¡Hola, soy tu nuevo asistente de investigación!"

;; Sencillamente escribimos nuestro mensaje entre comillas y el computador hizo eco del mismo. No obstante, esto es poco útil.
;; Y es que no podemos controlar cuándo y cómo se mostrará el mensaje; en el momento mismo en que evaluamos la expresión, el computador
;; (o para ser más precisos, nuestro intérprete) la imprime. 

;; Cuando escribimos un programa para que sea ejecutado escribimos una serie de instrucciones en un orden estipulado. Pero todo programa
;; necesita datos sobre los cuales operar, por ende, un programa es como una función matemática o como un sistema del tipo _black box_
;; que recibe un **input** o insumo y produce un **output** o resultado.

;; Así que si, por ejemplo, deseamos hacer algo con la oración de arriba (de ahora en adelante lo llamaremos _cadena de caracteres_ o _string_)
;; necesitamos definirlo. Para esto utilizamos el símbolo *def* de _define_. 

(def saludo "¡Hola, soy tu nuevo asistente de investigación!")

;; Al definirlo el intérprete reconocerá el símbolo que asociamos con el valor correspondiente y al _llamarlo_ por
;; ese símbolo nos devolverá el valor.

saludo

;; Lamentablemente, a diferencia de nuestros intérpretes de lenguajes naturales (otros seres humanos),
;; el computador no puede inferir nuestra intención comunicativa, por lo que si nos equivocamos 
;; obtendremos lo que en la jerga de ciencias de la computación se llama una **excepción**. 
;; Digamos que escribimos

;; _saludos_

;; Entonces veríamos por pantalla algo parecido a lo siguiente:
^{:nextjournal.clerk/visibility {:code :hide}} 
(ImageIO/read (io/file "resources/excepcion.png"))

;; Las excepciones son mensajes de otro programa, el intérprete o compilador según sea el caso, que 
;; nos indica qué instrucción el computador no pudo interpretar correctamente. En este caso, el mensaje
;; es bastante claro: nos está diciendo que no sabe qué significa _saludos_ en este contexto (¿Cuál contexto?
;; Pues, el _namespace_ que es una especie de diccionario en el que estamos escribiendo nuestro programa y 
;; definiendo los símbolos que le darán sentido y significado al mismo).
;; Una excepción detiene abruptamente la ejecución de nuestro programa. Hay formas de 
;; evitarlo (lo que se llama _capturar la excepción_), pero eso lo veremos más adelante. Por ahora aprenderemos
;; a convivir con ellas y a interpretar lo que nos dicen. 
;; 
;; Pues bien, una vez que hemos definido uno o más datos (en este caso una cadena de caracteres que identificamos con el símbolo
;; _saludo_) podemos emplear una o más funciones que transformen estos datos para producir nueva información.
;; Por ejemplo, digamos que queremos contar cuántos caracteres tiene nuestro _string_.

(count saludo)

;; Otra cosa sencilla que podemos hacer con este _string_ es concatenarlo con otro _string_

(str saludo " Mi nombre es Intelli-Search")

;; También podemos pasar todos los caracteres a mayúsculas

(upper-case saludo)

;; Nuestros datos son inmutables, así que si volvemos a llamar a _saludo_ obtendremos su valor original.

saludo

;; Podemos preguntarle al intérprete si nuestro _string_ empieza o termina con cierto caracter  

(starts-with? saludo "s")

(ends-with? saludo "!")

;; Vemos que el valor que nos devuelven estas funciones son _false_ (falso) y _true_ (verdadero) respectivamente.
;; Como tendremos ocasión de ver estos valores (llamados **booleanos**) son muy importantes en las ciencias de la 
;; computación, ya que se usan para estructurar programas en función de árboles de decisión "Si A entonces ejecuta B, 
;; de lo contrario ejecuta C".

;; Por supuesto que también podemos asociar un número con símbolo y así guardar este dato para luego procesarlo con alguna 
;; función. Por ejemplo,

(def dni 23202431)

;; Ahora, llamamos a _dni_ (de igual forma noten que, al definirlo, nuestro intérprete nos muestra el valor en la celda de abajo,
;; sin embargo, nos interesa marcar la diferencia entre _definir_ y _llamar_; sobretodo porque en otro entorno no se comportará
;; igual).

dni

;; Aquel fue un número entero, pero también podemos utilizar números reales

(def tasa-de-interes 23.55)

tasa-de-interes

;; O racionales (aunque el intérprete en particular que estamos usando ahora los convierte a reales)

(def numero-racional 3/66)

numero-racional

^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/html [:div {:style {:background-color :#E6E6E6 :padding :1rem}}
             [:h3 "¿Por qué existen tipos de datos y por qué son importantes?"]
             [:p " Más allá de lo tautológico, digamos que un número es un número y una letra es una letra, tengamos en cuenta que nuestro amigo el computador
                  habla lenguaje binario (y no, no nos referimos al lenguaje de género), esto es, entiende sólo unos y ceros. Y todo lo que procesa debe 
                  asignarle un espacio en memoria, la cual es medida en bytes (lo que equivale a 8 bits o" [:i " binary digits"] "); entonces el procesador codifica la información 
                  clasificándola según la cantidad de bytes que necesite para almacenarla. De esta forma, aunque el computador no entienda nada de su entorno 
                  puede procesar información y distinguir una cosa de otra. Dicho de otra manera, los tipos de datos le permiten al ordenador representar entidades 
                  del mundo exterior en su lenguaje binario."]])

;; Ahora bien, ¿no resulta trabajoso tener que asociar un símbolo por cada valor? ¿Cómo hacemos cuando tenemos miles de datos
;; que procesar? Para eso existen las **estructuras de datos**.

;; Las más básicas son los vectores (representados por los símbolos **[ ]**), los conjuntos (**#{ }**), las listas (**'( )**) y los 
;; mapas (**{ }**). A estas estructuras de datos se les denomina genéricamente **colecciones**. 

;; Los vectores y las listas son estructuras de datos secuenciales, es decir, que almacenan sus elementos en orden
;; otorgándole a cada uno un índice mediante el cual se puede acceder al elemento correspondiente.

(def autores ["Foucault" "Habermas" "Parsons" "Simmel" "Mead" "Luhmann"])

;; Acá, por ejemplo, creamos un vector. Lo poblamos con _strings_ o cadenas de caracteres representando distintos autores. Y vínculamos
;; a ese vector el símbolo _autores_ para que así lo podamos invocar luego. Contemos cuántos elementos tiene nuestro vector.

(count autores)

;; Ahora digamos que quiero obtener el primero de la lista

(first autores)

;; ¡Bien! Hay otra forma de hacerlo indicando explícitamente el índice con la función **nth**

(nth autores 1)

;; ¡Un momento! El primero autor no es Habermas. Pues bien, resulta que casi todas las colecciones secuenciales comienzan a contar
;; desde cero, por lo que si queremos el primer elemento:

(nth autores 0)

;; Y ¿si queremos el último? ¿Pueden adivinar cómo se llama la función? Sí, **last**

(last autores)

;; Trabajar con listas es muy parecido a trabajar con vectores (aunque por lo general, en este lenguaje que estamos usando, llamado Clojure,
;; se prefiere trabajar con vectores):

(def lista-textos '("El Capital" 
                    "Teoría de la Acción Comunicativa" 
                    "Economía y Sociedad" 
                    "La tercera vía" 
                    "Identity and Control" 
                    "La sociedad de la sociedad"))

;; También podríamos definir la lista invocando a la función list

;; ```clojure
;; (def list-textos (list "El Capital" 
;;                         "Teoría de la Acción Comunicativa" 
;;                         "Economía y Sociedad" 
;;                         "La tercera vía" 
;;                         "Identity and Control" 
;;                         "La sociedad de la sociedad"))
;; ```

;;Y podemos hacer las mismas operaciones

(first lista-textos)

(second lista-textos)

(last lista-textos)

(nth lista-textos 3)

;; Para agregar nuevos elementos a nuestras listas o vectores podemos usar las funciones _conj_ o _cons_.
;; Sin embargo, estas funciones no operan igual en las listas que en los vectores. En las listas los elementos siempre son
;; agregados de primeros, no importa qué función utilicemos.

(cons "El proceso civilizatorio" lista-textos)

(conj lista-textos "Meditaciones cartesianas")

;; En los vectores, por el contrario, con _cons_ agregamos al principio mientras que con _conj_ adjuntamos al final.

(cons "Bourdieu" autores)

(conj autores "Husserl")

;; En nuestras listas o vectores podemos agregar datos de diferentes tipos. Ya conocemos los _strings_ y los _números_ (enteros o integer
;; reales o double y racionales), pero también tenemos los booleanos (que mencionamos brevemente), los caracteres (incluyendo los símbolos 
;; unicode), las llaves o keywords y los símbolos.

(def elementos-varios ["Hola" 123 12.23 2/7 true false \a \u26A0 :llave 'simbolo])

;; Los conjuntos como estructuras de datos que se asemejan a los conjuntos formalmente definidos en las matemáticas.
;; Sin necesidad de ahondar mucho en este tema, por ahora nos conformamos con apuntar algo básico: los conjuntos no 
;; tienen elementos repetidos y no ordenan sus elementos. 
;; Podemos crear un conjunto utilizando el literal #{}

(def conjunto #{'A 'B 'C 'D 'E})

;; o llamando una función como

(hash-set :alfa :beta :zeta :theta)

;; Y si necesitamos que estén ordenados los elementos podemos llamar a 

(sorted-set 3 43 3 23 2 4 2 1 10 33)

;; Noten que los elementos repetidos se eliminan

;; Con _conj_ podemos agregar elementos a nuestro conjunto 

(conj conjunto 'L)

(conj conjunto 'K 'L 'M)

;; También podríamos usar _cons_, sin embargo, noten que en este caso nos devuelve un tipo diferente de colección.

(def nuevo-conjunto (cons 'X conjunto))

^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/html [:div {:style {:background-color :#E6E6E6 :padding :1rem}}
             [:h3 "Nota al margen"]
             [:p "Preguntémosle al intérprete qué tipo de dato nos está devolviendo esa operación. 
                  Para tal propósito emplearemos la función " [:b "type."]]
             [:span (clerk/code '(type nuevo-conjunto))]
             [:p "El resultado es la clase " [:b "clojure.lang.Cons"] " (luego veremos qué es una clase). Comparemos con las otras colecciones."]
             [:span (clerk/code '(type elementos-varios))]
             [:p "Éste es un vector (aunque " [:b "PersistentVector"] " es el nombre formal de la clase)."]
             [:span (clerk/code '(type lista-textos))]
             [:p "Ésta es una lista (" [:b "PersistentList"] ")"]
             [:span (clerk/code '(type conjunto))]
             [:p "Éste es un conjunto hash o HashSet (" [:b "PersistentHashSet"] "). Ahora si preguntamos por el tipo del sorted set, debería ser 
                  un ¿PersistentSortedSet?"]
             [:span (clerk/code '(type (sorted-set 3 43 3 23 2 4 2 1 10 33)))]
             [:p "Pues no, es un" [:b " PersistentTreeSet"] ", que también es una implementación de un conjunto ordenado."]
             [:p "Los detalles no importan ahora pero todas estas clases provienen del lenguaje en el que está implementado Clojure, a saber, Java. 
                  Más adelante hablaremos sobre lenguajes de programación y cómo se relacionan entre sí, pero por ahora bástanos recordar lo que dijimos arriba sobre "
              [:b "completitud en Turing."]]                                                
             [:p "En resumidas cuentas, es importante siempre tener en cuenta cuál es el tipo de dato que nos devuelve la función. Esta información la podemos encontrar 
                  a menudo en la documentación de la función en cuestión, de lo contrario, como hemos hecho ahora podemos pasarle la expresión en cuestión a la función "
              [:b "type."]]])

;; Finalmente, tenemos los mapas. Se trata de estructuras de datos asociativas, esto es, que relacionan una _llave_
;; con un _valor_. En ambos casos, llave y valor pueden ser de cualquier tipo de dato, sin embargo, en Clojure
;; es usual utilizar _keywords_ para este propósito. 

(def persona {:nombre "Miguel"
              :apellido "Sánchez"
              :dni 23230230
              :direccion "Avellaneda 2090"})

;; Una restricción es que, por razones obvias, una llave no puede estar repetida en un mapa (no pueden existir dos llaves para el mismo valor).
;; Si deseamos agregar un nuevo par llave-valor (key - value) a nuestro mapa, utilizamos la función _assoc_.

(assoc persona :hijos ["Marta" "Juan" "Miguel"])

;; Si deseamos quitar uno o más pares llave-valor del mapa, utilizamos la función _dissoc_.

(dissoc persona :dni :direccion)

;; Recordemos que nuestras estructuras de datos son inmutables, cualquier función que le apliquemos no afectará la integridad
;; de nuestros datos. Si deseamos conservar una estructura de datos modificada para luego poder hacer referencia a la misma,
;; debemos definirla. Por ejemplo:

(def persona-datos-basicos (dissoc persona :direccion))

persona-datos-basicos

;; Si deseamos unir dos mapas, utilizamos la función _merge_.

(merge persona {:edad 56 
                :casado true 
                :hijos ["Marta" "Juan" "Miguel"]})

;; Estas estructuras de datos básicas las podemos combinar como deseemos, siempre y cuando tenga sentido. Podemos tener vectores de 
;; vectores (usualmente usados para representar matrices), mapas de mapas, vectores de mapas, valores de mapas que son vectores o 
;; conjuntos, etc. Por ejemplo, un patrón muy usual para representar datos es un vector de mapas:

(def bibliografía [{:titulo "Los orígenes del totalitarismo"
                    :autor "Hannah Arendt"
                    :año 2004
                    :editorial "Taurus"}
                   {:titulo "Meaning and Understanding in the history of Ideas"
                    :autor "Quentin Skinner"
                    :revista "History & Theory"
                    :volumen 8
                    :año 1969}
                   {:autor "Pierre Bourdieu"
                    :titulo "La distinction: Critique sociale du jugement"
                    :año 1979}])

;; He colocado las llaves desordenadas y dispares en cada mapa para demostrar lo flexible que es el mapa para adaptarse a la información disponible.
;; Para acceder a un vector, que es una colección secuencial, pedimos el indice (_nth_), el primero (_first_), el segundo (_second_), el último (_last_)
;; o la cola (todos menos el primero) (_rest_). Mientras que para acceder a las llaves de un mapa, sencillamente utilizamos la llave:


(:titulo ;;con esta llave obtenemos el valor asociado a título
 (first bibliografía)) ;; y con first recuperamos el primer mapa de la colección

;; Ahora que sabemos organizar nuestros datos en estructuras de datos y que tenemos los rudimentos para manipular estas estructuas,
;; podemos explorar un poco más sobre las funciones. Todo este tiempo hemos estado trabajando con funciones, si nos damos cuenta.
;; Una función en ciencias de la computación es análoga a una función matemática, es decir, existe un _dominio_ (rango de inputs aceptados)
;; y un _codominio_ (rango de outputs posibles). Visto desde otra perspectiva, una función es como un contrato en el cual dado un número de
;; parámetros del tipo y la cantidad estipulados se devuelve un resultado del tipo ofrecido con antelación.

;; En Clojure la unidad más básica son las denominadas **expresiones simbólicas**, esto es, una lista **( )** cuyo primer elemento debe ser
;; siempre una función y luego sucesivamente sus argumentos. Cuando tenemos varias expresiones simbólicas anidadas, las denominamos 
;; **formas**. De manera análoga, otros lenguajes de programación operan bajo el mismo principio, es decir, primero escribimos el nombre
;; de la función que queremos invocar y luego en orden sucesivo sus argumentos.

;; Tomemos como ejemplo a la función _first_. 

^{:nextjournal.clerk/visibility {:code :hide}}
(ImageIO/read (io/file "resources/first_docu.png"))
;; Acá tenemos la documentación donde vemos que recibe un solo argumento que debe ser una colección
;; (es decir, que puede ser un vector, una lista, un mapa o un conjunto) y que nos devuelve el primer item 
;; de la colección respectiva y que si la colección está vacía (por lo que al llamarla retorna _nil_),
;; entonces devolverá _nil_. 

;; Pero ¿qué significa _nil_? Se trata de un símbolo usado para representar la ausencia de un valor.

;; Existe una gran variedad de funciones que vienen con el lenguaje (en lo que se denomina biblioteca base o core, nótese en la imagen 
;; que se hace referencia a la función de la siguiente manera: _clojure.core/first_. Pues esto indica que la función se encuentra en el _namespace_ _core_) 
;; como las que hemos usado hasta ahora, y que son lo suficientemente genéricas como para ayudarnos en la mayoría de tareas. Pero en muchas ocasiones
;; también necesitaremos definir nuestras propias funciones.
;; Para definir una función podemos hacer lo siguiente:

(def saludar (fn [nombre] (str "Hola, " nombre)))

;; Ahora llamamos a la función, recuerden abrimos paréntesis, colocamos el nombre de la función y luego el argumento:

(saludar "Juan")

;; Sin embargo, existe una forma más compacta de definir una función, a saber, _defn_ el cual es un macro (más adelante veremos qué es esto) que une _def_ y _fn_:

(defn obtener-elemento-medio
  "Esta función toma una colección secuencial y devuelve el item que se encuentra en el medio" ;; Este string es la documentación de la función. Es una buena práctica documentar siempre
  [coll]
  (nth coll (/ (count coll) 2)))

;; Veamos con detalle qué hicimos acá. En primer lugar destaquemos la estructura de la función. En primer lugar, tenemos el macro _defn_, luego un símbolo
;; que le dará nombre a la función, le siguen los comentarios, un vector con los argumentos y el cuerpo de la función. 
;; En el cuerpo de la función contamos los elementos, dividimos el resultado entre dos y tomamos ese número como argumento para pasárselo a la función _nth_.
;; Podrá extrañarles el símbolo / allí donde está, pero en Clojure (y otros lenguajes de la familia LISP) la función siempre va primero; y todos los operadores 
;; matemáticos (+ * / -) son funciones. A esto se le denomina notación **pre-fija**, en contraste con la tradicional notación infija. En notación infija, por ejemplo, 
;; sumamos de la siguiente manera 2 + 2 + 3 + ..., mientras que en la notación prefija sólo tenemos que escribir una vez el signo de suma (+ 2 4 2 ...). 
;; Veamos como funciona:

(obtener-elemento-medio lista-textos)

;; ¡Genial! Pero atentos, porque si le pasamos como argumento una colección que no sea secuencial obtendremos una excepción, y es que la función _nth_ trabaja con
;; índices y al no encontrarlos lanza un error.

;; Es menester recalcar que no debemos mezclar nuestros datos y las funciones que las manipulan, es decir, no
;; debemos definir estructuras de datos en el cuerpo de nuestras funciones (a menos que sea como herramienta auxiliar en la transformación
;; del input, lo cual es muy común) ni emplear funciones en la definición de nuestros datos. De esta manera es mucho más fácil razonar
;; sobre nuestros programas y corregir errores cuando estos surjan.

;; Pues bien, ahora que nos sentimos un poco más cómodos con esto de la programación, podemos enfocarnos a resolver nuestro problema: crear
;; nuestro pequeño asistente de investigación

;; ## ¿Qué vamos a hacer?

;; Vamos crear un pequeño programa que tome imágenes de texto y las convierta en un string que podamos manipular

;; ## ¿Qué necesitamos?

;; La tecnología que nos permite convertir imagen a texto se llama **OCR**, esto es, _Optical Character Recognition_, así
;; que necesitamos una librería que nos permite realizar este proceso. 
;; Y ¿qué es una librería? Se trata simplemente de un programa que escribió otra persona y que aloja en un repositorio (la 
;; mayoría de las veces de forma pública y gratuita) desde donde lo podemos descargar. Una vez que lo tengamos donde corresponde 
;; podemos emplear las funciones que ese programa exponga para su consumo. A esto se le llama API o **Application Programming Interface**.
;; Una de las ventajas de Clojure y Clojurescript (un lenguaje hermano que corre en el navegador) consiste en que podemos tomar librerías 
;; escritas en los lenguajes más importantes, a saber, _Java_, _Javascript_ (no tiene nada que ver con Java), _Python_ e incluso _R_.

;; También necesitamos algunas imágenes de prueba.

;; Comencemos

;; Ya tenemos algunas imágenes que he reservado para este fin, también me he ocupado de importar las librerías necesarias,
;; así que lo primero que haremos será crear una estructura de datos con nuestras imagenes.
;; Utilizaremos un vector 

(def rutas-imagenes ["resources/temas de hoy.jpg" 
                     "resources/Carlos_Reynoso_I.jpg"
                     "resources/Carlos_Reynoso_II.jpg"
                     "resources/Carlos_Reynoso_III.jpg"])

;; Este vector que hemos creado contiene en un string la ubicación relativa de los archivos. Sin embargo, ese string no representa
;; el archivo, por ende, debemos convertir cada string en un objeto del tipo _File_. Para eso utilizaremos el método _file_ 
;; perteneciente a la biblioteca **io** (donde IO representa los conceptos de Input-Output) la cual trabaja con lectura y escritura de archivos 
;; de todo tipo.

;; Pero ¿cómo le aplicamos esa función a cada uno de los elementos del vector? 
;; Para eso utilizaremos la función _map_, la cual toma una función y una colección (secuencial, usualmente) y aplica la 
;; función sobre cada elemento de la misma.

(def imagenes (map (fn [elem] (io/file elem)) rutas-imagenes))
 
;; Las funciones pueden ser nombradas, como las que trabajamos arriba, o anónimas como esta que vemos acá con _fn_. El principio es el 
;; mismo la función en posición de función, seguido por los argumentos o parámetros y luego el cuerpo.

;; Estas son las imágenes con las que vamos a trabajar

^{:nextjournal.clerk/visibility {:code :hide}}
(map #(ImageIO/read %) imagenes)

;; A continuación tenemos preparada una función que convertirá esas imágenes a texto.

(defn iniciar-ocr
  "Recibe una imagen (img) de tipo File, el lenguaje (leng) en el que se encuentra el input 
   (e.g. 'spa') de tipo String y devuelve el texto de la imagen como String. Si se desea agregar
   el modelo de otro lenguaje además de los incluidos acá (spa=español y eng=inglés) descargar 
   el modelo de https://github.com/tesseract-ocr/tessdata_best y alojar en la carpeta resources/data."
  [img leng]
  (try 
    (-> (doto (Tesseract.)
          (.setLanguage leng)
          (.setDatapath "resources/data"))
        (.doOCR (ImageIO/read img)))
    (catch Exception e (.getMessage e))))

;; Veamos qué hicimos acá. En la documentación exponemos el contrato; aclaramos los tipos de datos correspondientes a los 
;; argumentos y el tipo de dato de retorno. Luego tenemos un forma con varias expresiones simbólicas anidadas. Como si de una
;; cebolla se tratase, vamos capa por capa, desde lo más externo hasta el interior.

;; En primer lugar tenemos la función _try_ la cual admite dos argumentos que también son expresiones simbólicas. 
;; La primera es la expresión a ejecutar y la segunda es una expresión cuya función es capturar el error y devolver el mensaje
;; de error. Así que esta función se encarga de capturar excepciones y enviarnos el mensaje de error para
;; ser capaces de debuggearlo. Como regla general, siempre que trabajemos con IO debemos envolver nuestras expresiones en un try-catch.

;; Luego tenemos un función muy rara, una flecha **->**. Este en realidad es un macro y su tarea consiste en evitarnos el inconveniente
;; de anidar demasiadas expresiones simbólicas, lo que haría al código complicado y difícil de entender. Básicamente lo que hace
;; es tomar el primer elemento y pasarle el resultado a la siguiente y así sucesivamente. Este _threading macro_ o macro de hilado
;; insertará el resultado de la expresión anterior en el lugar del primer argumento de la expresión siguiente. Si necesitamos
;; que los valores sean insertados en el último lugar debemos usar **->>**.

;; En el macro de hilado tenemos a la función _doto_ la cual toma un objeto Java (más adelante hablaremos sobre los objetos) y sucesivamente 
;; llama a los métodos (una clase de funciones) que este objeto pone a disposición para ejecutar la tarea de leer la imagen
;; y convertirla a texto. 

;; La librería con la que estamos trabajando nos pide que debemos especificar: a) el lenguaje con el que vamos a trabajar, b) la imagen
;; representada como un objeto del tipo archivo y c) la ruta donde se encuentra el archivo que contiene los modelos entrenados para 
;; reconocer la lengua seleccionada en cuestión.

;; Nosotros hardcodeamos esa ruta (hardcodear significa fijar el valor de un parámetro, de modo que no se pueda cambiar a no ser
;; cambiando el código) y recibimos como parámetro la imagen y el lenguaje.

;; En nuestro vector todos los archivos que tenemos son de textos en español. Tenemos un en inglés que podemos usar para realizar
;; una pequeña prueba. Veamos

;; Esta es la imagen que vamos a probar.

^{:nextjournal.clerk/visibility {:code :hide}}
(ImageIO/read (io/file "resources/best_shuffle.png"))

;; Y he aquí el resultado:

(def texto-ingles (-> (io/file "resources/best_shuffle.png")
                      (iniciar-ocr "eng")))

texto-ingles

;; ¡Funcionó! Ahora probemos con nuestra colección. Tal y como hicimos arriba podemos valernos de nuevo de la función _map_.

(def textos-en-espanol (map (fn [archivo] (iniciar-ocr archivo "spa")) imagenes))

textos-en-espanol

;; No es perfecto, es cierto. Pero este problema lo podemos corregir con mejores fotos y tuneando algunos parámetros con las 
;; herramientas que nos ofrece la librería (cuestión que dejaremos para otra ocasión).

;; Una vez que tenemos estos datos como string podemos hacer lo que deseemos con ellos (siempre y cuando se trate de una operación permita para objetos de este tipo)
;; Por ejemplo, 
;; * pidamos el primer texto, 
;; * dividámoslo por líneas, 
;; * quitemos los strings vacíos 
;; * sustituyamos la última palabra por su valor real, a saber, _output_
;; * y devolvamos como output un solo string con todo el contenido:

(as-> (first textos-en-espanol) txt
  (split-lines txt)
  (remove blank? txt)
  (remove (fn [elem] (= elem "[0]7]10]7)")) txt)
  (conj (vec txt) " output.")
  (reduce str txt))

;; La primera función que observamos es otro _threading macro_; dado que **->** inserta el resultado en el lugar del primer argumento
;; mientras que **->>** lo inserta en el segundo, ¿qué sucede cuando debemos insertar el argumento en diversas posiciones? Pues
;; para eso tenemos **as->**, el cual vincula el resultado con el símbolo que coloquemos al lado de la primera expresión
;; simbólica, en este caso, _txt_. 

;; Luego tomamos el primer elemento, lo dividimos por líneas con lo que transformamos
;; un solo string en un vector de strings; a continuación quitamos todos los espacios en blanco, buscamos los elementos
;; que sean iguales a _[0]7]10]7)_ (que son los caracteres que el OCR no pudo reconocer); posteriormente agregamos la palabra
;; correcta al final de la colección (para eso tenemos que convertir la lista que nos devuelve _remove_ en un vector, pues de lo
;; contrario insertará la palabra _output_ en el primer lugar de la colección) y finalmente aplicamos una reducción, esto es,
;; de una colección, en este caso de strings, aplicamos la función de concatenación _str_ y obtenemos como resultado un solo 
;; string. 

^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/html [:div {:style {:background-color :#E6E6E6 :padding :1rem}}
             [:h3 "Balance"]
             [:p "En esta lección hemos armado un pequeño programa que toma como insumo un conjunto de fotos de textos, las convierte a archivo, las lee con la
                  tecnología OCR y devuelve un texto que podemos manipular, bien sea aplicando una serie de transformaciones sobre el mismo
                   o escribiéndolos a un archivo, en fin, lo que deseemos."]
             [:p "En el interín hemos aprendido algunas cosas sobre programación."]
             [:ol
              [:li "Hemos aprendido sobre los tipos de datos (númericos, booleanos, strings, caracteres símbolos y llaves)"]
              [:li "Hemos aprendido a vincular un símbolo con el resultado de una expresión simbólica (lo que también suele denominarse "[:b "declarar una variable"] ")"]
              [:li "Hemos aprendido que las estructuras de datos almacenan elementos de diverso tipo y que entre ellas tenemos vectores [], listas (), conjuntos #{} y mapas {}."]
              [:li "Hemos aprendido a manipular, mediante las funciones correspondientes, estas estructuras de datos"]
              [:li "Hemos aprendido que podemos combinar las estructuras de datos como deseemos"]
              [:li "Hemos aprendido que en Clojure la unidad básica es la" [:b " expresión simbólica "] "(a saber, simplemente una lista con una función ocupando el 
                    primer puesto, seguido de sus argumentos) y que el anidamiento de expresiones simbólicas se denomina" [:b " forma"]] 
              [:li "Hemos aprendido cuál es la estructura de una función, cómo llamarla y cómo declararla cuando deseemos crear una propia"]
              [:li "Hemos aprendido que cuando cometemos un error (usual aunque no exclusivamente) el intérprete nos envía una excepción y que estas excepciones
                    pueden detener la ejecución de nuestro programa si no las capturamos con la función " [:b "try-catch."]]
              [:li "Y por último, pero no menos importante, hemos aprendido que un programa o software se compone de datos y funciones; que las funciones transforman esos 
                    datos para producir nueva información; y que debemos mantener la integridad de nuestros datos y la pureza de nuestras funciones para que así
                    podamos en todo momento comprender qué está haciendo nuestro programa."]]])

;; Y esto es todo por hoy. Espero que lo hayan disfrutado.

^{:nextjournal.clerk/visibility {:code :hide :result :hide}} 
(comment
  (clerk/serve! {:watch-paths ["src"]})
  (clerk/clear-cache!)

  (def imagen (io/file "resources/best_shuffle.png"))
  (ocr/do-ocr
   (ImageIO/read imagen)
   (ocr/set-language "spa"))
  
  (doto (Tesseract.)
    (.doOCR imagen))

  (as-> (LoadLibs/extractTessResources "win32-x86-64") tmpFolder
    (System/setProperty "java.library.path" (.getPath tmpFolder)))

  (def data (doto (Tesseract.) 
              (.setOcrEngineMode 1)
              (.setDatapath "resources/data")
              (.setLanguage "spa")    
              (.doOCR imagen))) 
  data 
  (-> (doto (Tesseract.)
        (.setLanguage "spa")
        (.setDatapath "resources/data"))
      (.doOCR (ImageIO/read imagen)))  
  
  
  )