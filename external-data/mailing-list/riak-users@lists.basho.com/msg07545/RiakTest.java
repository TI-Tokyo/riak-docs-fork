package com.chatterbox.persistence.riak;

import java.util.Collection;
import java.util.List;

import com.basho.riak.client.DefaultRiakObject;
import com.basho.riak.client.IRiakClient;
import com.basho.riak.client.IRiakObject;
import com.basho.riak.client.RiakException;
import com.basho.riak.client.RiakFactory;
import com.basho.riak.client.RiakLink;
import com.basho.riak.client.bucket.Bucket;
import com.basho.riak.client.query.WalkResult;

public class RiakTest
{
    public static void main(String[] args) throws Exception
    {
        RiakTest riakTest = new RiakTest();
        riakTest.createObjects();
        riakTest.getAllLinks();
    }

    private void createObjects()
    {
        IRiakClient riakClient = getClient();
        String user = "user2";
        try
        {
            long past = System.currentTimeMillis();
            
            Bucket userBucket = riakClient.createBucket("usersMine").execute();
            Bucket preferenceBucket = riakClient.createBucket("userPreferences").execute();
            userBucket.store(user, "blah").returnBody(true).execute();
            DefaultRiakObject userObject =(DefaultRiakObject) userBucket.fetch(user).execute();
            
            for (int i = 3000; i < 3010; i++)
            {
                String preferenceKey = "preference" + i;
                RiakLink link = new RiakLink("userPreferences", preferenceKey , "myPref");
                preferenceBucket.store(preferenceKey, "{junk}").returnBody(true).execute();
                DefaultRiakObject preferenceObject =(DefaultRiakObject)preferenceBucket.fetch(preferenceKey).execute();
                System.out.println(preferenceObject.getKey());
                userObject.removeLink(link); // Remove the link if it already exists
                userObject.addLink(link);    
            }
            DefaultRiakObject fetch =(DefaultRiakObject)  userBucket.store(user, userObject).returnBody(true).execute();
            int size = fetch.getLinks().size();
            long now = System.currentTimeMillis();
            System.out.println("User has " + size + " links" );
            System.out.println("Done in " + (now-past) + " ms");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(riakClient != null)
            {
                riakClient.shutdown();
            }
        }
    }
    
    private void getAllLinks()
    {
        String user="user2";
        IRiakClient riakClient = getClient();
        try
        {
            long past = System.currentTimeMillis();
            Bucket userBucket = riakClient.fetchBucket("usersMine").execute();
            DefaultRiakObject user1 =(DefaultRiakObject) userBucket.fetch(user).execute();
            List<RiakLink> links = user1.getLinks();
            
            System.out.println(links.size());
            WalkResult execute = riakClient.walk(user1).addStep("userPreferences", "myPref").execute();
            for(Collection<IRiakObject> collection : execute)
            {
                for(IRiakObject next : collection)
                {
                    System.out.println(next.getKey() + " " + next.getValueAsString());    
                }
            }
            long now = System.currentTimeMillis();
            System.out.println("Retrieval in " + (now-past) + " ms");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(riakClient != null)
            {
                riakClient.shutdown();
            }
        }        
    }
    
    private IRiakClient getClient() 
    {
        try
        {
            //return RiakFactory.pbcClient("localhost",8081);
            return RiakFactory.httpClient("http://localhost:8091/riak");
        }
        catch (RiakException e)
        {
            return null;
        }        
    }
}
