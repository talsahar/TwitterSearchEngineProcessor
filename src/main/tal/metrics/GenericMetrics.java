package main.tal.metrics;
import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.MetricDatum;
import com.amazonaws.services.cloudwatch.model.PutMetricDataRequest;
import com.amazonaws.services.cloudwatch.model.StandardUnit;

public class GenericMetrics {

    final AmazonCloudWatch cw = AmazonCloudWatchClientBuilder.defaultClient();
    String namespace;
    String metricName;
    Dimension dimension;
    MetricDatum datum;

    public GenericMetrics(String namespace, String metricName, String dimensionName, String dimensionValue) {
        this.namespace = namespace;
        this.metricName = metricName;
        dimension = new Dimension()
                .withName(dimensionName)
                .withValue(dimensionValue);

        datum = new MetricDatum()
                .withMetricName(metricName)
                .withUnit(StandardUnit.None)
                .withDimensions(dimension);
    }

    public void log(double value) {
        String str = new StringBuilder()
                .append("Debug metrics: [")
                .append("Namespace: " + namespace + ", ")
                .append("MetricName: " + metricName + ", ")
                .append("DimensionName: " + dimension.getName() + ", ")
                .append("DimensionValue: " + dimension.getValue() + ", ")
                .append("Value: " + value)
                .append("]").toString();
        System.out.println(str);
    }

    public void sendMetricData(double value) {
        this.log(value);
        datum.setValue(value);
        PutMetricDataRequest request = new PutMetricDataRequest()
                .withNamespace(namespace)
                .withMetricData(datum);
        cw.putMetricData(request);
    }
}

