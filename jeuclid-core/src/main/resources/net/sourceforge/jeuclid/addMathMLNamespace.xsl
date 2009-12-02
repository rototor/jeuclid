<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="xml" indent="no"/>
  <xsl:template match="*">
    <xsl:choose>
      <xsl:when test="namespace-uri()=''">
        <xsl:element name="mml:{local-name()}" namespace="http://www.w3.org/1998/Math/MathML">
          <xsl:copy-of select="@*"/>
          <xsl:apply-templates/>
        </xsl:element>
      </xsl:when>
      <xsl:otherwise>
        <xsl:element name="{name()}" namespace="{namespace-uri()}">
          <xsl:copy-of select="@*"/>
          <xsl:apply-templates/>
        </xsl:element>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
</xsl:stylesheet>
