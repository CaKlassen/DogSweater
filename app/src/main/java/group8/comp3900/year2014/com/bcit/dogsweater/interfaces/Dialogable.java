package group8.comp3900.year2014.com.bcit.dogsweater.interfaces;

import android.net.Uri;

/**
 * @author          Eric Tsang
 * @date            October 10 2014
 * @revisions       none
 *
 * instances that implement this interface describe an object that holds data
 * that can be displayed in various parts of a dialogue box.
 */
public interface Dialogable<T> {

    public T getItem();

    public long getItemId();

    /**
     * @author          Eric Tsang
     * @date            October 10 2014
     * @revisions       none
     * @return          String to display in the title of the dialogue box
     *
     * returns a String to display in the title of the dialogue box
     */
    public String getDialogueTitle();

    /**
     * @author          Eric Tsang
     * @date            October 10 2014
     * @revisions       none
     * @return          String to display in the description section of the
     *                  dialogue box
     *
     * returns a String to display in the description section of the dialogue
     * box
     */
    public String getDialogueDescription();

    /**
     * @author          Eric Tsang
     * @date            October 10 2014
     * @revisions       none
     * @return          String to display on the button of the dialogue box
     *
     * returns a String to display on the button of the dialogue box
     */
    public String getDialogueButtonText();

    /**
     * @author          Eric Tsang
     * @date            October 10 2014
     * @revisions       changed from returning a resource ID, to returning an
     *                  image URI instead
     * @return          integer resource id of the image to display in the
     *                  dialog
     *
     * returns an integer resource id of the image to display in the dialog
     */
    public Uri getDialogueImageUri();

    /**
     * @author          Eric Tsang
     * @date            October 18 2014
     * @revisions       none
     * @return          String specifying which activity to open next when
     *                  button is clicked
     */
    public String getNextScreen();

}
