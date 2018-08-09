<%--
  Copyright 2012 SapientNitro
  All Rights Reserved.

  ==============================================================================

  Initialize Script

  This script can be used by any component or script to initialize the
  process of setting up data. This script expects from the component to have 
  made an entry in ComponentViewMapper configurations.
  which declares a bean using the conventions as defined below:
    - key: The component name
    - value: The class associated which should implement interface aemframework.core.viewhelper.ViewHelper
  
  ==============================================================================
--%>
<%
   aemframework.core.config.ComponentViewMapper viewHelperConfig = 
        sling.getService(aemframework.core.config.ComponentViewMapper.class);

    String componentName = component.getName();
    String className = viewHelperConfig.getViewHelper(componentName);

    aemframework.core.viewhelper.ViewHelper viewHelper = 
        (aemframework.core.viewhelper.viewhelper.ViewHelper) sling.getService(Class.forName(className)) ;
%>

<c:set var="data" value="<%=viewHelper.getData(pageContext)%>"/>