package fr.xgouchet.packageexplorer.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Environment;
import android.util.Log;
import fr.xgouchet.androidlib.data.TextFileUtils;
import fr.xgouchet.axml.CompressedXmlParser;

public final class ManifestUtils {

	public static final String MANIFEST = "AndroidManifest.xml";

	/**
	 * 
	 * @param info
	 *            the package info
	 * @param context
	 *            the current application context
	 * @return
	 * @throws TransformerException
	 *             when an error occurs while converting DOM to file xml
	 * @throws FileNotFoundException
	 *             when the destination file can't be written
	 * @throws ZipException
	 *             if a zip error occurs while reading the source file
	 * @throws IOException
	 *             when a reading exception occurs
	 * @throws ParserConfigurationException
	 *             if a DocumentBuilder can't be created
	 */
	public static File exportManifest(final PackageInfo info,
			final Context context) throws TransformerException, ZipException,
			IOException, ParserConfigurationException {

		// Read doc
		File destFile = new File(exportedManifestPath(info));
		Document doc = getManifest(info, context);

		StringBuilder builder = new StringBuilder();
		formatXml(doc, builder, 0);

		Log.i("Apex", builder.toString());

		TextFileUtils.writeTextFile(destFile.getPath(), builder.toString(),
				"UTF-8");

		return destFile;
	}

	private static void formatXml(final Node node, final StringBuilder builder,
			final int depth) {
		NodeList children = node.getChildNodes();
		NamedNodeMap attrs = node.getAttributes();

		int childrenCount, attrsCount, i;
		childrenCount = children.getLength();
		attrsCount = (attrs == null) ? 0 : attrs.getLength();

		formatIndent(builder, depth);
		switch (node.getNodeType()) {
		case Node.DOCUMENT_NODE:
			for (i = 0; i < childrenCount; i++) {
				formatXml(children.item(i), builder, depth);
			}
			break;
		case Node.ELEMENT_NODE:
			builder.append('<');
			builder.append(node.getNodeName());

			if (attrs != null) {
				for (i = 0; i < attrsCount; i++) {
					formatXml(attrs.item(i), builder, 0);
				}
			}

			if (childrenCount == 0) {
				builder.append("/>\n");
			} else {
				builder.append(">\n");
				for (i = 0; i < childrenCount; i++) {
					formatXml(children.item(i), builder, depth + 1);
				}

				formatIndent(builder, depth);
				builder.append("</");
				builder.append(node.getNodeName());
				builder.append(">\n");
			}
			break;
		case Node.TEXT_NODE:
			builder.append(node.getNodeValue());
			break;
		case Node.ATTRIBUTE_NODE:
			builder.append(' ');
			builder.append(node.getNodeName());
			builder.append("=\"");
			builder.append(node.getNodeValue());
			builder.append("\"");
			break;
		default:
			builder.append("<!-- Unknown node [#" + node.getNodeType() + " : "
					+ node.getNodeValue() + "] -->");
			break;
		}
	}

	private static void formatIndent(final StringBuilder builder,
			final int depth) {
		int i;
		for (i = 0; i < depth; i++) {
			builder.append("  ");
		}
	}

	/**
	 * 
	 * @param info
	 *            the package info
	 * @param context
	 *            the current application context
	 * @return
	 * @throws TransformerException
	 *             when an error occurs while converting DOM to file xml
	 * @throws FileNotFoundException
	 *             when the destination file can't be written
	 * @throws ZipException
	 *             if a zip error occurs while reading the source file
	 * @throws IOException
	 *             when a reading exception occurs
	 * @throws ParserConfigurationException
	 *             if a DocumentBuilder can't be created
	 */
	private static Document getManifest(final PackageInfo info,
			final Context context) throws ZipException, IOException,
			ParserConfigurationException {
		String srcPackage = info.applicationInfo.publicSourceDir;
		File srcFile = new File(srcPackage);
		return parseManifestFile(srcFile);
	}

	/**
	 * Extracts the manifest file from an apk to the desired file
	 * 
	 * @param apkFile
	 *            the apk file to read
	 * @param destFolder
	 *            the dest folder
	 * @throws ZipException
	 *             if a zip error occurs while reading the source file
	 * @throws IOException
	 *             when a reading exception occurs
	 * @throws ParserConfigurationException
	 *             if a DocumentBuilder can't be created
	 */
	private static Document parseManifestFile(final File apkFile)
			throws ZipException, IOException, ParserConfigurationException {
		ZipFile zipFile;
		ZipEntry entry;
		Enumeration<? extends ZipEntry> entries;
		Document doc = null;

		zipFile = new ZipFile(apkFile);
		entries = zipFile.entries();
		CompressedXmlParser parser = new CompressedXmlParser();

		while (entries.hasMoreElements()) {
			entry = entries.nextElement();
			if (entry.getName().equals(MANIFEST)) {
				doc = parser.parseDOM(zipFile.getInputStream(entry));
				break;
			}
		}

		zipFile.close();

		return doc;
	}

	/**
	 * @param pkg
	 * @return the path to export the manifest
	 */
	private static String exportedManifestPath(final PackageInfo pkg) {
		String path;
		path = Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_DOWNLOADS).getPath();
		path = path + File.separator + pkg.packageName.replaceAll("\\.", "_")
				+ ".xml";
		return path;
	}

}
