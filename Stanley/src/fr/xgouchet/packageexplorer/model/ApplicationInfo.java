package fr.xgouchet.packageexplorer.model;

import java.util.HashMap;

import org.w3c.dom.Node;

public class ApplicationInfo {

	public ApplicationInfo() {
		mItems = new HashMap<String, ApplicationItemInfo>();
	}

	public void addChild(Node node) {
		if ("activity".equalsIgnoreCase(node.getNodeName())) {
			ActivityInfo info = new ActivityInfo(node);
		}
	}

	private HashMap<String, ApplicationItemInfo> mItems;
}
