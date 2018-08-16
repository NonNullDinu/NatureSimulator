# Language
## Method
### Call
<code>$TYPE_CALL ${METHOD_NAME}(${METHOD_ARGS})</code>, where $TYPE_CALL can be one of the two: SCR_CALL,
for a method within the script, or NUS_CALL, for a call to a method of the language. If it is neither of these,
a <code>WrongMethodTypeException</code> will be thrown
##### Important: Only one command per line. No more. No less.
## Declaration
<pre>method $METHOD_NAME()
{
$METHOD_CODE
}</pre>
The <code>$METHOD_CODE</code> may be formatted with tabs<br />
<code>$METHOD_ARGS</code> can be accessed with <code>$0, $1, $2, $3</code> and so on

### All scripts should define a <code>main</code> method - this is the entry point in the script - like so:
<pre>method main()
{
$CODE_HERE
}</pre>

## If statements
###### Basic implementation(Only == for String and Integer types. Combining them will result the condition evaluated to false). Can be nested, but that might crash
Syntax will be same as in Linux BASH<br />
<pre>if [[ $CONDITION ]] ; 
then $CODE
else $CODE
fi</pre>
Can also be defined as:
<pre>if [[ $CONDITION ]];
then $CODE
else $CODE
fi</pre>
or
<pre>if [[ $CONDITION ]] ;
then $CODE
else $CODE
fi</pre>

# Interpreter syntax analyze mechanism
###### This section is made so that you can understand better how the interpreter works if you want to commit to it, or simply to look and see how this interpreter does his job
## Methods breakdown mechanism
This is how a method looks like:
<pre>method $METHOD_NAME()
{
$METHOD_CODE
}
</pre>

This is how the interpreter sees it:
<pre>
MethodDeclarationToken: method $METHOD_NAME()
MethodBodyToken: rest of the method(untill -and including- '}')
</pre>
The <code>MethodBodyToken</code> is analyzed. If it is a call to another method (begins with <code>NUS_CALL</code> or <code>SCR_CALL</code>),
then it is evaluated as a <code>MethodCallToken</code>.The <code>MethodCallToken</code> has the function <code>execute(Token[] args)</code> which executes the method the <code>MethodCallToken</code> references to.

## If statements
This is how an if statement looks like:
<pre>if [[ $CONDITION ]] ;
then $CODE
else $CODE
fi</pre>



This is how the interpreter sees it:
<pre>$DECLARATION_OF_IF_STATEMENT(if) $CONDITION_BEGIN([[) $CONDITION $CONDITION_END(]]) ;
then $CONDITION_TRUE_TOKEN
else $CONDITION_FALSE_TOKEN
$END_DECLARATION(fi)</pre>
