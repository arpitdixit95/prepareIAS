package prepareias.tychestudios.com.prepareias;

/**
 * Created by Ankur on 5/8/2016.
 */
public class ListModel {

    private int serial ;
    private String title ;

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    @Override
    public String toString() {
        return "ListModel{" +
                "title='" + title + '\'' +
                ", serial=" + serial +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
