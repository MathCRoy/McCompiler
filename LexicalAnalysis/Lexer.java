package LexicalAnalysis;

import java.util.Optional;

import Exceptions.TokenGenerationException;

public class Lexer {

    int startPosition;
    int currentPosition;
    int EOF;
    TokenMap tokenMap;
    String code;

    public Lexer(String code){
        this.startPosition = 0;
        this.currentPosition = 0;
        this.EOF = code.length();
        this.code = code;
        tokenMap = TokenMap.getInstance();
    }

    public Token getNextToken() throws TokenGenerationException{

        startPosition = currentPosition;

        return getEofToken()
        .or(this::getWhitespaceToken)
        .or(this::getEosToken)
        .or(this::getIntOrDoubleToken)
        .or(this::getCharToken)
        .or(this::getStringToken)
        .or(this::getKeywordBoolOrIdToken)
        .or(this::getOperatorToken)
        .orElseThrow(() -> new TokenGenerationException(code.substring(startPosition,currentPosition)));

    }

    private Optional<Token> getEofToken() {
        if (startPosition == EOF)
            return Optional.of(new Token(TokenType.EOF, ""));
        return Optional.empty();
    }

    private Optional<Token> getWhitespaceToken() {
        if (code.charAt(currentPosition) == ' ') {
            currentPosition++;
            startPosition++;
            return Optional.of(getNextToken());
        }
        return Optional.empty();
    }

    private Optional<Token> getEosToken() {
        if (code.charAt(currentPosition) == ';')
            return Optional.of(new Token(TokenType.EOS, ""));
        return Optional.empty();
    }

    private Optional<Token> getIntOrDoubleToken(){
        if (Character.isDigit(code.charAt(currentPosition))){

            do {
                currentPosition++;
            } while (currentPosition < EOF && Character.isDigit(code.charAt(currentPosition)));

            if(!(code.charAt(currentPosition) == '.')) {
                return Optional.of(new Token(TokenType.LIT_INT,code.substring(startPosition,currentPosition)));
            } else {

                do {
                    currentPosition++;
                } while (currentPosition < EOF && Character.isDigit(code.charAt(currentPosition)));

                return Optional.of(new Token(TokenType.LIT_DOUBLE,code.substring(startPosition,currentPosition)));

            }
        }
        return Optional.empty();
    }

    private Optional<Token> getCharToken() throws TokenGenerationException{
        if (code.charAt(currentPosition) == '\'') {
            currentPosition++;
            if (currentPosition < EOF){
                currentPosition++;
                if (currentPosition < EOF && code.charAt(currentPosition) == '\'') {
                    return Optional.of(new Token(TokenType.LIT_CHAR,Character.toString(code.charAt(currentPosition-1))));
                } else {
                    throw new TokenGenerationException(code.substring(startPosition,currentPosition));
                }
            } else {
                throw new TokenGenerationException(code.substring(startPosition,currentPosition));
            }
        }
        return Optional.empty();

    }

    private Optional<Token> getStringToken() throws TokenGenerationException{
        if (code.charAt(currentPosition) == '\"') {
            do {
                currentPosition++;
            } while (currentPosition < EOF && !(code.charAt(currentPosition) == '\"'));

            if (!(code.charAt(currentPosition) == '\"'))
                throw new TokenGenerationException(code.substring(startPosition,currentPosition));

            currentPosition++;
            return Optional.of(new Token(TokenType.LIT_STRING,code.substring(startPosition+1,currentPosition-1)));

        }

        return Optional.empty();
    }

    private Optional<Token> getKeywordBoolOrIdToken() {
        if (Character.isLetter(code.charAt(currentPosition))) {
            do {
                currentPosition++;
            } while (currentPosition < EOF && (Character.isDigit(code.charAt(currentPosition)) || Character.isLetter(code.charAt(currentPosition))));

            String tokenText = code.substring(startPosition,currentPosition);
            TokenType tokenType = tokenMap.getTokenType(tokenText);

            if (tokenType != null){
                return Optional.of(new Token(tokenType,tokenText));
            } else {
                return Optional.of(new Token(TokenType.ID,tokenText));
            }
        }
        return Optional.empty();
    }

    private Optional<Token> getOperatorToken() {
        if (tokenMap.isOperatorCharacter(code.charAt(currentPosition))) {
            currentPosition++;
            if (currentPosition < EOF && tokenMap.isOperatorCharacter(code.charAt(currentPosition)))
                currentPosition++;

            String tokenText = code.substring(startPosition,currentPosition);
            TokenType tokenType = tokenMap.getTokenType(tokenText);

            if (tokenType != null){
                return Optional.of(new Token(tokenType,tokenText));
            } else {
                currentPosition--;
                tokenText = code.substring(startPosition, currentPosition);
                tokenType = tokenMap.getTokenType(tokenText);
                if (tokenType != null)
                    return Optional.of(new Token(tokenType,tokenText));
            }

        }
        return Optional.empty();
    }
}
