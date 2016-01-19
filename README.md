# vector-matrix-dsl

*vector-matrix-dsl* is a student project to interpret matrix and array operations (+, - and *). It it has been developed in Clojure as part of a school project at ISEP.

- \+ to add each element (i,j) together
- \- to substract each element (i,j) together
- \* to multiply each element (i,j) together

## Usage

An example of usage with the following instruction (type it in your REPL):

```clojure
(interpret
  (arith "a = %1 + [[1 2 3 4 5]
                    [1 2 3 4 5]
                    [1 2 3 4 5]];
          b = %2 * (a - m(3 5 7));
          a * b")
  
  "m(3 5 10)"
  
  "[[12  7 15  0  1]
    [ 5  9 78  1  3]
    [65  4  3  0 78]]")
```

## Syntax

### To define an array

```
[1 1 1 1]
```

```
v(4 1)
```

### To define a matrix

```
[[5 5 5 5]
 [5 5 5 5]
 [5 5 5 5]]
```

```
m(3 4 5)
```

### To affect a variable

```
a = [1 2 3 4]
```

### To do an operation

```
a = [1 2]; b = [3 4]; c = [5 6]; (a + b) * c + [7 8]
```

## Team students

- Sébastien Maupaté
- Jordan Sportes
- Matthieu Puibaraud
