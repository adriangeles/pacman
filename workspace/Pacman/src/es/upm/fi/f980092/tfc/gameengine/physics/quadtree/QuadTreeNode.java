package es.upm.fi.f980092.tfc.gameengine.physics.quadtree;

import java.util.ArrayList;

import android.util.Log;
import es.upm.fi.f980092.tfc.gameengine.physics.collision.I_Geometry;

public class QuadTreeNode {

	private static final String TAG = "TfcGameEngine";
	private static final String TAG2 = TAG + "::QuadTreeNode";
	
	@SuppressWarnings("unchecked")
	private ArrayList nodeElements;
	private ArrayList<QuadTreeNode> subNodes = null;
	private I_Geometry geometry;
	
	
	public QuadTreeNode() {
		super();
		nodeElements = new ArrayList<Object>();
		subNodes = new ArrayList<QuadTreeNode>();
	}

	@SuppressWarnings("unchecked")
	public ArrayList getNodeElements() {
		return nodeElements;
	}
	
	public ArrayList<QuadTreeNode> getSubNodes() {
		return subNodes;
	}

	public void setSubNodes(ArrayList<QuadTreeNode> subNodes) {
		this.subNodes = subNodes;
	}

	public void setNodeElements(ArrayList<Object> nodeElements) {
		this.nodeElements = nodeElements;
	}
	
	public I_Geometry getGeometry() {
		return geometry;
	}
	
	public void setGeometry(I_Geometry geometry) {
		this.geometry = geometry;
	} 
	
	public void printDebug() {
		Log.d(TAG, TAG2 + " -----------  Quadtree ------------");
		printNode("...", this);
		Log.d(TAG, TAG2 + " -----------  -------- ------------");
	}
	
	private void printNode(String tab, QuadTreeNode node) {
		Log.d(TAG, TAG2 + tab + "Number of elements: " + node.getNodeElements().size());
		if ( node.getSubNodes() == null  || node. getSubNodes().size() == 0) {
			Log.d(TAG, TAG2 + tab + "Nodo final");			
		} else {
			Log.d(TAG, TAG2 + tab + "Subnodes: "  + node. getSubNodes().size() );
			for( QuadTreeNode subnode : node.getSubNodes()) {
				printNode(tab + "...", subnode);
			}
		}
	}
}
