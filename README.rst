veemon
======

veemon wraps two private Sun APIs, jvmstat and the attach api, using Clojure. Using the APIs you can discover
JVMs running on a given server, attach to them, and request monitored information. Using the Attach API
you can request that a JVM load a JAR file containing Java agents, in particular to load a JMX server which
you can connect to and run JMX requests. The purpose of all these APIs is to monitor and manage JVMs; I'm 
focused on the monitoring part.


The Serviceability APIs
-----------------------

The APIs that veemon uses are part of the "Serviceability" aspects of Sun's JVMs. The home page for all
of these technologies is

http://openjdk.java.net/groups/serviceability/

JvmStat Java APIs are documented at

http://openjdk.java.net/groups/serviceability/jvmstat/overview-summary.html

and the Attach Java API is documented at

http://java.sun.com/javase/6/docs/jdk/api/attach/spec/index.html

Note that both of these APIs are on the one hand part of Sun's official tools for servicing JVMs, and on the
other hand are not part of a "standard" specification - e.g. not an official part of the JDK. The package names
belong to the com.sun namespace which means in principle, they could be modified at any time, for any reason, between
JVM releases. This is, I think, unlikely, but possible, so if you are using veemon be aware that if you aren't
using JMX (which has a standard) your code may break if Sun decides to change APIs.


Using veemon
------------

veemon is the pretty much the first Clojure code I've written and it's pretty crude and will be improved over time.
My own interest is in being able to write small bits of Clojure to easily monitor VMs I'm working with.

Rough notes::

  
