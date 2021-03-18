// Here we will define our Pirate Calligraphy language.
grammar Pirate_Calligraphy;

/*
    Parser Rules
*/

start:
    methodDeclarationStatement*
    BEGIN?
        statement*
    END?
    EOF;

statement
    : ifStatement
    | whileStatement
    | forStatement
    | variableDeclaration
    | printStatement
    | variableReDeclaration
    | expression
    ;

// Statements
returnStatement: RETURN expression SEMICOLON;
ifStatement: IF PAREN_OPEN expression PAREN_CLOSE BRACKET_OPEN statement+ BRACKET_CLOSE elseIfStatement* elseStatement?;
elseIfStatement: IF ELSE PAREN_OPEN expression PAREN_CLOSE BRACKET_OPEN statement+ BRACKET_CLOSE;
elseStatement: ELSE BRACKET_OPEN statement+ BRACKET_CLOSE;
whileStatement: WHILE PAREN_OPEN expression PAREN_CLOSE BRACKET_OPEN statement+ BRACKET_CLOSE;

forStatement:
    FOR PAREN_OPEN
        incremental=variableDeclaration?
        compareStatement SEMICOLON
        exp=expression?
    PAREN_CLOSE
    BRACKET_OPEN
        statement+
    BRACKET_CLOSE;

variableDeclaration: (INT | DOUBLE | STRING| BOOLEAN) ID IS expression SEMICOLON | (INT | DOUBLE | STRING| BOOLEAN) ID SEMICOLON;
variableReDeclaration: ID IS expression SEMICOLON;
printStatement: PRINT PAREN_OPEN expression PAREN_CLOSE SEMICOLON;

methodDeclarationStatement
    : PRIVACY returntype=(INT|DOUBLE|STRING|VOID) METHOD methodName=ID
      PAREN_OPEN parameters? PAREN_CLOSE
      BRACKET_OPEN statement* returnStatement? BRACKET_CLOSE;

parameters: parameter (',' parameter)*;
parameter: (type=(INT|DOUBLE|STRING|BOOLEAN) ID | (expression));


expression
    : BOOLEAN #boolean
    | PAREN_OPEN expression PAREN_CLOSE #parentheses
    | left=expression MULTIPLY right=expression #multiply
    | left=expression DIVIDE right=expression #divide
    | left=expression ADD right=expression #add
    | left=expression SUBSTRACT right=expression #substract
    | ID ADD ADD SEMICOLON? #addOne
    | ID SUBSTRACT SUBSTRACT SEMICOLON? #substractOne
    | left=expression op=(EQUALS | NOT_EQUALS | LARGER | SMALLER) right=expression #compare
    | left=expression op=(OR | AND) right=expression #OrAnd
    | DOUBLE_VALUE #double
    | INT_VALUE #int
    | ID #variableName
    | ID_HOOKS #id
    | ID PAREN_OPEN parameters? PAREN_CLOSE SEMICOLON? #methodCall
    ;

compareStatement: left=expression op=(EQUALS | NOT_EQUALS | LARGER | SMALLER) right=expression;
PRIVACY
    : PUBLIC
    | PROTECTED
    | PRIVATE
    ;

    //COMMENT: '#' WS? ID? WS? INT_VALUE? WS? DOUBLE_VALUE? WS? -> skip;

/*
    Lexer Rules
*/

WS: [ \t\r\n]+ -> skip;

BOOLEAN
    : TRUE
    | FALSE
    ;

RETURN: 'give';
BEGIN: 'SAIL';
END: 'RUM';
SMALLER: 'below';
LARGER: 'above';
INT: 'number';
DOUBLE: 'duplicate';
METHOD: 'objective';
VOID: 'nothing';
FLOAT: 'hover';
STRING: 'rope';
IF: 'assuming';
ELSE: 'extra';
WHILE: 'meantime';
PRINT: 'say';
FOR: 'fer';
ID: [A-Za-z]+ [0-9_]*;
ID_HOOKS:  '"' ([A-Za-z0-9_!?,`' ])* '"';
IS: '~';
EQUALS: IS IS;
NOT_EQUALS: NOT IS;

INT_VALUE
    : '0'
    | NONZERO_INT
    | NONZERO_INT+ '0'?
    | '-' NONZERO_INT '0'?
    ;

NONZERO_INT: [1-9]+;

DOUBLE_VALUE: INT_VALUE+ DOT INT_VALUE+
                  | DOT INT_VALUE+
                  ;

SEMICOLON: '$';
PAREN_OPEN: '<';
PAREN_CLOSE: '>';
BRACKET_OPEN: '[';
BRACKET_CLOSE: ']';
NOT: '!';
PUBLIC: 'loud';
PROTECTED: 'covered';
PRIVATE: 'quite';
CLASS: 'crew';
TRUE: 'aye';
FALSE: 'nay';
ADD: '+';
SUBSTRACT: '-';
MULTIPLY: '*';
DIVIDE: '/';
DOT: '.';
OR: '||';
AND: '&&';
