<%--
  Copyright 2012 SapientNitro
  All Rights Reserved.

  This software is the confidential and proprietary information of
  SapientNirto, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with SapientNitro.
  ==============================================================================
--%>
<%@include file="/libs/foundation/global.jsp"%>
<%@taglib prefix="la"
    uri="http://www.sapient.com/la/common/latags/1.0"%> 
<%@taglib prefix="laCMS"
    uri="http://www.sapient.com/la/cms/latags/1.0"%>
<c:set var="dateFormat"  value="MM/dd/yyyy" />
<c:set var="dateTimeZone"  value="EST" />
<c:set var="windowsResourcePath" value="${fn:replace(resource.path,'jcr:content', '_jcr_content')}" />
<cq:setContentBundle/>
