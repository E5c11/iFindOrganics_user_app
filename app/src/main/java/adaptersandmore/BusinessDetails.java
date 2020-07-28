package adaptersandmore;

public class BusinessDetails {

    private String z_businessName;
    private String z_businessSlogan;
    private String z_businessLogo;
    private String z_businessDes;
    private String z_businessBanner;
    private String title;
    private String value;
    private String businessAdContent;

    public BusinessDetails(String z_businessName, String z_businessSlogan, String z_businessLogo, String z_businessDes, String z_businessBanner, String businessAdContent, String title, String value) {

        this.z_businessName = z_businessName;
        this.z_businessSlogan = z_businessSlogan;
        this.z_businessLogo = z_businessLogo;
        this.z_businessDes = z_businessDes;
        this.z_businessBanner = z_businessBanner;
        this.businessAdContent = businessAdContent;
        this.title = title;
        this.value = value;
    }



    public BusinessDetails() {

    }

    public String getZ_businessName() {
        return z_businessName;
    }

    public void setZ_businessName(String z_businessName) {
        this.z_businessName = z_businessName;
    }

    public String getZ_businessSlogan() {
        return z_businessSlogan;
    }

    public void setZ_businessSlogan(String z_businessSlogan) {
        this.z_businessSlogan = z_businessSlogan;
    }

    public String getZ_businessLogo() {
        return z_businessLogo;
    }

    public void setZ_businessLogo(String z_businessLogo) {
        this.z_businessLogo = z_businessLogo;
    }

    public String getZ_businessDes() {
        return z_businessDes;
    }

    public void setZ_businessDes(String z_businessDes) {
        this.z_businessDes = z_businessDes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value.replace("\n","\n");
    }

    public String getBusinessAdContent() {
        return businessAdContent;
    }

    public void setBusinessAdContent(String businessAdContent) {
        this.businessAdContent = businessAdContent;
    }

    public String getZ_businessBanner() {
        return z_businessBanner;
    }

    public void setZ_businessBanner(String z_businessBanner) {
        this.z_businessBanner = z_businessBanner;
    }
}
