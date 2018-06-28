package com.tony.juetu.conversation;

import org.jivesoftware.smack.roster.RosterEntry;

import java.util.ArrayList;

public interface ContactView {
    void updateContact(ArrayList<RosterEntry>entries);
}
