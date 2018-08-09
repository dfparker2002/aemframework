<%--

  ==============================================================================
--%>
<%@include file="/libs/foundation/global.jsp"%>
<c:set var="dateFormat"  value="MM/dd/yyyy" />
<c:set var="dateTimeZone"  value="EST" />
<c:set var="windowsResourcePath" value="${fn:replace(resource.path,'jcr:content', '_jcr_content')}" />
<cq:setContentBundle/>
