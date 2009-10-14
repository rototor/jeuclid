<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:html="http://www.w3.org/1999/xhtml"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
xmlns:exsl="http://exslt.org/common"
               extension-element-prefixes="exsl"
              >
  <xsl:output media-type="text" omit-xml-declaration="yes"/>

  <xsl:template match="head"/>

  <xsl:template match="p"/>

  <xsl:template match="table"/>

  <xsl:template match="interface"><exsl:document
href="{@name}.java"
method="text"
omit-xml-declaration="yes"
><xsl:text
>/*
 * Copyright 2007 - 2007 JEuclid, http://jeuclid.sf.net
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.w3c.dom.mathml; import
  org.w3c.dom.*; 

/*
 * Please note: This file was automatically generated from the source of the MathML
 * specification. Do not edit it. If there are errors or missing elements, please
 * correct the stylesheet instead.
 */

/** </xsl:text><xsl:apply-templates 
select="descr"
/><xsl:text>*/&#x0A; public interface </xsl:text><xsl:value-of
  select="@name"/><xsl:if test="@inherits"><xsl:text> extends
  </xsl:text><xsl:value-of select="@inherits"/></xsl:if><xsl:text>
  {</xsl:text><xsl:apply-templates select="method|attribute"/><xsl:text>};
  </xsl:text></exsl:document></xsl:template>

  <xsl:template match="method"><xsl:text>&#x0A;/** </xsl:text
><xsl:apply-templates select="descr"
/><xsl:for-each select="parameters/param"
><xsl:text> @param </xsl:text
><xsl:value-of select="@name"
/><xsl:text> </xsl:text
><xsl:apply-templates select="descr"
/></xsl:for-each
><xsl:if test="returns/@type != 'void'"
><xsl:text> @return </xsl:text
><xsl:apply-templates select="returns/descr"
/></xsl:if
><xsl:for-each select="raises/exception"
><xsl:text> @throws </xsl:text
><xsl:value-of select="@name"
/><xsl:text> </xsl:text
><xsl:apply-templates select="descr"
/></xsl:for-each
><xsl:text> */&#x0A; </xsl:text
><xsl:call-template name="maptype"><xsl:with-param
name="type" select="returns/@type"/></xsl:call-template
><xsl:text> </xsl:text
><xsl:value-of
  select="@name"/><xsl:text>(</xsl:text><xsl:apply-templates
  select="parameters"/><xsl:text>) </xsl:text><xsl:apply-templates
  select="raises"/><xsl:text>; </xsl:text></xsl:template>

  <xsl:template match="parameters"><xsl:for-each select="param"
><xsl:if test="not(@diff='del')"
><xsl:call-template name="maptype"><xsl:with-param
name="type" select="@type"/></xsl:call-template><xsl:text> </xsl:text><xsl:value-of select="@name"/><xsl:if
  test="position()!=last()">, </xsl:if></xsl:if></xsl:for-each></xsl:template>

  <xsl:template match="raises"><xsl:if test="exception"><xsl:text> throws
  </xsl:text><xsl:for-each select="exception"><xsl:value-of
  select="@name"/><xsl:if
  test="position()!=last()">,</xsl:if></xsl:for-each></xsl:if></xsl:template>

  <xsl:template match="attribute"
><xsl:if test="not(@diff='del')"
><xsl:variable name="aname"><xsl:value-of
  select="translate(substring(@name,1,1),&quot;abcdefghijklmnopqrstuvwxyz&quot;,&quot;ABCDEFGHIJKLMNOPQRSTUVWXYZ&quot;)"/><xsl:value-of
  select="substring(@name,2)"/></xsl:variable
><xsl:variable name="mtype"><xsl:call-template name="maptype"><xsl:with-param
name="type" select="@type"/></xsl:call-template></xsl:variable
><xsl:text>&#x0A; /** </xsl:text
><xsl:apply-templates select="descr"
/><xsl:text> @return value of the </xsl:text
><xsl:value-of select="@name" /><xsl:text> attribute.</xsl:text><xsl:text> */ &#x0A;</xsl:text><xsl:value-of
  select="$mtype"/><xsl:text> get</xsl:text><xsl:value-of
  select="$aname"/><xsl:text>(); </xsl:text><xsl:if
  test="@readonly='no'"><xsl:text>&#x0A;/** setter for the </xsl:text
><xsl:value-of select="@name" /><xsl:text> attribute. @param </xsl:text
><xsl:value-of select="@name" /><xsl:text> new value for </xsl:text
><xsl:value-of select="@name" /><xsl:text>. @see #get</xsl:text
><xsl:value-of select="$aname" /><xsl:text>() </xsl:text 
><xsl:for-each select="setraises/exception"
><xsl:text> @throws </xsl:text
><xsl:value-of select="@name"
/><xsl:text> </xsl:text
><xsl:apply-templates select="descr"
/></xsl:for-each
><xsl:text> */&#x0A; void set</xsl:text><xsl:value-of
  select="$aname"/><xsl:text>(</xsl:text><xsl:value-of
  select="$mtype"/><xsl:text> </xsl:text><xsl:value-of
  select="@name"/><xsl:text>); </xsl:text></xsl:if></xsl:if></xsl:template>
<xsl:template name="maptype"><xsl:param 
name="type" /><xsl:choose><xsl:when
 test="$type='unsigned long'"
><xsl:text>int</xsl:text></xsl:when
><xsl:when
 test="$type='DOMString'"
><xsl:text>String</xsl:text></xsl:when
><xsl:when
 test="$type=''"
><xsl:text>void</xsl:text></xsl:when
><xsl:otherwise><xsl:value-of 
select="$type"/></xsl:otherwise></xsl:choose></xsl:template>
<xsl:template match="descr"><xsl:apply-templates
/></xsl:template>
<xsl:template match="descr/p"><xsl:element name="p"><xsl:apply-templates select="text()|*"
/></xsl:element></xsl:template>
<xsl:template match="kw"><xsl:element name="tt"><xsl:apply-templates select="text()|*"
/></xsl:element></xsl:template>
<xsl:template match="att"><xsl:element name="tt"><xsl:apply-templates select="text()|*"
/></xsl:element></xsl:template>
<xsl:template match="el"><xsl:element name="tt"><xsl:apply-templates select="text()|*"
/></xsl:element></xsl:template>
<xsl:template match="emph"><xsl:element name="em"><xsl:apply-templates select="text()|*"
/></xsl:element></xsl:template>
<xsl:template match="xspecref"><xsl:element name="a"
><xsl:attribute name="href"
><xsl:value-of select="@href" /></xsl:attribute><xsl:apply-templates select="text()|*"
/></xsl:element></xsl:template>
</xsl:stylesheet>
