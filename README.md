JEuclid
=========

This is a fork of http://jeuclid.sourceforge.net/ to get it working on JDK 9 and 
with Batik 1.9. Only the core is supported.

FOP and SWT support is removed at the moment. If you need them or need any other feature
not provided with this distribution, feel free to send me a pull request.

The main purpose of this fork is to get it working with 
[OpenHMLToPDF](https://github.com/danfickle/openhtmltopdf), see 
[this issue](https://github.com/danfickle/openhtmltopdf/issues/161) there.


## Maven Artefact
You can get this library from maven central using this dependency:

```xml
<dependency>
        <groupId>de.rototor.jeuclid</groupId>
        <artifactId>jeuclid-core</artifactId>
        <version>3.1.10</version>
</dependency>
```
## Changes

Version 3.1.10:
 - Removed FOP and SWT support
 - Made it compile with JDK9. This removes dynamic DOM change support for now.
 - Upgraded Batik and other dependencies to their current version.
 - Note: This is not a fully compatible replacement for 3.1.9, as this version
	misses FOP and SWT support.