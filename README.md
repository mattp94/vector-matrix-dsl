# vector-matrix-dsl

A language to interpret matrix and array operations (+, - and *). It has been developed in Clojure as part of a student module (Languages & Compilation from [ISEP](https://twitter.com/Isep) 2016).

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

#### Defining an array

```
[1 1 1 1]
```

```
v(4 1)
```

#### Defining a matrix

```
[[5 5 5 5]
 [5 5 5 5]
 [5 5 5 5]]
```

```
m(3 4 5)
```

#### Affecting a variable

```
a = [1 2 3 4]
```

#### Doing an operation

```
a = [1 2]; b = [3 4]; c = [5 6]; (a + b) * c + [7 8]
```

## Team students

- Sébastien Maupaté
- Matthieu Puibaraud
- Jordan Sportes
