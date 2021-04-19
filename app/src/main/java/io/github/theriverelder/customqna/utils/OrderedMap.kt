package io.github.theriverelder.customqna.utils


class OrderedMap<T : Any, K>(private val getKey: (T) -> K) {
    private val orderedKeys: MutableList<K> = ArrayList()
    private val map: MutableMap<K, T> = HashMap()

    public fun put(elem: T): OrderedMap<T, K> {
        val key: K = getKey(elem)
        if (!map.containsKey(key)) {
            orderedKeys.add(key)
        }
        map[key] = elem
        return this
    }

    public operator fun get(key: K): T? = map[key]

    public fun elemAt(index: Int): T? = if (index in (0 until orderedKeys.size)) map[orderedKeys[index]] else null

    public operator fun contains(key: K): Boolean = map.contains(key)

    public fun remove(key: K): T? {
        orderedKeys.remove(key)
        return map.remove(key)
    }

    public fun removeAt(index: Int): T? {
        val key = orderedKeys.removeAt(index) ?: return null
        return map.remove(key)
    }

    public val size get() = map.size

    public fun toList(): List<T> = orderedKeys.map { map[it] }.filterNotNull()

}