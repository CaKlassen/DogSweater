package group8.comp3900.year2014.com.bcit.dogsweater.classes.GridPopups;

import android.net.Uri;

import group8.comp3900.year2014.com.bcit.dogsweater.classes.Project;
import group8.comp3900.year2014.com.bcit.dogsweater.interfaces.Dialogable;

/**
 * Created by Georgi on 05-Nov-14.
 */
public class ProjectDialogable implements Dialogable {

    private Project project;

    public ProjectDialogable(Project p)
    {
        this.project = p;
    }

    public Project getProject() {
        return project;
    }

    @Override
    public long getItemId() { return project.getId(); }

    @Override
    public String getDialogueTitle() {
        return project.getName();
    }

    @Override
    public String getDialogueDescription() {
        return "The notes of the project go here.";
    }

    @Override
    public String getDialogueButtonText() {
        return "";
    }

    @Override
    public Uri getDialogueImageUri() {
        return null;
    }

    @Override
    public String getNextScreen() {
        return "group8.comp3900.year2014.com.bcit.dogsweater.ProjectPattern";
    }
}