package ru.sber.streams


// 1. Используя withIndex() посчитать сумму элементов листа, индекс которых кратен 3. (нулевой индекс тоже входит)
fun getSumWithIndexDivisibleByThree(list: List<Long>): Long {
    return list
        .asSequence()
        .withIndex()
        .filter { it.index % 3 == 0 }
        .sumOf { it.value }
}

// 2. Используя функцию generateSequence() создать последовательность, возвращающую числа Фибоначчи.
fun generateFibonacciSequence(): Sequence<Int> {
    return generateSequence(Pair(0, 1)) { Pair(it.second, it.second + it.first) }.map { it.first }
}

// 3. Получить города, в которых есть покупатели.
fun Shop.getCustomersCities(): Set<City> = customers
    .asSequence()
    .map { it.city }
    .toSet()

// 4. Получить все когда-либо заказанные продукты.
fun Shop.allOrderedProducts(): Set<Product> = customers
    .flatMap { it.orders }
    .flatMap { it.products }
    .toSet()

// 5. Получить покупателя, который сделал больше всего заказов.
fun Shop.getCustomerWithMaximumNumberOfOrders(): Customer? = customers
    .maxByOrNull { it.orders.size }

// 6. Получить самый дорогой продукт, когда-либо приобртенный покупателем.
fun Customer.getMostExpensiveProduct(): Product? = orders
    .asSequence()
    .flatMap { it.products }
    .maxByOrNull { it.price }

// 7. Получить соответствие в мапе: город - количество заказанных и доставленных продуктов в данный город.
fun Shop.getNumberOfDeliveredProductByCity(): Map<City, Int> = customers
    .groupBy(
        { it.city },
        { it.orders
            .asSequence()
            .filter { it.isDelivered }
            .sumOf { it.products.size }
    })
    .mapValues { it.value.sum() }

// 8. Получить соответствие в мапе: город - самый популярный продукт в городе.
fun Shop.getMostPopularProductInCity(): Map<City, Product> = customers
    .groupBy { it.city }
    .mapValues { it.value
        .flatMap { it.orders }
        .flatMap { it.products }
        .groupingBy { it }
        .eachCount()
    }
    .map { it.key to it.value.maxByOrNull { it.value }!!.key }
    .toMap()

// 9. Получить набор товаров, которые заказывали все покупатели.
fun Shop.getProductsOrderedByAll(): Set<Product> = customers
    .asSequence()
    .map { it.orders
        .flatMap { it.products }
        .toSet()
    }
    .reduce { acc: Set<Product>, set: Set<Product> -> acc.intersect(set) }
