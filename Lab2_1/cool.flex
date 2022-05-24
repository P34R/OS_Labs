/*
 *  The scanner definition for COOL.
 */

/*
 *  Stuff enclosed in %{ %} in the first section is copied verbatim to the
 *  output, so headers and global definitions are placed here to be visible
 * to the code in the file.  Don't remove anything that was here initially
 */
%{
#include <cool-parse.h>
#include <stringtab.h>
#include <utilities.h>
#include <string.h>

/* The compiler assumes these identifiers. */
#define yylval cool_yylval
#define yylex  cool_yylex

/* Max size of string constants */
#define MAX_STR_CONST 1025
#define YY_NO_UNPUT   /* keep g++ happy */

extern FILE *fin; /* we read from this file */

/* define YY_INPUT so we read from the FILE fin:
 * This change makes it possible to use this scanner in
 * the Cool compiler.
 */
#undef YY_INPUT
#define YY_INPUT(buf,result,max_size) \
    if ( (result = fread( (char*)buf, sizeof(char), max_size, fin)) < 0) \
        YY_FATAL_ERROR( "read() in flex scanner failed");

char string_buf[MAX_STR_CONST]; /* to assemble string constants */
char *string_buf_ptr;

extern int curr_lineno;
extern int verbose_flag;

extern YYSTYPE cool_yylval;

/*
 *  Add Your own definitions here
 */

int comment_counter=0;
int str_leng=0;

%}

/*
 * Define names for regular expressions here.
 */

%x COMMENT
%x SIMPLE_COMMENT
%x STRING
%x STR_SKIP

DIGIT [0-9]

/* keywords */
CLASS         (?i:class)
ELSE         (?i:else)
FI             (?i:fi)
IF             (?i:if)
IN             (?i:in)
INHERITS     (?i:inherits)
ISVOID         (?i:isvoid)
LET         (?i:let)
LOOP         (?i:loop)
POOL         (?i:pool)
THEN         (?i:then)
WHILE         (?i:while)
CASE         (?i:case)
ESAC         (?i:esac)
NEW         (?i:new)
OF             (?i:of)
NOT         (?i:not)

OBJECTID    ([a-z][a-zA-Z0-9_]*)
TYPEID        ([A-Z][a-zA-Z0-9_]*)

FALSE         (f)(?i:alse)
TRUE         (t)(?i:rue)

DARROW          "=>"
LE                 "<="
ASSIGN            "<-"

WHITESPACE     (" "|\f|\r|\t|\v)
%%

 /*
  *  Nested comments
  */
<INITIAL,COMMENT>"(*" {
    comment_counter++;
    BEGIN COMMENT;
}

<COMMENT>. { }

<COMMENT>\n { curr_lineno++; }

<COMMENT>"*)" {
    comment_counter--;
    if (comment_counter==0){
        BEGIN 0;
    }
}

<COMMENT><<EOF>> {
    yylval.error_msg="EOF in comment";
    BEGIN 0;
    return ERROR;
}

"--" {BEGIN SIMPLE_COMMENT; }

<SIMPLE_COMMENT>\n {
    curr_lineno++;
    BEGIN 0;
}

<SIMPLE_COMMENT>. { }

"\"" {
    memset(string_buf,0,MAX_STR_CONST);
    string_buf_ptr=string_buf;
    BEGIN STRING;
}

<STRING>{

\" {
        string_buf_ptr= (char*) &string_buf;
	cool_yylval.symbol = stringtable.add_string(string_buf_ptr,str_leng);
	str_leng=0;
	BEGIN 0;
	return STR_CONST;
}

<<EOF>> {
    cool_yylval.error_msg = "EOF in string constant";
	BEGIN 0;
	return ERROR;
}


\0 {
    cool_yylval.error_msg = "String contains null character";
	str_leng=0;
	BEGIN STR_SKIP;
	return ERROR;
}

\n {
    cool_yylval.error_msg = "Unterminated string constant";
	str_leng=0;
	curr_lineno++;
	BEGIN 0;
	return ERROR;
}

"\\n" {
    if (str_leng+1>=MAX_STR_CONST){
	    cool_yylval.error_msg = "String constant too long";
		str_leng=0;
		BEGIN STR_SKIP;
		return ERROR;
	}
	*string_buf_ptr++ = '\n';
	str_leng++;
}

"\\b" {
    if (str_leng+1>=MAX_STR_CONST){
	    cool_yylval.error_msg = "String constant too long";
		str_leng=0;
		BEGIN STR_SKIP;
		return ERROR;
	}
	*string_buf_ptr++ = '\b';
	str_leng++;
}

"\\t" {
    if (str_leng+1>=MAX_STR_CONST){
	    cool_yylval.error_msg = "String constant too long";
		str_leng=0;
		BEGIN STR_SKIP;
		return ERROR;
	}
	*string_buf_ptr++ = '\t';
	str_leng++;
}


"\\f" {
    if (str_leng+1>=MAX_STR_CONST){
	    cool_yylval.error_msg = "String constant too long";
		str_leng=0;
		BEGIN STR_SKIP;
		return ERROR;
	}
	*string_buf_ptr++ = '\f';
	str_leng++;
}

"\\"[^\0] {
    if (str_leng+1>=MAX_STR_CONST){
	    cool_yylval.error_msg = "String constant too long";
		str_leng=0;
		BEGIN STR_SKIP;
		return ERROR;
	}
    *string_buf_ptr++ = yytext[1];
    str_leng++;
}

. {
    if (str_leng+1>=MAX_STR_CONST){
	    cool_yylval.error_msg = "String constant too long";
		str_leng=0;
		BEGIN STR_SKIP;
		return ERROR;
	}
	*string_buf_ptr++ = yytext[0];
	str_leng++;
}
}

<STR_SKIP>[\n|"] {BEGIN 0;}
<STR_SKIP>[^\n|"] { }

"*)" {
    yylval.error_msg="Unmatched *)";
    return ERROR;
}


 /*
  *  The multiple-character operators.
  */

{CLASS}            {return CLASS;}
{ELSE}             {return ELSE;}
{FI}               {return FI;}
{IF}               {return IF;}
{IN}               {return IN;}
{INHERITS}         {return INHERITS;}
{ISVOID}           {return ISVOID;}
{LET}              {return LET;}
{LOOP}             {return LOOP;}
{POOL}             {return POOL;}
{THEN}             {return THEN;}
{WHILE}            {return WHILE;}
{CASE}             {return CASE;}
{ESAC}             {return ESAC;}
{NEW}              {return NEW;}
{OF}               {return OF;}
{NOT}              {return NOT;}

{DARROW}           {return DARROW;}
{LE}               {return LE;}
{ASSIGN}           {return ASSIGN;}
 /*
  * Keywords are case-insensitive except for the values true and false,
  * which must begin with a lower-case letter.
  */


 /*
  *  String constants (C syntax)
  *  Escape sequence \c is accepted for all characters c. Except for 
  *  \n \t \b \f, the result is c.
  *
  */

{DIGIT}+ {
    cool_yylval.symbol=inttable.add_string(yytext);
    return INT_CONST;
}

{TRUE} {
    cool_yylval.boolean=true;
    return BOOL_CONST;
}
{FALSE} {
    cool_yylval.boolean=false;
    return BOOL_CONST;
}

\n { curr_lineno++; }

{WHITESPACE}+ { }

{OBJECTID} {
    cool_yylval.symbol=idtable.add_string(yytext);
    return OBJECTID;
}

{TYPEID} {
    cool_yylval.symbol=idtable.add_string(yytext);
    return TYPEID;
}

"+"        { return int('+'); }

"-"        { return int('-'); }

"/"        { return int('/'); }

"*"        { return int('*'); }

"="        { return int('='); }

";"        { return int(';'); }

"{"        { return int('{'); }

"}"        { return int('}'); }

"("        { return int('('); }

")"        { return int(')'); }

":"        { return int(':'); }

"."        { return int('.'); }

","        { return int(','); }

"~"        { return int('~'); }

"@"        { return int('@'); }

"<"        { return int('<'); }

. {
    cool_yylval.error_msg=yytext;
    return ERROR;
}

%%