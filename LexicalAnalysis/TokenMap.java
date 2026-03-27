package LexicalAnalysis;

import java.util.HashMap;
import java.util.Map;

public class TokenMap {

    private static final TokenMap INSTANCE = new TokenMap();
    private final Map<String, TokenType> keyMap; 
    private final String operators = "+-*/%=<>!&|"; 

    private TokenMap(){

       Map<String, TokenType> map = new HashMap<>(); 
       map.put("true", TokenType.LIT_BOOL);
       map.put("false", TokenType.LIT_BOOL);
       map.put("if", TokenType.KEYWORD);
       map.put("else", TokenType.KEYWORD);
       map.put("do", TokenType.KEYWORD);
       map.put("while", TokenType.KEYWORD);
       map.put("+", TokenType.OP);
       map.put("-", TokenType.OP);
       map.put("/", TokenType.OP);
       map.put("*", TokenType.OP);
       map.put("&", TokenType.OP);
       map.put("|", TokenType.OP);
       map.put("=", TokenType.OP);
       map.put("==", TokenType.OP);
       map.put("<", TokenType.OP);
       map.put("<=", TokenType.OP);
       map.put(">", TokenType.OP);
       map.put(">=", TokenType.OP);
       map.put("!", TokenType.OP);
       this.keyMap = Map.copyOf(map);

    }

    public static TokenMap getInstance() {return INSTANCE;}
    public TokenType getTokenType(String tokenValue){
        return keyMap.get(tokenValue);
    }

    public boolean isOperatorCharacter(char character){
        return "+-*/%=<>!&|".indexOf(character) != -1;
    }
    
}
