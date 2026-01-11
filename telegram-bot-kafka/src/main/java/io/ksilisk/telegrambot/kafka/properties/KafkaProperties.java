package io.ksilisk.telegrambot.kafka.properties;

public class KafkaProperties {
    private String rejectionTopic;
    private String exceptionTopic;
    private String noMatchTopic;

    public String getRejectionTopic() {
        return rejectionTopic;
    }

    public void setRejectionTopic(String rejectionTopic) {
        this.rejectionTopic = rejectionTopic;
    }

    public String getExceptionTopic() {
        return exceptionTopic;
    }

    public void setExceptionTopic(String exceptionTopic) {
        this.exceptionTopic = exceptionTopic;
    }

    public String getNoMatchTopic() {
        return noMatchTopic;
    }

    public void setNoMatchTopic(String noMatchTopic) {
        this.noMatchTopic = noMatchTopic;
    }
}
