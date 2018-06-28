package com.tony.juetu.manager;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.RosterEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dev on 6/28/18.
 */

public class DataManager {

    private ArrayList<Presence> presences;
    private ArrayList<RosterEntry> rosterEntries;
    private static DataManager sInstance;

    public static DataManager getInstance()
    {
        if (sInstance == null)
        {
            synchronized (DataManager.class)
            {
                if (sInstance == null)
                {
                    sInstance = new DataManager();
                }
            }
        }
        return sInstance;
    }

    private DataManager() {
        if (presences == null)
        {
            presences = new ArrayList<>();
        }
        if (rosterEntries == null)
        {
            rosterEntries = new ArrayList<>();
        }
    }

    public void addToPresencesManager(Presence presence)
    {
        if (!presences.contains(presence))
        {
            presences.add(presence);
        }
    }

    public void addToPresencesManager(List<Presence> presence)
    {
        presences.clear();
        presences.addAll(presence);
    }
    public void removePresensesFromManager(Presence presence)
    {
        if (presences.contains(presence))
        {
            presences.remove(presence);
        }
    }

    public ArrayList<Presence> getPresences()
    {
        return presences;
    }

    public ArrayList<RosterEntry> getRosterEntries() {
        return rosterEntries;
    }

    public void addRosterEntry(RosterEntry entry)
    {
        if (!rosterEntries.contains(entry))
        {
            rosterEntries.add(entry);
        }
    }

    public void addRosterEntries(List<RosterEntry>entries)
    {
        rosterEntries.clear();
        rosterEntries.addAll(entries);
    }

    public void removeRosterEntry(RosterEntry entry)
    {
        if (rosterEntries.contains(entry))
        {
            rosterEntries.remove(entry);
        }
    }
}
