# Needle — Regex Engine

A regex engine built from scratch in Java, based on Thompson NFA construction. It compiles a regex pattern into an AST, builds an NFA from that AST, and matches input via NFA simulation.

## Features

- Concatenation, alternation (`|`), and grouping (`(...)`)
- Quantifiers: `*`, `+`, `?`, `{n}`, `{n,}`, `{n,m}`
- Character classes: `[abc]`, `[a-z]`, negation `[^...]`
- Wildcard: `.`
- Escapes: `\` for matching special characters literally
- Three execution modes:
  - `exact` &rarr; the entire input must match the pattern
  - `contains` &rarr; any substring of the input must match
  - `grep` &rarr; line-by-line search across the given files

## Project Structure

- [src/main/java/Needle/Preprocessor.java](src/main/java/Needle/Preprocessor.java) — Resolves escapes and tags characters
- [src/main/java/Needle/Lexer.java](src/main/java/Needle/Lexer.java) — Token generation
- [src/main/java/Needle/Parser.java](src/main/java/Needle/Parser.java) — Recursive-descent parser, produces the AST
- [src/main/java/Needle/AstNode.java](src/main/java/Needle/AstNode.java) — AST node definitions
- [src/main/java/Needle/NfaBuilder.java](src/main/java/Needle/NfaBuilder.java) — Thompson NFA construction from the AST
- [src/main/java/Needle/NfaSimulation.java](src/main/java/Needle/NfaSimulation.java) — NFA simulation (the matching engine)
- [src/main/java/Needle/Core.java](src/main/java/Needle/Core.java) — High-level API for compile + match
- [src/main/java/Needle/Main.java](src/main/java/Needle/Main.java) — CLI entry point
- [src/main/java/Needle/cfg.md](src/main/java/Needle/cfg.md) — Supported grammar (BNF)

## Grammar

For the full supported grammar, see [src/main/java/Needle/cfg.md](src/main/java/Needle/cfg.md).

## Build

```powershell
.\gradlew.bat build
```

## Test

```powershell
.\gradlew.bat test
```

Test classes:

- [src/test/java/LexerTest.java](src/test/java/LexerTest.java)
- [src/test/java/AstPrinterTest.java](src/test/java/AstPrinterTest.java)
- [src/test/java/NfaSimulationTest.java](src/test/java/NfaSimulationTest.java)
- [src/test/java/SubstringSearchTest.java](src/test/java/SubstringSearchTest.java)

## Usage

CLI arguments:

| Argument                         | Description                                             |
|----------------------------------|---------------------------------------------------------|
| `--regex=<pattern>`              | Regex pattern                                           |
| `--input=<text>`                 | Input to match against (for `exact` / `contains` modes) |
| `--mode=<exact\|contains\|grep>` | Execution mode (default: `exact`)                       |
| `--files=f1,f2,...`              | Files to scan in `grep` mode                            |

### Examples

Exact match:

```powershell
java -jar needle.jar --regex="a(b|c)+" --input="abccb" --mode=exact
```

Substring search:

```powershell
java -jar needle.jar --regex="[0-9]{3}" --input="code: 421 ok" --mode=contains
```

Grep across files:

```powershell
java -jar needle.jar --regex="error" --mode=grep --files=log1.txt,log2.txt
```

## Architecture

```
regex string
    │
    ▼
Preprocessor  ── resolves escapes, produces a Pchar list
    │
    ▼
Lexer         ── token stream
    │
    ▼
Parser        ── AST
    │
    ▼
NfaBuilder    ── NFA via Thompson's construction
    │
    ▼
NfaSimulation ── simulates the NFA over the input
    │
    ▼
result (boolean / matching lines)
```
