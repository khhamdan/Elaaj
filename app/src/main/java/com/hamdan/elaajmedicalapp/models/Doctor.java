
package com.hamdan.elaajmedicalapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Doctor implements Serializable {

    @SerializedName("profile_pic")
    @Expose
    private String profilePic;
    @SerializedName("name")
    @Expose
    private String name;

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private void readObject(ObjectInputStream aInputStream) throws ClassNotFoundException, IOException
    {
        profilePic = aInputStream.readUTF();
        name = aInputStream.readUTF();
    }
    private void writeObject(ObjectOutputStream aOutputStream) throws IOException
    {
        aOutputStream.writeUTF(profilePic);
        aOutputStream.writeUTF(name);
    }
}
