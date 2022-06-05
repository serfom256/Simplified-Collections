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
- [ ] MaxBinaryHeap (Based on Binary Tree)
- [ ] Phonetic Algorithms
- [ ] BK Tree



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

SqlBuilder returns [AbstractDynamicString](src/main/java/additional/dynamicstring/AbstractDynamicString.java)

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