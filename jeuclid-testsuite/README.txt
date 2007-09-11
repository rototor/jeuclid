To view the TestSuite you need an SVG compatible browser, for example:

  FireFox >= 1.5
  Safari >= 3.0
  Opera >= 9.0

Internet Explorer does NOT work!

Simply run:

  mvn jetty:run


and point your browser to:
http://localhost:8080/jeuclid-testsuite/index.html

The configuration contains an auto-reload. So if you change any code in core,
go to the jeuclid-core directory and type "mvn install". There is no need to
restart the webserver, the new changes will be auto-loaded!

Unfortunately there is a classloader-bug in the operator-dictionary, which will
fail on reload. If you need mo's to work properly, you'll need to restart jetty.
