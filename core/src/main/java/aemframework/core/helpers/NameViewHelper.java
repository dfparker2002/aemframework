package aemframework.core.helpers;

import java.util.HashMap;
import java.util.Map;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.ResourceResolver;

import com.day.cq.wcm.api.Page;

import aemframework.core.viewhelper.ViewHelperImpl;
@Component(description = "NameViewHelper", immediate = true, metatype = true, label = "NameViewHelper")
@Service(value = NameViewHelper.class)
public class NameViewHelper extends ViewHelperImpl {

	@Override
	public Map<String, Object> onGetData(Map<String, Object> content, Map<String, Object> objects) {
		Page currentPage = (Page) objects.get("currentPage");
		final ResourceResolver resourceResolver = (ResourceResolver) objects.get("resourceResolver");
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("pageTitle", currentPage.getTitle());
		result.put("myresourceId", resourceResolver.getUserID());
		return result;
	}

}
