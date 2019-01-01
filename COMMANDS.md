# In-game console comands
Meaning of simbols are as follows<br/>
<> - mandatory<br/>
[] - optional<br/>
$sarg* - string argument (referenced as arg*)<br/>
$narg* - numeric argument (same as sarg*, referenced as arg*)<br/>

## SYNTAX
`action-identifier arg...`
Command structure is as follows:
	1.`Action identifier`. Is a structure of only one word.
	If there are multiple words they will be separated by `-`<br/>
	2.`Action arguments`. Can be either string arguments or numeric arguments

IMPORTANT: No modification to the `action identifier` is allowed if not specified with optional part.

## Time operations commands
### show-time
    This command is used to show the time.
    The format that it uses is:
    Simulation seconds since begun: $TIME;
    days passed in simulation $DAY_COUNT

### time-rate <$narg1>
    This command sets the time rate to latexmath:[\frac{arg1}{ms} = \frac{\Delta t}{ms}]
    Arguments:
	    arg1 = latexmath:[\Delta t]
	    Easy explanation: In-game seconds witch will pass in one real second

### time-rate-add <$narg1>
	This command sets the time rate to
	latexmath:[\frac{\Delta t + arg1}{s}=\frac{\Delta t + some\:ms}{ms}]
	Arguments:
		arg1 = latexmath:[some\:ms]
		Easy explanation: What we need to add to the current time rate to get the desired time rate. +

## Physics engine commands
### print <$sarg1>:
	Prints the current value of the specified physics engine property arg1
	Arguments:
		arg1 = the name of the property

## Other commands
`h[elp]`::
	This commands shows a list of all the commands

IMPORTANT: Only works with "h" or "help"