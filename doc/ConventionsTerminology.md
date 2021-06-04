# Naming Conventions
* Types starting with `I` are always interfaces (e.g. ISessionListener<>)
* Types ending with `Base` are always abstract classes, as base implementations of an interface (e.g. SessionListenerBase<>).
Instead of fully implementing an interface yourself, you can instead extend these classes as a baseline.
* Types starting with `Basic` are always instantiable classes that are either extending a `Base` class or implementing an interface (e.g. BasicRepository or BasicVISABFile).
`Basic` classes, do not implement functionality beyond what is required by their interface or abstract parent class. If there is no Base class to inherit from, you can inherit from `Basic` types instead.
* Types starting with `Default` are implementations for games that dont have an implementation yet in VISAB.
E.g. DefaultSessionListener will be used when a game is allowed in the settings, but does not have a ISessionListener<> implementation yet.
These types are usually found in a packages named `starter`.

# Terminology
* Session: A Session within VISAB, is what game clients have to open to send data to VISABs WebApi. 
Each session has a SessionId, using which it can be uniquely identified.
The more concrete name "transmission session" can be used interchangeably with session and may be found in comments or javadoc.