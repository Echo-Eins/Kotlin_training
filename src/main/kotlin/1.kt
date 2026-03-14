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

