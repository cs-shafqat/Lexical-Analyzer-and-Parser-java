# Lexical Analyzer and Parser (Java) ⚙️

A Java-based compiler front-end implementation featuring a **Lexical Analyzer (Scanner)** and a **Syntax Parser** designed to tokenize and parse a subset of C++ source code.

---

## 🚀 Overview & Workflow

The pipeline runs in two distinct compiler phases:

1. **Lexical Analysis (Scanner):**
   - Ingests raw C++ source code.
   - Evaluates input character streams against formal regular expressions to generate a token stream.
   - Writes structured tokens to an intermediate file.

2. **Syntax Analysis (Parser):**
   - Reads tokens from the intermediate file.
   - Validates the token sequence against grammar rules.
   - Detects structural errors and terminates on the first syntax violation.

---

## 📑 Lexical Specifications & Token Rules

The scanner identifies and categorizes tokens (keywords, identifiers, numeric literals, operators, and delimiters) based on formal regular expressions and automata specs. 

📄 **[View Full Lexical Specification & Regular Expressions (PDF)](./README.pdf)**

---

## 🛠️ Key Features & Error Handling

- **Token Generation:** Automatically scans raw C++ source files and categorizes elements into valid language tokens.
- **Strict Error Detection:** Terminates execution immediately upon encountering the first syntax error.
- **Informative Diagnostic Messaging:** Pinpoints the exact location of structural invalidity by outputting the specific line number.

---

## 📋 Program Output Examples

### **Successful Parse**
When the input source code contains valid syntax:
```text
The program is parsed successfully

```

### **Syntax Error Encountered**

Execution halts on the first invalid token sequence, identifying the target line:

```text
Syntax Error: Line # 3

```

---

## 🛠️ Technical Stack & Concepts

* **Language:** Java (JDK 8+)
* **Core Concepts:** Compiler Design, Formal Languages, Deterministic Finite Automata (DFA), Regular Expressions, Context-Free Grammars (CFG), File I/O Streams.
