# Methods
## Call
<code>$TYPE_CALL ${METHOD_NAME}(${METHOD_ARGS})</code>, where $TYPE_CALL can be one of the two: SCR_CALL,
for a method within the script, or NUS_CALL, for a call to a method of the language. If it is neither of these,
a <code>WrongMethodTypeException</code> will be thrown
##### Important: Only one command per line. No more. No less.
## Declaration
<code>method $METHOD_NAME()<br/>
{<br/>
$METHOD_CODE<br/>
}</code>
<br>
The <code>$METHOD_CODE</code> may be formatted with tabs
<code>$METHOD_ARGS</code> can be accessed with <code>$0, $1, $2, $3</code> and so on

### All scripts should define a main method like so:
<code>method main()<br/>
{<br/>
$CODE_HERE<br/>
}</code>

# Conditions
###### Not implemented yet
Syntax will be same as in Linux BASH<br>
<code>if [[ $CONDITION ]] ;<br/>
then $CODE<br/>
else $CODE<br/>
fi</code>
