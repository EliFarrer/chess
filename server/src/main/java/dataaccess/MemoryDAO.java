package dataaccess;

import java.util.Map;

public class MemoryDAO implements DataAccess {
    public Map<String, Object> data;

    public void clearAllEntries() {
        data.clear();
    }
}
