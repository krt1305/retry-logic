package customLogic;

import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.Map;

public class RedisUtil {

    public static void main(String[] args) {
        //Connect to Redis server using localhost
     /*   Jedis jedis = new Jedis("localhost");
        System.out.println("Connection successful");
        //Checking server
        System.out.println("Getting response from the server: "+jedis.ping());
        jedis.set("eduonix", "Redis cache tutorial");
        //Retrieve the string value from the cache
        System.out.println("Stored data in cache: "+ jedis.get("eduonix"));*/
        JedisConnectionFactory jedisConFactory
                = new JedisConnectionFactory();
        jedisConFactory.setHostName("localhost");
        jedisConFactory.setPort(6379);

        JedisCluster jedisCluster = new JedisCluster(new HostAndPort("localhost", 6379));
       /* ClusterInfo clusterInfo = ((JedisClusterConnection) connection)
                .clusterGetClusterInfo();*/
        jedisCluster.getClusterNodes().size();


        for (Map.Entry<String, JedisPool> node : jedisCluster.getClusterNodes().entrySet()) {
            try (Jedis jedis = node.getValue().getResource()) {
                System.out.println(jedis.info("memory"));
                System.out.println(jedis.dbSize());

                //Node is OK
            } catch (JedisConnectionException jce) {
                //Node FAILS
            }
        }




    }
}
