package gagan.learn;

import static org.neo4j.helpers.collection.MapUtil.map;

import java.util.Map;

import org.neo4j.rest.graphdb.RestAPI;
import org.neo4j.rest.graphdb.RestAPIFacade;
import org.neo4j.rest.graphdb.query.RestCypherQueryEngine;
import org.neo4j.rest.graphdb.util.QueryResult;

public class CypherQuery {

	public static void main(String[] args) {
		try {
			System.out.println("starting test");
			final RestAPI api = new RestAPIFacade(
					"http://localhost:7474/db/data/");
			System.out.println("API created");
			final RestCypherQueryEngine engine = new RestCypherQueryEngine(api);
			System.out.println("engine created");
			final QueryResult<Map<String, Object>> result = engine
					.query("start n=node({id}) return id(n) as id, n.node_id? as name;",
							map("id", 200));

			System.out.println("query created");
			for (Map<String, Object> row : result) {
				String id = (String) (row.get("name"));
				System.out.println("id is " + id);
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
	}
}