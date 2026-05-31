Regex       &rarr; Alternation EOF

Alternation &rarr; Concatenation ("|" Concatenation)*

Concatenation &rarr; Repetition+

Repetition  &rarr; Atom Quantifier*

Quantifier  &rarr; "*"
| "+"
| "?"
| "{" Number "}"
| "{" Number "," "}"
| "{" Number "," Number "}"

Atom        &rarr; Literal
| "."
| CharClass
| Group
| Escape

Group       &rarr; "(" Alternation ")"

CharClass   &rarr; "[" "^"? CharClassItems "]"

CharClassItems &rarr; CharClassItem+

CharClassItem  &rarr; CharRange
| CharClassLiteral
| Escape

CharRange   &rarr; CharClassLiteral "-" CharClassLiteral

Literal     &rarr; any character except:
"(", ")", "[", "]", "{", "}", "|", "*", "+", "?", ".", "\"

Escape      &rarr; "\" any character

Number      &rarr; Digit+

Digit       &rarr; "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9"

EOF         &rarr; end of input