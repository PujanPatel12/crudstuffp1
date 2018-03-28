public class Person {
    private int personid;
    private String personname;
    private String personemail;
    private int statusid;
    private String statusdesc;

    @Override
    public String toString() {
        return "Person{" +
                "personid=" + personid +
                ", personname='" + personname + '\'' +
                ", personemail='" + personemail + '\'' +
                ", statusid=" + statusid +
                ", statusdesc='" + statusdesc + '\'' +
                '}';
    }

    public int getPersonid() {
        return personid;
    }

    public void setPersonid(int personid) {
        this.personid = personid;
    }

    public String getPersonname() {
        return personname;
    }

    public void setPersonname(String personname) {
        this.personname = personname;
    }

    public String getPersonemail() {
        return personemail;
    }

    public void setPersonemail(String personemail) {
        this.personemail = personemail;
    }

    public int getStatusid() {
        return statusid;
    }

    public void setStatusid(int statusid) {
        this.statusid = statusid;
    }

    public String getStatusdesc() {
        return statusdesc;
    }

    public void setStatusdesc(String statusdesc) {
        this.statusdesc = statusdesc;
    }
}
