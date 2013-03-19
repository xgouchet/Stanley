package fr.xgouchet.packageexplorer.model;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.os.Parcel;
import android.os.Parcelable;

public class ManifestInfo implements Parcelable {

	public static final String MANIFEST_URI = "http://schemas.android.com/apk/res/android";

	public ApplicationInfo application;
	public SdkInfo usesSdk;

	public ManifestInfo() {
		usesSdk = new SdkInfo();
		application = new ApplicationInfo();
	}

	public ManifestInfo(Parcel src) {

	}

	public void writeToParcel(Parcel dest, int flags) {

	}

	/**
	 * @param doc
	 *            the document
	 */
	public void setContent(Document doc) {
		readSdk(doc);
		readApplication(doc);
	}

	/**
	 * @param doc
	 *            the document
	 */
	private void readSdk(Document doc) {
		NodeList list;
		Node node;
		NamedNodeMap attrs;

		list = doc.getElementsByTagName("uses-sdk");
		int count = list.getLength();
		for (int i = 0; i < count; i++) {
			attrs = list.item(i).getAttributes();
			if (attrs != null) {
				node = attrs.getNamedItemNS(MANIFEST_URI, "minSdkVersion");
				if (node != null) {
					usesSdk.minSdkVersion = Integer
							.valueOf(node.getNodeValue());
				}
				node = attrs.getNamedItemNS(MANIFEST_URI, "targetSdkVersion");
				if (node != null) {
					usesSdk.targetSdkVersion = Integer.valueOf(node
							.getNodeValue());
				}
			}
		}
	}

	private void readApplication(Document doc) {
		NodeList list, childrenList;
		int i, j, count, childrenCount;

		list = doc.getElementsByTagName("application");
		count = list.getLength();
		for (i = 0; i < count; ++i) {
			childrenList = list.item(i).getChildNodes();
			childrenCount = childrenList.getLength();
			for (j = 0; j < childrenCount; ++j) {
				application.addChild(childrenList.item(j));
			}
		}
	}

	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<ManifestInfo> CREATOR = new Parcelable.Creator<ManifestInfo>() {
		public ManifestInfo createFromParcel(Parcel in) {
			return new ManifestInfo(in);
		}

		public ManifestInfo[] newArray(int size) {
			return new ManifestInfo[size];
		}
	};
}
