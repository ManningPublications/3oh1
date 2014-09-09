import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxProfile

driver = {

    def forcedLocale = "en"

    FirefoxProfile p = new FirefoxProfile();
    p.setPreference( "intl.accept_languages", forcedLocale );

    new FirefoxDriver(p)
}
