package io.threeohone

import com.maxmind.geoip.Location
import com.maxmind.geoip.LookupService

import javax.servlet.http.HttpServletRequest

class RedirectLoggingService {

    def userAgentIdentService

    LookupService geoIpService

    def log(Shortener shortener, HttpServletRequest request = null) {

        def log = new RedirectLog(
                shortener: shortener,
                referer: request?.getHeader("referer"),
                lientInformation: parseClientInformationFromRequest()
        )


        if (request) {
            def ip = request.remoteAddr
            def location = geoIpService.getLocation(ip)
            if (location) {
                log.clientLocation = createClientLocationFromLocation(location)
            }
        }

        log.save(failOnError: true)
    }

    private ClientInformation parseClientInformationFromRequest() {

        new ClientInformation(
                browserName: userAgentIdentService.getBrowser(),
                browserVersion: userAgentIdentService.getBrowserVersion(),
                operatingSystem: userAgentIdentService.getOperatingSystem(),
                mobileBrowser: userAgentIdentService.isMobile()
        )
    }


    private ClientLocation createClientLocationFromLocation(Location location) {

            new ClientLocation(
                    countryCode: location.countryCode,
                    countryName: location.countryName,
                    region: location.region,
                    city: location.city,
                    postalCode: location.postalCode,
                    latitude: location.latitude,
                    longitude: location.longitude,
                    dmaCode: location.dma_code,
                    areaCode: location.area_code,
                    metroCode: location.metro_code
            )

    }
}
