package EBS_Proj.EBS_Proj;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Bolt extends BaseRichBolt {

    private static final long serialVersionUID = 4;
    private long windowSize;
    private List<WeatherModel> window;
    private int port;
    private Server server;

    public Bolt(int port) {
        this.port = port;
    }

    public void prepare(Map topoConf, TopologyContext context, OutputCollector collector) {
        System.out.println("----- Started bolt task: " + context.getThisTaskId());
        this.windowSize = (long) topoConf.get("window_size");
        this.window = new ArrayList<WeatherModel>();
        this.server = new Server(this.port);
        this.server.start();
    }

    public void execute(Tuple input) {
        if (window.size() >= windowSize) {
            // notify subscribers somehow
            this.notifyWindow(window);
            window = new ArrayList<WeatherModel>();
        }
        WeatherModel weather = (WeatherModel) input.getValue(0);
        window.add(weather);

        // notify subscribers somehow
        try {
            this.notifySingle(weather);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("=========================================================================");
        System.out.println(weather);
        System.out.println("=========================================================================");
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }

    private void notifySingle(WeatherModel weatherModel) throws IOException {
        for (ClientHandler ch : this.server.subscribers) {
            StringBuilder sb = new StringBuilder(ch.getSubscription());
            sb.deleteCharAt(0);
            sb.deleteCharAt(sb.length() - 1);
            boolean isConditionMet = true;
            for (String cond : sb.toString().split(";")) {
                StringBuilder sb1 = new StringBuilder(cond);
                sb1.deleteCharAt(0);
                sb1.deleteCharAt(sb.length() - 1);

                String[] parts = sb1.toString().split(",");
                Object val = null;
                switch (parts[0]) {
                    case "city":
                        val = weatherModel.getCity();
                        break;
                    case "stationId":
                        val = weatherModel.getStationId();
                        break;
                    case "temp":
                        val = weatherModel.getTemp();
                        break;
                    case "rain":
                        val = weatherModel.getRain();
                        break;
                    case "wind":
                        val = weatherModel.getWind();
                        break;
                    case "direction":
                        val = weatherModel.getDirection();
                        break;
                    case "date":
                        val = weatherModel.getDate().toString();
                        break;
                    default:
                        System.out.println("UNKNOWN FIELD");
                        break;
                }
                if (val == null) {
                    return;
                }
                switch (parts[1]) {
                    case "=":
                        if (!val.equals(parts[2])) {
                            isConditionMet = false;
                        }
                        break;
                    case ">":
                        if ((double) val <= Double.parseDouble(parts[2])) {
                            isConditionMet = false;
                        }
                        break;
                    case "<":
                        if ((double) val >= Double.parseDouble(parts[2])) {
                            isConditionMet = false;
                        }
                        break;
                    case ">=":
                        if ((double) val < Double.parseDouble(parts[2])) {
                            isConditionMet = false;
                        }
                        break;
                    case "<=":
                        if ((double) val > Double.parseDouble(parts[2])) {
                            isConditionMet = false;
                        }
                        break;
                    default:
                        System.out.println("UNKNOWN OPERATOR");
                        return;
                }
            }
            if (isConditionMet) {
                ch.notifySingle(weatherModel);
            }
        }
    }

    private void notifyWindow(List<WeatherModel> window) {

    }

    public void cleanup() {
        System.out.println("Closed bolt");
    }
}
