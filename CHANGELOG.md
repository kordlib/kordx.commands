# 0.3.0

## Additions

* Added `Argument#flatten` to flatten an `Argument<Either<T,T>, CONTEXT>` into a `Argument<T, CONTEXT>`.

# 0.2.1

## Fixes

* Fixed messages dropping out of the events flow buffer.

## Changes 

* CommandProcessor will run commands in parallel.
 
# 0.2.0

## Changes

* Prefixes have been reworked considerably. #3

## Additions

* Added support for aliases
* You can now handle exceptions using the `ErrorHandler`.

#0.1.2

## Fixes

* Fixed an incorrect version range for Kord in kordx.commands.

#0.1.1

## Fixes

* Fixed an unescaped regex when enabling `enableMentionPrefix`
