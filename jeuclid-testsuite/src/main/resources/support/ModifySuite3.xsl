<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml" xmlns:html="http://www.w3.org/1999/xhtml" version="1.0">
  <xsl:template name="copy">
    <xsl:copy>
      <xsl:apply-templates select="@*"/>
      <xsl:apply-templates/>
    </xsl:copy>
  </xsl:template>
  <xsl:template match="html:a">
    <xsl:choose>
      <xsl:when test="text()='simple'"/>
      <xsl:when test="text()='plain'"/>
      <xsl:when test="text()='form'"/>
      <xsl:when test="text()='slideshow'"/>
      <xsl:otherwise>
        <xsl:call-template name="copy"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  <xsl:template match="html:b[contains(.,'Your browser')]">
    <b>JEuclids rendering</b>
  </xsl:template>
  <xsl:template match="html:hr[2]">
    <hr/>
    <p>This page is based on the <a href="http://www.w3.org/Math/testsuite/">MathML Testsuite</a> which is released under the <a href="http://www.w3.org/Consortium/Legal/copyright-documents">W3C Document License</a>. It has been modified for <a href="http://jeuclid.sourceforge.net/">JEuclid</a>. The modifications are available under the <a href="http://www.apache.org/licenses/">Apache Software License, Version 2.0</a>.</p>
    <hr/>
  </xsl:template>
  <xsl:template match="@*|node()">
    <xsl:call-template name="copy"/>
  </xsl:template>
</xsl:stylesheet>
