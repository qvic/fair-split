# Fair split

Calculate efficient way to return money based on borrowing history.

```mermaid
graph LR;
   A(John) -->|100| B(Alice);
   B -->|200| C(Bob);
   C -->|150| A;
```

Tested using property-based testing with [jqwik](https://github.com/jlink/jqwik).

### Example

Columns: `From, To, Amount`.

Input file:
```csv
John, Alice, 100
Alice, Bob, 200
Bob, John, 150
```

Output file:
```csv
Bob, Alice, 50
John, Alice, 50
```

Use `--graph` option to create dot graphs.
Then use, for example, `dot -Tsvg in.csv.dot -o in.svg` to create svg from dot file.

For input above, this will produce such diagrams:

### Build from source

```shell
mvn clean package
java -jar target/app.jar in.csv out.csv --graph
```
