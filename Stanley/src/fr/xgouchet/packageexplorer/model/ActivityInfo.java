package fr.xgouchet.packageexplorer.model;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class ActivityInfo extends IntentAppItemInfo {

	public ActivityInfo(Node node) {
		NamedNodeMap attrs = node.getAttributes();
		Node attr;

		attr = attrs.getNamedItemNS(MANIFEST_URI, "name");
		if (attr != null) {
			name = node.getNodeValue();
		}

	}

}
