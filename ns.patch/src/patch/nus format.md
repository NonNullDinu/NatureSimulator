### Not stable now
# Interpreted NUS
## Methods
### Call
<code>$TYPE_CALL ${METHOD_NAME}(${METHOD_ARGS})</code>, where $TYPE_CALL can be one of the two: SCR_CALL,
for a method within the script, or NUS_CALL, for a call to a method of the language. If it is neither of these,
a <code>WrongMethodTypeException</code> will be thrown
##### Important: Only one command per line. No more. No less.
### Declaration
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

# LineByLineNUS
This is a special type of NUS that interprets code line by line.
It cannot have more than one statement per line
## Methods
Method calls are a little different than how they are in interpreted NUS, but only language calls can be made.
### Call
<code>NUS_CALL $METHOD_NAME(\[$ARGS])</code>

## Conditions
Conditions can be defined as follows(for now, only equality between strings is supported):<br>
<code>CONDITION $RIGHT == $LEFT</code>

To execute some code if the previous condition is true, use <code>ON_CONDITION CODE</code>.<br>
To execute some code if the previous condition is false, use <code>ON_NOT_CONDITION CODE</code>.

## Variable reference
Another difference from Interpreted NUS is how variables are referenced.<br>
This is a valid reference: <code>"#VAR_NAME"</code>

### Setting the values
Values of variables can be set so:
<code>VAR_SET VAR_NAME=VAR_VALUE</code>, where <code>VAR_NAME</code> is the variable name and the <code>VAR_VALUE</code> is the value that the variable will have.