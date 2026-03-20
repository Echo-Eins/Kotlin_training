import java.io.File
/*
fun main() {
    // val (value) - неизменяемая ссылка
    // Как только значение присвоено, перезаписать его в эту же переменную нельзя
    val targetOS = "ArchLinux"

    // var (variable) - изменяемая переменная
    // Значение можно перезаписывать сколько угодно раз в пределах ее типа
    var compilationTime = 120

    println("Сборка под $targetOS заняла $compilationTime минут.")

    // Меняем значение переменной var - все ок
    compilationTime = 45
    println("После обновления ядра $targetOS: $compilationTime минут.")

    // В Kotlin мы стараемся использовать везде val, чтобы нее получить race condition с var
    // var используем в локальных циклах и тд. Kotlin - статически типизированный язык.
    // Он сам определяем тип переменной, при это определение происходит на этапе компиляции, а не в рантайме
    // Если требуется указать тип явно, то используем через двоеточие, например, val targetOS: String = "Gentoo"
}

*/

/*
fun main() {
    // 1. ОБЫЧНЫЙ ТИП (Non-nullable)
    // Компилятор здесь железобетонно гарантирует, что тут всегда есть строка
    var activeInterface: String = "wlan0"  // <-- Пишем String просто для наглядности

    // 2. NULLABLE тип (со знаком вопроса ?)
    // Если мы допускаем, что данных может не быть (например, драйвер не загрузился),
    // Мы обязаны явно сказать об этом компилятору, добавив "?"
    var wifiModule: String? = "QCNCM865"

    // Допустим, что в процессе работы модуль отвалился:
    wifiModule = null

    // КАК ЭТО ОБРАБОТАТЬ?

    // ВАРИАНТ А: Safe call оператор (?.)
    // Выполнит действие, только если переменная не null. Иначе вернет null, без краша
    val nameLength = wifiModule?.length
    println("Длина названия модуля: $nameLength")

    // Вариант Б: Elvis оператор (?:)
    // Читается как "А иначе ...". Задает дефолтное значение, если слева пришел null
    val currentStatus = wifiModule?.length ?: "Модуль не найден, идите нахуй"
    println("Текущий статус: $currentStatus ")

    // Вариант В: Оператор утверждения (!!) - "Мамой клянусь, тут не null"
    // Заставляет компилятор закрыть глаза. На работе за это отрываю руки
}
*/
/*
fun main() {
    var username: String? = null
    println(username?.uppercase() ?: "АНОНИМ")
}
 */

// ФУНКЦИИ //

// fun имя(параметр: Тип): ТипВозвращаемогоЗначения {...}

/*
fun calculateBandwidth(packets: Int, size: Int): Int {
    return packets * size
}
 */

// Если функция просто выполняет действие и ничего не возвращает, то тип возвращаемого значения
// писать не нужно, под капотом Kotlin подставит туда тип Unit (аналог void)

// в Kotlin аргументы функции всегда являются неизменяемыми константами (val).

/*
fun formatClientName (input: String?): String {
    // Если ты попытаешься написать: input = "что-то другое"
    // IDE сразу ударит по рукам ошибкой "Val cannot be reassigned"

    return input?.uppercase() ?: "GUEST"
}

fun main() {
    println(formatClientName("neo"))
    println(formatClientName(null))
}
 */

// ИЛИ (более коротко)

/*
fun formatClientName(input: String?): String = input?.uppercase() ?: "GUEST"
fun main() {
    println(formatClientName("neo"))  // Выведет: NEO
    println(formatClientName(null))   // Выведет: GUEST
}
 */
/*
fun calculateRemainingVram (requestedGigabytes: Int?): Int {
    val minus = requestedGigabytes ?: 0
    return 96-minus
}

fun main() {
    println(calculateRemainingVram(80))
    println(calculateRemainingVram(null))
}
 */

/*
fun calculateRemainingVram (requestedGigabytes: Int?): Int = 96 - (requestedGigabytes ?: 0)
fun main(){
    println(calculateRemainingVram (null))
    println(calculateRemainingVram (80))
}

 */

// ВТОРОЙ ЭТАП: ЛИНЕЙНЫЙ ПРОЕКТ

// Функции в реальных проектах всегда инкапсулированы внутри логических сущностей. Нужно переходить к классам
// Класс - это чертеж
// Объект - это физическая реализация чертежа в оперативной памяти сервера

// В Kotlin классы пишутся максимально компактно Конструктор (то что мы передаем при создании объекта)
// объявляется прямо в заголовке класса

// И у функций, и у классов типы входящих параметров указывать ОБЯЗАТЕЛЬНО ВСЕГДА. Неважно, ждешь ты null или нет.

// 1. Первичный конструктор и Свойства (Properties)
//Конструктор — это то, какие данные класс просит при создании объекта. Он пишется прямо в круглых скобках после имени класса.
//
//Здесь кроется важнейшая фича Kotlin: наличие или отсутствие val/var в конструкторе меняет всё.

/*
// Вариант А: Без val/var
class GpuNode(id: String) {
    // id здесь — это просто временный аргумент.
    // Он существует только в момент создания объекта.
    // Ты не сможешь обратиться к узлу и спросить его id извне.
}

// Вариант Б: С val/var (Так делают в 95% случаев)
class GpuNode(val id: String, var vramTotal: Int) {
    // Добавив val и var, мы автоматически сделали их "Свойствами" класса.
    // Теперь они навсегда пришиты к объекту.
    // id менять нельзя (val), а vramTotal — можно (var).
}
 */

// 2. Блок инициализации (init)
//Поскольку первичный конструктор в Kotlin не имеет фигурных скобок, возникает вопрос: а где писать стартовую логику? Например, если при создании узла мы хотим сразу написать об этом в логи или проверить валидность данных. Для этого есть блок init.
//
//Он выполняется автоматически в момент создания объекта:
/*
class GpuNode(val id: String, var vramTotal: Int) {

    init {
        // Этот код выполнится сразу при создании
        require(vramTotal >= 0) { "Память не может быть отрицательной!" }
        println("Узел $id успешно поднят. Доступно VRAM: $vramTotal ГБ.")
    }
}
 */

// 3. Методы класса
//Методы — это функции, которые живут внутри класса и имеют прямой доступ к его свойствам (id и vramTotal).

/*
class GpuNode(val id: String, var vramTotal: Int) {

    // ... тут может быть init блок ...

    fun allocateTask(requested: Int?): Boolean {
        // 1. Распаковываем запрос через Элвиса
        val needed = requested ?: 0

        // 2. Проверяем, хватает ли памяти
        if (vramTotal >= needed) {
            vramTotal -= needed // Отнимаем память
            println("[$id] Задача принята. Остаток: $vramTotal ГБ.")
            return true
        } else {
            println("[$id] ОТКАЗ. Не хватает памяти.")
            return false
        }
    }
}
 */

// 4. Как это использовать (Создание объектов)
//В функции main мы создаем конкретные "инстансы" (экземпляры) нашего класса. Слово new в Kotlin не используется.

/*
fun main() {
    // Создаем два разных узла
    val nodeAlpha = GpuNode("Alpha-1", 96)
    val nodeBeta = GpuNode("Beta-2", 24)

    // Обращаемся к их методам
    nodeAlpha.allocateTask(80)   // Хватит, останется 16
    nodeAlpha.allocateTask(40)   // Не хватит (нужно 40, а осталось 16)

    nodeBeta.allocateTask(null)  // Запросили null (0), останется 24
}
 */

// Главный класс бэкендера: Data Class
//В бэкенде мы постоянно гоняем данные туда-сюда: получаем JSON от клиента, пишем в базу данных, отправляем ответы.
// Для сущностей, которые просто хранят данные и не имеют сложной логики, в Kotlin есть супер-оружие — data class.

/*
data class UserRequest(val username: String, val token: String)
 */

// Одно слово data перед классом заставляет компилятор автоматически сгенерировать для этого класса кучу полезных служебных методов:
// для сравнения объектов (equals), для красивого вывода в консоль (toString), для копирования (copy) и так далее.
// Мы будем использовать их постоянно, когда дойдем до парсинга запросов в Ktor.

/*
class GpuNode(val name: String, var Vram: Int) {

    init{
        require(Vram >= 0) { "Памяти нет!" }
        println("Узел $name поднят. Доступно VRAM: $Vram")
    }

    fun allocateTask(req:Int?): Boolean{
        val minus = req ?: 0

        if (Vram >= minus) {
            Vram -= minus
            println("$name принят. Оста VRAM: $Vram ГБ")
            return true
        }
        else {
            println("$name - Отказ, недостачно VRAM")
            return false
        }
    }
}

fun main() {
    val N1 = GpuNode("Node1", 80)

    N1.allocateTask(50)
    N1.allocateTask(null)
    N1.allocateTask(40)
}
 */

// КОЛЛЕКЦИИ И ЦИКЛЫ:

// Все коллекции строго разделены на два типа:
// 1) Неизменяемые (RO): Можно только читать элементы. Нельзя добавлять новый, удалять старый
// 2) Изменяемые (Mutable): Можно добавлять, удалять, сортировать

// listOf() создает неизменяемый список.
// Компилятор сам понимает, что это List<GpuNode>
/*
var cluster = listOf(
    GpuNode("N1", 24)
    GpuNode("N2", 80)
    GpuNode("N3", 40)
)
 */
// Если бы нам нужно было динамически добавлять узлы,
// мы бы использовали mutableListOf()


// Циклы: Как перебрать кластер
//В Kotlin классический цикл for с индексами (как в Си: for(int i=0; i<10; i++))
// отправлен на свалку истории. Вместо него используется лаконичный for-in,
// который элегантно перебирает любые коллекции:
/*
for (node in cluster) {
    println("Перепроверяем узел: ${node.id}")
}
 */

// 1. Убийца if-else: Конструкция when
/*
fun main() {
    val statusCode = 404

// Читается интуитивно: "Когда statusCode равен..."
    when (statusCode) {
        200 -> println("Успех: Данные отправлены")
        400, 404 -> println("Ошибка клиента: Запрос неверный или не найден")
        in 500..599 -> println("Ошибка сервера: Бэкенд упал")
        else -> println("Неизвестный статус") // else обязателен, если мы не перебрали все варианты
    }


    // А теперь самое крутое: как и if, when может возвращать значение. Это позволяет писать невероятно лаконичный код:
    val role = "admin"
    // Результат выполнения when сразу записывается в переменную permissions
    val permission = when (role) {
        "admin" -> "full_access"
        "moderator" -> "write_access"
        "guest" -> "read_only"
        else -> "no_access"
    }
    println(permission)
}

// 2. Управление циклами: break против return
// Как остановить цикл? Есть два пути:
// Путь А: break (Остановка цикла)
//Команда break просто ломает текущий цикл,
// но функция продолжает выполняться дальше (идет к строкам, которые написаны после цикла).

fun searchTarget() {
    val numbers = listOf(1,2,3,4,5)

    for (num in numbers) {
        if (num == 3) {
            println("3 found. Braking the cycle")
            break
        }
        println("Смотрим: $num")
    }
    // после break код прыгнет сюда
    println("Функция завершает работу")
}

// Путь Б: return (Жесткий выход из функции)
//Команда return не просто ломает цикл, она вообще убивает выполнение всей функции в эту же миллисекунду.
// Всё, что написано после цикла, уже никогда не выполнится.

fun dispatch() {
    val servers = listOf("S1", "S2")

    for (s in servers) {
        if (s == "S1") {
            println("Отдали задачу на $s. Ливаем")
            return // функция dispatch() немедленно завершается
        }
    }
    // до этой строки выполнение НИКОГДА не дойдет в данном примере
    println("Этот текст никто не увидит")
}
*/
/*
class GpuNode(val name: String, var vram: Int) {
    init {
        require(vram >= 0) {"Памяти нет!"}
        println("Узел $name поднят. Доступно VRAM: $vram")
    }

    fun allocateTask(req:Int?): Boolean{
        val minus = req ?: 0

        if (vram >= minus) {
            vram -= minus
            println("$name принят. Остаток VRAM: $vram ГБ")
            return true
        }
        else {
            println("$name - Отказ, недостачно VRAM")
            return false
        }
    }
}
fun dispatchTask(cluster:List<GpuNode>, requested: Int?) {
    for (s in cluster) {
        if (s.allocateTask(requested)) {
            return
        }
    }
    println("Задача отклонена, нет свободной памяти!")
}

fun main() {
    val myCluster = listOf(
        GpuNode("N24", 24),
        GpuNode("N40", 40),
        GpuNode("N80", 80)
    )

    dispatchTask(myCluster, null)
    dispatchTask(myCluster, 50)
    dispatchTask(myCluster, 40)
    dispatchTask(myCluster, 90)
}
 */

/*
Блок 1: Ввод и Безопасность
В Kotlin чтение строки с клавиатуры делается одной простой командой: readln().
Она останавливает программу и ждет, пока пользователь нажмет Enter,
после чего возвращает строгий тип String.

Если нам нужно число, мы должны конвертировать строку. У строк есть методы .toInt(), .toLong(), .toDouble().

Но есть проблема. Если юзер введет abc, метод .toInt() выбросит исключение NumberFormatException и программа сдохнет.

Для безопасной работы в Kotlin есть "мягкие" методы конвертации: .toIntOrNull().
Ты уже знаешь, как работать с Nullable-типами. Если конвертация не удалась,
метод просто вернет null, и программа продолжит работу.
 */

/*
fun main() {
    println("Введите возраст:")
    val input: String = readln()

    // Пытаемся превратить строку в число
    val age: Int? = input.toIntOrNull()

    if (age != null) {
        println("Через 5 лет вам будет ${age+5}")
    } else {
        println("Ошибка: вы ввели не число!")
    }
}
*/
/*
class GpuNode(val name: String, var vram: Int) {
    init {
        require(vram >= 0) {"Памяти нет!"}
        println("Узел $name поднят. Доступно VRAM: $vram")
    }

    fun allocateTask(req:Int?): Boolean{
        val minus = req ?: 0

        if (vram >= minus) {
            vram -= minus
            println("$name принят. Остаток VRAM: $vram ГБ")
            return true
        }
        else {
            println("$name - Отказ, недостачно VRAM")
            return false
        }
    }
}

fun dispatchTask(cluster:List<GpuNode>, requested: Int?) {
    for (s in cluster) {
        if (s.allocateTask(requested)) {
            return
        }
    }
    println("Задача отклонена, нет свободной памяти!")
}

fun buildCluster(): MutableList<GpuNode> {
    val cluster = mutableListOf<GpuNode>()

    while (true) {
        val next = cluster.size + 1
        println("Введите объем памяти для Node$next (или 'exit' для выхода)")

        val input = readln()

        if (input == "undo") {
            if (cluster.isNotEmpty()) {
                cluster.removeLast()
                println("ОТМЕНА: Последний узел удален.")
            } else {
                println("Ошибка: Кластер пуст, удалять нечего.")

            }
            continue
        }

        if (input == "exit") {
            break
        }

        val mem = input.toIntOrNull() ?: -1
        if (mem <= 0){
            println("Ошибка: введите положительное число!")
            continue
        }


        val nodeName = "Node$next"
        cluster.add(GpuNode(nodeName, mem))
        println("Узел $nodeName на $mem ГБ успешно добавлен в кластер!\n")
    }
    return cluster
}

fun main() {
    println("Иницализация кластера:")
    val myCluster = buildCluster()
    println("\n КЛАСТЕР СОБРАН")
    println("Всего узлов: ${myCluster.size}")
}
*/



// Блок 2: Арсенал структур данных

// 1. Списки (List) против Массивов (Array)
// Списки (List, MutableList): Это высокоуровневая абстракция.
// Тот же MutableList под капотом (на уровне JVM) обычно реализован как ArrayList.
// Когда ему не хватает места, он создает в памяти новый массив большего размера и копирует туда старые данные.
// Это удобно, но требует дополнительных ресурсов процессора и памяти.
//
// Массивы (Array): Это низкоуровневая структура. Размер массива жестко фиксируется при создании.
// Ты не можешь сделать .add() или .remove().
// Зато он лежит в памяти единым сплошным блоком, и доступ к элементам по индексу происходит мгновенно.

// Секретное оружие производительности: Если тебе нужен массив чисел, никогда не используй Array<Int>.
// В Kotlin для этого есть IntArray (и аналоги DoubleArray, ByteArray).
// Array<Int> создает тяжелые объекты-обертки для каждого числа, а IntArray хранит голые примитивы.
// В ML-расчетах разница в потреблении памяти и скорости может достигать десятков раз.

// 2. Множества (Set)
// Особенность: Хранит только уникальные элементы. Дубликаты игнорируются.
// Под капотом: Работает на основе хэш-таблиц. Поиск элемента в Set происходит за константное время O(1).
// Преобразование: Любой список или массив можно мгновенно лишить дубликатов, вызвав у него метод .toSet().

// 3. Кортежи (Tuples): Pair и Triple
// Часто функция должна вернуть не одно значение, а два (например, статус и само сообщение).
// Писать для этого отдельный class каждый раз — муторно. Используем Pair (пара) или Triple (тройка).

/*
fun getSystemStatus(): Pair<Int, String> {
    return Pair(200, "All system operational")
}

fun main() {
    // // Деструктуризация: мы "распаковываем" ответ сразу в две переменные
    val (code, mesage) = getSystemStatus()
    println("Код: $code, Текст: $mesage")
}
*/

// 4. Срезы (Slices)
// Стандартная библиотека делает это лаконично:
//
//list.take(3) — берет первые 3 элемента.
//
//list.takeLast(2) — берет 2 элемента с конца.
//
//list.drop(1) — отбрасывает первый элемент и возвращает всё остальное.
/*
class GpuNode(val name: String, var vram: Int) {
    init {
        require(vram >= 0) {"Памяти нет!"}
        println("Узел $name поднят. Доступно VRAM: $vram")
    }

    fun allocateTask(req:Int?): Boolean{
        val minus = req ?: 0

        if (vram >= minus) {
            vram -= minus
            println("$name принят. Остаток VRAM: $vram ГБ")
            return true
        }
        else {
            println("$name - Отказ, недостачно VRAM")
            return false
        }
    }
}

fun dispatchTask(cluster:List<GpuNode>, requested: Int?) {
    for (s in cluster) {
        if (s.allocateTask(requested)) {
            return
        }
    }
    println("Задача отклонена, нет свободной памяти!")
}

fun buildCluster(): MutableList<GpuNode> {
    val cluster = mutableListOf<GpuNode>()

    while (true) {
        val next = cluster.size + 1
        println("Введите объем памяти для Node$next (или 'exit' для выхода)")

        val input = readln()

        if (input == "undo") {
            if (cluster.isNotEmpty()) {
                cluster.removeLast()
                println("ОТМЕНА: Последний узел удален.")
            } else {
                println("Ошибка: Кластер пуст, удалять нечего.")

            }
            continue
        }

        if (input == "exit") {
            break
        }

        val mem = input.toIntOrNull() ?: -1
        if (mem <= 0){
            println("Ошибка: введите положительное число!")
            continue
        }


        val nodeName = "Node$next"
        cluster.add(GpuNode(nodeName, mem))
        println("Узел $nodeName на $mem ГБ успешно добавлен в кластер!\n")
    }
    return cluster
}

fun analyzeLogs(logs:Array<String>): Pair<Int, List<String>> {
    val mid = logs.toSet()
    val len = mid.size
    val top3e = mid.take(3)
    return Pair(len, top3e)
}

fun main() {
    println("Иницализация кластера:")
    val myCluster = buildCluster()
    println("\n КЛАСТЕР СОБРАН")
    print("Всего узлов: ${myCluster.size}\n")

    val rawLogs = arrayOf("Timeout", "GPU_Temp_High", "Timeout", "VRAM_Overflow", "Timeout", "Network_Loss")

    val (len, top3e) = analyzeLogs(rawLogs)
    println("Найдено уникальных ошибок: $len. Топ-3 для анализа: $top3e")
}
*/

/*
// Функциональная магия (Трансформации)
// Kotlin поддерживает функциональный стиль программирования.
// Это значит, что функции можно передавать внутрь других функций. Для этого используются лямбда-выражения — блоки кода в фигурных скобках { }.

// Внутри лямбды в Kotlin есть магическое слово it (от англ. "он/это").
// Оно автоматически означает "текущий элемент", который мы сейчас обрабатываем. Тебе даже не нужно придумывать ему имя.

// Разбираем три главных метода:
// 1. Фильтрация: filter:
// Оставляет в коллекции только те элементы, которые подходят под условие. Остальные выбрасываются (создается новый список).

val numbers = listOf(1, -5, 10, -2, 4)
// "Оставь только те элементы (it), которые больше нуля"
val positiveNumbers = numbers.filter { it > 0 }
// Результат: [1, 10, 4]

// 2. Преобразование map:
// Берет каждый элемент коллекции, делает с ним то, что ты скажешь, и кладет результат в новый список.
// Размер итогового списка всегда равен исходному.

val names = listOf("alpha", "beta", "gamma")
// "Возьми каждую строку (it) и сделай ее заглавной"
val upperNames = names.map { it.uppercase() }
// Результат: [ALPHA, BETA, GAMMA]

// 3. Цепочки вызовов (Chaining)
// Тебе не нужно создавать промежуточные переменные, условно, val mid = ...
// Ты можешь вызывать эти методы один за другим, выстраивая конвейер (pipeline) обработки данных.

val rawData = listOf("node 1 ", "node 2" , "", "node 3" )

// Конвейер:
// 1. Убираем пустые строки
// 2. У оставшихся обрезаем пробелы по краям (trim)
// 3. Делаем все заглавными буквами

val cleanData = rawData
    .filter { it. isNotBlank() }
    .map { it.trim() }
    .map { it.uppercase() }

// Результат: [NODE1, NODE2, NODE3]
*/

/*
val rawRequests = listOf(16, -5, 0, 32, 8, -1, 64)

val cleanRequests = rawRequests
    .filter { it > 0 }
    .map { it*1024 }

fun main() {
    println(cleanRequests)
}
*/
/*
// БЛОК 4: МАТРИЦЫ И ВЫЧИСЛЕНИЯ

// В Kotlin нет встроенного слова Matrix. Матрица здесь - это массив массивов.
// Но, вспоминая наш набор структур данных, мы будем писать сразу оптимизированный production код

// Не будем использовать тяжелый Array<Array<Int>>.
// Будем использовать массив, состоящий из легковесных массивов примитивов: Array<Int<Array>.

// Вот как создается и читается двумерная структура:

// Создаем матрицу 3x3
val myMatrix: Array<IntArray> = arrayOf(
    intArrayOf(1, 2, 3),
    intArrayOf(4, 5, 6),
    intArrayOf(7, 8, 9)
)

// Обращение идет по классике: строка, затем столбец (индексация с нуля)
val element = myMatrix[0][2] // Возьмет первую строку (индекс 0) и третий элемент (индекс 2) -> вернет 3
*/
/*
class Matrix3x3 (val data:Array<IntArray>) {

    init {
        require( data.size == 3) {"Неверная размерность матрицы! Введено ${data.size} строк вместо 3!"}

        for (col in data) {
            require(col.size == 3) {
                "Неверная размерность матрицы! В строке ${col.size} элементов, вместо 3!"
            }
        }
    }

    fun determinant(): Int {
        val det = data[0][0] * data[1][1] * data[2][2] + data[1][0] * data[2][1] * data[0][2] + data[0][1] * data[1][2] * data[2][0] -
                data[0][2] * data[1][1] * data[2][0] - data[1][0] * data[0][1] * data[2][2] - data[0][0] * data[2][1] * data[1][2]
        return det
    }
}

fun main() {
    val rawData = arrayOf(
        intArrayOf(5, 3, 2),
        intArrayOf(1, 4, 6),
        intArrayOf(7, 8, 9)
    )

    val matrix = Matrix3x3(rawData)
    println(matrix.determinant())
}
*/

// БЛОК 5: ФАЙЛОВАЯ СИСТЕМА (I/O)
// Для работы с файлами используется класс File из библиотеки Java, но Kotlin добавил к нему мощнейшие функции-расширения.
// Чтобы они заработали, в самом верху файла (до всего кода) нужно добавить импорт: import java.io.File

// Базовый арсенал:
// 1) Запись с перезаписью (уничтожает старые данные):
// File("result.txt").writeText("Здесь будет текст")

// 2) Добавление в конец файла (например, для логов):
// File("result.txt"). appendText("\nНовая строчка снизу")

// 3) Чтение файла целиком (в одну строку):
// val text = File("result.txt").readText()

// 4) Чтение по строкам (вернет List<String>):
// val lines = File("result.txt").readLines()

// Если файл result.txt не существует, методы записи (writeText, appendText) создадут его автоматически.
// Метод чтения (readText), если файла нет, выдаст ошибку FIleNotFoundException

/*
class Matrix3x3(val data: Array<IntArray>) {
    init {
        require(data.size == 3) { "Неверная размерность матрицы! ООжидаемое количество строк: 3, получено: ${data.size}." }

        for (col in data) {
            require(col.size == 3) { "Неверная размерность матрицы! Ожидаемое количество столбцов: 3, получено: ${col.size}. " }
        }
    }

    fun determinant(): Int {
        val det = data[0][0] * data[1][1] * data[2][2] + data[1][0] * data[2][1] * data[0][2] + data[0][1] * data[1][2] * data[2][0] -
                data[0][2] * data[1][1] * data[2][0] - data[1][0] * data[0][1] * data[2][2] - data[0][0] * data[2][1] * data[1][2]
        return det
    }
}

fun main() {
    val rawData = arrayOf(
        intArrayOf(5, 3, 2),
        intArrayOf(1, 4, 6),
        intArrayOf(7, 8, 9)
    )

    val matrix = Matrix3x3(rawData)
    println("Определитель: ${matrix.determinant()}")

    File("matrix_dump.txt").writeText("Матрица инициализирована. Детерминант: ${matrix.determinant()}")
    val check = File("matrix_dump.txt").readText()
    println("ПРОВЕРКА ФАЙЛОВОЙ СИСТЕМЫ: \n$check")
}
*/

////////////////// Архитектура 2.0: Интерфейсы и Полиморфизм ///////////////////////

// Чтобы не писать сотни независимых классов, профессионалы используют Интерфейсы.
// Интерфейс - это контракт. Он не содержит самих данных, он лишь говорит:
// "Любой класс, который подпишет этот контракт, ОБЯЗАН иметь вот такие функции."

/*
// 1) Создаем контракт. Любая фигура должна уметь считать свою площадь.
interface Shape {
    fun calculateArea(): Double
}

// 2) Класс Квадрат "подписывает" контракт (через двоеточие)
class Square(val side: Double) : Shape {
    override fun calculateArea(): Double {
        return side*side
    }
}
// 3) Класс Круг тоже подписывает контракт
class Circle(val radius: Double) : Shape {
    override fun calculateArea(): Double {
        return Math.PI * radius* radius
    }
}

// Теперь можно создавать список, который хранит не конкретные квадраты или круги, а любые объекты, подписавшие контракт Shape.
// И теперь без разницы, что это за фигура, так как у нее гарантированно есть calculateArea().

val myShapes: List<Shape> = listOf(Square(4.0), Circle(2.5), Square(10.0))

val totalArea = myShapes.sumOf { it.calculateArea() }

fun main() {
    println(totalArea)
}
*/

// Когда ты пишешь val shapes: List<Shape3D>, ты говоришь компилятору:
//"Мне абсолютно плевать, как эта фигура устроена внутри. Мне неважно, есть ли у нее радиус, грани или углы. Единственное, что меня волнует — эта фигура подписала контракт Shape3D,
// а значит, она гарантированно имеет функцию volume(). Пропусти ее в список."
//
//Это позволяет тебе в одном цикле дергать метод volume() у совершенно разных математических объектов, и каждый объект сам решит, по какой формуле ему считать свой объем.
// Это и есть полиморфизм (многообразие форм) в действии.

// Если бы мы не использовали интерфейсы, тебе пришлось бы для хранения 10 разных фигур создавать 10 разных списков (List<Cube>, List<Sphere>, List<Pyramid>),
// а потом писать 10 разных циклов for, чтобы посчитать их общий объем. Это архитектурный ад.

// Команда readln() всегда возвращает сырой текст — тип String. Даже если пользователь ввел цифру 5, для машины это просто символ "пять", а не математическое значение.

// Для конвертации у строк в Kotlin есть семейство встроенных методов-расширений. Как мы уже выяснили на горьком опыте, в продакшене всегда используются безопасные версии с суффиксом OrNull,
// чтобы программа не падала от мусорного ввода.

// В дробное число (Double): readln().toDoubleOrNull()
// Примечание: В Kotlin разделителем дроби при парсинге по умолчанию считается точка (5.5), а не запятая.
//
// В дробное число одинарной точности (Float): readln().toFloatOrNull()
//
// В целое длинное число (Long): readln().toLongOrNull() (используется для ID в базах данных или миллисекунд).
//
// В логический тип (Boolean): readln().toBooleanStrictOrNull()
// Примечание: Вернет true только если юзер введет строго "true", и false если "false". Любое другое слово даст null.
//
// В строку (String): Ничего конвертировать не надо! val text = readln() уже кладет строку в переменную.

/*
interface Shape3D {
    fun volume(): Double
}

class Cube(val side: Double): Shape3D {
    override fun volume(): Double {
        return side*side*side
    }
}
class Sphere(val radius: Double): Shape3D {
    override fun volume(): Double {
        return (4 * Math.PI * Math.pow(radius, 3.0))/3 // или return (4.0/3.0) * Math.PI * Math.pow(radius, 3.0)
    }
}

fun main() {
    val shit: List<Shape3D> = listOf(Cube(3.0), Sphere(3.0), Sphere(8.0))
    val totalsum = shit.sumOf { it.volume() }
    println(totalsum)
    println(Math.PI)
}
*/

// БЛОК 6: Умные данные (Data Classes) и магия операторов
// Если ты создашь обычный класс class Vector(val x: Double, val y: Double, val z: Double) и попробуешь вывести его в консоль через println(myVector),
// ты увидишь мусор вроде Vector@5a39699c (хэш в памяти). Чтобы сравнить два вектора через ==, тебе придется писать километры кода.
// В Kotlin для это есть data class
// Добавив всего одно слово data перед class, компилятор под капотом сам напишет для тебя:
// 1) Красивый вывод в консоль: Vector(x=1.0, y=2.0, z=3.0)
// 2) Правильное сравнение (== будет сравнивать значение внутри, а не адреса в памяти)
// 3) Метод .copy() для быстрого клонирования объекта с изменением пары свойств:

// Метод .copy() генерируется компилятором исключительно для data class. У обычного class этой функции "из коробки" нет.
// Но здесь кроется фундаментальный архитектурный нюанс, который ты должен усвоить.
// Метод .copy() не меняет свойства старого объекта. В серьезной бэкенд-разработке свойства классов-контейнеров почти всегда объявляются через val (неизменяемые).
// Если ты создал вектор val v1 = Vector3D(1.0, 2.0, 3.0), ты физически не сможешь написать v1.y = 99.0 — компилятор выдаст ошибку.
// Метод .copy() берет исходный объект, создает его полный клон в новой ячейке памяти, и прямо в момент клонирования позволяет подменить значения.
// Старый объект при этом остается нетронутым (это называется Immutability — неизменяемость состояния, защита от багов при многопоточности).

// Какие свойства можно менять?
// Абсолютно любые, которые прописаны в первичных скобках (в главном конструкторе) твоего data class. Ты обращаешься к ним по имени.
// На примере твоего будущего вектора:
// data class Vector3D(val x: Double, val y: Double, val z: Double)
//
//fun main() {
//    val v1 = Vector3D(1.0, 2.0, 3.0)
//
//    // Клонируем вектор v1, но принудительно заменяем координату Z
//    // Координаты X и Y автоматически скопируются из v1
//    val v2 = v1.copy(z = 10.0)
//
//    // Можно заменить сразу несколько свойств:
//    val v3 = v1.copy(x = 0.0, y = 0.0)
//
//    println(v1) // Выведет: Vector3D(x=1.0, y=2.0, z=3.0) - оригинал цел!
//    println(v2) // Выведет: Vector3D(x=1.0, y=2.0, z=10.0)
//    println(v3) // Выведет: Vector3D(x=0.0, y=0.0, z=3.0)
//}

// Перегрузка операторов (Operator Overloading):
// Если у тебя есть два объекта v1 и v2, по умолчанию ты не можешь написать v1 + v2.
// Компилятор не знает, как "плюсовать" объекты. Но в Kotlin можно его этому научить с помощью слова operator.

/*
data class Point(val x: Double, val y: Double) {
    // Учим класс реагировать на математический плюс (+)
    operator fun plus(other: Point): Point {
        return Point(this.x + other.x, this.y + other.y)
    }
}
// Теперь в main можно писать: val p3 = p1 + p2
*/

// 1. Что такое this и other?
//Так как p1 + p2 — это на самом деле вызов метода p1.plus(p2), роли распределяются жестко:
//this (этот) — это объект, который стоит слева от плюса (p1). Тот самый объект, у которого мы сейчас вызвали метод.
//other (другой) — это объект, который стоит справа от плюса (p2). Тот самый объект, который мы передали внутрь функции как аргумент.
//Примечание: Слово other — это не зарезервированное слово языка. Ты можешь назвать этот аргумент хоть valera, просто other — это негласный стандарт индустрии.

// 2. Это только для объектов ОДНОГО датакласса?
//НЕТ. Ты можешь научить свой класс складываться с чем угодно. Тип аргумента зависит только от того, какую логику ты хочешь реализовать.

// Пример с разными типами:
// data class Point(val x: Double, val y: Double) {
//
//    // Учим складывать Point и обычный Int
//    operator fun plus(number: Int): Point {
//        // this.x - это координата точки слева
//        // number - это число, которое написали справа от плюса
//        return Point(this.x + number, this.y + number)
//    }
//}
//
//// Теперь в main компилятор разрешит это:
//// val myPoint = Point(2.0, 3.0)
//// val shiftedPoint = myPoint + 5

// 3. Почему ожидаемый тип: Point?
// То, что стоит после двоеточия в конце функции — это тип возвращаемого результата.
// Ты как архитектор должен задать себе вопрос: "Если я математически сложу эти две сущности, что должно получиться в итоге?"
//
// Если ты складываешь две координаты, логично, что в результате рождается новая координата. Поэтому метод plus возвращает новый объект Point.
//
// Если ты умножаешь вектор на число, рождается новый вектор. Значит, возвращаемый тип будет: Vector3D.
//
// А если ты делаешь скалярное произведение двух векторов (как в твоей текущей задаче), математика говорит нам, что результатом будет одно-единственное число (скаляр).
// Значит, функция dotProduct должна возвращать не вектор, а тип: Double.

/*
data class Vector3D(val x: Double, val y: Double, val z: Double) {
    operator fun plus(other: Vector3D): Vector3D {
        return Vector3D(this.x + other.x, this.y + other.y, this.z + other.z)
    }
    operator fun minus(other: Vector3D): Vector3D {
        return Vector3D(this.x - other.x, this.y - other.y, this.z - other.z)
        // вычитаем из текущего вектора тот, что передали в аргумент (other)!
    }
    operator fun times(other: Vector3D): Vector3D {
        return Vector3D(this.x * other.x, this.y * other.y, this.z * other.z)
    }

    fun dotProduct(other: Vector3D): Double {
        return this.x * other.x + this.y * other.y + this.z * other.z
    }
}

fun main() {
    val v1 = Vector3D(2.0, 3.0, 4.0)
    val v2 = Vector3D(1.0, -1.0, 5.0)

    val v3 = v1 + v2
    val v4 = v1 - v2
    println("Сумма: $v3. Разность: $v4")
    val v5 = v1.dotProduct(v2)
    println("Скалярное произведение: $v5")
}
*/