import org.openqa.selenium.Dimension
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxProfile
import org.openqa.selenium.phantomjs.PhantomJSDriver
import org.openqa.selenium.phantomjs.PhantomJSDriverService
import org.openqa.selenium.remote.DesiredCapabilities

def defaultLocale = "en"

driver = {

    capabilities = DesiredCapabilities.phantomjs();


    ArrayList<String> cliArgsCap = new ArrayList<String>();
    cliArgsCap.add("--web-security=false");
    cliArgsCap.add("--ssl-protocol=any");
    cliArgsCap.add("--ignore-ssl-errors=true");


    ArrayList<String> ghostdriverCliArgs = new ArrayList<String>();
    ghostdriverCliArgs.add("--logLevel=2")

    capabilities.setCapability("intl.accept_languages", defaultLocale);
    capabilities.setCapability("phantomjs.page.customHeaders.Accept-Language", defaultLocale);
    capabilities.setCapability("takesScreenshot", true);
    capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsCap);
    capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_GHOSTDRIVER_CLI_ARGS, ghostdriverCliArgs);

    def driver = new PhantomJSDriver(capabilities);
    driver.manage().window().setSize(new Dimension(1028, 768))
    return driver

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
}
