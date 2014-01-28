package com.cisco.swtg.scim.embedded.neo4j.example;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.kernel.EmbeddedGraphDatabase;

import com.cisco.swtg.scim.domain.NodeTypes;
import com.cisco.swtg.scim.services.Properties;

public class EmbeddedNeo4jExample {

	public static GraphDatabaseService graphDb = null;

	public IndexHits<Node> findAll(String nodeType, String nodeId) {

		return indexFor(nodeType).get(Properties.NODE_ID, nodeId);

	}

	public Node find(String nodeType, String nodeId) {

		return indexFor(nodeType).get(Properties.NODE_ID, nodeId).getSingle();

	}

	public Node findOrCreateICO(String nodeType, String nodeId, String icoType,
			String icoTitle) {
		Node existing = find(nodeType, nodeId);

		if (existing != null)
			return existing;

		return createICO(nodeType, nodeId, icoType, icoTitle);

	}

	public Node createICO(String nodeType, String nodeId, String icoType,
			String icoTitle) {
		Node newNode = graphDb.createNode();

		newNode.setProperty(Properties.NODE_TYPE, nodeType);
		newNode.setProperty(Properties.NODE_ID, nodeId);
		newNode.setProperty(Properties.ICO_TYPE, icoType);
		newNode.setProperty(Properties.ICO_TYPE, icoTitle);
		return newNode;
	}

	public Index<Node> indexFor(String nodeType) {
		return graphDb.index().forNodes(nodeType);
	}

	public void addToIndex(Node node, String nodeType, String property,
			String value) {
		indexFor(nodeType).add(node, property, value);
	}

	public void removeFromIndex(Node node, String nodeType, String property,
			String value) {
		indexFor(nodeType).remove(node, property, value);
	}

	public void updateIndex(Node node, String nodeType, String property,
			String value) {
		removeFromIndex(node, nodeType, property, value);
		addToIndex(node, nodeType, property, value);

	}

	public Node find(String nodeType, String property, String value) {

		return indexFor(nodeType).get(property, value).getSingle();

	}

	public static void main(String[] args) {
		EmbeddedNeo4jExample example = new EmbeddedNeo4jExample();
		graphDb = new EmbeddedGraphDatabase("C:\\graphdb");
		Transaction transaction = graphDb.beginTx();
		Node n = graphDb.createNode();

		n.setProperty("node_id", "WWWWWWWWWW");
		example.addToIndex(n, NodeTypes.ICO.name(), Properties.NODE_ID,
				"WWWWWWWWWW");

		n.setProperty(Properties.ICO_TYPE, NodeTypes.PID.name());
		example.addToIndex(n, NodeTypes.ICO.name(), Properties.ICO_TYPE,
				NodeTypes.PID.name());

		n.setProperty(Properties.ICO_TYPE, NodeTypes.SERIAL_NO.name());
		example.addToIndex(n, NodeTypes.ICO.name(), Properties.ICO_TYPE,
				NodeTypes.SERIAL_NO.name());

		transaction.success();
		transaction.finish();

		IndexHits nn = example.findAll(NodeTypes.ICO.name(), "WWWWWWWWWW");
		while (nn.hasNext()) {
			Node nnn = (Node) nn.next();
			System.out.println(nnn.getProperty("node_id"));
			if (nnn.hasProperty("node_id")) {
				System.out.println("Hurray");
			}
		}
		// System.out.println( nn.getProperty(Properties.ICO_TYPE));
		graphDb.shutdown();

	}

}
