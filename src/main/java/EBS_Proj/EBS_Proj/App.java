package EBS_Proj.EBS_Proj;

import org.apache.storm.LocalCluster;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.topology.TopologyBuilder;


public class App 
{
	private static final String SPOUT_ID = "source_text_spout";
	
    public static void main( String[] args ) throws Exception
    {
    	TopologyBuilder builder = new TopologyBuilder();
    	SourceWeatherSpout spout = new SourceWeatherSpout();

    	builder.setSpout(SPOUT_ID, spout);

    	
    	LocalCluster cluster = new LocalCluster();
    	StormTopology topology = builder.createTopology();
    }
}
