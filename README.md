# Lexical Analyzer and Parser (Java) ⚙️

A Java-based **compiler front-end** implementing a two-phase pipeline: a **Lexical Analyzer (Scanner)** and a **Table-Driven LL(1) Syntax Parser**, designed to tokenize and parse a subset of C++ source code.

*Developed as a course project for Compiler Construction, B.Sc. Computer Science, University of Lahore (2015).*

---

## 🚀 Pipeline Overview

The system runs in two distinct compiler phases:

```text
input.txt (C++ source)
      │
      ▼
┌─────────────────────┐
│  Lexical Analyser   │  Phase 1: Tokenizes raw source code
│  (part1 package)    │
└─────────────────────┘
      │ output.txt (token stream)
      ▼
┌─────────────────────┐
│     Parser          │  Phase 2: Validates token stream
│  (Part2 package)    │  against LL(1) grammar rules
└─────────────────────┘
      │
      ▼
Parse result (success / error with line number)

```

---

## 📦 Project Structure

```text
compiler-frontend/
├── src/
│   ├── part1/
│   │   └── Lexical_Analyser.java   # Scanner — tokenizes C++ source
│   └── Part2/
│       └── Parser.java             # Table-driven LL(1) parser
├── input.txt                       # C++ source file to analyse
├── output.txt                      # Intermediate token stream (auto-generated)
├── parseTableCformatted.txt        # LL(1) parse table (tab-separated)
└── grammarC++.txt                  # Context-Free Grammar productions

```

---

## 🔍 Phase 1 — Lexical Analyser

**Class:** `part1.Lexical_Analyser`

Reads C++ source line-by-line and classifies each character sequence into a typed token, writing the token stream to `output.txt`.

### Lexical Specification (Token Types)

📄 **[View Full Lexical Specification & Regular Expressions (PDF)](./README.pdf)**

| Token Type | Lexical Rule | Output |
| --- | --- | --- |
| **keyword** | `while`, `if`, `else`, `return`, `break`, `continue`, `int`, `float`, `void` | literal keyword |
| **identifier** | `letter → [A-Z \| a-z]` <br> `id → letter (letter \| digit \| _)*` | `identifier` |
| **num** | See formal grammar below | `num` |
| **addop** | `+`, `-` | `addOp` |
| **mulop** | `*`, `/` | `mulOp` |
| **relop** | `<`, `>`, `<=`, `>=`, `==`, `!=` | `relOp` |
| **assignop** | `=` | `=` |
| **and** | `&&` | `and` |
| **or** | `\|\|` | `or` |
| **not** | `!` | `not` |
| **Delimiters** | `(`, `)`, `{`, `}`, `[`, `]`, `;` | literal symbol |
| **Whitespace** | spaces, tabs | *ignored* |
| **Unknown** | any unrecognised character | `error` |

### Formal Grammar for `num` Token

```text
digit              → [0-9]
digits             → digit digit*
optional-fraction  → ( . digits ) | ε
optional-exponent  → ( E ( + | - | ε ) digits ) | ε
num                → digits optional-fraction optional-exponent

```

This covers three numeric formats:

* **Integer:** `42`, `100`
* **Float:** `3.14`, `0.5`
* **Scientific notation:** `1.5E+10`, `2E3`, `1.0E-5`

---

## 🔍 Phase 2 — Table-Driven LL(1) Parser

**Class:** `Part2.Parser`

Implements a **predictive (LL(1)) parser** using an explicit stack and an external parse table — the standard table-driven approach from compiler theory.

### How It Works

1. Load token stream from `output.txt`.
2. Load LL(1) parse table from `parseTableCformatted.txt`.
3. Load grammar productions from `grammarC++.txt`.
4. Initialize stack: `[$, Program]` (`$` = end marker).
5. For each input token:
* If stack top is **terminal** → match with input token.
* If stack top is **non-terminal** → look up parse table, push RHS production symbols in reverse order.
* If production is **epsilon ($\epsilon$)** → skip (pop only).
* If no valid production found → **PARSE ERROR**.


6. Accept if stack and input are both exhausted.

* **Grammar Entry Point:** `startSymbol = "Program"` — top-level non-terminal representing a complete C++ program unit.
* **Terminal Detection:** Terminals are identified by a lowercase first character; non-terminals by an uppercase first character.

### External Input Files

| File | Format | Purpose |
| --- | --- | --- |
| `input.txt` | C++ source | Source program to compile |
| `parseTableCformatted.txt` | Tab-separated rows | LL(1) parse table — rows = non-terminals, columns = terminals |
| `grammarC++.txt` | `N. LHS --> RHS` format | CFG grammar productions referenced by parse table |

---

## 📋 Output Examples

### Successful Parse

```text
*****Parsing*****
    successfully parsed input

```

> **Task specification output:** *"The program is parsed successfully"*

### Syntax Error

```text
*****Parsing*****
    parse error  :: At Line#3

```

> **Task specification output:** *"Syntax Error: Line # 3"*

### Lexer Console Output (per line)

```text
Line#1
KeyWord : int
Identifier : x
assignop : =
Number : 42
; : ;
    --------------------

```

---

## 📐 Context-Free Grammar (28 Productions)

The full CFG covers a meaningful C++ subset including function declarations, variable declarations, control flow, and expressions:

```text
1.  Program          --> FunDeclList
2.  VarDeclList      --> VarDeclListTail | ε
3.  VarDeclListTail  --> VarDecl VarDeclListTail'
4.  VarDeclListTail' --> VarDeclListTail | ε
5.  VarDecl          --> Type VarList ;
6.  VarList          --> identifier VarList'
7.  VarList'         --> , VarList | ε
8.  FunDeclList      --> FunDecl FunDeclList'
9.  FunDeclList'     --> FunDeclList | ε
10. FunDecl          --> Type identifier ( ParamDeclList ) Block
11. ParamDeclList    --> ParamDeclListTail | ε
12. ParamDeclListTail--> ParamDecl ParamDeclListTail'
13. ParamDeclListTail'-> , ParamDeclListTail | ε
14. ParamDecl        --> Type identifier
15. Block            --> { VarDeclList StmtList }
16. Type             --> int | float | void
17. StmtList         --> Stmt StmtList'
18. StmtList'        --> StmtList | ε
19. Stmt             --> ;
                       | Expr ;
                       | return Expr ;
                       | break ;
                       | continue ;
                       | if ( Expr ) Stmt Stmt'
                       | while ( Expr ) Stmt
                       | Block
20. Stmt'            --> else Stmt | ε
21. Expr             --> identifier Primary
                       | UnaryOp Expr Expr'
                       | num Expr'
22. Expr'            --> BinOp Expr Expr' | ε
23. Primary          --> ( ExprList ) Expr'
                       | = Expr Expr'
                       | Expr'
                       | ε
24. ExprList         --> ExprListTail | ε
25. ExprListTail     --> Expr ExprListTail'
26. ExprListTail'    --> , ExprListTail | ε
27. UnaryOp          --> addOp | not
28. BinOp            --> addOp | mulOp | relOp | and | or

```

*Left recursion eliminated throughout using prime (`'`) non-terminals — a required transformation for top-down LL(1) parsing.*

---

## 🧪 Sample Input / Output

### Input (`input.txt`) — C++ source

```cpp
void add(int a, float b)
{
    int sum;
    sum = 0;
    while(sum <= 50000 || a<6)
    {
        sum = sum - 10.43 * 34E4;
        if (sum == 4000)
            break;
    }
}
void main()
{
    int sum;
    sum = 0;
    while(sum == 23E5)
    {
        sum = sum + 10.43 + 34E4 + 45.34E-4 + E43 + 34;
    }
}

```

### Output (`output.txt`) — Token stream written by Lexer

```text
void identifier ( int identifier , float identifier )
{
int identifier ;
identifier = num ;
while ( identifier relOp num or identifier relOp num )
{
identifier = identifier addOp num mulOp num ;
if ( identifier relOp num )
break ;
}
}
void identifier ( )
{
int identifier ;
identifier = num ;
while ( identifier relOp num )
{
identifier = identifier addOp num addOp num addOp num addOp identifier addOp num ;
}
}

```

### 🔎 Notable Edge Case — `E43`

In the input, `E43` appears to look like scientific notation. However, since it **starts with a letter**, the lexer correctly classifies it as an `identifier`, not a `num`:

```text
// Input:  sum + 10.43 + 34E4 + 45.34E-4 + E43 + 34
// Output: identifier addOp num addOp num addOp num addOp identifier addOp num
//                                                              ^^^
//                                               E43 → identifier (not num)

```

This demonstrates correct DFA boundary detection — the lexer reads character-by-character and applies formal token rules strictly.

---

## 📊 LL(1) Parse Table

The parse table (`parseTableCformatted.txt`) is tab-separated with:

* **Rows:** Non-terminals (`Program`, `VarDeclList`, `FunDecl`, `Stmt`, `Expr`, etc.)
* **Columns:** 24 terminals (`identifier`, `int`, `float`, `void`, `return`, `break`, `continue`, `if`, `while`, `else`, `num`, `relOp`, `addOp`, `mulOp`, `not`, `and`, `or`, `=`, `(`, `)`, `{`, `}`, `;`, `,`, `$`)
* **Entries:** Production number (e.g., `10`), `epsilon`, or empty (error).

---

## 🛠️ Technical Concepts

* **Deterministic Finite Automaton (DFA):** Lexer uses character-by-character state logic to recognize token boundaries.
* **Formal Language Theory:** Token rules follow formal language definitions for identifiers, numbers, and operators.
* **Context-Free Grammar (CFG):** Parser grammar loaded from external configuration files.
* **LL(1) Parsing:** Top-down, left-to-right, leftmost derivation with 1 token lookahead.
* **Predictive Parse Table:** Maps `(non-terminal, terminal)` pairs to grammar production numbers.
* **Explicit Stack:** Replaces the implicit call stack of recursive descent parsers.

---

## ▶️ Build & Run

**Requirements:** Java JDK 8+, NetBeans IDE (or terminal execution).

```bash
# Compile both packages from project root
javac src/part1/Lexical_Analyser.java
javac src/Part2/Parser.java

# Run the parser (automatically invokes the lexer first)
java -cp src Part2.Parser

```

*Ensure `input.txt`, `parseTableCformatted.txt`, and `grammarC++.txt` are present in the working directory before running.*

---

## 📚 Project Context

| Detail | Info |
| --- | --- |
| **Course** | Compiler Construction |
| **Degree** | B.Sc. Computer Science |
| **Institution** | University of Lahore, Pakistan |
| **Year** | 2015 |
| **Language** | Java (JDK 8) |
