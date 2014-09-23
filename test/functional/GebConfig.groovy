import org.openqa.selenium.Dimension
import org.openqa.selenium.firefox.FirefoxBinary
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxProfile
import org.openqa.selenium.phantomjs.PhantomJSDriver
import org.openqa.selenium.remote.DesiredCapabilities

def defaultLocale = "en"

driver = {

    DesiredCapabilities capabilities = DesiredCapabilities.phantomjs();

    capabilities.setCapability("intl.accept_languages", defaultLocale);
    capabilities.setCapability("phantomjs.page.customHeaders.Accept-Language", defaultLocale);
    def d = new PhantomJSDriver(capabilities);
    d.manage().window().setSize(new Dimension(1028, 768))
    d
}

environments {

    // "grails test-app functional: -Dgeb.env=firefox"
    'firefox' {
        driver = {

            FirefoxProfile p = new FirefoxProfile();
            p.setPreference("intl.accept_languages", defaultLocale);

            new FirefoxDriver(p)
        }
    }

    // "grails test-app functional: -Dgeb.env=chrome"
    'chrome' {
//        driver = {
//
//            System.setProperty('webdriver.chrome.driver', '/Users/tomaslin/drivers/chromedriver')
//            new ChromeDriver()
//        }

        FirefoxBinary firefoxBinary = new FirefoxBinary()
          FirefoxProfile profile = new FirefoxProfile()

        driver = {
            new FirefoxDriver(firefoxBinary, profile)
        }
    }


}
