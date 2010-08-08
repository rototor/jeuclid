<?xml-stylesheet type="text/xsl" href="xsldoc.xsl"?>
<xsl:stylesheet
  version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:mml="http://www.w3.org/1998/Math/MathML"
  xmlns:h="http://www.w3.org/1999/xhtml"
  xmlns:msxsl="urn:schemas-microsoft-com:xslt"
  xmlns:doc="http://www.dcarlisle.demon.co.uk/xsldoc"
  xmlns:user="http://www.dcarlisle.demon.co.uk/mmlxsl"
  xmlns:ie5="http://www.w3.org/TR/WD-xsl"
  extension-element-prefixes="msxsl"
>

<!--
$Id: xsldoc.xsl,v 1.2 2002/06/13 15:16:22 rminer Exp $

Copyright David Carlisle 2001, 2002.

Use and distribution of this code are permitted under the terms of the <a
href="http://www.w3.org/Consortium/Legal/copyright-software-19980720"
>W3C Software Notice and License</a>.
-->

<xsl:template match="/">
<html>
<head>
<title>XSL stylesheet</title>
</head>
<xsl:apply-templates/>
<body>
</body>
</html>
</xsl:template>

<xsl:template match="xsl:stylesheet/h:*">
<xsl:apply-templates mode="doc" select="."/>
</xsl:template>

<xsl:template match="xsl:stylesheet/h:*[@doc:ref]"/>


<xsl:template mode="doc" match="h:*">
<xsl:element name="{local-name(.)}">
 <xsl:attribute name="style">color: #557700;</xsl:attribute>
 <xsl:copy-of select="@*"/>
<xsl:apply-templates mode="doc"/>
</xsl:element>
</xsl:template>

<xsl:template match="comment()">
<pre style="color: #FF0000;">&lt;!--
<xsl:value-of select="."/>
--></pre>
</xsl:template>

<xsl:template match="*">
<xsl:param name="indent"/>
<xsl:variable name="colour">
<xsl:choose>
<xsl:when test="namespace-uri(.)='http://www.w3.org/1999/XSL/Transform'">3322AA</xsl:when>
<xsl:when test="namespace-uri(.)='http://www.w3.org/TR/WD-xsl'">AA3322</xsl:when>
<xsl:when test="namespace-uri(.)='urn:schemas-microsoft-com:xslt'">111155</xsl:when>
<xsl:otherwise>119900</xsl:otherwise>
</xsl:choose>
</xsl:variable>
<xsl:if test="@doc:id">
<br/>
<xsl:value-of select="$indent"/>
<xsl:apply-templates
select="/xsl:stylesheet/h:*[@doc:ref=current()/@doc:id]" mode="doc"/>
</xsl:if>
<xsl:if test="not(preceding-sibling::node()[1][self::text()])">
<br/><xsl:value-of select="$indent"/>
</xsl:if>
<span style="color: #{$colour};">&lt;<xsl:value-of select="name(.)"/>
<xsl:for-each select="namespace::*[not(.=../../namespace::*)][not(.='http://www.w3.org/XML/1998/namespace')]">
<xsl:text>&#160;</xsl:text>
<xsl:value-of select="name()"/>="<xsl:value-of select="."/>"<br/><xsl:value-of select="$indent"/>
</xsl:for-each>
<xsl:for-each select="@*">
<xsl:text>&#160;</xsl:text>
<xsl:value-of select="name()"/>="<xsl:value-of select="."/>"<br/><xsl:value-of select="$indent"/>
</xsl:for-each>
</span>
<xsl:choose>
<xsl:when test="node()">
<span style="color: #{$colour};">&gt;</span>
<xsl:apply-templates>
 <xsl:with-param name="indent" select="concat($indent,'&#160;&#160;')"/>
</xsl:apply-templates>
<xsl:if test="not(node()[last()][self::text()])">
<br/><xsl:value-of select="$indent"/>
</xsl:if>
<span style="color: #{$colour};"><xsl:text/>&lt;/<xsl:value-of select="name(.)"/>&gt;<xsl:text/></span>
</xsl:when>
<xsl:otherwise>
<span style="color: #{$colour};">/&gt;</span>
</xsl:otherwise>
</xsl:choose>
</xsl:template>



<xsl:template match="text()">
 <xsl:call-template name="br"/>
</xsl:template>

<xsl:template name="br">
  <xsl:param name="x" select="."/>
  <xsl:choose>
   <xsl:when test="contains($x,'&#10;')">
   <xsl:value-of select="substring-before($x,'&#10;')"/><br/>
   <xsl:call-template name="br">
    <xsl:with-param name="x" select="substring-after($x,'&#10;')"/>
   </xsl:call-template>
  </xsl:when>
  <xsl:otherwise>
    <xsl:value-of select="$x"/>
  </xsl:otherwise>
  </xsl:choose>
</xsl:template>

</xsl:stylesheet>

