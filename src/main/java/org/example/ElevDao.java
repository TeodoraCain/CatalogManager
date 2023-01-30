package org.example;

import java.util.List;

public interface ElevDao {
    public List<Elev> getElevList();
    public Elev getElev(int code);
    public void updateElev(Elev elev);
    public void deleteElev(int code);
    public void insertElev(Elev elev);
    
}
