
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.apache.derby.jdbc.ClientDriver;
import javax.sql.rowset.CachedRowSet;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

@ManagedBean(name = "dbBean")
//@SessionScoped
public class DbBean {

    public String ogrUname = "";
    public String ogrPassword = "";
    public String hocaPassword = "";
    public String hocaUname = "";
    public String dersNo = "0";
    public static String loggedUserID = "";
    public String alinmakIstenenDers = "";
    public static String loggedUserName = "";
    public String silmekIstenenDers = "";
    
    
    public String getSilmekIstenenDers(){
        return silmekIstenenDers;
    }
    
    public void setSilmekIstenenDers(String sDersNo){
        this.silmekIstenenDers = sDersNo;
    }

    public String getLoggedUserName() {
        return loggedUserName;
    }

    public String getAlinmakIstenenDers() {
        return alinmakIstenenDers;
    }

    public void setAlinmakIstenenDers(String aDersNo) {
        this.alinmakIstenenDers = aDersNo;
    }

    public String getDersNo() {
        return dersNo;
    }

    public void setDersNo(String dersNo) {
        this.dersNo = dersNo;
    }

    public String getOgrUname() {

        return ogrUname;
    }

    public void setOgrUname(String uname) {
        this.ogrUname = uname;
    }

    public String getOgrPassword() {

        return ogrPassword;
    }

    public void setOgrPassword(String pass) {
        this.ogrPassword = pass;
    }

    public String getHocaPassword() {

        return hocaPassword;
    }

    public void setHocaPassword(String pass) {
        this.hocaPassword = pass;
    }

    public String getHocaUname() {

        return hocaUname;
    }

    public void setHocaUname(String uname) {
        this.hocaUname = uname;
    }

    public String ogrLoginMethod() throws SQLException {
        if (conn == null) {
            System.out.println("veritabani bagli degil. baglaniyorum.");
            baglan();
        }
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT UNAME,PASSWORD,STUDENTNO,FNAME,LNAME FROM TBL_OGRENCI WHERE UNAME =?");
            ps.setString(1, getOgrUname());
            ResultSet rs = ps.executeQuery();
            CachedRowSet resultSet1 = new com.sun.rowset.CachedRowSetImpl();
            resultSet1.populate(rs);

            if (resultSet1.next()) {
                String password = resultSet1.getString(2);

                if (password.equals(getOgrPassword())) {
                    int userID = resultSet1.getInt(3);
                    loggedUserID = Integer.toString(userID);
                    loggedUserName = resultSet1.getString(4) + " " + resultSet1.getString(5);
                    return "ogrenciPanel.xhtml";
                } else {
                    return "errorPage.xhtml";
                }
            } else {
                return "errorPage.xhtml";
            }

        } finally {

        }

    }

    public String hocaLoginMethod() throws SQLException {
        if (conn == null) {
            System.out.println("veritabani bagli degil. baglaniyorum.");
            baglan();
        }
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT UNAME,PASSWORD,OGRETMENNO FROM TBL_OGRETMENLER WHERE UNAME =?");
            ps.setString(1, getHocaUname());
            ResultSet rs = ps.executeQuery();
            CachedRowSet resultSet1 = new com.sun.rowset.CachedRowSetImpl();
            resultSet1.populate(rs);

            if (resultSet1.next()) {
                String password = resultSet1.getString(2);

                if (password.equals(getHocaPassword())) {
                    int userID = resultSet1.getInt(3);
                    loggedUserID = Integer.toString(userID);
                    return "hocaPanel.xhtml";
                } else {
                    return "errorPage.xhtml";
                }
            } else {
                return "errorPage.xhtml";
            }

        } finally {

        }

    }

    private Connection conn;

    public Connection baglan() {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            System.out.println("baglanti basarili");
            conn = DriverManager.getConnection("jdbc:derby://localhost:1527/studentDB");
        } catch (Exception e) {
            System.out.println("baglantida sorun var");
        }
        return conn;
    }

    public ResultSet ogrencileriListele() throws SQLException {
        if (conn == null) {
            System.out.println("veritabani bagli degil. baglaniyorum.");
            baglan();
        }
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("Select STUDENTNO,FNAME,LNAME,DEPARTMANNAME From TBL_OGRENCI INNER JOIN TBL_DEPARTMAN ON TBL_OGRENCI.DEPARTMANNO = TBL_DEPARTMAN.DEPARTMANNO");
            CachedRowSet resultSet1 = new com.sun.rowset.CachedRowSetImpl();
            resultSet1.populate(rs);
            return resultSet1;

        } finally {

        }
    }

    public ResultSet ogretmenler() throws SQLException {
        if (conn == null) {
            System.out.println("veritabani bagli degil. baglaniyorum.");
            baglan();
        }
        try {

            PreparedStatement ps = conn.prepareStatement("SELECT * FROM TBL_OGRETMENLER WHERE OGRETMENNO =?");
            ps.setInt(1, 4);
            ResultSet rs = ps.executeQuery();
            CachedRowSet resultSet1 = new com.sun.rowset.CachedRowSetImpl();
            resultSet1.populate(rs);
            return resultSet1;

        } finally {

        }
    }

    public ResultSet dersKodlari() throws SQLException {
        if (conn == null) {
            System.out.println("veritabani bagli degil. baglaniyorum.");
            baglan();
        }
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT DERSNO,DERSADI,FNAME,LNAME FROM TBL_DERSLER INNER JOIN TBL_OGRETMENLER ON TBL_DERSLER.OGRETMENNO = TBL_OGRETMENLER.OGRETMENNO");
            ResultSet rs = ps.executeQuery();
            CachedRowSet resultSet1 = new com.sun.rowset.CachedRowSetImpl();
            resultSet1.populate(rs);
            return resultSet1;

        } finally {

        }
    }

    public ResultSet secilenDersinOgrencileri() throws SQLException {
        if (conn == null) {
            System.out.println("veritabani bagli degil. baglaniyorum.");
            baglan();
        }
        try {

            PreparedStatement ps = conn.prepareStatement("SELECT TBL_ALINANDERSLER.OGRENCINO,TBL_OGRENCI.FNAME,TBL_OGRENCI.LNAME\n"
                    + "FROM TBL_ALINANDERSLER INNER JOIN TBL_OGRENCI ON OGRENCINO=STUDENTNO WHERE DERSNO=?");
            ps.setInt(1, Integer.parseInt(getDersNo()));
            ResultSet rs = ps.executeQuery();
            CachedRowSet resultSet1 = new com.sun.rowset.CachedRowSetImpl();
            resultSet1.populate(rs);
            return resultSet1;

        } finally {

        }
    }

    public ResultSet alinanDersler() throws SQLException {
        if (conn == null) {
            System.out.println("veritabani bagli degil. baglaniyorum.");
            baglan();
        }
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT TBL_DERSLER.DERSADI FROM TBL_ALINANDERSLER \n"
                    + "INNER JOIN TBL_OGRENCI ON TBL_ALINANDERSLER.OGRENCINO = TBL_OGRENCI.STUDENTNO\n"
                    + "Inner JOIN TBL_DERSLER ON TBL_ALINANDERSLER.DERSNO = TBL_DERSLER.DERSNO\n"
                    + "WHERE TBL_OGRENCI.STUDENTNO =?");
            ps.setInt(1, Integer.parseInt(loggedUserID));
            ResultSet rs = ps.executeQuery();
            CachedRowSet resultSet1 = new com.sun.rowset.CachedRowSetImpl();
            resultSet1.populate(rs);
            return resultSet1;

        } finally {

        }
    }

    public String dersEkle() throws SQLException {
        if (conn == null) {
            System.out.println("veritabani bagli degil. baglaniyorum.");
            baglan();
        }
        try {

            PreparedStatement ps = conn.prepareStatement("SELECT * FROM TBL_ALINANDERSLER WHERE OGRENCINO = ? AND DERSNO = ?");
            ps.setInt(1, Integer.parseInt(loggedUserID));
            ps.setInt(2, Integer.parseInt(alinmakIstenenDers));

            ResultSet rs = ps.executeQuery();
            CachedRowSet resultSet1 = new com.sun.rowset.CachedRowSetImpl();
            resultSet1.populate(rs);
            if (resultSet1.next() || Integer.parseInt(alinmakIstenenDers) > 14 || Integer.parseInt(alinmakIstenenDers) < 1) {
                return "dersAlma.xhtml";
            } else {
                PreparedStatement ps2 = conn.prepareStatement("INSERT INTO TBL_ALINANDERSLER (DERSNO, OGRENCINO) VALUES (?,?)");
                ps2.setInt(1, Integer.parseInt(alinmakIstenenDers));
                ps2.setInt(2, Integer.parseInt(loggedUserID));
                ps2.executeUpdate();
                return "dersAlma.xhtml";
            }

        } finally {

        }
    }

    public String dersKaydiSil() throws SQLException {
        if (conn == null) {
            System.out.println("veritabani bagli degil. baglaniyorum.");
            baglan();
        }
        try {
            PreparedStatement ps2 = conn.prepareStatement("DELETE FROM TBL_ALINANDERSLER WHERE DERSNO = ? AND OGRENCINO = ?");
            ps2.setInt(1, Integer.parseInt(silmekIstenenDers));
            ps2.setInt(2, Integer.parseInt(loggedUserID));
            ps2.executeUpdate();
            return "dersAlma.xhtml";

        } finally {

        }
    }

}
