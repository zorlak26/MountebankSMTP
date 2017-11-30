package cl.unab.testing;

import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class MountebankImposter {
    private String name;
    private String protocol;
    private int port;
    private List<Object> stubs;
    private List<Map<String,Object>> requests;

    @SuppressWarnings("unused") public String getName() { return name; }

    @SuppressWarnings("unused") public void setName(String name) { this.name = name; }

    @SuppressWarnings("unused") public String getProtocol() { return protocol; }

    @SuppressWarnings("unused") public void setProtocol(String protocol) { this.protocol = protocol; }

    @SuppressWarnings("unused") public int getPort() {
        return port;
    }

    @SuppressWarnings("unused") public void setPort(int port) { this.port = port; }

    @SuppressWarnings("unused") public List<Object> getStubs() { return stubs; }

    @SuppressWarnings("unused") public void setStubs(List<Object> stubs) { this.stubs = stubs; }

    @SuppressWarnings("unused") public List<Map<String, Object>> getRequests() {
        return requests;
    }

    @SuppressWarnings("unused") public void setRequests(List<Map<String, Object>> requests) { this.requests = requests; }
}
