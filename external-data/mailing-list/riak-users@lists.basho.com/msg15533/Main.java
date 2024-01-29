import java.net.UnknownHostException;

import com.basho.riak.client.api.RiakClient;
import com.basho.riak.client.core.RiakCluster;
import com.basho.riak.client.core.RiakNode;


public class Main {
	private static RiakClient client;
    private static RiakCluster cluster;
    

	public static void setup() throws UnknownHostException {
		
		RiakNode node = new RiakNode.Builder().withRemoteAddress("127.0.0.1")
				.withRemotePort(8087).build();
		cluster = new RiakCluster.Builder(node).build();
		client = new RiakClient(cluster);
		cluster.start(); 
	}
	
	public static void main(String[] args) {
		try {
			setup();
			User bob = new User("Bob", "Builder",client);
			System.out.println(bob.getLastName() + "," + bob.getFirstName() +": " + bob.getVisits());
			shutdown();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private static void shutdown() {
		//cluster.shutdown();
	}

}
