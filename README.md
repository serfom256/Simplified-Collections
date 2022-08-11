# This is the simplified version of the java collections with some additions

### Released Collections

- [x] MaxBinaryHeap (Based at Array)
- [x] PriorityQueue (Based at MaxBinaryHeap)
- [x] Single Linked List
- [x] Doubly Linked List
- [x] Array List
- [x] HashTable
- [x] Set (Based on HashTable)
- [x] Set (Based on BST tree)
- [x] Set (Based on RBT tree)
- [x] Stack(Based on Array)
- [x] Sorted List(Based on Skip List)
- [x] Expression interpreter
- [x] TrieMap
- [x] FuzzySearchTrie

### In developing
- Sql Builder
- RadixTrie


### Will be implemented later
- [ ] Binary tree printer
- [ ] Phonetic Algorithms
- [ ] Splay Tree

## Tries usage examples
[FuzzyTrie](src/main/java/tries/tries/FuzzyTrie.java), [SimpleTrieMap](src/main/java/tries/tries/SimpleTrieMap.java), [SearchTrieMap](src/main/java/tries/tries/SearchTrieMap.java) could be suitable for fuzzy search

FuzzyTrie offers efficient autocomplete with a certain indistinct distance
Example Usage:
<br>
SimpleTrieMap can store Key Value pairs and support fuzzy search by keys or values
<br>
Fuzzy search method call example

### TrieMap.lookup([word to search], [fuzzy search distance], [SimpleTrieMap.Verbose.(MIN/MAX)])

SimpleTrieMap.Verbose.MAX used for lookup all nodes in the trie from the root
<br>
SimpleTrieMap.Verbose.MIN used for lookup to search values only in the one branch
```java
        final SimpleTrieMap trieMap = new SimpleTrieMap();
        trieMap.add("1234", "value");
        trieMap.add("4567", "value");
        trieMap.add("qwe", "00000");
        trieMap.add("qwe", "11111111111");
        trieMap.add("test", "00000");
        trieMap.add("test", "00000");
        trieMap.add("abc909", "qwe");
        trieMap.add("12345", "val");
        trieMap.add("12345", "temp");
        trieMap.add("java", "qwerty");
        trieMap.add("java", "random value");
        trieMap.add("c++", "some value");
        assertEquals(8, trieMap.getPairsCount());
        assertEquals(76, trieMap.getSize());
        System.out.println(trieMap.lookup("jva", 1, SimpleTrieMap.Verbose.MAX)); // [(java : {qwerty, random value})]
        System.out.println(trieMap.lookup("0000-", 1, SimpleTrieMap.Verbose.MAX)); // [(test : {qwe, 00000})]
        System.out.println(trieMap.lookup("_alue", 1, SimpleTrieMap.Verbose.MAX)); // [(4567 : {value})]
        System.out.println(trieMap.lookup("144", 2, SimpleTrieMap.Verbose.MAX)); // [(1234 : {value})]
```


## Sql Builder usage examples

SqlBuilder from package sql generates dynamic sql that can be executed by jdbc or jdbcTemplate

```java
SqlBuilder builder = new SqlBuilder();

String query = builder.create()
       .select("id", "name", "email")
       .from(AliasKeyword.as("account","acc"))
       .join(AliasKeyword.as("person_info","p"), "acc.id=p.id").inner()
       .where("id", ">", "100")
       .build().toString();
```

SqlBuilder returns [DynamicString](src/main/java/additional/dynamicstring/DynamicString.java)

Provided above code generates the following sql

```sql
SELECT `id`, `name`, `email`
FROM account AS `acc`
         INNER JOIN person_info AS `p` ON `acc`.`id` = `p`.`id`
WHERE `id` > 100 
```

##
Nested queries support
```java
SqlBuilder subQuery = new SqlBuilder();

subQuery.create()
        .select(Aggregators.avg("salary"))
        .from("users")
        .where("salary", ">", "10000");

String query = new SqlBuilder().create()
        .select("id", "name", "salary").distinct()
        .from("payments")
        .where("salary", ">", subQuery.getInstance().build().toString())
        .and("id", "<", "12345678")
        .build().toString();
```

Provided above code generates the following sql
```sql
SELECT DISTINCT `id`, `name`, `salary`
FROM payments
WHERE `salary` > (SELECT AVG(`salary`) FROM users WHERE `salary` > 10000)
  AND `id` < 12345678 
```

##
A few more examples:

```java
SqlBuilder builder = new SqlBuilder();

String query = builder.create()
       .select("id", "user_name", "email")
       .from("testdb", AliasKeyword.as("users", "u"))
       .where("u.id", "<", "1000").build().toString();
```
Provided above code generates the following sql

```sql
SELECT `id`, `user_name`, `email`
FROM testdb.users AS `u`
WHERE `u`.`id` < 1000 
```

##
```java
String query = builder.create().select("date_id", "value", "date")
       .from("some_table")
       .where("date", "=", Date.date("10.10.2010"))
       .orderBy("date_id", Order.DESC).build().toString();
```
Provided above code generates the following sql
```sql
SELECT `date_id`, `value`, `date`
FROM some_table
WHERE `date` = DATE(10.10.2010)
ORDER BY `date_id` DESC 
```