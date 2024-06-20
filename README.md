JEuclid
=========

[![Build Status](https://api.travis-ci.org/rototor/jeuclid.svg?branch=master)](https://travis-ci.org/rototor/jeuclid)

This is a fork of http://jeuclid.sourceforge.net/ to get it working on JDK 11 and 
with Batik 1.13. Only the core and the FOP plugin are supported.

SWT support is removed at the moment. If you need them or need any other feature
not provided with this distribution, feel free to send me a pull request.

The main purpose of this fork is to get it working with 
[OpenHMLToPDF](https://github.com/danfickle/openhtmltopdf), see 
[this issue](https://github.com/danfickle/openhtmltopdf/issues/161) there. So the main focus is
on rendering.


## Maven Artefact
You can get this library from maven central using this dependency:

```xml
<dependency>
        <groupId>de.rototor.jeuclid</groupId>
        <artifactId>jeuclid-core</artifactId>
        <version>3.1.14</version>
</dependency>
```
## FOP plugin

There is no ServiceLoader configuration in this fork of the FOP plugin so it will not be auto-configured
and needs to be loaded manually when desired using the following code: `JEuclidFopFactoryConfigurator.configure(factory);`

## Changes

Version 3.1.15 (not released yet):
 - Bump Batik Version to 1.17
 - [#15](https://github.com/rototor/jeuclid/pull/15) by @rack197 for better FOP support.

Version 3.1.14:
 - [#5](https://github.com/rototor/jeuclid/pull/5) Restored the FOP plugin. Thanks @dubinsky.

Version 3.1.13:
 - [#2](https://github.com/rototor/jeuclid/pull/2):  Fix the SVGDOMImplementation import. Thanks @markushenninger.

Version 3.1.12:
 - Allow users to set a FontFactory for use on a per-thread basis. Thanks @danfickle.
 - Lazy load default fonts. Fonts will now only be searched for when the user does not override the FontFactory.

Version 3.1.11: 
 - First version released to maven central.
 - Fixes a small race in a testdriver, which caused the release of 3.1.10 to fail.
 
Version 3.1.10:
 - Removed FOP and SWT support
 - Made it compile with JDK9. This removes dynamic DOM change support for now.
 - Upgraded Batik and other dependencies to their current version.
 - Note: This is not a fully compatible replacement for 3.1.9, as this version
	misses FOP and SWT support.
 - Note: This version was never released, due some races in the test 
 drivers while releasing.
