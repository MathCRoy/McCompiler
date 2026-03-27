import LexicalAnalysis.Lexer;
import LexicalAnalysis.Token;
import LexicalAnalysis.TokenType;

public class Main {
    public static void main(String[] args) {
        String source = "a+2.3";
        Lexer lexer = new Lexer(source);
        Token token;
        do {
            token = lexer.getNextToken();
            System.out.println(token);
        } while (token.tokenType() != TokenType.EOF);
    }
}
