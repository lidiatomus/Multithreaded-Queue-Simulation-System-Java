package lidia_assignement2.business;

import lidia_assignement2.model.Server;
import lidia_assignement2.model.Task;
import java.util.List;

public class ShortestQueueStrategy implements Strategy {
    // find adn return the server with the shortest queue
    @Override
    public Server findBestServer(List<Server> servers) {
        Server best = servers.get(0);
        for (Server s : servers) {
            if (s.getTasks().size() < best.getTasks().size()) {
                best = s;
            }
        }
        return best;
    }

}
