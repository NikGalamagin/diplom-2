package jsons.orders;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrdersModel {
    List<String> ingrideints;

    public List<String> getIngrideints() {
        return ingrideints;
    }

    public void setIngrideints(List<String> ingrideints) {
        this.ingrideints = ingrideints;
    }

    public OrdersModel(List<String> ingrideints) {
        this.ingrideints = ingrideints;
    }
}
