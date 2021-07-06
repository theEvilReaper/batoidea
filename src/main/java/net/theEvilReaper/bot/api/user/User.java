package net.theEvilReaper.bot.api.user;

import com.github.manevolent.ts3j.api.Client;

public interface User {

    /**
     * Set the group id from the user.
     * @param groupID The id to set
     */

    void setGroupID(int groupID);

    /**
     * Change the verified status. Standard value is false
     * @param verified If the player is verified as boolean
     */
    
    void setVerified(boolean verified);

    /**
     * Returns the group id from the user.
     * @return the group id from the user
     */

    int getGroupID();

    /**
     * Returns if the user is verified.
     * @return true when verified otherwise false
     */

    boolean isVerified();

    /**
     * Returns the {@link Client} from the user.
     * @return the given client
     */

    Client getClient();
}
