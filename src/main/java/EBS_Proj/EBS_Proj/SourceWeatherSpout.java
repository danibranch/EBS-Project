package EBS_Proj.EBS_Proj;

import java.util.Map;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

public class SourceWeatherSpout extends BaseRichSpout {
	private SpoutOutputCollector collector;
	private String task;
	private WeatherGenerator weatherGen;
	private long msgId = 0;

	public SourceWeatherSpout() {
	}

	@Override
	public void open(Map<String, Object> conf, TopologyContext context, SpoutOutputCollector collector) {
		// TODO Auto-generated method stub
		this.collector = collector;
		this.task = context.getThisComponentId()+" "+context.getThisTaskId();
		System.out.println("----- Started spout task: "+this.task);
		this.weatherGen = new WeatherGenerator();
	}

	@Override
	public void nextTuple() {
		WeatherModel model = this.weatherGen.GenerateWeather();
//		collector.emit(new Values(model.getStationId(), model.getCity(), model.getTemp(), model.getRain(), model.getWind(), model.getDirection(), model.getDate()), msgId++);
		collector.emit(new Values(model), msgId++);
//		System.out.println("Emitted");

		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
//		declarer.declare(new Fields("stationId", "city", "temp", "rain", "wind", "direction", "date"));
		declarer.declare(new Fields("weather"));
	}

}
