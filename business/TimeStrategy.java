package lidia_assignement2.business;

import lidia_assignement2.model.Server;
import lidia_assignement2.model.Task;
import java.util.List;

public class TimeStrategy implements Strategy {

    @Override
    public Server findBestServer(List<Server> servers) {
        Server best = servers.get(0);
        for (Server s : servers) {
            if (s.getWaitingPeriod() < best.getWaitingPeriod()) {
                best = s;
            }
        }
        return best;
    }

}
