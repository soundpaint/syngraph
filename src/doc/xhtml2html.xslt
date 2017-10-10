<xsl:stylesheet
    version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:html='http://www.w3.org/1999/xhtml'
    exclude-result-prefixes="html">
  <xsl:output method="html"
	      doctype-public="-//W3C//DTD HTML 3.2 Final//EN"/>
  <xsl:template match="@*|node()">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()"/>
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>

<!--
  Local Variables:
    coding:utf-8
    mode:sgml
    mode:nXML
  End:
-->
