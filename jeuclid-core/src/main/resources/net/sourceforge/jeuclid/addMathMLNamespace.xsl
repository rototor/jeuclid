<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  exclude-result-prefixes="#all">
  <xsl:output method="xml" indent="no"/>
  <xsl:template match="*">
    <xsl:element name="mml:{local-name()}" namespace="http://www.w3.org/1998/Math/MathML">
      <xsl:copy-of select="@*" />
      <xsl:apply-templates />
    </xsl:element>
  </xsl:template>
</xsl:stylesheet>