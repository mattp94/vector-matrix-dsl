(ns vector-matrix-dsl.core
  (:require [instaparse.core :as insta]
            [clojure.core.matrix :as m])
  (:import (clojure.asm         Opcodes Type ClassWriter)
           (clojure.asm.commons Method GeneratorAdapter)))

(def arith
  "grammar parsing our language. A programe is a sequence of instructions separated by a semi-colon.
  It takes arguments %1 ..%n.
  It returns the value of the last instruction."
  (insta/parser
   "prog = expr space (<';'> space expr)*
    <expr> = assig | addsub
    assig = varname space <'='> space expr
    <addsub> = multdiv | add | sub
    add = addsub space <'+'> space multdiv
    sub = addsub space <'-'> space multdiv
    <multdiv> = factor | mult
    mult = multdiv space <'*'> space factor
    varget = varname | argument
    varname = #'[a-zA-Z]\\w*'
    argument= <'%'>#'[0-9]+'
    <space> = <#'\\s*'>
    number = #'[0-9]+'

    <factor> = array | arrayconst | matrix | matrixconst | <'('> space expr space <')'> | varget | assig

    <lbrack> = <'['>
    <rbrack> = <']'>
    array = lbrack (space number space)+ rbrack
    arrayconst = <'v('> space number space number space <')'>
    matrix = lbrack (space array space)+ rbrack
    matrixconst = <'m('> space number space number space number space <')'>"))

(defn interpret-instr [env ast]
  "Interpret an instruction with a given environnement.
  The result is stored in a special :res_ key of the environnement."
  (let [op-fun (fn [op]
                 (fn[{v1 :res_ :as env1} {v2 :res_ :as env2}]
                   (assoc (merge env1 env2) :res_ (op v1 v2))))
        array-from-node (fn[& nodes](assoc env :res_ (vec (map :res_ nodes))))
        array-const (fn[size value] (vec (repeat size value)))]
    (insta/transform {:assig (fn[{varname :res_ :as env1} {value :res_ :as env2}]
                               (assoc (merge env1 env2) varname value :res_ value))
                      :add (op-fun m/add)
                      :sub (op-fun m/sub)
                      :mult (op-fun m/mul)
                      :number #(assoc env :res_ (Integer/parseInt %))
                      :array array-from-node
                      :arrayconst (fn[{size :res_} {value :res_}](assoc env :res_ (array-const size value)))
                      :matrix array-from-node
                      :matrixconst (fn[{size1 :res_} {size2 :res_} {value :res_}](assoc env :res_ (array-const size1 (array-const size2 value))))
                      :varname #(assoc env :res_ (keyword %)) 
                      :argument #(assoc env :res_ (keyword (str "%" %)))
                      :varget (fn [{varname :res_ :as env1}]
                                (assoc env1 :res_ (get env1 varname 0)))}
                     ast)))

(defn interpret [prog & args]
  "Interpret a given program with given arguments."
  (:res_ (let [env (into {} (map-indexed #(vector (->> %1 inc (str "%") keyword) (interpret (arith %2))) args))]
           (reduce interpret-instr env (rest prog))))) ;; rest to skip :prog tag
