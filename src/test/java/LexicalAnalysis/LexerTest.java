package LexicalAnalysis;

import static org.junit.jupiter.api.Assertions.*;

import Exceptions.TokenGenerationException;
import java.util.List;
import org.junit.jupiter.api.Test;

public class LexerTest {

  private List<Token> lex(String input) {
    Lexer lexer = new Lexer(input);
    List<Token> tokens = new java.util.ArrayList<>();
    Token token;
    do {
      token = lexer.getNextToken();
      tokens.add(token);
    } while (token.tokenType() != TokenType.EOF);
    return tokens;
  }

  // --- EOF ---

  @Test
  void emptyInputReturnsEof() {
    List<Token> tokens = lex("");
    assertEquals(1, tokens.size());
    assertEquals(TokenType.EOF, tokens.get(0).tokenType());
  }

  // --- EOS ---

  @Test
  void semicolonReturnsEos() {
    List<Token> tokens = lex(";");
    assertEquals(TokenType.EOS, tokens.get(0).tokenType());
  }

  // --- LIT_INT ---

  @Test
  void singleDigitInt() {
    List<Token> tokens = lex("7");
    assertEquals(TokenType.LIT_INT, tokens.get(0).tokenType());
    assertEquals("7", tokens.get(0).lexeme());
  }

  @Test
  void multiDigitInt() {
    List<Token> tokens = lex("1234");
    assertEquals(TokenType.LIT_INT, tokens.get(0).tokenType());
    assertEquals("1234", tokens.get(0).lexeme());
  }

  // --- LIT_DOUBLE ---

  @Test
  void doubleWithDecimal() {
    List<Token> tokens = lex("3.14");
    assertEquals(TokenType.LIT_DOUBLE, tokens.get(0).tokenType());
    assertEquals("3.14", tokens.get(0).lexeme());
  }

  @Test
  void doubleWithZeroDecimal() {
    List<Token> tokens = lex("0.0");
    assertEquals(TokenType.LIT_DOUBLE, tokens.get(0).tokenType());
    assertEquals("0.0", tokens.get(0).lexeme());
  }

  // --- LIT_CHAR ---

  @Test
  void singleChar() {
    List<Token> tokens = lex("'a'");
    assertEquals(TokenType.LIT_CHAR, tokens.get(0).tokenType());
    assertEquals("a", tokens.get(0).lexeme());
  }

  @Test
  void unterminatedCharThrows() {
    assertThrows(TokenGenerationException.class, () -> lex("'a"));
  }

  // --- LIT_STRING ---

  @Test
  void emptyString() {
    List<Token> tokens = lex("\"\"");
    assertEquals(TokenType.LIT_STRING, tokens.get(0).tokenType());
    assertEquals("", tokens.get(0).lexeme());
  }

  @Test
  void simpleString() {
    List<Token> tokens = lex("\"hello\"");
    assertEquals(TokenType.LIT_STRING, tokens.get(0).tokenType());
    assertEquals("hello", tokens.get(0).lexeme());
  }

  @Test
  void unterminatedStringThrows() {
    assertThrows(TokenGenerationException.class, () -> lex("\"hello"));
  }

  // --- LIT_BOOL ---

  @Test
  void trueIsLitBool() {
    List<Token> tokens = lex("true");
    assertEquals(TokenType.LIT_BOOL, tokens.get(0).tokenType());
    assertEquals("true", tokens.get(0).lexeme());
  }

  @Test
  void falseIsLitBool() {
    List<Token> tokens = lex("false");
    assertEquals(TokenType.LIT_BOOL, tokens.get(0).tokenType());
    assertEquals("false", tokens.get(0).lexeme());
  }

  // --- KEYWORD ---

  @Test
  void ifIsKeyword() {
    List<Token> tokens = lex("if");
    assertEquals(TokenType.KEYWORD, tokens.get(0).tokenType());
  }

  @Test
  void whileIsKeyword() {
    List<Token> tokens = lex("while");
    assertEquals(TokenType.KEYWORD, tokens.get(0).tokenType());
  }

  // --- ID ---

  @Test
  void simpleIdentifier() {
    List<Token> tokens = lex("foo");
    assertEquals(TokenType.ID, tokens.get(0).tokenType());
    assertEquals("foo", tokens.get(0).lexeme());
  }

  @Test
  void identifierWithDigits() {
    List<Token> tokens = lex("x1");
    assertEquals(TokenType.ID, tokens.get(0).tokenType());
    assertEquals("x1", tokens.get(0).lexeme());
  }

  @Test
  void keywordPrefixIsNotKeyword() {
    List<Token> tokens = lex("iffy");
    assertEquals(TokenType.ID, tokens.get(0).tokenType());
    assertEquals("iffy", tokens.get(0).lexeme());
  }

  // --- OP ---

  @Test
  void singleCharOperator() {
    List<Token> tokens = lex("+");
    assertEquals(TokenType.OP, tokens.get(0).tokenType());
    assertEquals("+", tokens.get(0).lexeme());
  }

  @Test
  void doubleCharOperator() {
    List<Token> tokens = lex("==");
    assertEquals(TokenType.OP, tokens.get(0).tokenType());
    assertEquals("==", tokens.get(0).lexeme());
  }

  @Test
  void lessThanOrEqual() {
    List<Token> tokens = lex("<=");
    assertEquals(TokenType.OP, tokens.get(0).tokenType());
    assertEquals("<=", tokens.get(0).lexeme());
  }

  // --- Whitespace ---

  @Test
  void whitespaceIsSkipped() {
    List<Token> tokens = lex("   42");
    assertEquals(TokenType.LIT_INT, tokens.get(0).tokenType());
    assertEquals("42", tokens.get(0).lexeme());
  }

  // --- Multi-token sequences ---

  @Test
  void simpleAssignment() {
    List<Token> tokens = lex("x = 1;");
    assertEquals(TokenType.ID, tokens.get(0).tokenType());
    assertEquals(TokenType.OP, tokens.get(1).tokenType());
    assertEquals(TokenType.LIT_INT, tokens.get(2).tokenType());
    assertEquals(TokenType.EOS, tokens.get(3).tokenType());
    assertEquals(TokenType.EOF, tokens.get(4).tokenType());
  }

  @Test
  void ifCondition() {
    List<Token> tokens = lex("if x == 42");
    assertEquals(TokenType.KEYWORD, tokens.get(0).tokenType());
    assertEquals(TokenType.ID, tokens.get(1).tokenType());
    assertEquals(TokenType.OP, tokens.get(2).tokenType());
    assertEquals(TokenType.LIT_INT, tokens.get(3).tokenType());
  }

  @Test
  void unknownCharacterThrows() {
    assertThrows(TokenGenerationException.class, () -> lex("@"));
  }
}
