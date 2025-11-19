package lidia_assignement2.business;

import lidia_assignement2.model.Server;
import lidia_assignement2.model.Task;
import java.util.List;

public interface Strategy {
    Server findBestServer(List<Server> servers);

}
