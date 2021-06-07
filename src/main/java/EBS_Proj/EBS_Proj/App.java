package EBS_Proj.EBS_Proj;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.topology.TopologyBuilder;


public class App 
{
	private static final String SPOUT_ID = "source_weather_spout";
	private static final String BOLT1_ID = "bolt_1";
	private static final String BOLT2_ID = "bolt_2";
	private static final String WEATHER_TOPOLOGY = "weather_topology";

    public static void main( String[] args ) throws Exception
    {
    	TopologyBuilder builder = new TopologyBuilder();
    	SourceWeatherSpout spout = new SourceWeatherSpout();

    	builder.setSpout(SPOUT_ID, spout);
		builder.setBolt(BOLT1_ID, new Bolt(5056)).globalGrouping(SPOUT_ID);
		builder.setBolt(BOLT2_ID, new Bolt(5057)).globalGrouping(SPOUT_ID);

		Config config = new Config();
    	LocalCluster cluster = new LocalCluster();
    	StormTopology topology = builder.createTopology();

    	config.put(Config.TOPOLOGY_EXECUTOR_RECEIVE_BUFFER_SIZE, 1024);
    	config.put(Config.TOPOLOGY_TRANSFER_BATCH_SIZE, 1);
    	config.put("window_size", 10);
    	cluster.submitTopology(WEATHER_TOPOLOGY, config, topology);

    	try {
    		Thread.sleep(200000);
		} catch (InterruptedException e) {
    		e.printStackTrace();
		}

    	cluster.killTopology(WEATHER_TOPOLOGY);
    	cluster.shutdown();
    }
}
