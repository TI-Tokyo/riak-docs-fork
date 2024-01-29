import com.basho.riak.client.api.RiakClient;
import com.basho.riak.client.api.commands.datatypes.Context;
import com.basho.riak.client.api.commands.datatypes.CounterUpdate;
import com.basho.riak.client.api.commands.datatypes.FetchMap;
import com.basho.riak.client.api.commands.datatypes.MapUpdate;
import com.basho.riak.client.api.commands.datatypes.RegisterUpdate;
import com.basho.riak.client.api.commands.datatypes.UpdateMap;
import com.basho.riak.client.core.query.Location;
import com.basho.riak.client.core.query.Namespace;
import com.basho.riak.client.core.query.crdt.types.RiakMap;
import com.basho.riak.client.core.util.BinaryValue;

public class User {
    private String key;
    private Location location;
    private RiakClient client;
    String bucketType = "maps";
    String bucket = "users";

    public User(String firstName, String lastName, RiakClient client) throws Exception {
        this.client = client;
    	this.key = String.format("%s_%s", firstName.toLowerCase(), lastName.toLowerCase());
        this.location = new Location(new Namespace(bucketType, bucket), key);
        RegisterUpdate ru1 = new RegisterUpdate(BinaryValue.create(firstName));
        RegisterUpdate ru2 = new RegisterUpdate(BinaryValue.create(lastName));
        MapUpdate mu = new MapUpdate()
                .update("first_name", ru1)
                .update("last_name", ru2);
        updateMapWithoutContext(mu);
        
        
    }

    /**
     * Fetches our map's abstract context object, which assists Riak in
     * making intelligent decisions about map convergence behind the
     * scenes. This will assist us later in the tutorial.
     */

    private Context getMapContext() throws Exception {
        FetchMap fetch = new FetchMap.Builder(location).build();
        return client.execute(fetch).getContext();
    }

    /**
     * Updates our map using the abstract context object fetched using
     * the private getMapContext() function above.
     */

    private void updateMapWithContext(MapUpdate mu) throws Exception {
        Context ctx = getMapContext();
        UpdateMap update = new UpdateMap.Builder(location, mu)
                .withContext(ctx)
                .build();
        client.execute(update);
    }

    /**
     * Updates our map without an abstract context object. Context is not
     * needed for some map updates.
     */

    private void updateMapWithoutContext(MapUpdate mu) throws Exception {
        UpdateMap update = new UpdateMap.Builder(location, mu).build();
        client.execute(update);
    }

    
    public void visitPage() throws Exception {
        CounterUpdate cu = new CounterUpdate(1);

        // To decrement a counter, pass a negative number to the
        // CounterUpdate object

        MapUpdate mu = new MapUpdate()
                .update("visits", cu);

        // Using our updateMapWithoutContext method from above, as
        // context is not necessary for counter updates
        updateMapWithoutContext(mu);
    }
    
    /**
     * Fetches our map from Riak in its current state, which enables us
     * to fetch current values for all of the fields of the map, as
     * in the methods below.
     */

    private RiakMap getMap() throws Exception {
        FetchMap fetch = new FetchMap.Builder(location).build();
        return client.execute(fetch).getDatatype();
    }
    
    public String getFirstName() throws Exception {
        return getMap().getRegister("first_name").toString();
    }

    public String getLastName() throws Exception {
    	return getMap().getRegister("last_name").toString();
    }

    public Long getVisits() throws Exception {
        return getMap().getCounter("visits").view();
    }

    public boolean getAccountStatus() throws Exception {
        return getMap().getFlag("paid_account").view();
    }

	
}