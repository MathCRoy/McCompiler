# McCompiler — CLAUDE.md

## Project Overview

McCompiler is a from-scratch compiler implementation following the structure and theory laid out in the **Dragon Book** (*Compilers: Principles, Techniques, and Tools*, 2nd ed., Aho, Lam, Sethi, Ullman).

The goal is a complete, well-structured compiler pipeline — from raw source text to target code — built phase by phase as described in the book.

## Compiler Pipeline (Dragon Book phases)

Implement in this order:

1. **Lexical Analysis (Scanning)** — tokenizer/lexer, DFA-based, regex → NFA → DFA construction
2. **Syntax Analysis (Parsing)** — context-free grammars, parse trees; prefer LL or LR parsing (SLR, LALR(1), LR(1))
3. **Semantic Analysis** — symbol tables, type checking, scope resolution, attribute grammars
4. **Intermediate Code Generation** — three-address code (TAC) or similar IR (quadruples, triples)
5. **Code Optimization** — basic blocks, flow graphs, data-flow analysis, constant folding, dead code elimination
6. **Code Generation** — target machine code or assembly; register allocation, instruction selection

## Architecture Principles

- Each phase is a distinct module with a clean interface
- Phases communicate via well-defined data structures (token stream, AST, IR, CFG)
- Prefer immutable/value-based IR representations where practical
- Keep the theory visible in the code — name things after the book's terminology (e.g., `NFA`, `DFA`, `ProductionRule`, `SymbolTable`, `BasicBlock`, `FlowGraph`)

## Key Data Structures

- `Token` — type + lexeme + source location
- `AST` / parse tree nodes
- `Symbol` / `SymbolTable` with scoped environments
- `ThreeAddressCode` / `Quadruple` for IR
- `BasicBlock` + `FlowGraph` for optimization
- `Instruction` for target code

## Conventions

- **Language**: Java 21 (LTS). Use records for immutable data, sealed classes + pattern matching for tree structures, enums for token/node types.
- **Target**: LLVM IR — compiler stops at TAC/IR generation, LLVM handles optimization and native code generation
- All source files live under `LexicalAnalysis/`, `SyntaxAnalysis/`, `SemanticAnalysis/`, etc. (flat for now, restructure if needed)
- Tests live under `tests/`, with subdirectories mirroring `src/`
- No external parser generators (no yacc/bison/ANTLR) — build from first principles
- Grammar rules should be documented alongside the parser code

## Reference

The Dragon Book PDF is in the repo root. Page references in comments are welcome where theory maps directly to code.

## Student Profile

- Java is their best language, not expert level
- Previous course (IFT-3101) covered the full pipeline in C# using teacher-provided libraries — touched all phases but shaky on internals
- Learns well by writing code first, then discussing
- Prefers concise naming: `ID`, `OP`, `EOS` over `IDENTIFIER`, `OPERATOR`, `END_STATEMENT`

## Progress

### Session 1 — Token foundations
- Confirmed understanding: token = type + lexeme; whitespace/comments detected but discarded
- Clarified: lexer vs parser vs semantic analysis (syntax vs meaning)
- Built `LexicalAnalysis/TokenType.java` — enum: `KEYWORD, ID, OP, LIT_INT, LIT_DOUBLE, LIT_CHAR, LIT_STRING, LIT_BOOL, EOS, EOF`
- Built `LexicalAnalysis/Token.java` — record with `TokenType type, String lexeme`
- Known issue: field typo `tokenTypek` in `Token.java`

## Next Step

Build `LexicalAnalysis/Lexer.java`:
- Input: raw source `String`
- Output: `List<Token>`
- Walks source character by character, groups into lexemes
- Start by asking: how does the lexer know where one token ends and the next begins?

## Notes for Claude

- **Role: teacher, not programmer.** Guide the user through concepts, explain theory, ask questions, and let the user write the code. Do not write implementation code unless explicitly asked.
- Explain the Dragon Book concepts behind each task before diving into specifics
- When in doubt about theory, ask rather than guess
- Prefer Socratic guidance — point toward the answer, don't just give it
